package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.player.PlayerRepository;
import com.hangbokwatch.backend.dao.user.FavoriteRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.user.Favorite;
import com.hangbokwatch.backend.dto.FavoriteDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.auth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowUserFavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    PlayerRepository playerRepository;


    private final HttpSession httpSession;

    public List<PlayerListDto> findMyFavoritePlayerList(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> findMyFavoritePlayerList 호출 | 내가 즐겨찾기한 플레이어 데이터 조회 시작", sessionBattleTag);

        List<PlayerListDto> playerListDtoList = new ArrayList<PlayerListDto>();

        //로그인한 유저인지 확인하여 즐겨찾기를 조회
        SessionUser sessionUser = (SessionUser)sessionItems.get("loginUser");
        try {
            if (sessionUser == null) {
                //로그인 하지 않은 유저인 경우 session을 검색
                log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | 세션에 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag);

                Map<Long, FavoriteDto> favoriteList = (Map<Long, FavoriteDto>) httpSession.getAttribute("favoriteList");
                if (favoriteList != null) {
                    Iterator<Long> ids = favoriteList.keySet().iterator();
                    while (ids.hasNext()) {
                        Long id = ids.next();
                        log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | 세션에 저장된 즐겨찾기 유저id : {} ", sessionBattleTag, id);
                        Player player = playerRepository.findPlayerById(id);

                        Double winRate = (new Double(player.getWinGame()) / new Double(player.getWinGame() + player.getLoseGame()) * 100);
                        Integer winRateInt = (int) (double) winRate;

                        PlayerListDto playerListDto = new PlayerListDto(player.getId(), player.getBattleTag(), player.getPlayerName(), player.getForUrl(), player.getPlayerLevel()
                                , player.getIsPublic(), player.getPlatform(), player.getPortrait(), player.getTankRatingPoint(), player.getDealRatingPoint(), player.getHealRatingPoint()
                                , player.getTankRatingImg(), player.getDealRatingImg(), player.getHealRatingImg()
                                , player.getWinGame(), player.getLoseGame(), player.getDrawGame(), Integer.toString(winRateInt)
                                , player.getMostHero1(), player.getMostHero2(), player.getMostHero3(), player.getUdtDtm().format(DateTimeFormatter.ISO_DATE));

                        log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | 세션에 저장된 즐겨찾기 유저 정보 : {} ", sessionBattleTag, playerListDto);

                        int cnt = 3;
                        if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
                        if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
                        if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
                        if(cnt == 0 ) {cnt = 1;}
                        playerListDto.setCnt(cnt);
                        playerListDtoList.add(playerListDto);
                    }
                } else {
                    log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | 세션에 저장된 즐겨찾기 유저가 존재하지 않음.", sessionBattleTag);
                }
            } else {
                log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | DB에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag);

                List<Favorite> favoriteList = favoriteRepository.findFavoritesByIdAndLikeornot(sessionUser.getId(), "Y");
                for (Favorite favorite : favoriteList) {
                    Long id = favorite.getClickedId();
                    log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | DB에 저장된 즐겨찾기 유저id : {} ", sessionBattleTag, id);
                    Player player = playerRepository.findPlayerById(id);

                    Double winRate = (new Double(player.getWinGame()) / new Double(player.getWinGame() + player.getLoseGame()) * 100);
                    Integer winRateInt = (int) (double) winRate;

                    PlayerListDto playerListDto = new PlayerListDto(player.getId(), player.getBattleTag(), player.getPlayerName(), player.getForUrl(), player.getPlayerLevel()
                            , player.getIsPublic(), player.getPlatform(), player.getPortrait(), player.getTankRatingPoint(), player.getDealRatingPoint(), player.getHealRatingPoint()
                            , player.getTankRatingImg(), player.getDealRatingImg(), player.getHealRatingImg()
                            , player.getWinGame(), player.getLoseGame(), player.getDrawGame(), Integer.toString(winRateInt)
                            , player.getMostHero1(), player.getMostHero2(), player.getMostHero3(), player.getUdtDtm().format(DateTimeFormatter.ISO_DATE));

                    log.debug("{} >>>>>>>> findMyFavoritePlayerList 진행중 | DB에 저장된 즐겨찾기 유저 정보 : {} ", sessionBattleTag, playerListDto);

                    int cnt = 3;
                    if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
                    if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
                    if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
                    if(cnt == 0 ) {cnt = 1;}
                    playerListDto.setCnt(cnt);
                    playerListDtoList.add(playerListDto);
                }
            }
        }catch (NullPointerException e) {
            playerListDtoList = null;
        }
        Collections.sort(playerListDtoList);

        log.info("{} >>>>>>>> findMyFavoritePlayerList 종료 | 내가 즐겨찾기한 플레이어 데이터 조회 종료", sessionBattleTag);
        return playerListDtoList;
    }

    public List<PlayerListDto> findFavoritingMePlayerList(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> findFavoritingMePlayerList 호출 | 나를 즐겨찾기한 플레이어 데이터 조회 시작", sessionBattleTag);
        List<PlayerListDto> playerListDtoList = new ArrayList<PlayerListDto>();

        //로그인한 유저인지 확인하여 즐겨찾기를 조회
        SessionUser sessionUser = (SessionUser)sessionItems.get("loginUser");

        if(sessionUser == null) {
            log.debug("{} >>>>>>>> findFavoritingMePlayerList 종료 | 배틀넷 미연동 유저는 해당 기능을 이용할 수 없음.", sessionBattleTag);

            return null;

        }else {
            log.debug("{} >>>>>>>> findFavoritingMePlayerList 진행중 | DB에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag);
            try {
                List<Favorite> favoriteList = favoriteRepository.findFavoritesByClickedIdAndLikeornot(sessionUser.getId(), "Y");
                for (Favorite favorite : favoriteList) {
                    Long id = favorite.getId();
                    log.debug("{} >>>>>>>> findFavoritingMePlayerList 진행중 | DB에 저장된 나를 즐겨찾기한 유저id : {} ", sessionBattleTag, id);
                    Player player = playerRepository.findPlayerById(id);

                    Double winRate = (new Double(player.getWinGame())/new Double(player.getWinGame()+player.getLoseGame())*100);
                    Integer winRateInt = (int) (double) winRate;

                    PlayerListDto playerListDto = new PlayerListDto(player.getId(), player.getBattleTag(), player.getPlayerName(), player.getForUrl(), player.getPlayerLevel()
                            , player.getIsPublic(), player.getPlatform(), player.getPortrait(), player.getTankRatingPoint(), player.getDealRatingPoint(), player.getHealRatingPoint()
                            , player.getTankRatingImg(), player.getDealRatingImg(), player.getHealRatingImg()
                            , player.getWinGame(), player.getLoseGame(), player.getDrawGame(), Integer.toString(winRateInt)
                            , player.getMostHero1(), player.getMostHero2(), player.getMostHero3(), player.getUdtDtm().format(DateTimeFormatter.ISO_DATE));

                    log.debug("{} >>>>>>>> findFavoritingMePlayerList 진행중 | DB에 저장된 나를 즐겨찾기한 유저 정보 : {} ", sessionBattleTag, playerListDto);

                    int cnt = 3;
                    if(playerListDto.getTankRatingPoint() == 0) {cnt--;}
                    if(playerListDto.getDealRatingPoint() == 0) {cnt--;}
                    if(playerListDto.getHealRatingPoint() == 0) {cnt--;}
                    if(cnt == 0 ) {cnt = 1;}
                    playerListDto.setCnt(cnt);
                    playerListDtoList.add(playerListDto);
                }
            }catch (NullPointerException e) {
                playerListDtoList = null;
            }
        }
        Collections.sort(playerListDtoList);
        log.info("{} >>>>>>>> findFavoritingMePlayerList 종료 | 나를 즐겨찾기한 플레이어 데이터 조회 종료", sessionBattleTag);
        return playerListDtoList;
    }
}
