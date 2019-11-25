var playerData = [];
var sendData = [];

var main = {
    init : function(){
        // alert('main init 호출');
        var _this = this;
        $('#btn-search').on('click', function (event) {
            _this.search();

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
                _this.search();

                e.preventDefault();

                $('html,body').animate({
                    scrollTop: $('.goto-here').offset().top
                }, 500, 'easeInOutExpo');

                return false;
            }
        });
    },
    search : function () {
        // alert('main search 호출');
        playerData = [];
        $(".player").remove();
        $(".more_btn_div").remove();

        var playerName = $('input[id="playerName"]').val();
        var inputName = {
            playerName : playerName
        }

        $.ajax({
            type: 'POST',
            url: '/showPlayerList',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(inputName),
            async : false
        }).done(function (datas) {
            console.log("showPlayerList => " + datas);
            console.log("/showPlayerList => 결과 내역 사이즈 : " + datas.length);
            if(datas.length != 0) {
                $.each(datas, function (idx, data) {
                    playerData.push(data);
                });
                morePlayers();
            }else {
                $.ajax({
                    type: 'POST',
                    url: '/showUserList',
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(inputName),
                    async : false
                }).done(function (crawlingResult) {
                    console.log("showUserList => " + crawlingResult);
                    console.log("/showUserList => 결과 내역 사이즈 : " + crawlingResult.length);
                    if(crawlingResult.length == 0) {
                        const noticeDiv = $('<div class="player notice_playerList"> 행복워치에 등록된 유저가 아닙니다.<br>' +
                            '행복워치에서 최초 검색 시 배틀태그(플레이어명#00000)를 정확하게 입력해야 합니다.</div>');
                        //const testDiv = $('<div></div>');
                        $("#search-result").append(noticeDiv);
                    }else {
                        $.ajax({
                            type: 'POST',
                            url: '/showUserProfile',
                            dataType: 'json',
                            contentType: 'application/json; charset=utf-8',
                            data: JSON.stringify(crawlingResult),
                            async : false
                        }).done(function(players){
                            $.each(players, function(idx, player){
                                drawList(player);
                            });
                        });
                    }
                });
            }
        });
    }
}
main.init();

function morePlayers() {

    $(".more_btn_div").remove();
    var cnt = 15;
    if (playerData.length < 15) {
        cnt = playerData.length;
    }
    for (var i = 0 ; i < cnt ; i++) {
        var data = playerData[i];
        if(data.battleTag === 'message') {
            alert(data.playerName);
            return false;
        }
        drawList(data);
        if(cnt == 15) {
            var moreButtonDiv = $('<div class="more_btn_div" align="center">'
                + '<hr><a id="more_btn" href="javascript:morePlayers();">더보기(More)</a><hr>'
                + '</div>');
            $("#more-button").append(moreButtonDiv);
        }
    }
    // $.each(playerData, function(idx, player){
    //     drawList(player);
    // });
    // if(cnt == 15) {
    //     var moreButtonDiv = $('<div class="more_btn_div" align="center">'
    //         + '<hr><a id="more_btn" href="javascript:morePlayers();">더보기(More)</a><hr>'
    //         + '</div>');
    //     $("#more-button").append(moreButtonDiv);
    // }
    playerData.splice(0,cnt);
    // alert(playerData.length);
}

function drawList(data) {
    // return new Promise(function(data){})
    if(data.battleTag === 'message') {
        alert(data.playerName);
        return false;
    }
    const playerDiv = $('<div class="player col-md-12"></div>');
    const detailDiv2 = $('<div class="player-detail" onclick="location.href=\'/showPlayerDetail/'+data.playerName+'#'+data.battleTag+'\'" style="cursor: pointer">'
        + '<img src="'+data.portrait+'" onerror="this.src = \'https://blzgdapipro-a.akamaihd.net/game/unlocks/0x02500000000002F7.png\';" alt="">'
        +'</div>');
    const detailDiv = $('<a class="player-detail" onclick="playerDetail('+data+')">'
        +    '<div class="player-portrait" ><img class="player-portrait" src="'+data.portrait+'" onerror="this.src = \'https://blzgdapipro-a.akamaihd.net/game/unlocks/0x02500000000002F7.png\';" alt></div>'
        // +    '<div class="player-portrait" style="background-image: url(\''+data.portrait+'\')"></div>'
        //   +    '<div class="player-platform"><svg class="platform-icon" viewBox="0 0 70 70"><text x="35" y=35" >'+data.platform+'</text></svg></div>'
        +    '<div class="player-platform"><div class="player-platform-icon">'+data.platform+'</div></div>'
        +    '<div class="player-name" title="'+data.battleTag+'">'+data.battleTag+'<br>'+'Lv. '+data.playerLevel+'</div>'
        +    '<div class="player-rating"><!--div class="for-table"-->'
        +         '<div class="player-isPublic-'+data.isPublic+' player-tank-rating">'+'돌격<br>'+data.tankRatingPoint+'</div>'
        +         '<div class="player-isPublic-'+data.isPublic+' player-deal-rating">'+'공격<br>'+data.dealRatingPoint+'</div>'
        +         '<div class="player-isPublic-'+data.isPublic+' player-heal-rating">'+'지원<br>'+data.healRatingPoint+'</div>'
        +         '<div class="player-isPublic-'+data.isPublic+' player-lock col-md-12">'+'프로필 비공개'+'</div></div><!--/div-->'
        + '</a>');

    // console.log(playerDiv);
    playerDiv.append(detailDiv);
    if(data.isPublic == 'Y') {
        $("#search-result").append(playerDiv);
    }else {
        $("#search-result-lock").append(playerDiv);
    }
}

function playerDetail(data) {
    if(data.isPublic == 'Y') {
        location.href = "/showPlayerDetail/"+data.forUrl;
    } else {
        alert("플레이어가 프로필을 비공개했습니다.\n" +
            "인게임에서 프로필 공개 이후 다시 검색해주세요.");
        return;
    }
}