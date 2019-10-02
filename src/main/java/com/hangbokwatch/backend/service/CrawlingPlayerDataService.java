package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

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
    private static String GET_PLAYER_PROFILE_URL = "https://playoverwatch.com/ko-kr/career/pc/";
//    private List<PlayerListDto> playerList;

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
                PlayerListDto playerListDto = new PlayerListDto(battleTag, pName, forUrl, dto.getPlayerLevel(), isPublic, dto.getPlatform(), portrait, tankratingPoint, dealRatingPoint, healRatingPoint);
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
                PlayerListDto playerListDto = new PlayerListDto("message","현재 배틀넷 서버 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","", 0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == UnknownHostException.class){
                PlayerListDto playerListDto = new PlayerListDto("message","연결된 인터넷에서 배틀넷 서버로 접속할 수 없습니다.","",0,"","","",0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == SocketTimeoutException.class){
                PlayerListDto playerListDto = new PlayerListDto("message","현재 배틀넷 서버 내부 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","",0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }
            e.printStackTrace();
        }
        return playerList;
    }

    public PlayerListDto crawlingPlayerProfile(PlayerListDto playerListDto) {
        ObjectMapper mapper = new ObjectMapper();
//        playerList = new ArrayList<PlayerListDto>();
        try {
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getForUrl())
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
