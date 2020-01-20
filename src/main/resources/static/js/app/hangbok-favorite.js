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
    },
    search : function (userInput) {
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
    console.log(data.udtDtm);
    return {portrait: data.portrait, battleTag: data.battleTag, playerLevel: data.playerLevel, platform: data.platform, tankRatingPoint: data.tankRatingPoint, dealRatingPoint: data.dealRatingPoint, healRatingPoint: data.healRatingPoint, winRate: data.winRate,
        mostHero1: "/HWimages/hero/"+data.mostHero1+"_s.png", mostHero2: "/HWimages/hero/"+data.mostHero2+"_s.png", mostHero3: "/HWimages/hero/"+data.mostHero3+"_s.png", isPublic: data.isPublic, forUrl: data.forUrl, tankRatingImg: data.tankRatingImg,
        dealRatingImg: data.dealRatingImg, healRatingImg: data.healRatingImg, wingame: data.winGame, losegame: data.loseGame, udtDtm: data.udtDtm};
}

