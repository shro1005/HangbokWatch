let count;
let detailList = [];
const tierDetailList = [];
const rankerDetailList = [];
let cnt = 0;
let before_order = 1;
let now_order = 1;
const trend_tank_point = [];
const trend_deal_point = [];
const trend_heal_point = [];
const trend_tank_wingame = [];
const trend_deal_wingame = [];
const trend_heal_wingame = [];
const trend_tank_losegame = [];
const trend_deal_losegame = [];
const trend_heal_losegame = [];
const trend_udt_dtm = [];
let radarLabel = [];
let radarData = [];
let tierRadarLabel = [];
let tierRadarData = [];
let rankerRadarLabel = [];
let rankerRadarData = [];
const chartColors = ['#fcb150', '#11a8ab' ,'#e64c65'];
const heal_hero = "/아나/바티스트/브리기테/루시우/메르시/모이라/젠야타";
const deal_hero = "/애쉬/바스티온/둠피스트/겐지/한조/정크랫/맥크리/메이/파라/리퍼/솔저: 76/솜브라/시메트라/토르비욘/트레이서/위도우메이커";
const tank_hero = "/디바/오리사/라인하르트/윈스턴/자리야/로드호그/레킹볼/시그마";

$(window).resize(function () {
   // 창크기 변화 감지
    setContainerHeight();
    // if(cnt ==0) {
    //     cnt += 1;
    // }
});

Handlebars.registerHelper('isLike',function (like_or_not, options) {
    if(like_or_not === 'Y') {
        return options.fn(this);
    }else {
        return options.reverse(this);
    }
});

const main = {
    init : function(){
        // console.log("main.init 호출");
        const _this = this;
        $('#btn-search').on('click', function (event) {
            _this.search();
            return false;
        });

        $('.btn-refresh').on('click', function (event){
           _this.doRefresh();
           return false;
        });

        $('.btn-like').on('click', function () {
            _this.doFavorite();
            return false;
        });

        // 엔터키 눌렀을 때 search 메소드 호
        $(document).keypress(function (e) {
            if (e.which == 13) {
                _this.search();
                return false;
            }
        });
        drawProgressBar();
        getDetailData();

        const message = $(".message").val();
        // console.log(message);
        if(message != "success") {
            alert(message);
        }
    },
    search : function () {
        // alert('main search 호출');
        let playerName = $('input[id="playerName"]').val();
        if(playerName =="") {return false;}
        else if(playerName.indexOf("#") != -1) {
            // alert("detail playerName # : " + playerName);
            playerName = playerName.replace("#", "-");
        }
        // alert("detail playerName - : " + playerName);
        // console.log("검색한 playerName : " + playerName);
        location.href = "/search/" + playerName;

    },
    doRefresh : function () {
        // console.log("doRefresh 호출");
        $('.btn-refresh').addClass('clicked');
        // console.log($('.user-name').text());
        const forUrl = $('.user-name').text().replace(" #" , "-");
        // console.log(forUrl);

        location.href = "/refreshPlayerDetail/" + forUrl;
        // $('.btn-refresh').removeClass('clicked');

    },
    doFavorite : function () {
        // console.log("doFavorite 호출");
        const id = $('.user-name').attr('id');
        let input = {
            id : id,
            playerName : 'N'
        }
        if($('.btn-like').hasClass('clicked') === true) {
            input.playerName = 'N';
            $('.btn-like').removeClass('clicked');
            $('.mdi-heart').addClass('mdi-heart-outline');
            $('.mdi-heart-outline').removeClass('mdi-heart');
        }else {
            input.playerName = 'Y';
            $('.btn-like').addClass('clicked');
            $('.mdi-heart-outline').addClass('mdi-heart');
            $('.mdi-heart').removeClass('mdi-heart-outline');
        }

        $.ajax({
            type: 'POST',
            url: '/refreshFavorite',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(input)
        });
    }
};

const getDetailData = () => {
    // console.log("getDetailData 호출");
    let hero_list = $("#hero-list").html();
    let template = Handlebars.compile(hero_list);

    let hero = {
        heros:[]
    };

    const id = $('.user-name').attr("id");
    const input = {id : id};
    let count = 0;
    $.ajax({
        type: 'POST',
        url: '/getDetailData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(input),
        async : false
    }).done(function (datas) {
        /* 영웅 상세정보 파싱*/
        console.log(datas.tierDetail);
        console.log(datas.rankerDetail);
        count = datas.detail.length;
        $.each(datas.detail, function (i, val) {
            if (i <= 6) {
                const heroNameKR = val.heroNameKR;
                const heroName = val.heroName;
                const winRate = val.winRate;
                const playTime = val.playTime;
                const spentOnFireAvg = val.spentOnFireAvg;
                const order = val.order;
                const killPerDeath = val.killPerDeath;

                hero.heros.push({
                    heroName: heroName,
                    heroNameKR: heroNameKR,
                    playTime: playTime,
                    detail: "javascript:drawDetail("+order+")",
                    src: "/HWimages/hero/" + heroName + "_s.png",
                    order: order,
                    winRate: winRate,
                    killPerDeath: killPerDeath,
                    spentOnFireAvg: spentOnFireAvg
                });
            }
            detailList.push(val);
            // console.log(val);
        });
        $.each(datas.tierDetail, function (i, val) {
            tierDetailList.push(val);
        });
        $.each(datas.rankerDetail, function (i, val) {
            rankerDetailList.push(val);
        });
        /*추세선 파싱*/
        // console.log(datas.trendline);
        $.each(datas.trendline, function (i, val) {
            moment.locale('ko');
            // console.log(val.udtDtm);
            // console.log(moment(val.udtDtm).format('MMM Do(ddd)'));
            trend_udt_dtm.push(moment(val.udtDtm).format('MMM Do(ddd)')); trend_tank_point.push(val.tankRatingPoint);
            trend_deal_point.push(val.dealRatingPoint); trend_heal_point.push(val.healRatingPoint);
            trend_tank_wingame.push(val.tankWinGame); trend_tank_losegame.push(val.tankLoseGame);
            trend_deal_wingame.push(val.dealWinGame); trend_deal_losegame.push(val.dealLoseGame);
            trend_heal_wingame.push(val.healWinGame); trend_heal_losegame.push(val.healLoseGame);
        });
        const item = template(hero);
        $('.menu-box-menu').append(item);
    });
    if (count > 7) {
        const moreButtonDiv = $('<li><a class="more-button" href="javascript:moreHero();">전체 조회</a></li>');
        $('.menu-box-menu').append(moreButtonDiv);
    }
    drawDetail(1);
    drawTrendline();
};

const moreHero = () => {
    // console.log("moreHero 호출");
    $('.more-button').parent().remove();

    let hero_list = $("#hero-list").html();
    let template = Handlebars.compile(hero_list);

    let hero = {
        heros:[]
    };

    $.each(detailList, function (i, val) {
        if (i >= 7) {
            const heroName = val.heroName;

            hero.heros.push({
                heroName: heroName,
                heroNameKR: val.heroNameKR,
                playTime: val.playTime,
                detail: "javascript:drawDetail("+val.order+")",
                src: "/HWimages/hero/" + heroName + "_s.png",
                order: val.order,
                winRate: val.winRate,
                killPerDeath: val.killPerDeath,
                spentOnFireAvg: val.spentOnFireAvg
            });
        }
    });
    const item = template(hero);
    $('.menu-box-menu').append(item);
    $('.menu-box-menu')[0].lastElementChild.lastElementChild.style.border = "hidden";
    setContainerHeight();
};

const drawDetail = (order) => {
    // if(cnt ==0) {
    //     cnt += 1;
    // }
    before_order = now_order;
    now_order = order;
    // console.log(before_order, now_order);
    $(`#order-${before_order}`).removeClass('clicked');
    $(`#order-${now_order}`).addClass('clicked');

    $('.detail-header').remove();
    $('.detail-body').remove();

    radarData = [];
    radarLabel = [];
    tierRadarData = [];
    rankerRadarData = [];

    const hero = detailList[order-1];
    let tierData = ""; let rankerData = "";
    const heroName = hero.heroName;
    $.each(tierDetailList, function (i, val) {
       if(val.heroName == heroName) {
           tierData = val;
           return false;
       }
    });
    $.each(rankerDetailList, function (i, val) {
        if(val.heroName == heroName) {
            rankerData = val;
            return false;
        }
    });
    console.log(tierData);
    console.log(rankerData);

    // detail 부분
    let hero_detail = $("#hero-detail").html();
    let template = Handlebars.compile(hero_detail);

    let detail = {
        detail:[]
    };

    // detail_text 부분
    let detail_text = $("#detail-text").html();
    let template2 = Handlebars.compile(detail_text);

    let text = {
        detailText: []
    };

    checkHero(hero, detail, text, radarData, radarLabel);
    checkTierAndRankerData(tierData, tierRadarData, tierRadarLabel);
    checkTierAndRankerData(rankerData, rankerRadarData, rankerRadarLabel);

    const item = template(detail);
    $('.detail').append(item);

    const item2 = template2(text);
    $('.index-text').append(item2);

    drawRadarChart();
    setContainerHeight();
};

const checkHero = (hero, detail, text, radar_data, radar_label) => {
    const heroNameKR = hero.heroNameKR;
    let deathAvg = parseFloat(hero.deathAvg);
    const spentOnFireAvg = hero.spentOnFireAvg;
    let healPerLife = parseFloat(hero.healPerLife);
    const heroName = hero.heroName;
    const winRate = hero.winRate;
    const playTime = hero.playTime;
    let killPerDeath = parseFloat(hero.killPerDeath);
    let blockDamagePerLife = parseFloat(hero.blockDamagePerLife);
    let lastHitPerLife = parseFloat(hero.lastHitPerLife);
    let damageToHeroPerLife = parseFloat(hero.damageToHeroPerLife);
    let damageToShieldPerLife = parseFloat(hero.damageToShieldPerLife);
    const index = [hero.index1, hero.index2, hero.index3, hero.index4, hero.index5];
    const title = [hero.title1, hero.title2 ,hero.title3, hero.title4, hero.title5];

    detail.detail.push({
        heroName: heroName,
        heroNameKR: heroNameKR,
        playTime: playTime,
        src: "/HWimages/hero/" + heroName + "_s.png",
        killPerDeath: killPerDeath,
        winRate: winRate,
        spentOnFireAvg: spentOnFireAvg
    });
    // console.log(heroNameKR);
    text.detailText.push({title: "평균 죽음", index: deathAvg});
    text.detailText.push({title: "목숨당 처치", index: killPerDeath});
    text.detailText.push({title: "목숨당 영웅 피해량", index: damageToHeroPerLife});
    radar_label.push("평균 죽음"); radar_label.push("목숨당 처치"); radar_label.push("목숨당 영웅 딜량");
    if (deathAvg >= 10) { deathAvg = 20;} else if(deathAvg >= 2) {deathAvg = 100 - (deathAvg-2)*10; } else { deathAvg = 100;}
    if (killPerDeath >= 8) { killPerDeath = 100;} else { killPerDeath = 8*killPerDeath + 32; }
    radar_data.push(Math.round(deathAvg*100)/100.0); radar_data.push(Math.round(killPerDeath*100)/100.0);

    if(heal_hero.indexOf(heroNameKR) > 0){          // 힐러 영웅일 시
        if(damageToHeroPerLife >= 1000) { damageToHeroPerLife= 100;} else {damageToHeroPerLife = 0.1*damageToHeroPerLife ;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);
        text.detailText.push({title: "목숨당 힐량", index: healPerLife});
        radar_label.push("목숨당 힐량");
        if(healPerLife >= 2000) { healPerLife= 100;} else {healPerLife = 0.05*healPerLife ;}
        radar_data.push(Math.round(healPerLife*100)/100.0);
        if("바티스트" == heroNameKR) {
            text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
            radar_label.push("목숨당 방벽 피해량");
            if(damageToShieldPerLife >= 1000) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = 0.1*damageToShieldPerLife ;}
            radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        }
    }else if(deal_hero.indexOf(heroNameKR) > 0) {   // 딜러 영웅일 시
        if(damageToHeroPerLife >= 1500) { damageToHeroPerLife = 100;} else {damageToHeroPerLife = damageToHeroPerLife/15;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);
        if("둠피스트" != heroNameKR) {
            text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
            radar_label.push("목숨당 방벽 피해량");
            if(damageToShieldPerLife >= 1500) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = damageToShieldPerLife/15;}
            radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        }
        text.detailText.push({title: "목숨당 결정타(킬캐치)", index: lastHitPerLife});
        radar_label.push("목숨당 결정타(킬캐치)");
        if (lastHitPerLife >= 5) { lastHitPerLife = 100;} else { lastHitPerLife = 20*lastHitPerLife; }
        radar_data.push(Math.round(lastHitPerLife*100)/100.0);
        if("/메이/시메트라".indexOf(heroNameKR) > 0 ) {
            text.detailText.push({title: "목숨당 막은피해", index: blockDamagePerLife});
            radar_label.push("목숨당 막은피해");
            if(blockDamagePerLife >= 1000) { blockDamagePerLife= 100;} else {blockDamagePerLife = 0.1*blockDamagePerLife ;}
            radar_data.push(Math.round(blockDamagePerLife*100)/100.0);
        }
    }else if(tank_hero.indexOf(heroNameKR) > 0) {   // 탱커 영웅일 시
        if(damageToHeroPerLife >= 1800) { damageToHeroPerLife= 100;} else {damageToHeroPerLife = damageToHeroPerLife/18 ;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);
        text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
        radar_label.push("목숨당 방벽 피해량");
        if(damageToShieldPerLife >= 1500) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = damageToShieldPerLife/15 ;}
        radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        if("/로드호그/레킹볼".indexOf(heroNameKR) <= 0 ) {
            text.detailText.push({title: "목숨당 막은피해", index: blockDamagePerLife});
            radar_label.push("목숨당 막은피해");
            if(blockDamagePerLife >= 3000) { blockDamagePerLife= 100;} else {blockDamagePerLife = 0.033*blockDamagePerLife ;}
            radar_data.push(Math.round(blockDamagePerLife*100)/100.0);
        }
    }
    $.each(title, function (i, idx) {
        if(idx != "") {
            text.detailText.push({title: title[i], index: index[i]});
            radar_label.push(title[i]);
            let index_val = 0;
            if("/직격률/갈고리 명중률/저격 명중률/저격 치명타 명중률/치명타 명중률/평균 에너지/격려(패시브) 지속률".indexOf(title[i]) > 0) {
                index_val = parseFloat(index[i].replace("%", ""));
                if("갈고리 명중률"== title[i]) {
                    if (index_val >= 50) { index_val = 100;} else { index_val = 2*index_val; }
                }else if("평균 에너지" == title[i] || "저격 명중률" == title[i]) {
                    if (index_val >= 60) { index_val = 100;} else { index_val = 5*index_val/3; }
                }else if("격려(패시브) 지속률" == title[i]) {
                    if (index_val >= 40) { index_val = 100;} else { index_val = 2.5*index_val; }
                }else if("직격률" == title[i]){
                    if (index_val >= 20) { index_val = 100;} else { index_val = 5*index_val; }
                }else if("저격 치명타 명중률" == title[i]) {
                    if (index_val >= 30) { index_val = 100;} else { index_val = 3.33*index_val; }
                }else {
                    if (index_val >= 12) { index_val = 100;} else { index_val = 8.33*index_val; }
                }
            }else {
                index_val = parseFloat(index[i]);
                if("목숨당 흡수한 피해" == title[i]) {
                    if (index_val >= 600) { index_val = 100;} else { index_val = index_val/6; }
                }else if("평균 주는방벽" == title[i] ) {
                    if (index_val >= 33) { index_val = 100;} else { index_val = index_val*3; }
                }else if("평균 고에너지 처치" == title[i] || "평균 경계모드 처치" == title[i]) {
                    if (index_val >= 25) { index_val = 100;} else { index_val = index_val*4; }
                }else if("평균 초월 힐" == title[i]) {
                    if (index_val >= 250) { index_val = 100;} else { index_val = index_val/2.5; }
                }else if("평균 자폭 처치" == title[i] || "평균 대지분쇄 처치" == title[i] ||
                    "평균 나노강화제 주입" == title[i] || "평균 불사장치 세이브" == title[i] || "평균 강화메트릭스 사용" == title[i] ||
                    "평균 전차모드 처치" == title[i] || "평균 죽이는 타이어 처치" == title[i] || "평균 난사 처치" == title[i] ||
                    "평균 BOB 처치" == title[i] || "평균 초고열 용광로 처치" == title[i] || "평균 눈보라 처치" == title[i] ||
                    "평균 중력자탄 처치" == title[i]) {
                    if (index_val >= 5.5) { index_val = 100;} else { index_val = index_val*18; }
                }else if("평균 끈 적" == title[i] || "평균 직접 처치" == title[i]) {
                    if (index_val >= 18) { index_val = 100;} else { index_val = index_val*5.55; }
                }else if("평균 밀친 적" == title[i]) {
                    if (index_val >= 85) { index_val = 100;} else { index_val = index_val*1.17; }
                }else if("평균 갈고리 처치" == title[i] || "평균 파일드라이버 처치" == title[i] ) {
                    if (index_val >= 10) { index_val = 100;} else { index_val = index_val*10; }
                }else if("평균 생체수류탄 처치" == title[i] || "평균 융화 처치" == title[i] || "평균 부활" == title[i] || "평균 용검 처치" ==title[i] ||
                    "평균 충격 지뢰 처치" == title[i] || "평균 죽음의꽃 처치" == title[i] || "평균 다이너마이트 처치" == title[i] ||
                    "평균 포화 처치" == title[i] || "평균 폭풍 화살 처치" == title[i] || "평균 처치시야 지원" == title[i] ||
                    "평균 강착 처치" == title[i]) {
                    if (index_val >= 8) { index_val = 100;} else { index_val = index_val*13.33; }
                }else if("평균 재운적" == title[i]) {
                    if (index_val >= 2) { index_val = 100;} else { index_val = index_val*50; }
                }else if("목숨당 방어력 제공" == title[i]) {
                    if (index_val >= 1000) { index_val = 100;} else { index_val = index_val/10; }
                }else if("평균 융화 힐" == title[i] || "평균 공격력 증폭" == title[i] || "평균 튕겨낸 피해량" == title[i] ||
                    "평균 보호막 생성량" == title[i]) {
                    if (index_val >= 2600) { index_val = 100;} else { index_val = index_val/26; }
                }else if("목숭당 자힐량" == title[i]) {
                    if (index_val >= 600) { index_val = 100;} else { index_val = index_val/6; }  //350
                }else if("평균 소리방벽 사용" == title[i] || "평균 덫으로 묶은 적" == title[i] || "평균 포탑 처치" == title[i] ||
                    "평균 얼린적" == title[i] || "평균 감시포탑 처치" == title[i] || "평균 중력붕괴 처치" == title[i]) {
                    if (index_val >= 15) { index_val = 100;} else { index_val = index_val*6.66; }
                }else if("평균 기술로 준 피해" == title[i]) {
                    if (index_val >= 5500) { index_val = 100;} else { index_val = index_val/55; }
                }else if("평균 파멸의 일격 처치" == title[i] || "평균 황야의 무법자 처치" == title[i] || "평균 수색모드 처치" == title[i] ||
                    "평균 단독처치" == title[i] || "평균 충격샷건 처치" == title[i] || "평균 펄스폭탄 부착" == title[i] ||
                    "평균 펄스폭탄 처치" == title[i] || "평균 용의 일격 처치" == title[i] || "평균 원시의분노 처치" == title[i] ||
                    "평균 지뢰밭 처치" == title[i] || "평균 돌진 처치" == title[i] || "평균 돼재앙 처치" == title[i]) {
                    if (index_val >= 4) { index_val = 100;} else { index_val = index_val*25; }
                }else if("평균 해킹한 적" == title[i] || "평균 순간이동한 아군" == title[i]) {
                    if (index_val >= 30) { index_val = 100;} else { index_val = index_val*3.33; }
                }else if("평균 EMP맞춘 적" == title[i] || "평균 나선 로켓 처치" == title[i] || "평균 점프팩 처치" == title[i]) {
                    if (index_val >= 12) { index_val = 100;} else { index_val = 8.33*index_val; }
                }else if("평균 로켓 명중" == title[i]) {
                    if (index_val >= 40) { index_val = 100;} else { index_val = index_val*2.5; }
                }else  {
                    if (index_val <= 50) {
                        if (index_val >= 8) { index_val = 100;} else { index_val = 8*index_val + 32; }
                    }else {
                        if(index_val >= 3000) { index_val= 100;} else {index_val = 0.033*index_val ;}
                    }
                }
            }
            radar_data.push(Math.round(index_val*100)/100.0);
        }else {
            return;
        }
    });
    // console.log(text.detailText);
};

const checkTierAndRankerData = (hero, radar_data) => {
    const heroNameKR = hero.heroNameKR;
    let deathAvg = parseFloat(hero.deathAvg);
    let healPerLife = parseFloat(hero.healPerLife);
    let killPerDeath = parseFloat(hero.killPerDeath);
    let blockDamagePerLife = parseFloat(hero.blockDamagePerLife);
    let lastHitPerLife = parseFloat(hero.lastHitPerLife);
    let damageToHeroPerLife = parseFloat(hero.damageToHeroPerLife);
    let damageToShieldPerLife = parseFloat(hero.damageToShieldPerLife);
    const index = [hero.index1, hero.index2, hero.index3, hero.index4, hero.index5];
    const title = [hero.title1, hero.title2 ,hero.title3, hero.title4, hero.title5];

    if (deathAvg >= 10) { deathAvg = 20;} else if(deathAvg >= 2) {deathAvg = 100 - (deathAvg-2)*10; } else { deathAvg = 100;}
    if (killPerDeath >= 8) { killPerDeath = 100;} else { killPerDeath = 8*killPerDeath + 32; }
    radar_data.push(Math.round(deathAvg*100)/100.0); radar_data.push(Math.round(killPerDeath*100)/100.0);

    if(heal_hero.indexOf(heroNameKR) > 0){          // 힐러 영웅일 시
        if(damageToHeroPerLife >= 1000) { damageToHeroPerLife= 100;} else {damageToHeroPerLife = 0.1*damageToHeroPerLife ;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);

        if(healPerLife >= 2000) { healPerLife= 100;} else {healPerLife = 0.05*healPerLife ;}
        radar_data.push(Math.round(healPerLife*100)/100.0);
        if("바티스트" == heroNameKR) {
            if(damageToShieldPerLife >= 1000) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = 0.1*damageToShieldPerLife ;}
            radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        }
    }else if(deal_hero.indexOf(heroNameKR) > 0) {   // 딜러 영웅일 시
        if(damageToHeroPerLife >= 1500) { damageToHeroPerLife= 100;} else {damageToHeroPerLife = damageToHeroPerLife/15;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);
        if("둠피스트" != "heroNameKR") {
            if(damageToShieldPerLife >= 1500) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = damageToShieldPerLife/15;}
            radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        }
        if (lastHitPerLife >= 5) { lastHitPerLife = 100;} else { lastHitPerLife = 20*lastHitPerLife; }
        radar_data.push(Math.round(lastHitPerLife*100)/100.0);
        if("/메이/시메트라".indexOf(heroNameKR) > 0 ) {
            // radar_label.push("목숨당 막은피해");
            if(blockDamagePerLife >= 1000) { blockDamagePerLife= 100;} else {blockDamagePerLife = 0.1*blockDamagePerLife ;}
            radar_data.push(Math.round(blockDamagePerLife*100)/100.0);
        }
    }else if(tank_hero.indexOf(heroNameKR) > 0) {   // 탱커 영웅일 시
        if(damageToHeroPerLife >= 1800) { damageToHeroPerLife= 100;} else {damageToHeroPerLife = damageToHeroPerLife/18 ;}
        radar_data.push(Math.round(damageToHeroPerLife*100)/100.0);
        // radar_label.push("목숨당 방벽 피해량");
        if(damageToShieldPerLife >= 1500) { damageToShieldPerLife= 100;} else {damageToShieldPerLife = damageToShieldPerLife/15 ;}
        radar_data.push(Math.round(damageToShieldPerLife*100)/100.0);
        if("/로드호그/레킹볼".indexOf(heroNameKR) <= 0 ) {
            // radar_label.push("목숨당 막은피해");
            if(blockDamagePerLife >= 3000) { blockDamagePerLife= 100;} else {blockDamagePerLife = 0.033*blockDamagePerLife ;}
            radar_data.push(Math.round(blockDamagePerLife*100)/100.0);
        }
    }
    $.each(title, function (i, idx) {
        if(idx != "") {
            // radar_label.push(title[i]);
            let index_val = 0;
            if("/직격률/갈고리 명중률/저격 명중률/저격 치명타 명중률/치명타 명중률/평균 에너지/격려(패시브) 지속률".indexOf(title[i]) > 0) {
                index_val = parseFloat(index[i].replace("%", ""));
                if("갈고리 명중률"== title[i]) {
                    if (index_val >= 50) { index_val = 100;} else { index_val = 2*index_val; }
                }else if("평균 에너지" == title[i] || "저격 명중률" == title[i]) {
                    if (index_val >= 60) { index_val = 100;} else { index_val = 5*index_val/3; }
                }else if("격려(패시브) 지속률" == title[i]) {
                    if (index_val >= 40) { index_val = 100;} else { index_val = 2.5*index_val; }
                }else if("직격률" == title[i]){
                    if (index_val >= 20) { index_val = 100;} else { index_val = 5*index_val; }
                }else if("저격 치명타 명중률" == title[i]) {
                    if (index_val >= 30) { index_val = 100;} else { index_val = 3.33*index_val; }
                }else {
                    if (index_val >= 12) { index_val = 100;} else { index_val = 8.33*index_val; }
                }
            }else {
                index_val = parseFloat(index[i]);
                if("목숨당 흡수한 피해" == title[i]) {
                    if (index_val >= 600) { index_val = 100;} else { index_val = index_val/6; }
                }else if("평균 주는방벽" == title[i] ) {
                    if (index_val >= 33) { index_val = 100;} else { index_val = index_val*3; }
                }else if("평균 고에너지 처치" == title[i] || "평균 경계모드 처치" == title[i]) {
                    if (index_val >= 25) { index_val = 100;} else { index_val = index_val*4; }
                }else if("평균 초월 힐" == title[i]) {
                    if (index_val >= 250) { index_val = 100;} else { index_val = index_val/2.5; }
                }else if("평균 자폭 처치" == title[i] || "평균 대지분쇄 처치" == title[i] ||
                    "평균 나노강화제 주입" == title[i] || "평균 불사장치 세이브" == title[i] || "평균 강화메트릭스 사용" == title[i] ||
                    "평균 전차모드 처치" == title[i] || "평균 죽이는 타이어 처치" == title[i] || "평균 난사 처치" == title[i] ||
                    "평균 BOB 처치" == title[i] || "평균 초고열 용광로 처치" == title[i] || "평균 눈보라 처치" == title[i] ||
                    "평균 중력자탄 처치" == title[i]) {
                    if (index_val >= 5.5) { index_val = 100;} else { index_val = index_val*18; }
                }else if("평균 끈 적" == title[i] || "평균 직접 처치" == title[i]) {
                    if (index_val >= 18) { index_val = 100;} else { index_val = index_val*5.55; }
                }else if("평균 밀친 적" == title[i]) {
                    if (index_val >= 85) { index_val = 100;} else { index_val = index_val*1.17; }
                }else if("평균 갈고리 처치" == title[i] || "평균 파일드라이버 처치" == title[i] ) {
                    if (index_val >= 10) { index_val = 100;} else { index_val = index_val*10; }
                }else if("평균 생체수류탄 처치" == title[i] || "평균 융화 처치" == title[i] || "평균 부활" == title[i] || "평균 용검 처치" ==title[i] ||
                    "평균 충격 지뢰 처치" == title[i] || "평균 죽음의꽃 처치" == title[i] || "평균 다이너마이트 처치" == title[i] ||
                    "평균 포화 처치" == title[i] || "평균 폭풍 화살 처치" == title[i] || "평균 처치시야 지원" == title[i] ||
                    "평균 강착 처치" == title[i]) {
                    if (index_val >= 8) { index_val = 100;} else { index_val = index_val*13.33; }
                }else if("평균 재운적" == title[i]) {
                    if (index_val >= 2) { index_val = 100;} else { index_val = index_val*50; }
                }else if("목숨당 방어력 제공" == title[i]) {
                    if (index_val >= 1000) { index_val = 100;} else { index_val = index_val/10; }
                }else if("평균 융화 힐" == title[i] || "평균 공격력 증폭" == title[i] || "평균 튕겨낸 피해량" == title[i] ||
                    "평균 보호막 생성량" == title[i]) {
                    if (index_val >= 2600) { index_val = 100;} else { index_val = index_val/26; }
                }else if("목숭당 자힐량" == title[i]) {
                    if (index_val >= 600) { index_val = 100;} else { index_val = index_val/6; }  //350
                }else if("평균 소리방벽 사용" == title[i] || "평균 덫으로 묶은 적" == title[i] || "평균 포탑 처치" == title[i] ||
                    "평균 얼린적" == title[i] || "평균 감시포탑 처치" == title[i] || "평균 중력붕괴 처치" == title[i]) {
                    if (index_val >= 15) { index_val = 100;} else { index_val = index_val*6.66; }
                }else if("평균 기술로 준 피해" == title[i]) {
                    if (index_val >= 5500) { index_val = 100;} else { index_val = index_val/55; }
                }else if("평균 파멸의 일격 처치" == title[i] || "평균 황야의 무법자 처치" == title[i] || "평균 수색모드 처치" == title[i] ||
                    "평균 단독처치" == title[i] || "평균 충격샷건 처치" == title[i] || "평균 펄스폭탄 부착" == title[i] ||
                    "평균 펄스폭탄 처치" == title[i] || "평균 용의 일격 처치" == title[i] || "평균 원시의분노 처치" == title[i] ||
                    "평균 지뢰밭 처치" == title[i] || "평균 돌진 처치" == title[i] || "평균 돼재앙 처치" == title[i]) {
                    if (index_val >= 4) { index_val = 100;} else { index_val = index_val*25; }
                }else if("평균 해킹한 적" == title[i] || "평균 순간이동한 아군" == title[i]) {
                    if (index_val >= 30) { index_val = 100;} else { index_val = index_val*3.33; }
                }else if("평균 EMP맞춘 적" == title[i] || "평균 나선 로켓 처치" == title[i] || "평균 점프팩 처치" == title[i]) {
                    if (index_val >= 12) { index_val = 100;} else { index_val = 8.33*index_val; }
                }else if("평균 로켓 명중" == title[i]) {
                    if (index_val >= 40) { index_val = 100;} else { index_val = index_val*2.5; }
                }else  {
                    if (index_val <= 50) {
                        if (index_val >= 8) { index_val = 100;} else { index_val = 8*index_val + 32; }
                    }else {
                        if(index_val >= 3000) { index_val= 100;} else {index_val = 0.033*index_val ;}
                    }
                }
            }
            radar_data.push(Math.round(index_val*100)/100.0);
        }else {
            return;
        }
    });
    // console.log(text.detailText);
};

const drawRadarChart = () => {
    const marksCanvas = document.getElementById("chartjs-radar-chart");
    const marksData = {
        labels: radarLabel,
        datasets: [{
            label: "플레이어",
            data: radarData,
            backgroundColor: chartColors[0],
            borderColor: chartColors[0],
            borderWidth: 3,
            pointRadius: 0,
            fill: false
        }, {
            label: "속한 티어 평균",
            data: tierRadarData,
            backgroundColor: chartColors[1],
            borderColor: chartColors[1],
            borderWidth: 3,
            pointRadius: 0,
            fill: false
        },  {
            label: "상위권 평균",
            data: rankerRadarData,
            backgroundColor: chartColors[2],
            borderColor: chartColors[2],
            borderWidth: 3,
            pointRadius: 0,
            fill: false
        }]
    };

    const marksOption = {
        legend: {
            labels: {
                fontColor: 'white',
                fontSize: 13,
                boxWidth: 30
            }
        },
        scale: {
            ticks: {
                maxTicksLimit: 5,
                minTicksLimit: 5,
                display: false,
                beginAtZero: true,
                suggestedMin: 0,
                suggestedMax: 100
                // fontColor: 'white', // labels such as 10, 20, etc
                // showLabelBackdrop: false // hide square behind text
            },
            gridLines: {
                // display:false,
                color: 'rgba(255, 255, 255, 0.2)',
                offsetGridLines: true
            },
            pointLabels: {
                fontColor: 'white', // labels around the edge like 'K/D'
                fontSize: 13
            },
            angleLines: {
                display: false
                // color: 'rgba(255, 255, 255, 0.2)' // lines radiating from the center
            }
        },
        // tooltips: {
        //     // display: false
        // }
    };

    const radarChart = new Chart(marksCanvas, {
        type: 'radar',
        data: marksData,
        options: marksOption
    });
};

const drawTrendline = () => {
    const target_html = $('#trendline-body').html();
    const trendline_template = Handlebars.compile(target_html);

    const roles = {
        role: [{role: "tank", roleImgPath: "/HWimages/role/icon-tank-8a52daaf01.png"},
                {role: "deal", roleImgPath: "/HWimages/role/icon-offense-6267addd52.png"},
                {role: "heal", roleImgPath: "/HWimages/role/icon-support-46311a4210.png"}]
    };

    const item = trendline_template(roles);
    $('.trendline-body').append(item);

    const tank_canvas = document.getElementById("tank-trendline-chart");
    const deal_canvas = document.getElementById("deal-trendline-chart");
    const heal_canvas = document.getElementById("heal-trendline-chart");

    const tank_data = {
        labels: trend_udt_dtm,
        datasets: [{
            label: '경쟁전 점수',
            data: trend_tank_point,
            backgroundColor: chartColors[0],
            borderColor: chartColors[0],
            fill: false,
            type: 'line',
            lineTension: 0,
            yAxisID: 'first-y-axis'
        }, {
            label: '승리 판수',
            data: trend_tank_wingame,
            backgroundColor: 'rgba(186,206,252,0.7)',
            borderColor: 'rgba(186,206,252,0.7)', /*'rgba(104,113,149,0.7)'*/
            type: 'bar',
            yAxisID: 'second-y-axis'
        }, {
            label: '패배 판수',
            data: trend_tank_losegame
            ,
            backgroundColor: 'rgba(12,12,27,0.7)',
            borderColor: 'rgba(12,12,27,0.7)',  /*'rgba(32,41,61,0.7)'*/
            type: 'bar',
            yAxisID: 'second-y-axis'
        }]
    };

    const deal_data = {
        labels: trend_udt_dtm,
        datasets: [{
            label: '경쟁전 점수',
            data: trend_deal_point,
            backgroundColor: chartColors[1],
            borderColor: chartColors[1],
            fill: false,
            type: 'line',
            lineTension: 0,
            yAxisID: 'first-y-axis'
        }, {
            label: '승리 판수',
            data: trend_deal_wingame,
            backgroundColor: 'rgba(186,206,252,0.7)',
            borderColor: 'rgba(186,206,252,0.7)',
            type: 'bar',
            yAxisID: 'second-y-axis'
        }, {
            label: '패배 판수',
            data: trend_deal_losegame,
            backgroundColor: 'rgba(12,12,27,0.7)',
            borderColor: 'rgba(12,12,27,0.7)',
            type: 'bar',
            yAxisID: 'second-y-axis'
        }]
    };

    const heal_data = {
        labels: trend_udt_dtm,
        datasets: [{
            label: '경쟁전 점수',
            data: trend_heal_point,
            backgroundColor: chartColors[2],
            borderColor: chartColors[2],
            fill: false,
            type: 'line',
            lineTension: 0,
            yAxisID: 'first-y-axis'
        }, {
            label: '승리 판수',
            data: trend_heal_wingame,
            backgroundColor: 'rgba(186,206,252,0.7)',
            borderColor: 'rgba(186,206,252,0.7)',
            type: 'bar',
            yAxisID: 'second-y-axis'
        }, {
            label: '패배 판수',
            data: trend_heal_losegame,
            backgroundColor: 'rgba(12,12,27,0.7)',
            borderColor: 'rgba(12,12,27,0.7)',
            type: 'bar',
            yAxisID: 'second-y-axis'
        }]
    };

    const option_sample = {
        maintainAspectRatio: false,
        responsive: true,
        tooltips: {
            mode: 'label',
            intersect: true
        },
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                display: true,
                gridLines: {
                    color: 'rgba(255, 255, 255, 0.2)',
                },
                ticks: {
                    color: 'rgba(255, 255, 255, 0.2)',
                    fontColor: 'white'
                }
            }],
            yAxes: [{
                type: "linear",
                display: true,
                position: "left",
                id: "first-y-axis",
                color: 'rgba(255, 255, 255, 0.2)',
                gridLines:{
                    display: true,
                    color: 'rgba(255, 255, 255, 0.2)'
                },
                ticks: {
                    beginAtZero: true,
                    maxTicksLimit: 3,
                    fontColor : 'white',
                    display: false,
                }
            }, {
                type: "linear",
                display: false,
                position: "right",
                id: "second-y-axis",
                gridLines:{
                    display: false
                },
                ticks: {
                    beginAtZero: true,
                    maxTicksLimit: 3,
                    display: false
                }
            }]
        },
        animation: {
            duration: 1,
            onComplete: function () {
                const chartInstance = this.chart,
                    ctx = chartInstance.ctx;
                ctx.font = Chart.helpers.fontString(15/*Chart.defaults.global.defaultFontSize*/, 600/*Chart.defaults.global.defaultFontStyle*/, Chart.defaults.global.defaultFontFamily);
                ctx.textAlign = 'center';
                ctx.textBaseline = 'bottom';
                // console.log(this.options.tooltips);
                this.data.datasets.forEach(function (dataset, i) {
                    if(i == 0) {
                        const meta = chartInstance.controller.getDatasetMeta(i);
                        meta.data.forEach(function (bar, index) {
                            let data = dataset.data[index];
                            if (data == 0) {
                                data = "(배치)";
                            }
                            // console.log(bar._model.y);
                            if (bar._model.y < 20) {
                                ctx.fillText(data, bar._model.x, bar._model.y + 21);
                            }else {
                                ctx.fillText(data, bar._model.x, bar._model.y - 5);
                            }
                        });
                    }
                });
            }
        }
    };

    const tankTrendlineChart = new Chart(tank_canvas, {
        type: 'bar',
        data: tank_data,
        options: option_sample
    });

    const dealTrendlineChart = new Chart(deal_canvas, {
        type: 'bar',
        data: deal_data,
        options: option_sample
    });

    const healTrendlineChart = new Chart(heal_canvas, {
        type: 'bar',
        data: heal_data,
        options: option_sample
    });
};

const drawProgressBar = () => {
    const divs = $('.progress-container');
    for (let i = 0 ; i < divs.length ; i++) {

        const winLoseGame = divs[i].id;
        const winGame = winLoseGame.substr(0, winLoseGame.indexOf("/"));
        const loseGame = winLoseGame.substr(winLoseGame.indexOf("/")+1, winLoseGame.length);

        let winRate = 0.0;
        if(winGame != '0' && loseGame != '0') {
            winRate = parseFloat(winGame) / (parseFloat(winGame) + parseFloat(loseGame));
        }
        let bar = new ProgressBar.Line(divs[i], {
            strokeWidth: 2,
            easing: 'easeInOut',
            duration: 1500,
            color: 'rgba(232,110,208,1)',
            trailColor: '#50597b',
            trailWidth: 2,
            svgStyle: {width: '100%', height: '100%'},
            text: {
                style: {
                    // Text color.
                    // Default: same as stroke color (options.color)
                    color: '#ffffff',
                    /* position: 'absolute', */
                    right: '0',
                    top: '15px',
                    padding: 0,
                    margin: 0,
                    fontSize: '15px',
                    transform: null
                },
                autoStyleContainer: false
            },
            from: {color: 'rgba(232,110,208,1)'},
            to: {color: 'rgba(232,110,208,1)'},   //#ED6A5A
            step: (state, bar) => {
                bar.setText(Math.round(bar.value() * 100) + ' %');
            }
        });

        bar.animate(winRate);  // Number from 0.0 to 1.0
    }
    setContainerHeight();
};

const setContainerHeight = (set, target) => {
    const window_width = $(window).width();
    // console.log("window width: " + window_width);
    const objSet = document.getElementsByClassName("resultContainer-detail")[0];
    let resultContainer_width = objSet.offsetWidth;

    const menu_box = $('.menu-box')[0];
    const profile = $('.profile')[0];
    const trendline = $('.trendline')[0];
    const detail_box = $('.detail-box')[0];

    let first_row_left = 0;
    let second_row_left = 0;
    let third_row_left = 0;
    let first_row_height = 0;
    let second_row_height = 0;
    let third_row_height = 0;

    if(window_width >= 1200) {
        second_row_left = resultContainer_width/3;
        third_row_left = resultContainer_width/3*2;

        menu_box.style.top = first_row_height + "px";
        menu_box.style.left = first_row_left + "px";
        first_row_height += menu_box.offsetHeight + 20;

        profile.style.top = second_row_height + "px";
        profile.style.left = second_row_left + "px";
        // second_row_height += profile.offsetHeight + 20;
        second_row_height += 510 + 20;

        trendline.style.top = third_row_height + "px";
        trendline.style.left = third_row_left + "px";
        third_row_height += trendline.offsetHeight + 20;
        // second_row_height += trendline.offsetHeight + 20;

        detail_box.style.top = second_row_height + "px";
        detail_box.style.left = second_row_left + "px";
        second_row_height += detail_box.offsetHeight + 20;

    }else if(window_width >= 768 && window_width < 1200) {
        second_row_left = resultContainer_width/2;

        profile.style.top = first_row_height + "px";
        profile.style.left = first_row_left + "px";
        first_row_height += 510 + 20;

        trendline.style.top = second_row_height + "px";
        trendline.style.left = second_row_left + "px";
        second_row_height += 510 + 20;

        menu_box.style.top = first_row_height + "px";
        menu_box.style.left = first_row_left + "px";
        first_row_height += menu_box.offsetHeight + 20;

        detail_box.style.top = second_row_height + "px";
        detail_box.style.left = second_row_left + "px";
        second_row_height += detail_box.offsetHeight + 20;
    }else {
        profile.style.top = first_row_height + "px";
        profile.style.left = first_row_left + "px";
        first_row_height += 510 + 20;

        trendline.style.top = first_row_height + "px";
        trendline.style.left = first_row_left + "px";
        first_row_height += trendline.offsetHeight + 20;

        menu_box.style.top = first_row_height + "px";
        menu_box.style.left = first_row_left + "px";
        first_row_height += menu_box.offsetHeight + 20;

        detail_box.style.top = first_row_height + "px";
        detail_box.style.left = first_row_left + "px";
        first_row_height += detail_box.offsetHeight + 20;
    }
    // console.log(second_row_left, third_row_left);
    const objTarHeight = document.getElementsByClassName("player-detail-layout")[0].offsetHeight;
    const biggest_height = Math.max(first_row_height, second_row_height, third_row_height);
    objSet.style.height = biggest_height + "px";
    // console.log(biggest_height);
};

main.init();