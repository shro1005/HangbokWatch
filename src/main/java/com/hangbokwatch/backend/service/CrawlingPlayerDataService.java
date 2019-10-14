package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hangbokwatch.backend.dao.DvaRepositroy;
import com.hangbokwatch.backend.dao.OrisaRepository;
import com.hangbokwatch.backend.dao.ReinhardtRepository;
import com.hangbokwatch.backend.domain.Dva;
import com.hangbokwatch.backend.domain.Orisa;
import com.hangbokwatch.backend.domain.Reinhardt;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class CrawlingPlayerDataService {
    private static String GET_PLAYER_LIST_URL = "https://playoverwatch.com/ko-kr/search/account-by-name/";
    private static String GET_PLAYER_PROFILE_URL2 = "https://ow-api.com/v1/stats/pc/asia/";
    private static String GET_PLAYER_PROFILE_URL = "https://playoverwatch.com/ko-kr/career/";
//    private List<PlayerListDto> playerList;

    @Autowired
    DvaRepositroy dvaRepositroy;
    @Autowired
    OrisaRepository orisaRepository;
    @Autowired
    ReinhardtRepository reinhardtRepository;

    public List<PlayerListDto>  crawlingPlayerList(String playerName) {
        // 반환할 playerListDto 초기화
        List<PlayerListDto> playerList = new ArrayList<PlayerListDto>();

        //Json String을 Json객체로 바꾸기 위한 매퍼 초기화
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("crawlingPlayerList working -> player name : " + playerName);
            // Jsoup를 이용해 오버워치 웹크롤링 : 유저 리스트를 json String 형식으로 가져오는 부분
            String json = Jsoup.connect(GET_PLAYER_LIST_URL+playerName)
                    .ignoreContentType(true)
                    .execute().body();
//            System.out.println("===== crawling result ===== \n" + json);
            //위에 선언한 매퍼를 통해 크롤링결과dto에 맞게 파싱하여 list에 추가
            List<PlayerCrawlingResultDto> playerCrawlingResultDtoList = mapper.readValue(json, new TypeReference<List<PlayerCrawlingResultDto>>(){});

            //위에 크롤링결과 dto를 반환활 playerListDto에 넣어주는 for 문
            for(PlayerCrawlingResultDto dto : playerCrawlingResultDtoList) {
                String battleTag = dto.getName();
                String pName = battleTag;
                if(dto.getPlatform().equals("pc")) {
                    pName = battleTag.substring(0, battleTag.indexOf("#"));
                }
                String forUrl = dto.getUrlName();
                String portrait = "https://d1u1mce87gyfbn.cloudfront.net/game/unlocks/" + dto.getPortrait() + ".png";  //d1u1mce87gyfbn.cloudfront.net
                String isPublic = "N"; Integer tankratingPoint = 0; Integer dealRatingPoint = 0; Integer healRatingPoint = 0;
                PlayerListDto playerListDto = new PlayerListDto(dto.getId(), battleTag, pName, forUrl, dto.getPlayerLevel(), isPublic, dto.getPlatform(), portrait, tankratingPoint, dealRatingPoint, healRatingPoint);
                if(dto.getIsPublic()) {
                    playerListDto.setIsPublic("Y");
                    // 프로필 공개한 플레이어의 경우 경쟁전 점수와 승리, 패배, 무승부 경기수를 가져온다.
//                    playerListDto = crawlingPlayerProfile2(playerListDto);
//                    System.out.println(playerListDto.getTankRatingPoint());
                }
                playerListDto.setCnt(3);
                System.out.println(playerListDto.toString());
                playerList.add(playerListDto);
            }
            Collections.sort(playerList);
        } catch(Exception e) {
            if(e.getClass() == SocketException.class) {
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","현재 배틀넷 서버 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","", 0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == UnknownHostException.class){
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","연결된 인터넷에서 배틀넷 서버로 접속할 수 없습니다.","",0,"","","",0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == SocketTimeoutException.class){
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","현재 배틀넷 서버 내부 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","",0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }
            e.printStackTrace();
        }
        return playerList;
    }


    /** 블리자드 오버워치 공식 홈페이지에서 웹크롤링 - 경쟁전 점수만 */
    public PlayerListDto crawlingPlayerProfile(PlayerListDto playerListDto) {
        ObjectMapper mapper = new ObjectMapper();
//        playerList = new ArrayList<PlayerListDto>();
        try {
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getPlatform()+"/"+playerListDto.getForUrl())
                                .get();
            Elements elements = rawData.select("div.competitive-rank-role");
            for (Element roleElement : elements) {
                Element roleIcon = roleElement.selectFirst("img[class=competitive-rank-role-icon]");
                if("https://static.playoverwatch.com/img/pages/career/icon-tank-8a52daaf01.png".equals(roleIcon.attr("src"))){
//                    System.out.println(roleElement.text());
                    playerListDto.setTankRatingPoint(Integer.parseInt(roleElement.text()));
                }else if("https://static.playoverwatch.com/img/pages/career/icon-offense-6267addd52.png".equals(roleIcon.attr("src"))){
                    playerListDto.setDealRatingPoint(Integer.parseInt(roleElement.text()));
                }else if("https://static.playoverwatch.com/img/pages/career/icon-support-46311a4210.png".equals(roleIcon.attr("src"))){
                    playerListDto.setHealRatingPoint(Integer.parseInt(roleElement.text()));
                }
            }
            int cnt = 3;
            if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
            if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
            if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
            if(cnt == 0 ) {cnt = 1;}
            playerListDto.setCnt(cnt);
        }catch(Exception e) {
            e.printStackTrace();
        }
//        return playerList;
        return playerListDto;
    }

    public PlayerDetailDto crawlingPlayerDetail(PlayerListDto playerListDto) {
        ObjectMapper mapper = new ObjectMapper();
        // 시간 확인을 위한 스탑워치
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("jsoup을 이용한 블리자드 크롤링 및 경쟁전 엘레멘트 추출");
        //
        try {
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getPlatform()+"/"+playerListDto.getForUrl())
                    .get();
            Elements competitivePoint = rawData.select("div.competitive-rank-role");
            Elements competitiveDatas = rawData.select("div#competitive");
            Elements competitiveHerosDetail = competitiveDatas.select("div.js-stats");
            // 시간 확인
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
            //
            for(Element heroDetails : competitiveHerosDetail) {
                Integer winGame = 0; String winRate = "0%"; Integer loseGame = 0; String playTime = "00:00"; String killPerDeath = "0"; String spentOnFireAvg = "00:00";
                Long death = 1l; String deathAvg = "0"; Long blockDamage = 0l; Long damageToHero = 0l; Long damageToShield = 0l;
                String goldMedal = "0"; String silverMedal = "0"; String bronzeMedal = "0";

                /**디바 시작 */
                if ("0x02E000000000007A".equals(heroDetails.attr("data-category-id"))) { //디바
                    //시간 확인
                    stopWatch.start("디바 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements dVaDatas = heroDetails.select("tr.DataTable-tableRow");

                    //Dva 영웅 특별 데이터
                    String mechaSuicideKillAvg = "0"; String mechaCallAvg = "0";

                    for(Element tr : dVaDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000039" :
                                td = tr.select("td");
                                winGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x08600000000003D1" :
                                td = tr.select("td");
                                winRate = td.last().text();
                                break;
                            case "0x0860000000000430" :
                                td = tr.select("td");
                                loseGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x0860000000000021" :
                                td = tr.select("td");
                                playTime = td.last().text().substring(0,2);
                                if(td.last().text().length() == 8) {
                                    playTime += "시간";
                                }else {
                                    if ("00".equals(playTime)) {
                                        playTime = td.last().text().substring(3) + "초";
                                    }else {
                                        playTime += "분";
                                    }
                                }
                                break;
                            case "0x08600000000003D2" :
                                td = tr.select("td");
                                killPerDeath = td.last().text();
                                break;
                            case "0x08600000000004DB" :
                                td = tr.select("td");
                                spentOnFireAvg = td.last().text();
                                break;
                            case "0x086000000000002A" :
                                td = tr.select("td");
                                death = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004D3" :
                                td = tr.select("td");
                                deathAvg = td.last().text();
                                break;
                            case "0x086000000000048E" :
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7" :
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000515" :
                                td = tr.select("td");
                                damageToShield = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004D1" :
                                td = tr.select("td");
                                mechaSuicideKillAvg = td.last().text();
                                break;
                            case "0x08600000000004D0" :
                                td = tr.select("td");
                                mechaCallAvg = td.last().text();
                                break;
                            case "0x086000000000036F" :
                                td = tr.select("td");
                                goldMedal = td.last().text();
                                break;
                            case "0x086000000000036E" :
                                td = tr.select("td");
                                silverMedal = td.last().text();
                                break;
                            case "0x086000000000036D" :
                                td = tr.select("td");
                                bronzeMedal = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Long blockDamagePerLife = Math.round((blockDamage/death*100)/100.0);
                    Long damageToHeroPerLife = Math.round((damageToHero/death*100)/100.0);
                    Long damageToShieldPerLife = Math.round((damageToShield/death*100)/100.0);
                    Dva dva = new Dva(playerListDto.getId(), winGame, loseGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), mechaSuicideKillAvg, mechaCallAvg, goldMedal, silverMedal, bronzeMedal);
                    dvaRepositroy.save(dva);
                    System.out.println("dva data save success : " + dva.toString());
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                    //
                /**오리사 시작 */
                }else if("0x02E000000000013E".equals(heroDetails.attr("data-category-id"))) {  //오리사
                    //시간 확인
                    stopWatch.start("오리사 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements orisaDates = heroDetails.select("tr.DataTable-tableRow");

                    for(Element tr : orisaDates) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000039" :
                                td = tr.select("td");
                                winGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x08600000000003D1" :
                                td = tr.select("td");
                                winRate = td.last().text();
                                break;
                            case "0x0860000000000430" :
                                td = tr.select("td");
                                loseGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x0860000000000021" :
                                td = tr.select("td");
                                playTime = td.last().text().substring(0,2);
                                if(td.last().text().length() == 8) {
                                    playTime += "시간";
                                }else {
                                    if ("00".equals(playTime)) {
                                        playTime = td.last().text().substring(3) + "초";
                                    }else {
                                        playTime += "분";
                                    }
                                }
                                break;
                            case "0x08600000000003D2" :
                                td = tr.select("td");
                                killPerDeath = td.last().text();
                                break;
                            case "0x08600000000004DB" :
                                td = tr.select("td");
                                spentOnFireAvg = td.last().text();
                                break;
                            case "0x086000000000002A" :
                                td = tr.select("td");
                                death = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004D3" :
                                td = tr.select("td");
                                deathAvg = td.last().text();
                                break;
                            case "0x086000000000048E" :
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7" :
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000515" :
                                td = tr.select("td");
                                damageToShield = Long.parseLong(td.last().text());
                                break;
                            case "0x086000000000036F" :
                                td = tr.select("td");
                                goldMedal = td.last().text();
                                break;
                            case "0x086000000000036E" :
                                td = tr.select("td");
                                silverMedal = td.last().text();
                                break;
                            case "0x086000000000036D" :
                                td = tr.select("td");
                                bronzeMedal = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Long blockDamagePerLife = Math.round((blockDamage/death*100)/100.0);
                    Long damageToHeroPerLife = Math.round((damageToHero/death*100)/100.0);
                    Long damageToShieldPerLife = Math.round((damageToShield/death*100)/100.0);
                    Orisa orisa = new Orisa(playerListDto.getId(), winGame, loseGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), goldMedal, silverMedal, bronzeMedal);
                    orisaRepository.save(orisa);
                    System.out.println("orisa data save success : " + orisa.toString());
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                    //
                /**라인하르트 시작 */
                }else if("0x02E0000000000007".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("라인하르트 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements reinhardtDates = heroDetails.select("tr.DataTable-tableRow");

                    //라인하르트 영웅 특별 데이터
                    String chargeKillAvg = "0"; String earthshatterKillAvg = "0"; String fireStrikeKillAvg = "0";

                    for (Element tr : reinhardtDates) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000039":
                                td = tr.select("td");
                                winGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x08600000000003D1":
                                td = tr.select("td");
                                winRate = td.last().text();
                                break;
                            case "0x0860000000000430":
                                td = tr.select("td");
                                loseGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x0860000000000021":
                                td = tr.select("td");
                                playTime = td.last().text().substring(0, 2);
                                if (td.last().text().length() == 8) {
                                    playTime += "시간";
                                } else {
                                    if ("00".equals(playTime)) {
                                        playTime = td.last().text().substring(3) + "초";
                                    } else {
                                        playTime += "분";
                                    }
                                }
                                break;
                            case "0x08600000000003D2":
                                td = tr.select("td");
                                killPerDeath = td.last().text();
                                break;
                            case "0x08600000000004DB":
                                td = tr.select("td");
                                spentOnFireAvg = td.last().text();
                                break;
                            case "0x086000000000002A":
                                td = tr.select("td");
                                death = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004D3":
                                td = tr.select("td");
                                deathAvg = td.last().text();
                                break;
                            case "0x086000000000048E":
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000515":
                                td = tr.select("td");
                                damageToShield = Long.parseLong(td.last().text());
                                break;
                            case "0x086000000000036F":
                                td = tr.select("td");
                                goldMedal = td.last().text();
                                break;
                            case "0x086000000000036E":
                                td = tr.select("td");
                                silverMedal = td.last().text();
                                break;
                            case "0x086000000000036D":
                                td = tr.select("td");
                                bronzeMedal = td.last().text();
                                break;
                            case "0x08600000000004E7" :
                                td = tr.select("td");
                                earthshatterKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E5" :
                                td = tr.select("td");
                                chargeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E8" :
                                td = tr.select("td");
                                fireStrikeKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Long blockDamagePerLife = Math.round((blockDamage / death * 100) / 100.0);
                    Long damageToHeroPerLife = Math.round((damageToHero / death * 100) / 100.0);
                    Long damageToShieldPerLife = Math.round((damageToShield / death * 100) / 100.0);
                    Reinhardt reinhardt = new Reinhardt(playerListDto.getId(), winGame, loseGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), earthshatterKillAvg, chargeKillAvg, fireStrikeKillAvg, goldMedal, silverMedal, bronzeMedal);
                    reinhardtRepository.save(reinhardt);
                    System.out.println("reinhardt data save success : " + reinhardt.toString());
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                    //
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 사설 오버워치 api (ow-api.com) 에서 데이터 받아옴 */
    public PlayerListDto crawlingPlayerProfile2(PlayerListDto playerListDto) {
        ObjectMapper mapper = new ObjectMapper();
//        playerList = new ArrayList<PlayerListDto>();
        try {
            System.out.println("crawlingPlayerProfile working -> battleTag : " + playerListDto.getForUrl());
            // Jsoup를 이용해 오버워치 웹크롤링 : 유저 프로필을 json String 형식으로 가져오는 부분
            String json = Jsoup.connect(GET_PLAYER_PROFILE_URL2+playerListDto.getForUrl()+"/profile")
                    .ignoreContentType(true)
                    .execute().body();
            JsonNode jsonNode = mapper.readTree(json);
//            System.out.println("jsonNode : " + jsonNode);
            if (jsonNode.isObject()) {
                ObjectNode obj = (ObjectNode) jsonNode;
                if(obj.has("ratings")){
                    JsonNode ratings = obj.get("ratings");
//                    System.out.println(ratings);
                    Iterator itr = ratings.elements();

                    while (itr.hasNext()) {
                        JsonNode rating = (JsonNode) itr.next();
                        ObjectNode ratingObj = (ObjectNode) rating;
                        if("tank".equals(ratingObj.get("role").asText())){
                            playerListDto.setTankRatingPoint(ratingObj.get("level").asInt());
                        }else if("damage".equals(ratingObj.get("role").asText())){
                            playerListDto.setDealRatingPoint(ratingObj.get("level").asInt());
                        }else if("support".equals(ratingObj.get("role").asText())){
                            playerListDto.setHealRatingPoint(ratingObj.get("level").asInt());
                        }
                    }
                }
            }
//            playerList.add(playerListDto);
        }catch(Exception e) {
            e.printStackTrace();
        }
//        return playerList;
        return playerListDto;
    }
}
