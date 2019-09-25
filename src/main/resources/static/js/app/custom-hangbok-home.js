
var main = {
    init : function(){
        alert('main init 호출');
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
        alert('main search 호출');

        var playerName = $('input[id="playerName"]').val();
        var inputName = {
            playerName : playerName
        }


        $.ajax({
            type: 'POST',
            url: '/account-by-username',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(inputName)
        }).done(function (datas) {
            $.each(datas, function (idx, data) {

            })
        });
    }
}

main.init();