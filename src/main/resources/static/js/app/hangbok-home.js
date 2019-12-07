let playerData = [];
let count = 0;

const main = {
    init : function(){
        // alert('main init 호출');
        $(window).on('load', function () {
            const flag = $('.fromDetail').attr("id");
            const userInput = $('.fromDetail').attr("value");;
            // console.log("flag : " + flag + " , userInput : " + userInput);
            if(flag=='Y') {
                _this.search(userInput);

                event.preventDefault();

                $('html,body').animate({
                    scrollTop: $('.goto-here').offset().top
                }, 500, 'easeInOutExpo');
            }
        });

        const _this = this;
        $('#btn-search').on('click', function (event) {
            _this.search("");

            event.preventDefault();

            $('html,body').animate({
                scrollTop: $('.goto-here').offset().top
            }, 500, 'easeInOutExpo');

            return false;
        });

        $('#btn-search-tap').on('click', function (event) {
            _this.search("");

            event.preventDefault();

            $('html,body').animate({
                scrollTop: $('.goto-here').offset().top
            }, 500, 'easeInOutExpo');

            return false;
        });
        // 엔터키 눌렀을 때 search 메소드 호
        $(document).keypress(function (e) {
            if (e.which == 13) {
                // alert('enter key is pressed');
                _this.search("");

                e.preventDefault();

                $('html,body').animate({
                    scrollTop: $('.goto-here').offset().top
                }, 500, 'easeInOutExpo');

                return false;
            }
        });
    },
    search : function (userInput) {
        // alert('main search 호출');
        playerData = [];
        $("#search-result").remove();
        $(".grid-header").remove();
        $(".more_btn_div").remove();
        let playerName = $('input[id="playerName"]').val();
        const playerName_tap = $('input[id="playerName-tap"]').val();

        if(playerName == "" && playerName_tap =="") {
            playerName = userInput;
        }else if(playerName == "" && playerName_tap !="") {
            playerName = playerName_tap;
        }
        const inputName = {
            playerName: playerName
        };
        console.log("playerName : " + playerName);
        $.ajax({
            type: 'POST',
            url: '/showPlayerList',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(inputName),
            async : false
        }).done(function (datas) {
            // console.log("showPlayerList => " + datas);
            // console.log("/showPlayerList => 결과 내역 사이즈 : " + datas.length);
            count = datas.length;
            if(datas.length != 0) {
                $.each(datas, function (idx, data) {
                    playerData.push(data);
                });
                let player_list = $("#player_list").html();
                let template = Handlebars.compile(player_list);

                initPlayers(template);
            }else {
                $.ajax({
                    type: 'POST',
                    url: '/showUserList',
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(inputName),
                    async : false
                }).done(function (crawlingResult) {
                    // console.log("showUserList => " + crawlingResult);
                    // console.log("/showUserList => 결과 내역 사이즈 : " + crawlingResult.length);
                    if(crawlingResult.length == 0) {
                        const noticeDiv = $('<div class="player notice_playerList"> 행복워치에 등록된 유저가 아닙니다.<br>' +
                            '행복워치에서 최초 검색 시 배틀태그(플레이어명#00000)를 정확하게 입력해야 합니다.</div>');
                        //const testDiv = $('<div></div>');
                        $("#search-result").append(noticeDiv);
                    }else {
                        $.each(crawlingResult, function(i, player){
                            // console.log(player.battleTag, player.playerName);
                            location.href = "/showPlayerDetail/" + player.forUrl;
                        });
                    }
                });
            }
        });

        $('input[id="playerName"]').val("");
    }
};
main.init();

function initPlayers(template) {

    $(".more_btn_div").remove();
    var cnt = 15;
    if (playerData.length < 15) {
        cnt = playerData.length;
    }

    let item = {
        players: []
    };
    for (var i = 0 ; i < cnt ; i++) {
        var data = playerData[i];
        if(data.battleTag === 'message') {
            alert(data.playerName);
            return false;
        }
        const player = drawList(data);
        item.players.push(player);
    }

    const player_list = template(item);
    $('.player-list-container').append(player_list);
    $('.grid-header').text("총 "+ count +"건");

    if(cnt == 15) {
        const moreButtonDiv = $('<div class="more_btn_div" align="center">'
            + '<hr><a id="more_btn" href="javascript:morePlayers('+template+');">더보기(More)</a><hr>'
            + '</div>');
        $("#more-button").append(moreButtonDiv);
    }
    playerData.splice(0,cnt);
}

function morePlayers() {
    $(".more_btn_div").remove();
    var cnt = 15;
    if (playerData.length < 15) {
        cnt = playerData.length;
    }

    let item = {
        players: []
    };
    for (var i = 0 ; i < cnt ; i++) {
        var data = playerData[i];
        if(data.battleTag === 'message') {
            alert(data.playerName);
            return false;
        }
        const player = drawList(data);
        item.players.push(player);
    }

    let more_player_list = $("#more_player_list").html();
    let template = Handlebars.compile(more_player_list);
    const player_list = template(item);
    $('.tbody').append(player_list);

    if(cnt == 15) {
        const moreButtonDiv = $('<div class="more_btn_div" align="center">'
            + '<hr><a id="more_btn" href="javascript:morePlayers('+template+');">더보기(More)</a><hr>'
            + '</div>');
        $("#more-button").append(moreButtonDiv);
    }
    playerData.splice(0,cnt);
}

function playerDetail(obj) {
    console.log($(obj).attr('attr-p'), $(obj).attr('attr-u'));
    const isPublic = $(obj).attr('attr-p');
    const forUrl = $(obj).attr('attr-u');
    if(isPublic == 'Y') {
        location.href = "/showPlayerDetail/"+forUrl;
    } else {
        alert("플레이어가 프로필을 비공개했습니다.\n" +
            "인게임에서 프로필 공개 이후 다시 검색해주세요.");
        return;
    }
}

function drawList(data) {
    // return new Promise(function(data){})
    if(data.battleTag === 'message') {
        alert(data.playerName);
        return false;
    }

    return {portrait: data.portrait, battleTag: data.battleTag, playerLevel: data.playerLevel, platform: data.platform, tankRatingPoint: data.tankRatingPoint, dealRatingPoint: data.dealRatingPoint, healRatingPoint: data.healRatingPoint, winRate: data.winRate,
    mostHero1: "/HWimages/hero/"+data.mostHero1+"_s.png", mostHero2: "/HWimages/hero/"+data.mostHero2+"_s.png", mostHero3: "/HWimages/hero/"+data.mostHero3+"_s.png", isPublic: data.isPublic, forUrl: data.forUrl, tankRatingImg: data.tankRatingImg,
    dealRatingImg: data.dealRatingImg, healRatingImg: data.healRatingImg, wingame: data.winGame, losegame: data.loseGame};
}

