package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.domain.*;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
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
    PlayerRepository playerRepository;
    @Autowired
    DvaRepositroy dvaRepositroy;
    @Autowired
    OrisaRepository orisaRepository;
    @Autowired
    ReinhardtRepository reinhardtRepository;
    @Autowired
    ZaryaRepository zaryaRepository;
    @Autowired
    RoadHogRepository roadhogRepository;
    @Autowired
    WinstonRepository winstonRepository;
    @Autowired
    SigmaRepository sigmaRepository;
    @Autowired
    WreckingBallRepository wreckingBallRepository;
    @Autowired
    AnaRepository anaRepository;
    @Autowired
    BaptisteRepository baptisteRepository;
    @Autowired
    BrigitteRepository brigitteRepository;
    @Autowired
    LucioRepository lucioRepository;
    @Autowired
    MercyRepository mercyRepository;
    @Autowired
    MoiraRepository moiraRepository;
    @Autowired
    ZenyattaRepository zenyattaRepository;

    @Value("${spring.HWresource.HWimages}")
    private String portraitPath;

    public List<PlayerListDto> crawlingPlayerList(String playerName) {
        // 반환할 playerListDto 초기화
        List<PlayerListDto> playerList = new ArrayList<PlayerListDto>();

        if(playerName.indexOf("#") == -1) {  // 배틀태그로 입력하지 않았을 경우
            System.out.println("최초 검색시 배틀태그로 입력해야합니다. / 검색값 : " + playerName );
            return playerList;
        }
        playerName = playerName.replace("#", "%23");  // # -> %23 으로 파싱한 후에 검색

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

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("jsoup을 이용한 프로필 row data 추출");
//        playerList = new ArrayList<PlayerListDto>();
        try {
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getPlatform()+"/"+playerListDto.getForUrl())
                                .get();
            stopWatch.stop();
            stopWatch.start("경쟁전 점수 추출");
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
            stopWatch.stop();
            stopWatch.start("프로필 사진 추출");
            Element portraitEl = rawData.selectFirst("img[class=player-portrait]");
            String portrait = portraitEl.attr("src");
            String substrPR = portrait.substring(portrait.indexOf("/overwatch/")+11, portrait.indexOf(".png"));

//            System.out.println("++++++++++++" + substrPR);
            stopWatch.stop();
            stopWatch.start("프로필 이미지 저장");
            /** 이미지 저장*/
            try {
                URL url = new URL(portrait);
                portrait = "/HWimages/portrait/"+ substrPR + ".png";
                BufferedImage bi = ImageIO.read(url);
                ImageIO.write(bi, "png", new File(portraitPath+"portrait/"+ substrPR + ".png"));
            }catch (IIOException e) {
                portrait = "/HWimages/portrait/0x02500000000002F7.png";
            }
            playerListDto.setPortrait(portrait);
            stopWatch.stop();

            int cnt = 3;
            if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
            if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
            if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
            if(cnt == 0 ) {cnt = 1;}
            playerListDto.setCnt(cnt);
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(stopWatch.prettyPrint());
        return playerListDto;
    }

    public CompetitiveDetailDto crawlingPlayerDetail(PlayerListDto playerListDto, CompetitiveDetailDto chdDto) {
        ObjectMapper mapper = new ObjectMapper();
        // 시간 확인을 위한 스탑워치
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("jsoup을 이용한 블리자드 크롤링 및 경쟁전 엘레멘트 추출");
        //
        try {
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getPlatform()+"/"+playerListDto.getForUrl())
                    .get();

            // 시간 확인
            stopWatch.stop();

            stopWatch.start("경쟁전 역할별 점수 및 티어 이미지 추출");
            Elements competitiveRole = rawData.select("div.competitive-rank-role");
            String tierUrl ="";
            String substrTier = "";
            for (Element roleElement : competitiveRole) {
                Element roleIcon = roleElement.selectFirst("img[class=competitive-rank-role-icon]");
                if("https://static.playoverwatch.com/img/pages/career/icon-tank-8a52daaf01.png".equals(roleIcon.attr("src"))){
//                    System.out.println(roleElement.text());
                    playerListDto.setTankRatingPoint(Integer.parseInt(roleElement.text()));
                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));
                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
//                    tierUrl = "/HWimages/tier/"+ substrTier + ".png";
                    playerListDto.setTankRatingImg(tierUrl);
                }else if("https://static.playoverwatch.com/img/pages/career/icon-offense-6267addd52.png".equals(roleIcon.attr("src"))){
                    playerListDto.setDealRatingPoint(Integer.parseInt(roleElement.text()));
                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));
                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
//                    tierUrl = "/HWimages/tier/"+ substrTier + ".png";
                    playerListDto.setDealRatingImg(tierUrl);
                }else if("https://static.playoverwatch.com/img/pages/career/icon-support-46311a4210.png".equals(roleIcon.attr("src"))){
                    playerListDto.setHealRatingPoint(Integer.parseInt(roleElement.text()));
                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));
                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
//                    tierUrl = "/HWimages/tier/"+ substrTier + ".png";
                    playerListDto.setHealRatingImg(tierUrl);
                }
            }
            // 시간 확인
            stopWatch.stop();

            /** 프로필 정보 추출 */
            stopWatch.start("프로필 사진 추출");
            Element portraitEl = rawData.selectFirst("img[class=player-portrait]");
            String portrait = portraitEl.attr("src");
            String substrPR = portrait.substring(portrait.indexOf("/overwatch/")+11, portrait.indexOf(".png"));
            //프로필 사진 저장
            portrait = saveImg(stopWatch, portrait, substrPR, "portrait");
            playerListDto.setPortrait(portrait);

            int cnt = 3;
            if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
            if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
            if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
            if(cnt == 0 ) {cnt = 1;}
            playerListDto.setCnt(cnt);

            /** 모스트 영웅3 추출*/
            Element competitiveDatas = rawData.selectFirst("div#competitive");
            Element progressData = competitiveDatas.selectFirst("div.progress-category");
            Elements mostHeros = progressData.select("div.ProgressBar-title");
            int mostCnt = 3;
            if(mostHeros.size() < 3) {mostCnt = mostHeros.size();}
            int count = 0;
            System.out.println(mostCnt);
            System.out.println(mostHeros.text());
            for(Element mostHero : mostHeros) {
                count++;
                String hero = mostHero.text().trim();
                switch (hero) {
                    case "솔저: 76":  hero = "soldier-76";    break;
                    case "리퍼":      hero = "reaper";        break;
                    case "정크랫":     hero = "junkrat";       break;
                    case "위도우메이커":  hero = "widowmaker";    break;
                    case "토르비욘":    hero = "torbjorn";      break;
                    case "트레이서":    hero = "tracer";        break;
                    case "파라":      hero = "pharah";        break;
                    case "한조":      hero = "hanzo";         break;
                    case "야쉬":      hero = "ashe";          break;
                    case "시메트라":    hero = "symmetra";      break;
                    case "솜브라":     hero = "sombra";        break;
                    case "바스티온":    hero = "bastion";       break;
                    case "메이":      hero = "mei";           break;
                    case "맥크리":     hero = "mccree";        break;
                    case "둠피스트":    hero = "doomfist";      break;
                    case "겐지":      hero = "genji";         break;
                    case "오리사":     hero = "orisa";         break;
                    case "시그마":     hero = "sigma";         break;
                    case "자리야":     hero = "zarya";         break;
                    case "라인하르트":   hero = "reinhardt";     break;
                    case "D.Va":     hero = "D.Va";          break;
                    case "윈스턴":     hero = "winston";       break;
                    case "로드호그":    hero = "roadhog";       break;
                    case "레킹볼":     hero = "wreckingball";  break;
                    case "모이라":     hero = "moira";         break;
                    case "아나":      hero = "ana";           break;
                    case "브리기테":    hero = "brigitte";      break;
                    case "바티스트":    hero = "baptiste";      break;
                    case "젠야타":     hero = "zenyatta";      break;
                    case "루시우":     hero = "lucio";         break;
                };
                if(count == 1) {playerListDto.setMostHero1(hero);}
                else if(count == 2) {playerListDto.setMostHero2(hero);}
                else if(count == 3) {playerListDto.setMostHero3(hero);}
                if(mostCnt <= count) {break;}
            }

            stopWatch.stop();

            /** 영웅별 경쟁전 상세정보 추출 */
            Elements competitiveHerosDetail = competitiveDatas.select("div.js-stats");

            // 포지션별 승리 게임, 패배 게임 저장을 위한 변수
            Integer tankWinGame = 0; Integer tankLoseGame = 0; Integer dealWinGame = 0; Integer dealLoseGame = 0; Integer healWinGame = 0; Integer healLoseGame = 0;
            Integer totalWinGame = 0; Integer totalLoseGame = 0;

            for(Element heroDetails : competitiveHerosDetail) {
                Integer winGame = 0;
                String winRate = "0%";
                Integer loseGame = 0;
                String playTime = "00:00";
                String killPerDeath = "0";
                String spentOnFireAvg = "00:00";
                Long death = 1l;
                String deathAvg = "0";
                Long blockDamage = 0l;
                Long damageToHero = 0l;
                Long damageToShield = 0l;
                Integer entireGame = 0;
                String goldMedal = "0";
                String silverMedal = "0";
                String bronzeMedal = "0";
                String soloKillAvg = "0";
                Long heal = 0l;

                if("0x02E00000FFFFFFFF".equals(heroDetails.attr("data-category-id"))) {
                    Elements totalDatas = heroDetails.select("tr.DataTable-tableRow");

                    for (Element tr : totalDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000003F5":
                                td = tr.select("td");
                                totalWinGame = Integer.parseInt(td.last().text());
                                break;
                            case "0x086000000000042E":
                                td = tr.select("td");
                                totalLoseGame = Integer.parseInt(td.last().text());
                                break;
                            default:
                                break;
                        }
                    }

                    /**디바 시작 */
                }else if ("0x02E000000000007A".equals(heroDetails.attr("data-category-id"))) { //디바
                    //시간 확인
                    stopWatch.start("디바 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements dVaDatas = heroDetails.select("tr.DataTable-tableRow");

                    //Dva 영웅 특별 데이터
                    String mechaSuicideKillAvg = "0";
                    String mechaCallAvg = "0";

                    for (Element tr : dVaDatas) {
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
                            case "0x08600000000002D5":
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
                            case "0x08600000000004D1":
                                td = tr.select("td");
                                mechaSuicideKillAvg = td.last().text();
                                break;
                            case "0x08600000000004D0":
                                td = tr.select("td");
                                mechaCallAvg = td.last().text();
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
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Dva dva = new Dva(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), mechaSuicideKillAvg,
                            mechaCallAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    dvaRepositroy.save(dva);
                    chdDto.setDva(dva);
                    System.out.println("============================ dva data save success =======================================");
                    System.out.println(dva.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**오리사 시작 */
                } else if ("0x02E000000000013E".equals(heroDetails.attr("data-category-id"))) {  //오리사
                    //시간 확인
                    stopWatch.start("오리사 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements orisaDates = heroDetails.select("tr.DataTable-tableRow");

                    String damageAmpAvg = "0";

                    for (Element tr : orisaDates) {
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
                            case "0x08600000000004F3":
                                td = tr.select("td");
                                damageAmpAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Orisa orisa = new Orisa(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), damageAmpAvg, goldMedal,
                            silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    orisaRepository.save(orisa);
                    chdDto.setOrisa(orisa);
                    System.out.println("=============================orisa data save success======================================");
                    System.out.println(orisa.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();
//                    System.out.println(stopWatch.prettyPrint());

                    /**라인하르트 시작 */
                } else if ("0x02E0000000000007".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("라인하르트 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements reinhardtDates = heroDetails.select("tr.DataTable-tableRow");

                    //라인하르트 영웅 특별 데이터
                    String chargeKillAvg = "0";
                    String earthshatterKillAvg = "0";
                    String fireStrikeKillAvg = "0";

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
                            case "0x0860000000000259":
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
                            case "0x08600000000004E7":
                                td = tr.select("td");
                                earthshatterKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E5":
                                td = tr.select("td");
                                chargeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E8":
                                td = tr.select("td");
                                fireStrikeKillAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Reinhardt reinhardt = new Reinhardt(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg,
                            deathAvg, blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            earthshatterKillAvg, chargeKillAvg, fireStrikeKillAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    reinhardtRepository.save(reinhardt);
                    chdDto.setReinhardt(reinhardt);
                    System.out.println("============================reinhardt data save success===================================");
                    System.out.println(reinhardt.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**자리야 시작 */
                } else if ("0x02E0000000000068".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("자리야 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements zaryaDates = heroDetails.select("tr.DataTable-tableRow");

                    //자리야 영웅 특별 데이터
                    String energyAvg = "0";
                    String highEnergyKillAvg = "0";
                    String gravitonSurgeKillAvg = "0";
                    String projectedBarrierAvg = "0";

                    for (Element tr : zaryaDates) {
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
                            case "0x0860000000000225":
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
                            case "0x0860000000000231":
                                td = tr.select("td");
                                energyAvg = td.last().text();
                                break;
                            case "0x08600000000004F0":
                                td = tr.select("td");
                                highEnergyKillAvg = td.last().text();
                                break;
                            case "0x08600000000004F1":
                                td = tr.select("td");
                                gravitonSurgeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004EF":
                                td = tr.select("td");
                                projectedBarrierAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Zarya zarya = new Zarya(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), energyAvg,
                            highEnergyKillAvg, projectedBarrierAvg, gravitonSurgeKillAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    zaryaRepository.save(zarya);
                    chdDto.setZarya(zarya);
                    System.out.println("===============================zarya data save success====================================");
                    System.out.println(zarya.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**로드호그 시작 */
                } else if ("0x02E0000000000040".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("로드호그 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements roadhogDates = heroDetails.select("tr.DataTable-tableRow");

                    //로드호그 영웅 특별 데이터
                    String wholeHogKillAvg = "0";
                    String chainHookAccuracy = "0%";
                    String hookingEnemyAvg = "0";
                    Long selfHeal = 0l;

                    for (Element tr : roadhogDates) {
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
                            case "0x08600000000004DA":
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
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
                            case "0x0860000000000500":
                                td = tr.select("td");
                                wholeHogKillAvg = td.last().text();
                                break;
                            case "0x086000000000020B":
                                td = tr.select("td");
                                chainHookAccuracy = td.last().text();
                                break;
                            case "0x08600000000004FF":
                                td = tr.select("td");
                                hookingEnemyAvg = td.last().text();
                                break;
                            case "0x08600000000003E6":
                                td = tr.select("td");
                                selfHeal = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }

                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;
                    Double selfHealPerLife = Math.round((selfHeal / (double) death) * 100) / 100.0;

                    RoadHog roadhog = new RoadHog(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            soloKillAvg, damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), wholeHogKillAvg,
                            chainHookAccuracy, hookingEnemyAvg, selfHealPerLife.toString(), goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    roadhogRepository.save(roadhog);
                    chdDto.setRoadHog(roadhog);
                    System.out.println("============================roadhog data save success=====================================");
                    System.out.println(roadhog.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**윈스턴 시작 */
                } else if ("0x02E0000000000009".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("윈스턴 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements winstonDates = heroDetails.select("tr.DataTable-tableRow");

                    //윈스턴 영웅 특별 데이터
                    String jumpPackKillAvg = "0";
                    String primalRageKillAvg = "0";
                    String pushEnmeyAvg = "0";

                    for (Element tr : winstonDates) {
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
                            case "0x0860000000000272":
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
                            case "0x0860000000000508":
                                td = tr.select("td");
                                jumpPackKillAvg = td.last().text();
                                break;
                            case "0x086000000000050B":
                                td = tr.select("td");
                                primalRageKillAvg = td.last().text();
                                break;
                            case "0x086000000000050A":
                                td = tr.select("td");
                                pushEnmeyAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Winston winston = new Winston(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), jumpPackKillAvg,
                            primalRageKillAvg, pushEnmeyAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    winstonRepository.save(winston);
                    chdDto.setWinston(winston);
                    System.out.println("============================winston data save success+====================================");
                    System.out.println(winston.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**시그마 시작 */
                } else if ("0x02E000000000023B".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("시그마 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements sigmaDates = heroDetails.select("tr.DataTable-tableRow");

                    //시그마 영웅 특별 데이터
                    Long absorptionDamage = 0l;
                    String graviticFluxKillAvg = "0";
                    String accretionKillAvg = "0";

                    for (Element tr : sigmaDates) {
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
                            case "0x08600000000006A1":
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
                            case "0x08600000000006B8":
                                td = tr.select("td");
                                absorptionDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000006C0":
                                td = tr.select("td");
                                graviticFluxKillAvg = td.last().text();
                                break;
                            case "0x08600000000006BB":
                                td = tr.select("td");
                                accretionKillAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;
                    Double absorptionDamagePerLife = Math.round((absorptionDamage / (double) death) * 100) / 100.0;

                    Sigma sigma = new Sigma(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), absorptionDamagePerLife.toString(),
                            graviticFluxKillAvg, accretionKillAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    sigmaRepository.save(sigma);
                    chdDto.setSigma(sigma);
                    System.out.println("============================sigma data save success=======================================");
                    System.out.println(sigma.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**레킹볼 시작 */
                } else if ("0x02E000000000023B".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("레킹볼 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements wreckingBallDates = heroDetails.select("tr.DataTable-tableRow");

                    //레킹볼 영웅 특별 데이터
                    String grapplingClawKillAvg = "0";
                    String piledriverKillAvg = "0";
                    String minefieldKillAvg = "0";

                    for (Element tr : wreckingBallDates) {
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
                            case "0x086000000000064C":
                                td = tr.select("td");
                                grapplingClawKillAvg = td.last().text();
                                break;
                            case "0x086000000000064F":
                                td = tr.select("td");
                                piledriverKillAvg = td.last().text();
                                break;
                            case "0x086000000000064D":
                                td = tr.select("td");
                                minefieldKillAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    WreckingBall wreckingBall = new WreckingBall(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), grapplingClawKillAvg,
                            piledriverKillAvg, minefieldKillAvg, goldMedal, silverMedal, bronzeMedal);

                    tankWinGame += winGame;
                    tankLoseGame += loseGame;

                    wreckingBallRepository.save(wreckingBall);
                    chdDto.setWreckingBall(wreckingBall);
                    System.out.println("============================wreckingBall data save success================================");
                    System.out.println(wreckingBall.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**아나 시작*/
                } else if ("0x02E000000000013B".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("아나 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements anaDates = heroDetails.select("tr.DataTable-tableRow");

                    //아나 영웅 특별 데이터
                    String nanoBoosterAvg = "0";
                    Long bioticGrenadeKill = 0l;
                    String sleepDartAvg = "0";

                    for (Element tr : anaDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x08600000000004C7":
                                td = tr.select("td");
                                nanoBoosterAvg = td.last().text();
                                break;
                            case "0x086000000000043C":
                                td = tr.select("td");
                                bioticGrenadeKill = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004C5":
                                td = tr.select("td");
                                sleepDartAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double bioticGrenadeKillPerLife = Math.round((bioticGrenadeKill / (double) death) * 100) / 100.0;

                    Ana ana = new Ana(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), nanoBoosterAvg, sleepDartAvg, bioticGrenadeKillPerLife.toString()
                            , goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    anaRepository.save(ana);
                    chdDto.setAna(ana);
                    System.out.println("============================ana data save success================================");
                    System.out.println(ana.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**바티스트 시작*/
                } else if ("0x02E0000000000221".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("바티스트 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements baptisteDates = heroDetails.select("tr.DataTable-tableRow");

                    //바티스트 영웅 특별 데이터
                    String immortalityFieldSaveAvg = "0";
                    String amplificationMatrixAvg = "0";

                    for (Element tr : baptisteDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
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
                            case "0x086000000000069A":
                                td = tr.select("td");
                                immortalityFieldSaveAvg = td.last().text();
                                break;
                            case "0x08600000000006AE":
                                td = tr.select("td");
                                amplificationMatrixAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / (double) death) * 100) / 100.0;

                    Baptiste baptiste = new Baptiste(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), immortalityFieldSaveAvg,
                            amplificationMatrixAvg, goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    baptisteRepository.save(baptiste);
                    chdDto.setBaptiste(baptiste);
                    System.out.println("============================baptiste data save success================================");
                    System.out.println(baptiste.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**브리기테 시작*/
                } else if ("0x02E0000000000195".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("브리기테 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements brigitteDates = heroDetails.select("tr.DataTable-tableRow");

                    //브리기테 영웅 특별 데이터
                    Long armor = 0l;
                    String inspireActiveRate = "0%";

                    for (Element tr : brigitteDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x0860000000000607":
                                td = tr.select("td");
                                armor = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000612":
                                td = tr.select("td");
                                inspireActiveRate = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double armorPerLife = Math.round((armor / (double) death) * 100) / 100.0;

                    Brigitte brigitte = new Brigitte(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), armorPerLife.toString(), inspireActiveRate,
                            goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    brigitteRepository.save(brigitte);
                    chdDto.setBrigitte(brigitte);
                    System.out.println("============================brigitte data save success================================");
                    System.out.println(brigitte.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**루시우 시작*/
                } else if ("0x02E0000000000079".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("루시우 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements lucioDates = heroDetails.select("tr.DataTable-tableRow");

                    //루시우 영웅 특별 데이터
                    String soundwaveAvg = "0";

                    for (Element tr : lucioDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x08600000000004D2":
                                td = tr.select("td");
                                soundwaveAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;

                    Lucio lucio = new Lucio(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), soundwaveAvg, goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    lucioRepository.save(lucio);
                    chdDto.setLucio(lucio);
                    System.out.println("============================lucio data save success================================");
                    System.out.println(lucio.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**메르시 시작*/
                } else if ("0x02E0000000000004".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("메르시 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements mercyDates = heroDetails.select("tr.DataTable-tableRow");

                    //메르시 영웅 특별 데이터
                    String resurrectAvg = "0";
                    String damageAmpAvg = "0";

                    for (Element tr : mercyDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x08600000000004C9":
                                td = tr.select("td");
                                resurrectAvg = td.last().text();
                                break;
                            case "0x08600000000004F3":
                                td = tr.select("td");
                                damageAmpAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;

                    Mercy mercy = new Mercy(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), resurrectAvg, damageAmpAvg, goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    mercyRepository.save(mercy);
                    chdDto.setMercy(mercy);
                    System.out.println("============================mercy data save success================================");
                    System.out.println(mercy.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**모이라 시작*/
                } else if ("0x02E00000000001A2".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("모이라 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements moiraDates = heroDetails.select("tr.DataTable-tableRow");

                    //모이라 영웅 특별 데이터
                    Long selfHeal = 0l;
                    String coalescenceKillAvg = "0";

                    for (Element tr : moiraDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x08600000000004C9":
                                td = tr.select("td");
                                selfHeal = Long.parseLong(td.last().text());
                                break;
                            case "0x086000000000058A":
                                td = tr.select("td");
                                coalescenceKillAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;
                    Double selfHealPerLife = Math.round((selfHeal / (double) death) * 100) / 100.0;

                    Moira moira = new Moira(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), coalescenceKillAvg, selfHealPerLife.toString(), goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    moiraRepository.save(moira);
                    chdDto.setMoira(moira);
                    System.out.println("============================moira data save success================================");
                    System.out.println(moira.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    /**젠야타 시작*/
                } else if ("0x02E0000000000020".equals(heroDetails.attr("data-category-id"))) {
                    //시간 확인
                    stopWatch.start("젠야타 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                    Elements zenyattaDates = heroDetails.select("tr.DataTable-tableRow");

                    //젠야타 영웅 특별 데이터
                    String transcendenceHealAvg = "0";

                    for (Element tr : zenyattaDates) {
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
                            case "0x08600000000003F7":
                                td = tr.select("td");
                                heal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004B7":
                                td = tr.select("td");
                                damageToHero = Long.parseLong(td.last().text());
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
                            case "0x0860000000000352":
                                td = tr.select("td");
                                transcendenceHealAvg = td.last().text();
                                break;
                            case "0x0860000000000038":
                                td = tr.select("td");
                                entireGame = Integer.parseInt(td.last().text());
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / (double) death) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / (double) death) * 100) / 100.0;

                    Zenyatta zenyatta = new Zenyatta(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), transcendenceHealAvg, goldMedal, silverMedal, bronzeMedal);

                    healWinGame += winGame;
                    healLoseGame += loseGame;

                    zenyattaRepository.save(zenyatta);
                    chdDto.setZenyatta(zenyatta);
                    System.out.println("============================zenyatta data save success================================");
                    System.out.println(zenyatta.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();
                }
            }

            stopWatch.start("player 테이블에 저장");
            Player player = new Player(playerListDto.getId(), playerListDto.getBattleTag(), playerListDto.getPlayerName(), playerListDto.getPlayerLevel(), playerListDto.getForUrl(), playerListDto.getIsPublic(), playerListDto.getPlatform()
                    , playerListDto.getPortrait(), playerListDto.getTankRatingPoint(), playerListDto.getDealRatingPoint(), playerListDto.getHealRatingPoint()
                    , playerListDto.getTankRatingImg(), playerListDto.getDealRatingImg(), playerListDto.getHealRatingImg(), tankWinGame, tankLoseGame,dealWinGame,dealLoseGame,healWinGame,healLoseGame
                    , totalWinGame, totalLoseGame, playerListDto.getDrawGame(), playerListDto.getMostHero1(), playerListDto.getMostHero2(), playerListDto.getMostHero3());
            playerRepository.save(player);
            chdDto.setPlayer(player);
            stopWatch.stop();
        }catch(Exception e) {
            e.printStackTrace();
        }
        // 시간 확인
        System.out.println(stopWatch.prettyPrint());
        return chdDto;
    }

    public String saveImg(StopWatch stopWatch, String imgUrl, String imgName, String savePath) {
        // 시간 확인
        stopWatch.stop();
        stopWatch.start(savePath+" 이미지 저장");
        String forDB = "/HWimages/"+savePath+"/"+ imgName + ".png";
        File file = new File(portraitPath+ savePath + "/" +imgName + ".png");
        if(!file.exists()) { // 파일 미존재시 저장
            /** 이미지 저장*/
            try {
                URL url = new URL(imgUrl);
                BufferedImage bi = ImageIO.read(url);
                ImageIO.write(bi, "png", new File(portraitPath + savePath + "/" + imgName + ".png"));
            } catch (IIOException | MalformedURLException e) {
                forDB = "/HWimages/" + savePath + "/default.png";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("이미 존재하는 이미지입니다. (" + forDB + ")");
        }
        return forDB;
    }

    /** 사설 오버워치 api (ow-api.com) 에서 데이터 받아옴  (미사용) */
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
