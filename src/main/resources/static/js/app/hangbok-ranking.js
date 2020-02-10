const tank_point = [];
const tank_ranking_player = [];
const tank_ranking_player_portrait = [];
const deal_point = [];
const deal_ranking_player = [];
const deal_ranking_player_portrait = [];
const heal_point = [];
const heal_ranking_player = [];
const heal_ranking_player_portrait = [];
const play_time = [];
const play_time_ranking_player = [];
const play_time_ranking_player_portrait = [];
const spent_on_fire = [];
const spent_on_fire_ranking_player = [];
const spent_on_fire_ranking_player_portrait = [];
const env_kill = [];
const env_kill_ranking_player = [];
const env_kill_ranking_player_portrait = [];

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
        location.href = "/showPlayerListFromDetail/" + playerName;
    }
};

const getRankingData = () => {
    console.log("getRankingData 호출");

    $.ajax({type: 'POST',
        url: '/getRankingData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        async : false
    }).done(function (datas) {
        const tankRating = datas.tankRating;
        const dealRating = datas.dealRating;
        const healRating = datas.healRating;
        const playTime = datas.playTime;
        const spentOnFire = datas.spentOnFire;
        const envKill = datas.envKill;

        $.each(tankRating, function (i, val) {
            tank_point.push(val.tankRatingPoint); tank_ranking_player.push(val.playerName); tank_ranking_player_portrait.push(val.portrait);
            deal_point.push(dealRating[i].dealRatingPoint); deal_ranking_player.push(dealRating[i].playerName); deal_ranking_player_portrait.push(dealRating[i].portrait);
            heal_point.push(healRating[i].healRatingPoint); heal_ranking_player.push(healRating[i].playerName); heal_ranking_player_portrait.push(healRating[i].portrait);
            play_time.push(playTime[i].playTime); play_time_ranking_player.push(playTime[i].playerName); play_time_ranking_player_portrait.push(playTime[i].portrait);
            spent_on_fire.push(spentOnFire[i].dealRatingPoint); spent_on_fire_ranking_player.push(spentOnFire[i].playerName); spent_on_fire_ranking_player_portrait.push(spentOnFire[i].portrait);
            env_kill.push(envKill[i].dealRatingPoint); env_kill_ranking_player.push(envKill[i].playerName); env_kill_ranking_player_portrait.push(envKill[i].portrait);
        });

    });
};

const setContainerHeight = () => {
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