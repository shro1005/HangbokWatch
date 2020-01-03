let count;
let detailList = [];
let cnt = 0;
let before_order = 1;
let now_order = 1;
const radar_label = [];
const radar_data = [];
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

const main = {
    init : function(){
        console.log("main.init 호출");
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
        location.href = "/showPlayerListFromDetail/" + playerName;

    },
    doRefresh : function () {
        console.log("doRefresh 호출");
        $('.btn-refresh').addClass('clicked');
        // console.log($('.user-name').text());
        const forUrl = $('.user-name').text().replace(" #" , "-");
        // console.log(forUrl);

        location.href = "/refreshPlayerDetail/" + forUrl;
        // $('.btn-refresh').removeClass('clicked');

    },
    doFavorite : function () {
        console.log("doFavorite 호출");
        if($('.btn-like').hasClass('clicked') === true) {
            $('.btn-like').removeClass('clicked');
            $('.mdi-heart').addClass('mdi-heart-outline');
            $('.mdi-heart-outline').removeClass('mdi-heart');
        }else {
            $('.btn-like').addClass('clicked');
            $('.mdi-heart-outline').addClass('mdi-heart');
            $('.mdi-heart').removeClass('mdi-heart-outline');
        }

    }
};

const getDetailData = () => {
    console.log("getDetailData 호출");
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
        count = datas.length;
        $.each(datas, function (i, val) {
            if (i <= 6) {
                const heroNameKR = val.heroNameKR;
                const heroName = val.heroName;
                const winRate = val.winRate;
                const playTime = val.playTime;
                const spentOnFireAvg = val.spentOnFireAvg;
                const order = val.order;

                hero.heros.push({
                    heroName: heroName,
                    heroNameKR: heroNameKR,
                    playTime: playTime,
                    detail: "javascript:drawDetail("+order+")",
                    src: "/HWimages/hero/" + heroName + "_s.png",
                    order: order
                });
            }
            detailList.push(val);
            // console.log(val);
        });
        const item = template(hero);
        $('.menu-box-menu').append(item);
    });
    if (count > 7) {
        const moreButtonDiv = $('<li><a class="more-button" href="javascript:moreHero();">전체 조회</a></li>');
        $('.menu-box-menu').append(moreButtonDiv);
    }
    drawDetail(1);
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
                order: val.order
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
    console.log(before_order, now_order);
    $(`#order-${before_order}`).removeClass('clicked');
    $(`#order-${now_order}`).addClass('clicked');

    $('.detail-header').remove();
    $('.detail-body').remove();

    const hero = detailList[order-1];
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

    checkHero(hero, detail, text);

    const item = template(detail);
    $('.detail').append(item);

    const item2 = template2(text);
    $('.index-text').append(item2);

    drawRadarChart();
    setContainerHeight();
};

const checkHero = (hero, detail, text) => {
    const heroNameKR = hero.heroNameKR;
    const deathAvg = hero.deathAvg;
    const spentOnFireAvg = hero.spentOnFireAvg;
    const healPerLife = hero.healPerLife;
    const heroName = hero.heroName;
    const winRate = hero.winRate;
    const playTime = hero.playTime;
    const killPerDeath = hero.killPerDeath;
    const blockDamagePerLife = hero.blockDamagePerLife;
    const lastHitPerLife = hero.lastHitPerLife;
    const damageToHeroPerLife = hero.damageToHeroPerLife;
    const damageToShieldPerLife = hero.damageToShieldPerLife;
    const index = [hero.index1, hero.index2, hero.index3, hero.index4, hero.index5];
    // const index2 = hero.index2;
    // const index3 = hero.index3;
    // const index4 = hero.index4;
    // const index5 = hero.index5;
    const title = [hero.title1, hero.title2 ,hero.title3, hero.title4, hero.title5];
    // const title2 = hero.title2;
    // const title3 = hero.title3;
    // const title4 = hero.title4;
    // const title5 = hero.title5;

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
    if(heal_hero.indexOf(heroNameKR) > 0){          // 힐러 영웅일 시
        text.detailText.push({title: "평균 죽음", index: deathAvg});
        text.detailText.push({title: "목숨당 힐량", index: healPerLife});
        text.detailText.push({title: "목숨당 영웅 피해량", index: damageToHeroPerLife});
        radar_label.push("평균 죽음"); radar_label.push("목숨당 힐량"); radar_label.push("목숨당 영웅 딜량");
        radar_data.push(deathAvg); radar_data.push(healPerLife); radar_data.push(damageToHeroPerLife);
        if("바티스트" == heroNameKR) {
            text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
            radar_label.push("목숨당 방벽 피해량"); radar_data.push(damageToShieldPerLife);
        }
    }else if(deal_hero.indexOf(heroNameKR) > 0) {   // 딜러 영웅일 시
        text.detailText.push({title: "평균 죽음", index: deathAvg});
        text.detailText.push({title: "목숨당 영웅 피해량", index: damageToHeroPerLife});
        text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
        text.detailText.push({title: "목숨당 결정타(킬캐치)", index: lastHitPerLife});
        radar_label.push("평균 죽음");radar_label.push("목숨당 영웅 딜량"); radar_label.push("목숨당 방벽 피해량"); radar_data.push("목숨당 결정타(킬캐치)")
        radar_data.push(deathAvg); radar_data.push(damageToHeroPerLife); radar_data.push(damageToShieldPerLife); radar_data.push(lastHitPerLife);
        if("/메이/시메트라".indexOf(heroNameKR) > 0 ) {
            text.detailText.push({title: "목숨당 막은피해", index: blockDamagePerLife});
            radar_label.push("목숨당 막은피해"); radar_data.push(blockDamagePerLife);
        }
    }else if(tank_hero.indexOf(heroNameKR) > 0) {   // 탱커 영웅일 시
        text.detailText.push({title: "평균 죽음", index: deathAvg});
        text.detailText.push({title: "목숨당 영웅 피해량", index: damageToHeroPerLife});
        text.detailText.push({title: "목숨당 방벽 피해량", index: damageToShieldPerLife});
        if("/로드호그".indexOf(heroNameKR) <= 0 ) {
            text.detailText.push({title: "목숨당 막은피해", index: blockDamagePerLife});
            radar_label.push("목숨당 막은피해"); radar_data.push(blockDamagePerLife);
        }
    }

    $.each(index, function (i, idx) {
        if(idx != "") {
            text.detailText.push({title: title[i], index: index[i]});
            radar_label.push(title[i]); radar_data.push(index[i]);
        }else {
            return;
        }
    });
    // console.log(text.detailText);
};

const drawRadarChart = () => {
    const marksCanvas = document.getElementById("chartjs-radar-chart");
    const marksData = {
        labels: ["English", "Maths", "Physics", "Chemistry", "Biology", "History"],
        datasets: [{
            label: "Student A",
            data: [24, 55, 30, 56, 60, 68],
            backgroundColor: chartColors[0],
            borderColor: chartColors[0],
            borderWidth: 3,
            pointRadius: 0,
            fill: false
        }, {
            label: "Student B",
            data: [54, 65, 60, 70, 70, 75],
            backgroundColor: chartColors[1],
            borderColor: chartColors[1],
            borderWidth: 3,
            pointRadius: 0,
            fill: false
        },  {
            label: "Student c",
            data: [43, 13, 33, 57, 50, 75],
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
                display: false
                // beginAtZero: true,
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
        }
    };

    const radarChart = new Chart(marksCanvas, {
        type: 'radar',
        data: marksData,
        options: marksOption
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
        second_row_height += 506 + 20;

        trendline.style.top = third_row_height + "px";
        trendline.style.left = third_row_left + "px";
        third_row_height += trendline.offsetHeight + 20;
        // second_row_height += trendline.offsetHeight + 20;

        detail_box.style.top = second_row_height + "px";
        detail_box.style.left = second_row_left + "px";
        second_row_height += detail_box.offsetHeight + 20;

    }else if(window_width >= 768 && window_width < 1200) {
        second_row_left = resultContainer_width/2;

        menu_box.style.top = first_row_height + "px";
        menu_box.style.left = first_row_left + "px";
        first_row_height += menu_box.offsetHeight + 20;

        profile.style.top = second_row_height + "px";
        profile.style.left = second_row_left + "px";
        second_row_height += profile.offsetHeight + 20;

        trendline.style.top = first_row_height + "px";
        trendline.style.left = first_row_left + "px";
        first_row_height += trendline.offsetHeight + 20;

        detail_box.style.top = second_row_height + "px";
        detail_box.style.left = second_row_left + "px";
        second_row_height += detail_box.offsetHeight + 20;
    }else {
        profile.style.top = first_row_height + "px";
        profile.style.left = first_row_left + "px";
        first_row_height += profile.offsetHeight + 20;

        menu_box.style.top = first_row_height + "px";
        menu_box.style.left = first_row_left + "px";
        first_row_height += menu_box.offsetHeight + 20;

        detail_box.style.top = first_row_height + "px";
        detail_box.style.left = first_row_left + "px";
        first_row_height += detail_box.offsetHeight + 20;

        trendline.style.top = first_row_height + "px";
        trendline.style.left = first_row_left + "px";
        first_row_height += trendline.offsetHeight + 20;
    }
    // console.log(second_row_left, third_row_left);
    const objTarHeight = document.getElementsByClassName("player-detail-layout")[0].offsetHeight;
    const biggest_height = Math.max(first_row_height, second_row_height, third_row_height);
    objSet.style.height = biggest_height + "px";
    // console.log(biggest_height);
};

main.init();