const total_ranker_list = [];
const tank_ranker_list = [];
const deal_ranker_list = [];
const heal_ranker_list = [];
let total_offset = 0;
let tank_offset = 0;
let deal_offset = 0;
let heal_offset = 0;
const limit = 25;
const chartColors = ['#fcb150', '#11a8ab' ,'#e64c65'];

$(window).resize(function () {
    // 창크기 변화 감지
    setContainerHeight();
});

const main = {
    init: function () {
        // console.log("main.init 호출");
        const _this = this;
        $('#btn-search').on('click', function (event) {
            _this.search();
            return false;
        });

        // 엔터키 눌렀을 때 search 메소드 호
        $(document).keypress(function (e) {
            if (e.which == 13) {
                // alert('enter key is pressed');
                _this.search("");

                return false;
            }
        });

        getRankingData();
        setContainerHeight();
        showRankerList("total");
    },
    search: function () {
        // alert('main search 호출');
        let playerName = $('input[id="playerName"]').val();
        if (playerName == "") {
            return false;
        } else if (playerName.indexOf("#") != -1) {
            // alert("detail playerName # : " + playerName);
            playerName = playerName.replace("#", "-");
        }
        // alert("detail playerName - : " + playerName);
        // console.log("검색한 playerName : " + playerName);
        location.href = "/search/" + playerName;
    }
};

const getRankingData = () => {
    console.log("getRankingData 호출");

    // 표시할 랭킹 목록 생성
    let ranking_list = $("#our-ranking-list").html();
    let ranking_list_template = Handlebars.compile(ranking_list);

    let list = {
        list:[{className : "tank-rating-ranking", title : "금주의 상한가(돌격)"},
              {className : "deal-rating-ranking", title : "금주의 상한가(공격)"},
              {className : "heal-rating-ranking", title : "금주의 상한가(지원)"},
              {className : "playtime-ranking", title : "금주의 오버워치 덕후"},
              {className : "spendonfire-ranking", title : "금주의 폭주왕"},
              {className : "envkill-ranking", title : "금주의 낙사왕"}]
    };

    const item = ranking_list_template(list);
    $('.main-container').append(item);

    // 랭킹 목록에 들어갈 각 내용들 조회
    $.ajax({type: 'POST',
        url: '/getRankingData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        async : false
    }).done(function (datas) {
        // const tankRating = datas.tankRating;
        // const dealRating = datas.dealRating;
        // const healRating = datas.healRating;
        // const playTime = datas.playTime;
        // const spentOnFire = datas.spentOnFire;
        // const envKill = datas.envKill;
        $.each(datas, function (j, data) {
            let ranking_contents = $("#our-ranking-contents").html();
            let ranking_cpontents_template = Handlebars.compile(ranking_contents);

            let ranking = {
                ranking:[]
            };
            let className = "";
            $.each(data, function (i, val) {
                let forUrl = val.battleTag.replace("#", '-');
                ranking.ranking.push({ranking : "ranking-" + (i+1), rankingImgPath : "/HWimages/ranking/icon-"+(i+1)+"-ranking.png", playerPortrait : val.portrait, playerName: val.playerName, score : val.score, forUrl: forUrl})
                className = val.className;
            });
            const item = ranking_cpontents_template(ranking);
            $('.'+className).append(item);
        });
    });
};

const getRankerData = (target, offset, limit) => {
    const input = {target : target, offset : offset, limit : limit};

    // 랭커 목록에 들어갈 각 내용들 조회
    $.ajax({type: 'POST',
        url: '/getRankerData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(input),
        async : false
    }).done(function (datas) {
        // console.log(datas);
        $.each(datas, function (i, data) {
            if(target == "total") {
                total_ranker_list.push(data);
            }else if(target == "tank") {
                tank_ranker_list.push(data);
            }else if(target == "deal") {
                deal_ranker_list.push(data);
            }else if(target == "heal") {
                heal_ranker_list.push(data);
            }
        });
    });
};

function drawList(i, data) {
    // console.log(data);
    // console.log(data.portrait, data.battleTag, data.playerLevel, data.mostHero1, data.mostHero3, data.tankRatingPoint);
    return {portrait: data.portrait, battleTag: data.battleTag, playerLevel: data.playerLevel, platform: data.platform, tankRatingPoint: data.tankRatingPoint, dealRatingPoint: data.dealRatingPoint, healRatingPoint: data.healRatingPoint, winRate: data.winRate,
        mostHero1: "/HWimages/hero/"+data.mostHero1+"_s.png", mostHero2: "/HWimages/hero/"+data.mostHero2+"_s.png", mostHero3: "/HWimages/hero/"+data.mostHero3+"_s.png", isPublic: data.isPublic, forUrl: data.forUrl, tankRatingImg: data.tankRatingImg,
        dealRatingImg: data.dealRatingImg, healRatingImg: data.healRatingImg, wingame: data.winGame, losegame: data.loseGame, udtDtm: data.udtDtm, ranking : (i+1)};
}

const drawMoreButton = (target) => {
    if(target == "total") {
        if (total_offset <= 100) {
            const moreButtonDiv = $('<div class="more-btn-div" align="center">'
                + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
                + '</div>');
            $(".player-list-container").append(moreButtonDiv);
        }

    }else if(target == "tank") {
        if (tank_offset <= 100) {
            const moreButtonDiv = $('<div class="more-btn-div" align="center">'
                + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
                + '</div>');
            $(".player-list-container").append(moreButtonDiv);
        }

    }else if(target == "deal") {
        if (deal_offset <= 100) {
            const moreButtonDiv = $('<div class="more-btn-div" align="center">'
                + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
                + '</div>');
            $(".player-list-container").append(moreButtonDiv);
        }
    }else if(target == "heal") {
        if (heal_offset <= 100) {
            const moreButtonDiv = $('<div class="more-btn-div" align="center">'
                + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
                + '</div>');
            $(".player-list-container").append(moreButtonDiv);
        }
    }
};

const drawRankerList = (target) => {
    $(".player-list-container").empty();
    const item = {
        players: []
    };

    if(target == "total") {
        $.each(total_ranker_list, function (i, data) {
            const player = drawList(i, data);
            item.players.push(player);
        });

    }else if(target == "tank") {
        // console.log(tank_ranker_list);
        $.each(tank_ranker_list, function (i, data) {
            const player = drawList(i, data);
            item.players.push(player);
        });

    }else if(target == "deal") {
        // console.log(deal_ranker_list);
        $.each(deal_ranker_list, function (i, data) {
            const player = drawList(i, data);
            item.players.push(player);
        });

    }else if(target == "heal") {
        // console.log(heal_ranker_list);
        $.each(heal_ranker_list, function (i, data) {
            const player = drawList(i, data);
            item.players.push(player);
        });

    }
    const ranker_list = $("#ranking_player_list").html();
    // console.log(ranker_list);
    const template = Handlebars.compile(ranker_list);
    const player_list = template(item);
    $(".player-list-container").append(player_list);

    drawMoreButton(target);
};

const showRankerList = (target) => {
    $('.active').removeClass('active');
    if(target == "total") {
        $('.total_titular').addClass('active');
        // console.log("total_offset : " +total_offset);
        if (total_offset == 0) {
            getRankerData(target, total_offset, limit);
            drawRankerList(target);
            total_offset += limit;
        }else {
            drawRankerList(target);
        }
    }else if(target == "tank") {
        $('.tank_titular').addClass('active');
        // console.log("tank_offset : " +tank_offset);
        if (tank_offset == 0) {
            getRankerData(target, tank_offset, limit);
            drawRankerList(target);
            tank_offset += limit;
        }else {
            drawRankerList(target);
        }
    }else if(target == "deal") {
        $('.deal_titular').addClass('active');
        // console.log("deal_offset : " +deal_offset);
        if (deal_offset == 0) {
            getRankerData(target, deal_offset, limit);
            drawRankerList(target);
            deal_offset += limit;
        }else {
            drawRankerList(target);
        }
    }else if(target == "heal") {
        $('.heal_titular').addClass('active');
        // console.log("heal_offset : " +heal_offset);
        if (heal_offset == 0) {
            getRankerData(target, heal_offset, limit);
            drawRankerList(target);
            heal_offset += limit;
        }else {
            drawRankerList(target);
        }
    }
};

const drawMoreRankerList = (target, offset) => {
    const item = {
        players: []
    };

    if(target == "total") {
        $.each(total_ranker_list, function (i, data) {
            if (i >= offset) {
                const player = drawList(i, data);
                item.players.push(player);
            }
        });

    }else if(target == "tank") {
        $.each(tank_ranker_list, function (i, data) {
            if (i >= offset) {
                const player = drawList(i, data);
                item.players.push(player);
            }
        });

    }else if(target == "deal") {
        $.each(deal_ranker_list, function (i, data) {
            if (i >= offset) {
                const player = drawList(i, data);
                item.players.push(player);
            }
        });

    }else if(target == "heal") {
        $.each(heal_ranker_list, function (i, data) {
            if (i >= offset) {
                const player = drawList(i, data);
                item.players.push(player);
            }
        });

    }
    const ranker_list = $("#more_player_list").html();
    const template = Handlebars.compile(ranker_list);
    const player_list = template(item);
    $('.tbody:last').append(player_list);

    drawMoreButton(target);
};

const morePlayers = (target) => {
    $(".more-btn-div").remove();
    if(target == "total") {
        getRankerData(target, total_offset, limit);
        drawMoreRankerList(target, total_offset);
        total_offset += limit;

    }else if(target == "tank") {
        getRankerData(target, tank_offset, limit);
        drawMoreRankerList(target, tank_offset);
        tank_offset += limit;

    }else if(target == "deal") {
        getRankerData(target, deal_offset, limit);
        drawMoreRankerList(target, deal_offset);
        deal_offset += limit;

    }else if(target == "heal") {
        getRankerData(target, heal_offset, limit);
        drawMoreRankerList(target, heal_offset);
        heal_offset += limit;
    }
};

function playerDetail(obj) {
    // console.log($(obj).attr('attr-p'), $(obj).attr('attr-u'));

    const forUrl = $(obj).attr('attr-u');
    location.href = "/showPlayerDetail/"+forUrl;
}

const setContainerHeight = () => {
    const window_width = $(window).width();
    // console.log("window width: " + window_width);
    const objSet = document.getElementsByClassName("resultContainer-detail")[0];
    let resultContainer_width = objSet.offsetWidth;

    const tank_rating_ranking_box = $('.tank-rating-ranking-box')[0];
    const deal_rating_ranking_box = $('.deal-rating-ranking-box')[0];
    const heal_rating_ranking_box = $('.heal-rating-ranking-box')[0];
    const playtime_ranking_box = $('.playtime-ranking-box')[0];
    const spendonfire_ranking_box = $('.spendonfire-ranking-box')[0];
    const envkill_ranking_box = $('.envkill-ranking-box')[0];

    let first_row_left = 0;
    let second_row_left = 0;
    let third_row_left = 0;
    let first_row_height = 0;
    let second_row_height = 0;
    let third_row_height = 0;

    if(window_width >= 1200) {
        second_row_left = resultContainer_width/3;
        third_row_left = resultContainer_width/3*2;

        tank_rating_ranking_box.style.top = first_row_height + "px";
        tank_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;
        //first_row_height += tank_rating_ranking_box.offsetHeight + 20;

        deal_rating_ranking_box.style.top = second_row_height + "px";
        deal_rating_ranking_box.style.left = second_row_left + "px";
        // second_row_height += deal_rating_ranking_box.offsetHeight + 20;
        second_row_height += 313;

        heal_rating_ranking_box.style.top = third_row_height + "px";
        heal_rating_ranking_box.style.left = third_row_left + "px";
        // third_row_height += heal_rating_ranking_box.offsetHeight + 20;
        third_row_height += 313;

        playtime_ranking_box.style.top = first_row_height + "px";
        playtime_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        spendonfire_ranking_box.style.top = second_row_height + "px";
        spendonfire_ranking_box.style.left = second_row_left + "px";
        second_row_height += 313;

        envkill_ranking_box.style.top = third_row_height + "px";
        envkill_ranking_box.style.left = third_row_left + "px";
        third_row_height += 313;

    }else if(window_width >= 768 && window_width < 1200) {
        second_row_left = resultContainer_width/2;

        tank_rating_ranking_box.style.top = first_row_height + "px";
        tank_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        deal_rating_ranking_box.style.top = second_row_height + "px";
        deal_rating_ranking_box.style.left = second_row_left + "px";
        second_row_height += 313;

        heal_rating_ranking_box.style.top = first_row_height + "px";
        heal_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        playtime_ranking_box.style.top = second_row_height + "px";
        playtime_ranking_box.style.left = second_row_left + "px";
        second_row_height += 313;

        spendonfire_ranking_box.style.top = first_row_height + "px";
        spendonfire_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        envkill_ranking_box.style.top = second_row_height + "px";
        envkill_ranking_box.style.left = second_row_left + "px";
        second_row_height += 313;
    }else {
        tank_rating_ranking_box.style.top = first_row_height + "px";
        tank_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        deal_rating_ranking_box.style.top = first_row_height + "px";
        deal_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        heal_rating_ranking_box.style.top = first_row_height + "px";
        heal_rating_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        playtime_ranking_box.style.top = first_row_height + "px";
        playtime_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        spendonfire_ranking_box.style.top = first_row_height + "px";
        spendonfire_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

        envkill_ranking_box.style.top = first_row_height + "px";
        envkill_ranking_box.style.left = first_row_left + "px";
        first_row_height += 313;

    }
    // console.log(second_row_left, third_row_left);
    const objTarHeight = document.getElementsByClassName("player-detail-layout")[0].offsetHeight;
    const biggest_height = Math.max(first_row_height, second_row_height, third_row_height);
    objSet.style.height = biggest_height + "px";
    // console.log(biggest_height);
};

main.init();