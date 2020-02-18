let playerData = [];
let my_favorite_count = 0;
let favoriting_me_count = 0;
let my_favorite_players = [];
let favoriting_me_players = [];

const main = {
    init : function(){
        // alert('main init 호출');
        $(window).on('load', function () {
            const flag = $('.fromDetail').attr("id");
            const userInput = $('.fromDetail').attr("value");
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

        $.ajax({
            type: 'POST',
            url: '/getFavoriteData',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            async : false
        }).done(function (datas) {
            const myFavorite = datas.myFavorite;
            const favoritingMe = datas.favoritingMe;

            // console.log(myFavorite);
            // console.log(favoritingMe);
            if(myFavorite != null) {
                // 서버에서 넘어온 myFavortie 리스트가 null이 아닌경우
                my_favorite_count = myFavorite.length;
                if(my_favorite_count != 0) {
                    // 서버에서 넘어온 myFavortie 리스트의 길이가 0이 아닌경우
                    // console.log("myFavorite is not null / 즐겨찾기한 유저 존재");
                    $.each(myFavorite, function (i, player) {
                         my_favorite_players.push(player);
                    });
                    let my_favorite_list = $("#favorite_player_list").html();
                    // console.log(my_favorite_list);
                    let template = Handlebars.compile(my_favorite_list);

                    initPlayers(template, 'favorite-player-list-container');
                }else {
                    // 서버에서 넘어온 myFavortie 리스트가 길이가 0인 경우
                    drawEmpty('favorite-player-list-container');
                }
            }else {
                // 서버에서 넘어온 myFavortie 리스트가 null인 경우
                drawEmpty('favorite-player-list-container');
            }

            if(favoritingMe != null) {
                // 서버에서 넘어온 favoritingMe 리스트가 null이 아닌경우
                favoriting_me_count = favoritingMe.length;
                if(favoriting_me_count != 0) {
                    // 서버에서 넘어온 favoritingMe 리스트의 길이가 0이 아닌경우
                    $.each(favoritingMe, function (i, player) {
                        favoriting_me_players.push(player);
                    });
                    let favoriting_me_list = $("#favorite_player_list").html();
                    let template = Handlebars.compile(favoriting_me_list);

                    initPlayers(template, 'player-list-container');
                }else {
                    // 서버에서 넘어온 favoritingMe 리스트가 길이가 0인 경우
                    drawEmpty('player-list-container');
                }
            }else {
                // 서버에서 넘어온 favoritingMe 리스트가 null인 경우
                // drawEmpty('player-list-container');
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
        location.href = "/search/" + playerName;
    }
};

function initPlayers(template, target) {
    // console.log("initPlayer 호출 : target => " + target);
    $("."+target+"-more-btn-div").remove();
    let cnt = 15;
    let count = 0;
    let item = {
        players: []
    };
    if(target === 'favorite-player-list-container') {
        if (my_favorite_players.length < 15) {
            cnt = my_favorite_players.length;
        }
        count = my_favorite_count;

        for (let i = 0; i < cnt; i++) {
            let data = my_favorite_players[i];
            if (data.battleTag === 'message') {
                alert(data.playerName);
                return false;
            }
            const player = drawList(data);
            item.players.push(player);
        }

        const player_list = template(item);
        $('.'+target).append(player_list);
        $('.grid-header:last').text("총 "+ count +"건");
        my_favorite_players.splice(0,cnt);

    }else if(target === 'player-list-container') {
        if (favoriting_me_players.length < 15) {
            cnt = favoriting_me_players.length;
        }

        count = favoriting_me_count;

        for (let i = 0; i < cnt; i++) {
            let data = favoriting_me_players[i];
            if (data.battleTag === 'message') {
                alert(data.playerName);
                return false;
            }
            const player = drawList(data);
            item.players.push(player);
        }

        const player_list = template(item);
        $('.'+target).append(player_list);
        $('.grid-header:first').text("총 "+ count +"건");
        favoriting_me_players.splice(0,cnt);
    }

    if(cnt == 15) {
        const moreButtonDiv = $('<div class="'+target+'-more-btn-div" align="center">'
            + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
            + '</div>');
        $("#"+target+"-more-button").append(moreButtonDiv);
    }
}

function morePlayers(target) {
    // console.log("morePlayers 호출 : target => " + target);
    $("."+target+"-more-btn-div").remove();
    var cnt = 15;
    let item = {
        players: []
    };
    if(target == 'favorite-player-list-container') {
        if (my_favorite_players.length < 15) {
            cnt = my_favorite_players.length;
        }

        for (var i = 0; i < cnt; i++) {
            var data = my_favorite_players[i];
            if (data.battleTag === 'message') {
                alert(data.playerName);
                return false;
            }
            const player = drawList(data);
            item.players.push(player);
        }
        let more_player_list = $("#more_player_list").html();
        let template = Handlebars.compile(more_player_list);
        const player_list = template(item);
        $('.tbody:last').append(player_list);
        my_favorite_players.splice(0,cnt);

    }else if(target ==='player-list-container'){
        if (favoriting_me_players.length < 15) {
            cnt = favoriting_me_players.length;
        }

        for (var i = 0; i < cnt; i++) {
            var data = favoriting_me_players[i];
            if (data.battleTag === 'message') {
                alert(data.playerName);
                return false;
            }
            const player = drawList(data);
            item.players.push(player);
        }

        let more_player_list = $("#more_player_list").html();
        let template = Handlebars.compile(more_player_list);
        const player_list = template(item);
        $('.tbody:first').append(player_list);
        favoriting_me_players.splice(0,cnt);
    }

    if(cnt == 15) {
        const moreButtonDiv = $('<div class="'+target+'-more-btn-div" align="center">'
            + '<hr><a id="more_btn" href="javascript:morePlayers(\''+target+'\');">더보기(More)</a><hr>'
            + '</div>');
        $("#"+target+"-more-button").append(moreButtonDiv);
    }
}

function playerDetail(obj) {
    // console.log($(obj).attr('attr-p'), $(obj).attr('attr-u'));
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
    // console.log(data.udtDtm);
    return {portrait: data.portrait, battleTag: data.battleTag, playerLevel: data.playerLevel, platform: data.platform, tankRatingPoint: data.tankRatingPoint, dealRatingPoint: data.dealRatingPoint, healRatingPoint: data.healRatingPoint, winRate: data.winRate,
        mostHero1: "/HWimages/hero/"+data.mostHero1+"_s.png", mostHero2: "/HWimages/hero/"+data.mostHero2+"_s.png", mostHero3: "/HWimages/hero/"+data.mostHero3+"_s.png", isPublic: data.isPublic, forUrl: data.forUrl, tankRatingImg: data.tankRatingImg,
        dealRatingImg: data.dealRatingImg, healRatingImg: data.healRatingImg, wingame: data.winGame, losegame: data.loseGame, udtDtm: data.udtDtm};
}

const drawEmpty = (target) => {
    const noticeDiv = $('<div class="not-fount-base row"><div class="player not-found-player col-md-12"> 즐겨찾기 플레이어가 없습니다.</div><div>');
    $("."+target).append(noticeDiv);
};

main.init();
