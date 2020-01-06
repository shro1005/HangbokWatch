package com.hangbokwatch.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.dao.hero.*;
import com.hangbokwatch.backend.dao.player.PlayerDetailRepository;
import com.hangbokwatch.backend.dao.player.PlayerRepository;
import com.hangbokwatch.backend.dao.player.TrendlineRepository;
import com.hangbokwatch.backend.domain.hero.*;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.domain.player.Trendline;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerCrawlingResultDto;
import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import sun.security.krb5.internal.ccache.CredentialsCache;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CrawlingPlayerDataService {
    private static String GET_PLAYER_LIST_URL = "https://playoverwatch.com/ko-kr/search/account-by-name/";
//    private static String GET_PLAYER_PROFILE_URL2 = "https://ow-api.com/v1/stats/pc/asia/";  //미사용
    private static String GET_PLAYER_PROFILE_URL = "https://playoverwatch.com/ko-kr/career/";
//    private List<PlayerListDto> playerList;
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerDetailRepository playerDetailRepository;
    @Autowired TrendlineRepository trendlineRepository;
    @Autowired SeasonRepository seasonRepository;

    @Autowired DvaRepositroy dvaRepositroy;
    @Autowired OrisaRepository orisaRepository;
    @Autowired ReinhardtRepository reinhardtRepository;
    @Autowired ZaryaRepository zaryaRepository;
    @Autowired RoadHogRepository roadhogRepository;
    @Autowired WinstonRepository winstonRepository;
    @Autowired SigmaRepository sigmaRepository;
    @Autowired WreckingBallRepository wreckingBallRepository;
    @Autowired AnaRepository anaRepository;
    @Autowired BaptisteRepository baptisteRepository;
    @Autowired BrigitteRepository brigitteRepository;
    @Autowired LucioRepository lucioRepository;
    @Autowired MercyRepository mercyRepository;
    @Autowired MoiraRepository moiraRepository;
    @Autowired ZenyattaRepository zenyattaRepository;
    @Autowired JunkratRepository junkratRepository;
    @Autowired GenjiRepository genjiRepository;
    @Autowired DoomfistRepository doomfistRepository;
    @Autowired ReaperRepository reaperRepository;
    @Autowired MccreeRepository mccreeRepository;
    @Autowired MeiRepository meiRepository;
    @Autowired BastionRepository bastionRepository;
    @Autowired Soldier76Repository soldier76Repository;
    @Autowired SombraRepository sombraRepository;
    @Autowired SymmetraRepository symmetraRepository;
    @Autowired AsheRepository asheRepository;
    @Autowired WidowmakerRepository widowmakerRepository;
    @Autowired TorbjornRepository torbjornRepository;
    @Autowired TracerRepository tracerRepository;
    @Autowired PharahRepository pharahRepository;
    @Autowired HanzoRepository hanzoRepository;

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
                String isPublic = "N";
                if(dto.getIsPublic()) {
                    isPublic = "Y";
                }else {
                    return playerList;
                }
                String battleTag = dto.getName();
                String pName = battleTag;
                if(dto.getPlatform().equals("pc")) {
                    pName = battleTag.substring(0, battleTag.indexOf("#"));
                }
                String forUrl = dto.getUrlName();
                String portrait = "https://d1u1mce87gyfbn.cloudfront.net/game/unlocks/" + dto.getPortrait() + ".png";  //d1u1mce87gyfbn.cloudfront.net

                Integer tankratingPoint = 0; Integer dealRatingPoint = 0; Integer healRatingPoint = 0;
                PlayerListDto playerListDto = new PlayerListDto(dto.getId(), battleTag, pName, forUrl, dto.getPlayerLevel(), isPublic, dto.getPlatform(), portrait, tankratingPoint, dealRatingPoint, healRatingPoint, null);

                playerListDto.setCnt(3);
                System.out.println(playerListDto.toString());
                playerList.add(playerListDto);
            }
            Collections.sort(playerList);
        } catch(Exception e) {
            if(e.getClass() == SocketException.class) {
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","현재 배틀넷 서버 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","", 0,0,0, null);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == UnknownHostException.class){
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","연결된 인터넷에서 배틀넷 서버로 접속할 수 없습니다.","",0,"","","",0,0,0, null);
                playerList.add(playerListDto);
                System.out.println("블리자드 내부 에러 발생");
            }else if(e.getClass() == SocketTimeoutException.class){
                PlayerListDto playerListDto = new PlayerListDto((long) 0,"message","현재 배틀넷 서버 내부 오류로 플레이어 목록을 불러올 수 없습니다.","",0,"","","",0,0,0, null);
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
            // rawData 추출

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
            Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+playerListDto.getPlatform()+"/"+playerListDto.getForUrl()).maxBodySize(Integer.MAX_VALUE)
                    .get();

            // 시간 확인
            stopWatch.stop();
//            //rawdata 추출
//            File file = new File(portraitPath + "raw_"+playerListDto.getPlayerName()+".txt");
//            try {
//                //파일에 문자열을 쓴다.
//                //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
//                //파일이 손산될 우려가 있다.
//                FileWriter fw = new FileWriter(file);
//                fw.write(rawData.html());
//                fw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            stopWatch.start("경쟁전 역할별 점수 및 티어 이미지 추출");
            Elements competitiveRole = rawData.select("div.competitive-rank-role");
            String tierUrl ="";
            String substrTier = "";
            for (Element roleElement : competitiveRole) {
                Element roleIcon = roleElement.selectFirst("img[class=competitive-rank-role-icon]");
                if("https://static.playoverwatch.com/img/pages/career/icon-tank-8a52daaf01.png".equals(roleIcon.attr("src"))){
                    playerListDto.setTankRatingPoint(Integer.parseInt(roleElement.text()));

                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));

                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
                    playerListDto.setTankRatingImg(tierUrl);
                }else if("https://static.playoverwatch.com/img/pages/career/icon-offense-6267addd52.png".equals(roleIcon.attr("src"))){
                    playerListDto.setDealRatingPoint(Integer.parseInt(roleElement.text()));

                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));

                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
                    playerListDto.setDealRatingImg(tierUrl);
                }else if("https://static.playoverwatch.com/img/pages/career/icon-support-46311a4210.png".equals(roleIcon.attr("src"))){
                    playerListDto.setHealRatingPoint(Integer.parseInt(roleElement.text()));

                    // 티어 이미지 추출
                    Element tierIcon = roleElement.selectFirst("img[class=competitive-rank-tier-icon]");
                    tierUrl = tierIcon.attr("src");
                    substrTier = tierUrl.substring(tierUrl.indexOf("rank-icons/")+11, tierUrl.indexOf(".png"));

                    // 티어 이미지 저장
                    tierUrl = saveImg(stopWatch, tierUrl, substrTier, "tier");
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

            stopWatch.stop();

            /** 영웅 상세정보 추출 */
            Integer tankWinGame = 0; Integer tankLoseGame = 0; Integer dealWinGame = 0; Integer dealLoseGame = 0; Integer healWinGame = 0; Integer healLoseGame = 0;
            Integer totalWinGame = 0; Integer totalLoseGame = 0;

            Element competitiveDatas = rawData.selectFirst("div#competitive");
//            //competitiveDatas 추출
//            file = new File(portraitPath + "competitiveData_html_"+playerListDto.getPlayerName()+".txt");
//            try {
//                //파일에 문자열을 쓴다.
//                //하지만 이미 파일이 존재하면 모든 내용을 삭제하고 그위에 덮어쓴다
//                //파일이 손산될 우려가 있다.
//                FileWriter fw = new FileWriter(file);
//                fw.write(competitiveDatas.html());
//                fw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            Element progressData = competitiveDatas.selectFirst("div.progress-category");
            Elements mostHeros = progressData.select("div.ProgressBar-title");

            Long season = seasonRepository.selectSeason(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));

            int count = 0;
            playerDetailRepository.deletePlayerDetailsByIdAndSeason(playerListDto.getId(), season);

            System.out.println(mostHeros.text());
            for(Element mostHero : mostHeros) {
                PlayerDetailDto pdDto = new PlayerDetailDto();
                pdDto.setId(playerListDto.getId());
                pdDto.setSeason(season);

                count++;
                String hero = mostHero.text().trim();
                pdDto.setOrder(count);
                if("D.Va".equals(hero)) {
                    pdDto.setHeroNameKR("디바");
                }else {
                    pdDto.setHeroNameKR(hero);
                }
                System.out.println(mostHero.text());
                List<Integer> winLoseGame;

                //heroListKR.add(hero);
                switch (hero) {
                    case "솔저: 76":  hero = "soldier-76"; winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000006E", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "리퍼":      hero = "reaper";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000002", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "정크랫":     hero = "junkrat";    winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000065", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "위도우메이커":  hero = "widowmaker"; winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000000A", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "토르비욘":    hero = "torbjorn";   winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000006", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "트레이서":    hero = "tracer";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000003", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "파라":      hero = "pharah";      winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000008", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "한조":      hero = "hanzo";       winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000005", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "애쉬":      hero = "ashe";        winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000200", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "시메트라":    hero = "symmetra";   winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000016", hero);  dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1); break;
                    case "솜브라":     hero = "sombra";      winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000012E", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "바스티온":    hero = "bastion";    winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000015", hero);  dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1); break;
                    case "메이":      hero = "mei";         winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E00000000000DD", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "맥크리":     hero = "mccree";      winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000042", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "둠피스트":    hero = "doomfist";    winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000012F", hero); dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1);  break;
                    case "겐지":      hero = "genji";       winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000029", hero);  dealWinGame += winLoseGame.get(0); dealLoseGame += winLoseGame.get(1); break;
                    case "오리사":     hero = "orisa";       winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000013E", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "시그마":     hero = "sigma";       winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000023B", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1);  break;
                    case "자리야":     hero = "zarya";       winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000068", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1);  break;
                    case "라인하르트":   hero = "reinhardt";  winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000007", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "D.Va":     hero = "D.Va";        winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000007A", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "윈스턴":     hero = "winston";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000009", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "로드호그":    hero = "roadhog";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000040", hero);  tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "레킹볼":     hero = "wreckingball"; winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E00000000001CA", hero); tankWinGame += winLoseGame.get(0); tankLoseGame += winLoseGame.get(1); break;
                    case "모이라":     hero = "moira";        winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E00000000001A2", hero); healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1);  break;
                    case "아나":      hero = "ana";          winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E000000000013B", hero);  healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1); break;
                    case "브리기테":    hero = "brigitte";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000195", hero); healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1);  break;
                    case "바티스트":    hero = "baptiste";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000221", hero); healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1);  break;
                    case "젠야타":     hero = "zenyatta";     winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000020", hero);  healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1); break;
                    case "루시우":     hero = "lucio";        winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000079", hero);  healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1); break;
                    case "메르시":     hero = "mercy";        winLoseGame = heroDetailParsing(playerListDto, pdDto, competitiveDatas, stopWatch, "0x02E0000000000004", hero);  healWinGame += winLoseGame.get(0); healLoseGame += winLoseGame.get(1); break;
                    default: break;
                }
                if(count == 1) {playerListDto.setMostHero1(hero);}
                else if(count == 2) {playerListDto.setMostHero2(hero);}
                else if(count == 3) {playerListDto.setMostHero3(hero);}

                //영어 이름 세팅
                pdDto.setHeroName(hero);

            }
            // 플레이어 전체 승수 및 패배수
            List<Integer> winLoseGame = heroDetailParsing(playerListDto, null, competitiveDatas, stopWatch, "0x02E00000FFFFFFFF", "");
            totalWinGame = winLoseGame.get(0); totalLoseGame = winLoseGame.get(1);

            stopWatch.start("player 테이블에 저장");
            Player player = new Player(playerListDto.getId(), playerListDto.getBattleTag(), playerListDto.getPlayerName(), playerListDto.getPlayerLevel(), playerListDto.getForUrl(), playerListDto.getIsPublic(), playerListDto.getPlatform()
                    , playerListDto.getPortrait(), playerListDto.getTankRatingPoint(), playerListDto.getDealRatingPoint(), playerListDto.getHealRatingPoint()
                    , playerListDto.getTankRatingImg(), playerListDto.getDealRatingImg(), playerListDto.getHealRatingImg(), tankWinGame, tankLoseGame,dealWinGame,dealLoseGame,healWinGame,healLoseGame
                    , totalWinGame, totalLoseGame, playerListDto.getDrawGame(), playerListDto.getMostHero1(), playerListDto.getMostHero2(), playerListDto.getMostHero3());
            playerRepository.save(player);

            Trendline trendline = new Trendline(playerListDto.getId(), new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis())
                                                ,playerListDto.getTankRatingPoint(), playerListDto.getDealRatingPoint(), playerListDto.getHealRatingPoint()
                                                ,tankWinGame, tankLoseGame, dealWinGame, dealLoseGame, healWinGame, healLoseGame);

            trendlineRepository.save(trendline);
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

    public List<Integer> heroDetailParsing(PlayerListDto playerListDto, PlayerDetailDto pdDto, Element competitiveDatas
            , StopWatch stopWatch, String tag, String hero ) {
        /** 영웅별 경쟁전 상세정보 추출 */
        Element heroDetails = competitiveDatas.selectFirst("div[data-category-id="+tag+"]");
        List<Integer> winLoseGame = new ArrayList<Integer>();

        if(heroDetails != null) {
            System.out.println(heroDetails.text());

            // 영웅별 데이 저장을 위한 변수
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
            Long lastHit = 0l;
            Long heal = 0l;

            if ("0x02E00000FFFFFFFF".equals(heroDetails.attr("data-category-id"))) {
                Elements totalDatas = heroDetails.select("tr.DataTable-tableRow");

                for (Element tr : totalDatas) {
                    Elements td;
                    switch (tr.attr("data-stat-id")) {
                        case "0x08600000000003F5":
                            td = tr.select("td");
                            winGame = Integer.parseInt(td.last().text());
                            break;
                        case "0x086000000000042E":
                            td = tr.select("td");
                            loseGame = Integer.parseInt(td.last().text());
                            break;
                        default:
                            break;
                    }
                }
                winLoseGame.add(0, winGame);
                winLoseGame.add(1, loseGame);
                return winLoseGame;
            }else {
                stopWatch.start(pdDto.getHeroNameKR() + " 엘레멘트에서 원하는 정보 추출 및 테이블 저장 시간");

                /** 공통 데이터 파싱*/
                Elements detailDatas = heroDetails.select("tr.DataTable-tableRow");

                for (Element tr : detailDatas) {
                    Elements td;
                    switch (tr.attr("data-stat-id")) {
                        case "0x0860000000000039":                  // 승리 판수
                            td = tr.select("td");
                            winGame = Integer.parseInt(td.last().text());
                            break;
                        case "0x08600000000003D1":                  // 승률
                            td = tr.select("td");
                            winRate = td.last().text();
                            break;
                        case "0x0860000000000430":                  // 패배 판수
                            td = tr.select("td");
                            loseGame = Integer.parseInt(td.last().text());
                            break;
                        case "0x0860000000000021":                  // 플레이 시간
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
                        case "0x08600000000003D2":                  // 목처
                            td = tr.select("td");
                            killPerDeath = td.last().text();
                            break;
                        case "0x08600000000004DB":                  // 불탄 시간
                            td = tr.select("td");
                            spentOnFireAvg = td.last().text();
                            break;
                        case "0x086000000000002A":                  // 죽음
                            td = tr.select("td");
                            death = Long.parseLong(td.last().text());
                            break;
                        case "0x08600000000004D3":                  // 평균 죽음 (10분)
                            td = tr.select("td");
                            deathAvg = td.last().text();
                            break;
//                        case "0x08600000000002D5":                  // 막은 피해량 <- 영웅별로 다름
//                            td = tr.select("td");
//                            blockDamage = Long.parseLong(td.last().text());
//                            break;
                        case "0x08600000000004B7":                  // 영웅에게 가한 피해량
                            td = tr.select("td");
                            damageToHero = Long.parseLong(td.last().text());
                            break;
                        case "0x0860000000000515":                  // 방어막에 가한 피해량
                            td = tr.select("td");
                            damageToShield = Long.parseLong(td.last().text());
                            break;
                        case "0x08600000000003F7":                 // 힐량
                            td = tr.select("td");
                            heal = Long.parseLong(td.last().text());
                            break;
                        case "0x086000000000036F":                  // 금메달
                            td = tr.select("td");
                            goldMedal = td.last().text();
                            break;
                        case "0x086000000000036E":                  // 은메달
                            td = tr.select("td");
                            silverMedal = td.last().text();
                            break;
                        case "0x086000000000036D":                  // 동메달
                            td = tr.select("td");
                            bronzeMedal = td.last().text();
                            break;
                        case "0x0860000000000038":                  // 전체 플레이 판수
                            td = tr.select("td");
                            entireGame = Integer.parseInt(td.last().text());
                            break;
                        case "0x086000000000002B":                  // 결정타
                            td = tr.select("td");
                            lastHit = Long.parseLong(td.last().text());
                            break;
                        default:
                            break;
                    }
                }
                /**디바 시작 */
                if ("0x02E000000000007A".equals(heroDetails.attr("data-category-id"))) { //디바
                    //Dva 영웅 특별 데이터
                    String mechaSuicideKillAvg = "0";
                    String mechaCallAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000002D5":                  // 막은 피해량 (영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004D1":                  // 평균 메카 자폭 킬 (10분)
                                td = tr.select("td");
                                mechaSuicideKillAvg = td.last().text();
                                break;
                            case "0x08600000000004D0":                  // 평균 메카 호출 (10분)
                                td = tr.select("td");
                                mechaCallAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1) ) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Dva dva = new Dva(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), mechaSuicideKillAvg,
                            mechaCallAvg, goldMedal, silverMedal, bronzeMedal);

                    dvaRepositroy.save(dva);

                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            mechaSuicideKillAvg, mechaCallAvg, "", "", "", "평균 자폭 킬", "평균 메카호출", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================ dva data save success =======================================");
                    System.out.println(dva.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**오리사 시작 */
                } else if ("0x02E000000000013E".equals(heroDetails.attr("data-category-id"))) {  //오리사
                    // 오리사 특수 데이터
                    String damageAmpAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x086000000000048E":                  // 막은 데미지 (영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004F3":                  // 평균 공격력 증폭 (10분)
                                td = tr.select("td");
                                damageAmpAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Orisa orisa = new Orisa(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), damageAmpAvg, goldMedal,
                            silverMedal, bronzeMedal);

                    orisaRepository.save(orisa);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            damageAmpAvg, "", "", "", "" , "평균 공격력 증폭", "", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    pdDto.setBlockDamagePerLife(blockDamagePerLife.toString());
                    pdDto.setDamageToHeroPerLife(damageToHeroPerLife.toString());
                    pdDto.setDamageToShieldPerLife(damageToShieldPerLife.toString());
                    pdDto.setHealPerLife("0");
                    pdDto.setKillPerDeath(killPerDeath);
                    pdDto.setDeathAvg(deathAvg);
                    pdDto.setWinRate(winRate);
                    pdDto.setPlayTime(playTime);
                    pdDto.setIndex1(damageAmpAvg);

                    System.out.println("=============================orisa data save success======================================");
                    System.out.println(orisa.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**라인하르트 시작 */
                } else if ("0x02E0000000000007".equals(heroDetails.attr("data-category-id"))) {
                    //라인하르트 영웅 특별 데이터
                    String chargeKillAvg = "0";
                    String earthshatterKillAvg = "0";
                    String fireStrikeKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000259":                  //막은 피해량(영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004E7":                  // 평균 대지강타 킬 (10분)
                                td = tr.select("td");
                                earthshatterKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E5":                  // 평균 돌진 킬 (10분)
                                td = tr.select("td");
                                chargeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E8":                  // 평균 화염강타 킬 (10분)
                                td = tr.select("td");
                                fireStrikeKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Reinhardt reinhardt = new Reinhardt(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg,
                            deathAvg, blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            earthshatterKillAvg, chargeKillAvg, fireStrikeKillAvg, goldMedal, silverMedal, bronzeMedal);

                    reinhardtRepository.save(reinhardt);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            earthshatterKillAvg, chargeKillAvg, fireStrikeKillAvg, "", "", "평균 대지분쇄 킬", "평균 돌진 킬", "평균 화염강타 킬", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================reinhardt data save success===================================");
                    System.out.println(reinhardt.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**자리야 시작 */
                } else if ("0x02E0000000000068".equals(heroDetails.attr("data-category-id"))) {
                    //자리야 영웅 특별 데이터
                    String energyAvg = "0";
                    String highEnergyKillAvg = "0";
                    String gravitonSurgeKillAvg = "0";
                    String projectedBarrierAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000225":                  // 막은 피해량(영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000231":                  // 평균 에너지 (10분)
                                td = tr.select("td");
                                energyAvg = td.last().text();
                                break;
                            case "0x08600000000004F0":                  // 평균 고에너지 킬 (10분)
                                td = tr.select("td");
                                highEnergyKillAvg = td.last().text();
                                break;
                            case "0x08600000000004F1":                  // 중력자탄 킬 (10분)
                                td = tr.select("td");
                                gravitonSurgeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004EF":                  // 평균 주는방벽 (10분)
                                td = tr.select("td");
                                projectedBarrierAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Zarya zarya = new Zarya(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), energyAvg,
                            highEnergyKillAvg, projectedBarrierAvg, gravitonSurgeKillAvg, goldMedal, silverMedal, bronzeMedal);

                    zaryaRepository.save(zarya);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            energyAvg, highEnergyKillAvg, projectedBarrierAvg, gravitonSurgeKillAvg, "", "평균 에너지", "평균 고에너지 킬", "평균 주는방벽", "평균 중력자탄 킬", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("===============================zarya data save success====================================");
                    System.out.println(zarya.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**로드호그 시작 */
                } else if ("0x02E0000000000040".equals(heroDetails.attr("data-category-id"))) {
                    //로드호그 영웅 특별 데이터
                    String wholeHogKillAvg = "0";
                    String chainHookAccuracy = "0%";
                    String hookingEnemyAvg = "0";
                    Long selfHeal = 0l;

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000500":                  // 평균 대재앙 킬 (10분)
                                td = tr.select("td");
                                wholeHogKillAvg = td.last().text();
                                break;
                            case "0x086000000000020B":                  // 갈고리 정확도
                                td = tr.select("td");
                                chainHookAccuracy = td.last().text();
                                break;
                            case "0x08600000000004FF":                  // 평균 갈고리로 끈 적 (10분)
                                td = tr.select("td");
                                hookingEnemyAvg = td.last().text();
                                break;
                            case "0x08600000000003E6":                  // 자힐
                                td = tr.select("td");
                                selfHeal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004EA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }

                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;
                    Double selfHealPerLife = Math.round((selfHeal / ((double) death + 1)) * 100) / 100.0;

                    RoadHog roadhog = new RoadHog(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            soloKillAvg, damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), wholeHogKillAvg,
                            chainHookAccuracy, hookingEnemyAvg, selfHealPerLife.toString(), goldMedal, silverMedal, bronzeMedal);

                    roadhogRepository.save(roadhog);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "", "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            wholeHogKillAvg, chainHookAccuracy, hookingEnemyAvg, selfHealPerLife.toString(), soloKillAvg, "평균 돼재앙 킬", "갈고리 명중률", "평균 끈 적", "목숭당 자힐량", "평균 단독처치");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================roadhog data save success=====================================");
                    System.out.println(roadhog.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**윈스턴 시작 */
                } else if ("0x02E0000000000009".equals(heroDetails.attr("data-category-id"))) {
                    //윈스턴 영웅 특별 데이터
                    String jumpPackKillAvg = "0";
                    String primalRageKillAvg = "0";
                    String pushEnmeyAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000272":                  // 막은 피해량 (영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000508":                  // 평균 점프팩 킬 (10분)
                                td = tr.select("td");
                                jumpPackKillAvg = td.last().text();
                                break;
                            case "0x086000000000050B":                  // 평균 원시의 분노 킬 (10분)
                                td = tr.select("td");
                                primalRageKillAvg = td.last().text();
                                break;
                            case "0x086000000000050A":                  // 평균 적군 밀친 횟수 (10분)
                                td = tr.select("td");
                                pushEnmeyAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Winston winston = new Winston(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), jumpPackKillAvg,
                            primalRageKillAvg, pushEnmeyAvg, goldMedal, silverMedal, bronzeMedal);

                    winstonRepository.save(winston);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            jumpPackKillAvg, primalRageKillAvg, pushEnmeyAvg, "", "", "평균 점프팩 킬", "평균 원시의분노 킬", "평균 밀친 적", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================winston data save success+====================================");
                    System.out.println(winston.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**시그마 시작 */
                } else if ("0x02E000000000023B".equals(heroDetails.attr("data-category-id"))) {
                    //시그마 영웅 특별 데이터
                    Long absorptionDamage = 0l;
                    String graviticFluxKillAvg = "0";
                    String accretionKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000006A1":                  // 막은 피해량 (영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000006B8":                  // 흡수한 피해량
                                td = tr.select("td");
                                absorptionDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000006C0":                  // 평균 중력붕괴 킬 (10분)
                                td = tr.select("td");
                                graviticFluxKillAvg = td.last().text();
                                break;
                            case "0x08600000000006BB":                  // 평균 강착 킬 (10분)
                                td = tr.select("td");
                                accretionKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;
                    Double absorptionDamagePerLife = Math.round((absorptionDamage / ((double) death + 1)) * 100) / 100.0;

                    Sigma sigma = new Sigma(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), absorptionDamagePerLife.toString(),
                            graviticFluxKillAvg, accretionKillAvg, goldMedal, silverMedal, bronzeMedal);

                    sigmaRepository.save(sigma);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            absorptionDamagePerLife.toString(), graviticFluxKillAvg, accretionKillAvg, "", "", "목숨당 흡수한 피해", "평균 중력붕괴 킬", "평균 강착 킬", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================sigma data save success=======================================");
                    System.out.println(sigma.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**레킹볼 시작 */
                } else if ("0x02E00000000001CA".equals(heroDetails.attr("data-category-id"))) {
                    //레킹볼 영웅 특별 데이터
                    String grapplingClawKillAvg = "0";
                    String piledriverKillAvg = "0";
                    String minefieldKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x086000000000048E":                  // 막은 피해량 (영우별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x086000000000064C":                  // 평균 갈고리 킬 (10분)
                                td = tr.select("td");
                                grapplingClawKillAvg = td.last().text();
                                break;
                            case "0x086000000000064F":                  // 평균 파일드라이브 킬 (10분)
                                td = tr.select("td");
                                piledriverKillAvg = td.last().text();
                                break;
                            case "0x086000000000064D":                  // 평균 지뢰밭 킬 (10분)
                                td = tr.select("td");
                                minefieldKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    WreckingBall wreckingBall = new WreckingBall(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            blockDamagePerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), grapplingClawKillAvg,
                            piledriverKillAvg, minefieldKillAvg, goldMedal, silverMedal, bronzeMedal);

                    wreckingBallRepository.save(wreckingBall);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            grapplingClawKillAvg, piledriverKillAvg, minefieldKillAvg, "", "", "평균 갈고리 킬", "평균 파일드라이버 킬", "평균 지뢰밭 킬", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================wreckingBall data save success================================");
                    System.out.println(wreckingBall.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**아나 시작*/
                } else if ("0x02E000000000013B".equals(heroDetails.attr("data-category-id"))) {
                    //아나 영웅 특별 데이터
                    String nanoBoosterAvg = "0";
                    Long bioticGrenadeKill = 0l;
                    String sleepDartAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004C7":                  // 평균 나노강화제 주입 (10분)
                                td = tr.select("td");
                                nanoBoosterAvg = td.last().text();
                                break;
                            case "0x086000000000043C":                  // 평균 생체 수류탄 킬 (10분)
                                td = tr.select("td");
                                bioticGrenadeKill = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004C5":                  // 평균 재운적 (10분)
                                td = tr.select("td");
                                sleepDartAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double bioticGrenadeKillPerLife = Math.round((bioticGrenadeKill / ((double) death + 1)) * 100) / 100.0;

                    Ana ana = new Ana(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), nanoBoosterAvg, sleepDartAvg, bioticGrenadeKillPerLife.toString()
                            , goldMedal, silverMedal, bronzeMedal);

                    anaRepository.save(ana);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            nanoBoosterAvg, sleepDartAvg, bioticGrenadeKillPerLife.toString(), "", "", "평균 나노강화제 주입", "평균 생체수류탄 킬", "평균 재운적", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================ana data save success================================");
                    System.out.println(ana.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**바티스트 시작*/
                } else if ("0x02E0000000000221".equals(heroDetails.attr("data-category-id"))) {
                    //바티스트 영웅 특별 데이터
                    String immortalityFieldSaveAvg = "0";
                    String amplificationMatrixAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x086000000000069A":                  // 평균 불사장치 세이브 수 (10분)
                                td = tr.select("td");
                                immortalityFieldSaveAvg = td.last().text();
                                break;
                            case "0x08600000000006AE":                  // 평균 강화메트릭스 수 (10분)
                                td = tr.select("td");
                                amplificationMatrixAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Baptiste baptiste = new Baptiste(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), immortalityFieldSaveAvg,
                            amplificationMatrixAvg, goldMedal, silverMedal, bronzeMedal);

                    baptisteRepository.save(baptiste);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            immortalityFieldSaveAvg, amplificationMatrixAvg, "", "", "", "평균 불사장치 세이브", "평균 강화메트릭스 사용", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================baptiste data save success================================");
                    System.out.println(baptiste.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**브리기테 시작*/
                } else if ("0x02E0000000000195".equals(heroDetails.attr("data-category-id"))) {
                    //브리기테 영웅 특별 데이터
                    Long armor = 0l;
                    String inspireActiveRate = "0%";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000607":                  // 방어력 제공
                                td = tr.select("td");
                                armor = Long.parseLong(td.last().text());
                                break;
                            case "0x0860000000000612":                  // 결려 지속량 (%)
                                td = tr.select("td");
                                inspireActiveRate = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double armorPerLife = Math.round((armor / ((double) death + 1)) * 100) / 100.0;

                    Brigitte brigitte = new Brigitte(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), armorPerLife.toString(), inspireActiveRate,
                            goldMedal, silverMedal, bronzeMedal);

                    brigitteRepository.save(brigitte);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            armorPerLife.toString(), inspireActiveRate, "", "", "", "목숨당 방어력 제공", "격려(패시브) 지속률", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================brigitte data save success================================");
                    System.out.println(brigitte.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**루시우 시작*/
                } else if ("0x02E0000000000079".equals(heroDetails.attr("data-category-id"))) {
                    //루시우 영웅 특별 데이터
                    String soundwaveAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004D2":                  // 평균 소리방볍 제공 (10분)
                                td = tr.select("td");
                                soundwaveAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;

                    Lucio lucio = new Lucio(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), soundwaveAvg, goldMedal, silverMedal, bronzeMedal);

                    lucioRepository.save(lucio);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            soundwaveAvg, "", "", "", "", "평균 소리방벽 사용", "", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================lucio data save success================================");
                    System.out.println(lucio.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**메르시 시작*/
                } else if ("0x02E0000000000004".equals(heroDetails.attr("data-category-id"))) {
                    //메르시 영웅 특별 데이터
                    String resurrectAvg = "0";
                    String damageAmpAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004C9":                  // 평균 부활 (10분)
                                td = tr.select("td");
                                resurrectAvg = td.last().text();
                                break;
                            case "0x08600000000004F3":                  // 평균 공격력 증폭 (10분)
                                td = tr.select("td");
                                damageAmpAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;

                    Mercy mercy = new Mercy(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), resurrectAvg, damageAmpAvg, goldMedal, silverMedal, bronzeMedal);

                    mercyRepository.save(mercy);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            resurrectAvg, damageAmpAvg, "", "", "", "평균 부활", "평균 공격력 증폭", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================mercy data save success================================");
                    System.out.println(mercy.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**모이라 시작*/
                } else if ("0x02E00000000001A2".equals(heroDetails.attr("data-category-id"))) {
                    //모이라 영웅 특별 데이터
                    Long selfHeal = 0l;
                    String coalescenceKillAvg = "0"; String coalescenceHealAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000003E6":                  //자힐
                                td = tr.select("td");
                                selfHeal = Long.parseLong(td.last().text());
                                break;
                            case "0x086000000000058A":                  // 평균 융화 킬 (10분)
                                td = tr.select("td");
                                coalescenceKillAvg = td.last().text();
                                break;
                            case "0x0860000000000591":                  // 평균 융화 힐 (10분)
                                td = tr.select("td");
                                coalescenceHealAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double selfHealPerLife = Math.round((selfHeal / ((double) death + 1)) * 100) / 100.0;

                    Moira moira = new Moira(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), coalescenceKillAvg, coalescenceHealAvg, selfHealPerLife.toString(), goldMedal, silverMedal, bronzeMedal);

                    moiraRepository.save(moira);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            coalescenceKillAvg, coalescenceHealAvg, selfHealPerLife.toString(), "", "", "평균 융화 킬", "평균 융화 힐", "목숭당 자힐량", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================moira data save success================================");
                    System.out.println(moira.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**젠야타 시작*/
                } else if ("0x02E0000000000020".equals(heroDetails.attr("data-category-id"))) {
                    //젠야타 영웅 특별 데이터
                    String transcendenceHealAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000352":
                                td = tr.select("td");
                                transcendenceHealAvg = td.last().text(); // 평균 초월 힐량 (10분)
                                break;
                            default:
                                break;
                        }
                    }
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;

                    Zenyatta zenyatta = new Zenyatta(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,
                            healPerLife.toString(), damageToHeroPerLife.toString(), transcendenceHealAvg, goldMedal, silverMedal, bronzeMedal);

                    zenyattaRepository.save(zenyatta);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, healPerLife.toString(), "0", "0", damageToHeroPerLife.toString(), "0",
                            transcendenceHealAvg, "", "", "", "", "평균 초월 힐", "", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================zenyatta data save success================================");
                    System.out.println(zenyatta.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**정크랫 시작*/
                }else if("0x02E0000000000065".equals(heroDetails.attr("data-category-id"))) {
                    //정크랫 영웅 특별 데이터
                    String steelTrapEnemyAvg = "0";
                    String concussionMineAvg = "0";
                    String ripTireKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004E9":                  // 평균 덫에 걸린 적  (10분)
                                td = tr.select("td");
                                steelTrapEnemyAvg = td.last().text();
                                break;
                            case "0x08600000000005B9":                  // 평균 충격 지뢰 킬 (10분)
                                td = tr.select("td");
                                concussionMineAvg = td.last().text();
                                break;
                            case "0x08600000000004EA":                  // 평균 죽이는 타이어 킬 (10분)
                                td = tr.select("td");
                                ripTireKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Junkrat junkrat = new Junkrat(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), steelTrapEnemyAvg, concussionMineAvg, ripTireKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    junkratRepository.save(junkrat);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            steelTrapEnemyAvg, concussionMineAvg, ripTireKillAvg, "", "", "평균 덫으로 묶은 적", "평균 충격 지뢰 킬", "평균 죽이는 타이어 킬", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================junkrat data save success================================");
                    System.out.println(junkrat.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**겐지 시작*/
                }else if("0x02E0000000000029".equals(heroDetails.attr("data-category-id"))) {
                    //겐지 영웅 특별 데이터
                    String dragonbladeKillAvg = "0";
                    String deflectDamageAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004DD":                  // 평균 용검 킬 (10분)
                                td = tr.select("td");
                                dragonbladeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DC":                  // 평균 튕겨낸 피해량 (10분)
                                td = tr.select("td");
                                deflectDamageAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Genji genji = new Genji(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg,lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), dragonbladeKillAvg, deflectDamageAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    genjiRepository.save(genji);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            dragonbladeKillAvg, deflectDamageAvg, soloKillAvg, "", "", "평균 용검 킬", "평균 튕겨낸 피해량", "평균 단독처치", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================gneji data save success================================");
                    System.out.println(genji.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**둠피스트 시작*/
                }else if("0x02E000000000012F".equals(heroDetails.attr("data-category-id"))) {
                    //둠피스트 영웅 특별 데이터
                    String skillDamageAvg = "0";
                    String createShieldAvg = "0";
                    String meteorStrikeKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x086000000000051B":                  // 평균 기술로 준 피해 (10분)
                                td = tr.select("td");
                                skillDamageAvg = td.last().text();
                                break;
                            case "0x0860000000000521":                  // 평균 보호막 생성량 (10분)
                                td = tr.select("td");
                                createShieldAvg = td.last().text();
                                break;
                            case "0x086000000000051E":                  // 평균 파멸의 일격 킬 (10분)
                                td = tr.select("td");
                                meteorStrikeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Doomfist doomfist = new Doomfist(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), skillDamageAvg, createShieldAvg, meteorStrikeKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    doomfistRepository.save(doomfist);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            skillDamageAvg, createShieldAvg, meteorStrikeKillAvg, soloKillAvg, "", "평균 기술로 준 피해", "평균 보호막 생성량", "평균 파멸의 일격 킬", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================doomfist data save success================================");
                    System.out.println(doomfist.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**리퍼 시작*/
                }else if("0x02E0000000000002".equals(heroDetails.attr("data-category-id"))){
                    //리퍼 영웅 특별 데이터
                    String deathBlossomKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004FD":                  // 평균 죽음의 꽃 킬 (10분)
                                td = tr.select("td");
                                deathBlossomKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Reaper reaper = new Reaper(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), deathBlossomKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    reaperRepository.save(reaper);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            deathBlossomKillAvg, soloKillAvg, "", "", "", "평균 죽음의꽃 킬", "평균 단독처치", "", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================reaper data save success================================");
                    System.out.println(reaper.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**맥크리 시작*/
                }else if("0x02E0000000000042".equals(heroDetails.attr("data-category-id"))){
                    //맥크리 영웅 특별 데이터
                    String peacekeeperKillAvg = "0"; String deadeyeKillAvg = "0"; String criticalHitRate = "0%";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004CE":                  // 평균 난사 킬 (10분)
                                td = tr.select("td");
                                peacekeeperKillAvg = td.last().text();
                                break;
                            case "0x08600000000004CD":                  // 평균 황야의 무법자 처치 (10분)
                                td = tr.select("td");
                                deadeyeKillAvg = td.last().text();
                                break;
                            case "0x08600000000003E2":                  // 치명타 명중률 (10분)
                                td = tr.select("td");
                                criticalHitRate = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Mccree mccree = new Mccree(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), peacekeeperKillAvg, deadeyeKillAvg, criticalHitRate, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    mccreeRepository.save(mccree);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            peacekeeperKillAvg, deadeyeKillAvg, criticalHitRate, soloKillAvg, "", "평균 난사 킬", "평균 황야의 무법자 킬", "치명타 명중률", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================mccree data save success================================");
                    System.out.println(mccree.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**메이 시작*/
                }else if("0x02E00000000000DD".equals(heroDetails.attr("data-category-id"))){
                    //메이 영웅 특별 데이터
                    String blizzardKillAvg = "0"; String freezingEnemyAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000002D9":                  // 막은 피해량(영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004CE":                  // 평균 눈보라 킬 (10분)
                                td = tr.select("td");
                                blizzardKillAvg = td.last().text();
                                break;
                            case "0x08600000000004CD":                  // 평균 얼린적 (10분)
                                td = tr.select("td");
                                freezingEnemyAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Mei mei = new Mei(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, blockDamagePerLife.toString(), lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), blizzardKillAvg, freezingEnemyAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    meiRepository.save(mei);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            blizzardKillAvg, freezingEnemyAvg, soloKillAvg, "", "", "평균 눈보라 킬", "평균 얼린적", "평균 단독처치", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================mei data save success================================");
                    System.out.println(mei.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**바스티온 시작*/
                }else if("0x02E0000000000015".equals(heroDetails.attr("data-category-id"))){
                    //바스티온 영웅 특별 데이터
                    String sentryModeKillAvg = "0"; String reconModeKillAvg = "0"; String tankModeKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004DF":                  // 평균 경계모드 킬 (10분)
                                td = tr.select("td");
                                sentryModeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DE":                  // 평균 수색모드 처치 (10분)
                                td = tr.select("td");
                                reconModeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E0":                  // 평균 전차모드 처치 (10분)
                                td = tr.select("td");
                                tankModeKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Bastion bastion = new Bastion(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), sentryModeKillAvg, reconModeKillAvg, tankModeKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    bastionRepository.save(bastion);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            sentryModeKillAvg, reconModeKillAvg, tankModeKillAvg, soloKillAvg, "", "평균 경계모드 킬", "평균 수색모드 킬", "평균 전차모드 킬", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================bastion data save success================================");
                    System.out.println(bastion.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**솔저76 시작*/
                }else if("0x02E000000000006E".equals(heroDetails.attr("data-category-id"))){
                    //솔저76 영웅 특별 데이터
                    String helixRocketKillAvg = "0"; String tacticalVisorKillAvg = "0"; String criticalHitRate = "0%";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000503":                  // 평균 나선 로켓 처치 (10분)
                                td = tr.select("td");
                                helixRocketKillAvg = td.last().text();
                                break;
                            case "0x0860000000000504":                  // 평균 전술조준경 처치 (10분)
                                td = tr.select("td");
                                tacticalVisorKillAvg = td.last().text();
                                break;
                            case "0x08600000000003E2":                  // 치명타 명중률
                                td = tr.select("td");
                                criticalHitRate = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double healPerLife = Math.round((heal / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Soldier76 soldier76 = new Soldier76(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, healPerLife.toString(), lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), helixRocketKillAvg, tacticalVisorKillAvg, criticalHitRate, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    soldier76Repository.save(soldier76);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,healPerLife.toString(), "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            helixRocketKillAvg, tacticalVisorKillAvg, criticalHitRate, soloKillAvg, "", "평균 나선 로켓 킬", "평균 전술조준경 킬", "치명타 명중률", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================soldier76 data save success================================");
                    System.out.println(soldier76.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**솜브라 시작*/
                }else if("0x02E000000000012E".equals(heroDetails.attr("data-category-id"))){
                    //솜브라 영웅 특별 데이터
                    String hackingEnemyAvg = "0"; String EMPEnemyAvg = "0"; String criticalHitRate = "0%";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000506":                  // 평균 해킹한 적 (10분)
                                td = tr.select("td");
                                hackingEnemyAvg = td.last().text();
                                break;
                            case "0x0860000000000505":                  // 평균 EMP맞춘 적 (10분)
                                td = tr.select("td");
                                EMPEnemyAvg = td.last().text();
                                break;
                            case "0x08600000000003E2":                  // 치명타 명중률
                                td = tr.select("td");
                                criticalHitRate = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Sombra sombra = new Sombra(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), hackingEnemyAvg, EMPEnemyAvg, criticalHitRate, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    sombraRepository.save(sombra);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            hackingEnemyAvg, EMPEnemyAvg, criticalHitRate, soloKillAvg, "", "평균 해킹한 적", "평균 EMP맞춘 적", "치명타 명중률", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================sombra data save success================================");
                    System.out.println(sombra.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**시메트라 시작*/
                }else if("0x02E0000000000016".equals(heroDetails.attr("data-category-id"))){
                    //시메트라 영웅 특별 데이터
                    String turretKillAvg = "0"; String teleportUsingAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x086000000000050C":                  // 막은 피해량(영웅별로 다름)
                                td = tr.select("td");
                                blockDamage = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004CE":                  // 평균 감시포탑 킬 (10분)
                                td = tr.select("td");
                                turretKillAvg = td.last().text();
                                break;
                            case "0x08600000000004CD":                  // 평균 순간이동한 플레이어 (10분)
                                td = tr.select("td");
                                teleportUsingAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double blockDamagePerLife = Math.round((blockDamage / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Symmetra symmetra = new Symmetra(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, blockDamagePerLife.toString(), lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), turretKillAvg, teleportUsingAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    symmetraRepository.save(symmetra);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg,"0", blockDamagePerLife.toString(), lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            turretKillAvg, teleportUsingAvg, soloKillAvg, "", "", "평균 감시포탑 킬", "평균 순간이동한 아군", "평균 단독처치", "", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================symmetra data save success================================");
                    System.out.println(symmetra.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**애쉬 시작*/
                }else if("0x02E0000000000200".equals(heroDetails.attr("data-category-id"))){
                    //애쉬 영웅 특별 데이터
                    String coachGunKillAvg = "0"; String dynamiteKillAvg = "0"; String BOBKillAvg = "0"; String scopeCriticalHitRate = "0%";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000668":                  // 평균 충격샷건 킬 (10분)
                                td = tr.select("td");
                                coachGunKillAvg = td.last().text();
                                break;
                            case "0x0860000000000664":                  // 평균 다이너마이트 킬 (10분)
                                td = tr.select("td");
                                dynamiteKillAvg = td.last().text();
                                break;
                            case "0x086000000000066D":                  // 평균 BOB 킳 (10분)
                                td = tr.select("td");
                                BOBKillAvg = td.last().text();
                                break;
                            case "0x086000000000062F":                  // 저격 치명타 명중률
                                td = tr.select("td");
                                scopeCriticalHitRate = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Ashe ashe = new Ashe(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), coachGunKillAvg, dynamiteKillAvg, BOBKillAvg, scopeCriticalHitRate, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    asheRepository.save(ashe);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            coachGunKillAvg, dynamiteKillAvg, BOBKillAvg, scopeCriticalHitRate, soloKillAvg, "평균 충격샷건 킬", "평균 다이너마이트 킬","평균 BOB 킳", "저격 치명타 명중률", "평균 단독처치");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================ashe data save success================================");
                    System.out.println(ashe.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**위도우메이커 시작*/
                }else if("0x02E000000000000A".equals(heroDetails.attr("data-category-id"))){
                    //위도우메이커 영웅 특별 데이터
                    String scopeHitRate = "0%"; String scopeCriticalHitRate = "0%"; String sightSupportAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004F9":                  // 평균 처치시야 지원
                                td = tr.select("td");
                                sightSupportAvg = td.last().text();
                                break;
                            case "0x086000000000066D":                  // 저격 명중률
                                td = tr.select("td");
                                scopeHitRate = td.last().text();
                                break;
                            case "0x086000000000062F":                  // 저격 치명타 명중률
                                td = tr.select("td");
                                scopeCriticalHitRate = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Widowmaker widowmaker = new Widowmaker(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), sightSupportAvg, scopeHitRate, scopeCriticalHitRate, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    widowmakerRepository.save(widowmaker);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            sightSupportAvg, scopeHitRate, scopeCriticalHitRate, soloKillAvg, "", "평균 처치시야 지원", "저격 명중률", "저격 치명타 명중률", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================widowmaker data save success================================");
                    System.out.println(widowmaker.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**토르비욘 시작*/
                }else if("0x02E0000000000006".equals(heroDetails.attr("data-category-id"))){
                    //토르비욘 영웅 특별 데이터
                    String moltenCoreKillAvg = "0"; String torbjornDirectKillAvg = "0"; String turretKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004E2":                  // 평균 초고열 용광로 처치 (10분)
                                td = tr.select("td");
                                moltenCoreKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E3":                  // 평균 직접 처치 (10분)
                                td = tr.select("td");
                                torbjornDirectKillAvg = td.last().text();
                                break;
                            case "0x08600000000004E4":                  // 평균 포탑 처치 (10분)
                                td = tr.select("td");
                                turretKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Torbjorn torbjorn = new Torbjorn(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), moltenCoreKillAvg, torbjornDirectKillAvg, turretKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    torbjornRepository.save(torbjorn);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            moltenCoreKillAvg, torbjornDirectKillAvg, turretKillAvg, soloKillAvg, "", "평균 초고열 용광로 처치", "평균 직접 처치", "평균 포탑 처치", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================torbjorn data save success================================");
                    System.out.println(torbjorn.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**트레이서 시작*/
                }else if("0x02E0000000000003".equals(heroDetails.attr("data-category-id"))){
                    //트레이서 영웅 특별 데이터
                    String pulseBombStickAvg = "0"; String pulseBombKillAvg = "0"; String criticalHitRate = "0%"; Long selfHeal = 0l;

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000512":                  // 평균 펄스 폭탄 부착 (10분)
                                td = tr.select("td");
                                pulseBombStickAvg = td.last().text();
                                break;
                            case "0x0860000000000511":                  // 평균 펄스 폭탄 처치 (10분)
                                td = tr.select("td");
                                pulseBombKillAvg = td.last().text();
                                break;
                            case "0x08600000000003E2":                  // 치명타 명중률
                                td = tr.select("td");
                                criticalHitRate = td.last().text();
                                break;
                            case "0x08600000000003E6":                  // 자가치유
                                td = tr.select("td");
                                selfHeal = Long.parseLong(td.last().text());
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;
                    Double selfHealPerLife = Math.round((selfHeal / ((double) death + 1)) * 100) / 100.0;

                    Tracer tracer = new Tracer(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), pulseBombStickAvg, pulseBombKillAvg, criticalHitRate, selfHealPerLife.toString(), soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    tracerRepository.save(tracer);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            pulseBombStickAvg, pulseBombKillAvg, criticalHitRate, selfHealPerLife.toString(), soloKillAvg, "평균 펄스폭탄 부착", "평균 펄스폭탄 킬", "치명타 명중률", "목숨당 자힐량", "평균 단독처치");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================tracer data save success================================");
                    System.out.println(tracer.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**파라 시작*/
                }else if("0x02E0000000000008".equals(heroDetails.attr("data-category-id"))){
                    //파라 영웅 특별 데이터
                    String rocketHitRateAvg = "0"; String straitHitRate = "0%"; String barrageKillAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x0860000000000502":                  // 평균 로켓 명중 (10분)
                                td = tr.select("td");
                                rocketHitRateAvg = td.last().text();
                                break;
                            case "0x0860000000000624":                  // 직격률
                                td = tr.select("td");
                                straitHitRate = td.last().text();
                                break;
                            case "0x0860000000000501":                  // 평균 포화 처치 (10분)
                                td = tr.select("td");
                                barrageKillAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Pharah pharah = new Pharah(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), rocketHitRateAvg, straitHitRate, barrageKillAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    pharahRepository.save(pharah);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            rocketHitRateAvg, straitHitRate, barrageKillAvg, soloKillAvg, "", "평균 로켓 명중", "직격률", "평균 포화 킿", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================pharah data save success================================");
                    System.out.println(pharah.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;

                    /**한조 시작*/
                }else if("0x02E0000000000005".equals(heroDetails.attr("data-category-id"))){
                    //한조 영웅 특별 데이터
                    String dragonStrikeKillAvg = "0"; String stormArrowKillAvg = "0"; String sightSupportAvg = "0";

                    for (Element tr : detailDatas) {
                        Elements td;
                        switch (tr.attr("data-stat-id")) {
                            case "0x08600000000004CB":                  // 평균 용의 일격 처치 (10분)
                                td = tr.select("td");
                                dragonStrikeKillAvg = td.last().text();
                                break;
                            case "0x0860000000000628":                  // 평균 폭풍 화살 처치 (10분)
                                td = tr.select("td");
                                stormArrowKillAvg = td.last().text();
                                break;
                            case "0x08600000000004F9":                  // 평균 처치 시야 지원 (10분)
                                td = tr.select("td");
                                sightSupportAvg = td.last().text();
                                break;
                            case "0x08600000000004DA":                  // 평균 단독 처치 (10분)
                                td = tr.select("td");
                                soloKillAvg = td.last().text();
                                break;
                            default:
                                break;
                        }
                    }
                    Double lastHitPerLife = Math.round((lastHit / ((double) death + 1)) * 100) / 100.0;
                    Double damageToHeroPerLife = Math.round((damageToHero / ((double) death + 1)) * 100) / 100.0;
                    Double damageToShieldPerLife = Math.round((damageToShield / ((double) death + 1)) * 100) / 100.0;

                    Hanzo hanzo = new Hanzo(playerListDto.getId(), winGame, loseGame, entireGame, winRate, playTime, killPerDeath, spentOnFireAvg, deathAvg, lastHitPerLife.toString(),
                            damageToHeroPerLife.toString(), damageToShieldPerLife.toString(), dragonStrikeKillAvg, stormArrowKillAvg, sightSupportAvg, soloKillAvg, goldMedal, silverMedal, bronzeMedal);

                    hanzoRepository.save(hanzo);
                    PlayerDetail playerDetail = new PlayerDetail(pdDto.getId(), pdDto.getSeason(), pdDto.getOrder(), hero, pdDto.getHeroNameKR(), killPerDeath,
                            winRate, playTime, deathAvg, spentOnFireAvg, "0", "0", lastHitPerLife.toString(), damageToHeroPerLife.toString(), damageToShieldPerLife.toString(),
                            dragonStrikeKillAvg, stormArrowKillAvg, sightSupportAvg, soloKillAvg, "", "평균 용의 일격 킬", "평균 폭풍 화살 킬", "평균 처치시야 지원", "평균 단독처치", "");

                    playerDetailRepository.save(playerDetail);
                    System.out.println("============================hanzo data save success================================");
                    System.out.println(hanzo.toString());
                    System.out.println("==========================================================================================");
                    // 시간 확인
                    stopWatch.stop();

                    winLoseGame.add(0, winGame);
                    winLoseGame.add(1, loseGame);
                    return winLoseGame;
                }else{
                    stopWatch.stop();
                }
            }
        }
        winLoseGame.add(0, 0);
        winLoseGame.add(1, 0);
        return winLoseGame;
    }

//    /** 사설 오버워치 api (ow-api.com) 에서 데이터 받아옴  (미사용) */
//    public PlayerListDto crawlingPlayerProfile2(PlayerListDto playerListDto) {
//        ObjectMapper mapper = new ObjectMapper();
////        playerList = new ArrayList<PlayerListDto>();
//        try {
//            System.out.println("crawlingPlayerProfile working -> battleTag : " + playerListDto.getForUrl());
//            // Jsoup를 이용해 오버워치 웹크롤링 : 유저 프로필을 json String 형식으로 가져오는 부분
//            String json = Jsoup.connect(GET_PLAYER_PROFILE_URL2+playerListDto.getForUrl()+"/profile")
//                    .ignoreContentType(true)
//                    .execute().body();
//            JsonNode jsonNode = mapper.readTree(json);
//            if (jsonNode.isObject()) {
//                ObjectNode obj = (ObjectNode) jsonNode;
//                if(obj.has("ratings")){
//                    JsonNode ratings = obj.get("ratings");
//                    Iterator itr = ratings.elements();
//
//                    while (itr.hasNext()) {
//                        JsonNode rating = (JsonNode) itr.next();
//                        ObjectNode ratingObj = (ObjectNode) rating;
//                        if("tank".equals(ratingObj.get("role").asText())){
//                            playerListDto.setTankRatingPoint(ratingObj.get("level").asInt());
//                        }else if("damage".equals(ratingObj.get("role").asText())){
//                            playerListDto.setDealRatingPoint(ratingObj.get("level").asInt());
//                        }else if("support".equals(ratingObj.get("role").asText())){
//                            playerListDto.setHealRatingPoint(ratingObj.get("level").asInt());
//                        }
//                    }
//                }
//            }
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return playerListDto;
//    }
}
