package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class CrawlingPlayerDataService {
    private static String GET_PLAYER_LIST_URL = "https://playoverwatch.com/ko-kr/search/account-by-name/";
    private static String GET_PLAYER_PROFILE_URL = "https://ow-api.com/v1/stats/pc/asia/";
    private List<PlayerListDto> playerList;

    public List<PlayerListDto>  crawlingPlayerList(String playerName) {
        // 반환할 playerListDto 초기화
        playerList = new ArrayList<PlayerListDto>();

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
                    playerListDto = crawlingPlayerProfile(playerListDto);
                    System.out.println(playerListDto.getTankRatingPoint());
                }
//                playerListDto.toString();
                playerList.add(playerListDto);
                Collections.sort(playerList);

//                System.out.println(pName +" / "+ battleTag);
            }
        } catch(Exception e) {
            if(e.getClass() == SocketException.class) {
                PlayerListDto playerListDto = new PlayerListDto("message","현재 배틀넷 서버 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","", 0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == UnknownHostException.class){
                PlayerListDto playerListDto = new PlayerListDto("message","연결된 인터넷에서 배틀넷 서버로 접속할 수 없습니다.","",0,"","","",0,0,0);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }
            e.printStackTrace();
        }
        return playerList;
    }

    public PlayerListDto crawlingPlayerProfile(PlayerListDto playerListDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("crawlingPlayerProfile working -> player url : " + playerListDto.getForUrl());
            // Jsoup를 이용해 오버워치 웹크롤링 : 유저 프로필을 json String 형식으로 가져오는 부분
            String json = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getForUrl()+"/profile")
                    .ignoreContentType(true)
                    .execute().body();
            JsonNode jsonNode = mapper.readTree(json);
            System.out.println("jsonNode : " + jsonNode);
            if (jsonNode.isObject()) {
                ObjectNode obj = (ObjectNode) jsonNode;
                if(obj.has("ratings")){
                    JsonNode ratings = obj.get("ratings");
                    System.out.println(ratings);
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
//                    JsonNode tankRating = obj.get("ratings").get(0);
//                    if(tankRating.isObject()) {
//                        ObjectNode tankObj = (ObjectNode) tankRating;
//                        if(tankObj.has("level")) {
//                            Integer tankRatingPoint = tankObj.get("level").asInt();
//                            System.out.println(tankRatingPoint);
//                            playerListDto.setTankRatingPoint(tankRatingPoint);
//                        }
//                    }
//                    JsonNode dealRating = obj.get("ratings").get(1);
//                    if(dealRating.isObject()) {
//                        ObjectNode dealObj = (ObjectNode) dealRating;
//                        if(dealObj.has("level")) { playerListDto.setTankRatingPoint(dealObj.get("level").asInt()); }
//                    }
//                    JsonNode healRating = obj.get("ratings").get(2);
//                    if(healRating.isObject()) {
//                        ObjectNode healObj = (ObjectNode) healRating;
//                        if(healObj.has("level")) { playerListDto.setTankRatingPoint(healObj.get("level").asInt()); }
//                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return playerListDto;
    }
}
