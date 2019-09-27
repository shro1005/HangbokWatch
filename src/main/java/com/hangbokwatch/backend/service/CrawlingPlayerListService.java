package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CrawlingPlayerListService {
    private static String URL = "https://playoverwatch.com/ko-kr/search/account-by-name/";
    private List<PlayerListDto> playerList;

    public List<PlayerListDto>  crawling(String playerName) {
        playerList = new ArrayList<PlayerListDto>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("crawling working -> player name : " + playerName);
            String json = Jsoup.connect(URL+playerName)
                    .ignoreContentType(true)
                    .execute().body();
            System.out.println("===== crawling result ===== \n" + json);
            List<PlayerCrawlingResultDto> playerCrawlingResultDtoList = mapper.readValue(json, new TypeReference<List<PlayerCrawlingResultDto>>(){});

            for(PlayerCrawlingResultDto dto : playerCrawlingResultDtoList) {
                String name = dto.getName();
                String pName = name.substring(0,name.indexOf("#"));
                String battleTag = name.substring(name.indexOf("#")+1, name.length());
                String portrait = "https://d1u1mce87gyfbn.cloudfront.net/game/unlocks/" + dto.getPortrait() + ".png";
                String isPublic = "N";
                if(dto.getIsPublic()) {isPublic ="Y";}

                PlayerListDto playerListDto = new PlayerListDto(battleTag, pName, dto.getPlayerLevel(), isPublic, dto.getPlatform(), portrait);
                playerListDto.toString();
                playerList.add(playerListDto);
                Collections.sort(playerList);

                System.out.println(pName +" / "+ battleTag);
            }
        } catch(Exception e) {
            if(e.getClass() == SocketException.class) {
                PlayerListDto playerListDto = new PlayerListDto("message","현재 배틀넷 서버 오류로 플레이어 목록을 불러올 수 없습니다.",0,"","","");
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == UnknownHostException.class){
                PlayerListDto playerListDto = new PlayerListDto("message","연결된 인터넷에서 배틀넷 서버로 접속할 수 없습니다.",0,"","","");
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }
            e.printStackTrace();
        }
        return playerList;
    }
}
