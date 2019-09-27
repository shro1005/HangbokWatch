
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
    },
    search : function () {
        // alert('main search 호출');
        $(".player").remove();

        var playerName = $('input[id="playerName"]').val();
        var inputName = {
            playerName : playerName
        }


        $.ajax({
            type: 'POST',
            url: '/showUserList',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(inputName)
        }).done(function (datas) {
            $.each(datas, function (idx, data) {
                // alert('ajax running');
                if(data.battleTag === 'message') {
                    alert(data.playerName);
                    return false;
                }
                var playerDiv = $('<div class="player col-md-12"></div>');
                var detailDiv = $('<div class="player-detail" onclick="location.href=\'/showPlayerDetail/'+data.playerName+'#'+data.battleTag+'\'" style="cursor: pointer">'
                                    + '<img src="'+data.portrait+'" onerror="this.src = \'https://blzgdapipro-a.akamaihd.net/game/unlocks/0x02500000000002F7.png\';" alt="">'
                                +'</div>');
                playerDiv.append(detailDiv);
                $("#search-result").append(playerDiv);
            })
        });
    }
}
main.init();

function detail(playerName, battleTag) {
    alert(playerName + "#" + battleTag);
}
