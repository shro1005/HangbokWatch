
const header = {
    init : function(){
        const _this = this;
        $('#btn-search-tap').on('click', function (event) {
            _this.search("");

            return false;
        });

        $('#playerName-tap').focus(function () {
            console.log("true");
            $(document).keypress(function (e) {
                if (e.which == 13) {
                    // alert('enter key is pressed');
                    _this.search("");

                    return false;
                }
            });
        });

        if(window.location.pathname.indexOf("myFavorite") > 0) {$('.navbar-nav .favorite-nav').addClass("active");}
        else if(window.location.pathname.indexOf("ranking") > 0) {$('.navbar-nav .ranking-nav').addClass("active");}
        else {$('.navbar-nav .index-nav').addClass("active");}
    },
    search : function () {
        // alert('main search 호출');
        let playerName = $('input[id="playerName-tap"]').val();
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

header.init();

