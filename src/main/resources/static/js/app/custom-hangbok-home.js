
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
                var detailDiv2 = $('<div class="player-detail" onclick="location.href=\'/showPlayerDetail/'+data.playerName+'-'+data.battleTag+'\'" style="cursor: pointer">'
                                    + '<img src="'+data.portrait+'" onerror="this.src = \'https://blzgdapipro-a.akamaihd.net/game/unlocks/0x02500000000002F7.png\';" alt="">'
                                +'</div>');
                var detailDiv = $('<a class="player-detail" href="/showPlaterDetail/'+data.forUrl+'">'
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
                playerDiv.append(detailDiv);
                $("#search-result").append(playerDiv);
            })
        });
    }
}
main.init();


// <div class="column sm-12 md-6 lg-3 end player-badge-wrapper" data-platform="pc">
//     <a class="player-badge" href="/ko-kr/career/pc/HAKSAL-2209">
//         <div class="player-badge-icon" style="background-image: url('https://d15f34w2p8l1cc.cloudfront.net/overwatch/8743f674a50c2fd9dead188c250f796c2de8e64d38d2981d10668c3da9534393.png')"></div>
//         <div class="player-badge-lock" data-visibility-private="false">
//             <svg class="ProfileStatusBar-icon u-inline-block u-align-middle" viewBox="0 0 40 40">
//                 <path d="M31.68,15.41H30.09V10.09A9.72,9.72,0,0,0,20.38.38h-.76a9.72,9.72,0,0,0-9.71,9.71v5.32H8.32a2.2,2.2,0,0,0-2.2,2.2V31.28a2.2,2.2,0,0,0,2.2,2.2H31.68a2.2,2.2,0,0,0,2.2-2.2V17.6A2.2,2.2,0,0,0,31.68,15.41ZM13.84,10.09a5.79,5.79,0,0,1,5.78-5.78h.76a5.79,5.79,0,0,1,5.78,5.78v5.32H13.84Z">
//                 </path>
//             </svg>
//         </div>
//         <div class="player-badge-name" title="HAKSAL#2209">HAKSAL#2209</div>
//         <div class="player-badge-level">
//             <div class="player-badge-level-value">191</div>
//         </div>
//         <div class="player-badge-platform">
//             <svg class="icon" viewBox="0 0 32 32" preserveAspectRatio="xMinYMin">
//                 <path d="M10.7,6.8c-2.1,0-4.2,0-5.6,0H3.1v18H5v-6.7c0.9,0,3.4,0,6,0c3.2,0,3.4-2.6,3.4-5.8C14.4,9,14.4,6.8,10.7,6.8z M12.7,12.6c0,2,0,3.6-2,3.6c-1.6,0-4.6,0-5.6,0V8.5c1.2,0,3.8,0,5.6,0C12.9,8.5,12.7,10.6,12.7,12.6z">
//                 </path>
//                 <path d="M26.8,18.3c0,0,1,4.9-3.7,4.9c-3.5,0-4-1.4-4.1-3.1v-8.3c0.1-1.7,0.5-3.1,4.1-3.1c4.7,0,3.7,3.6,3.7,3.6h2.1c0-3.4-0.2-5.4-5.7-5.4c-4.8,0-5.8,2.2-6,4.1l0,0v0.5c0,0.3,0,0.6,0,0.9v7.3c0,0.3,0,0.6,0,0.9v1.2h0.1c0.4,1.7,1.7,3.4,5.8,3.4c5.9,0,5.7-3.4,5.7-6.8h-2.1V18.3z">
//                 </path>
//             </svg>
//         </div>
//     </a>
// </div>
//
// .player-badge-wrapper {
//     display: table;
//     table-layout: fixed;
//     min-width: 385px;
//     float: left;
// }
// //
// .player-badge {
//     display: table-row;
//     background-color: #3c4860;
// }
// //
// .player-badge-icon {
//     display: table-cell;
//     width: 60px;
//     height: 60px;
//     background-size: cover;
// }
// //
// .player-badge-lock {
//     width: 21px;
// }
// .player-badge-level, .player-badge-lock, .player-badge-name, .player-badge-platform {
//     display: table-cell;
//     vertical-align: middle;
// }
// //lock svg
// .player-badge-lock svg {
//     display: none;
//     width: 21px;
//     height: 21px;
//     fill: hsla(0,0%,100%,.25);
// }
// .u-inline-block {
//     display: inline-block;
// }
// .u-align-middle {
//     vertical-align: middle;
// }
// .ProfileStatusBar-icon {
//     width: 1.5rem;
//     height: 1.5rem;
//     margin-left: 1rem;
// }
// //
// @media only screen and (min-width: 768px)
// .player-badge-name {
//     font-size: 1.75rem;
//     line-height: 2rem;
//     white-space: normal;
// }
// .player-badge-name {
//     padding: 0 5%;
//     text-align: left;
//     text-overflow: ellipsis;
//     overflow: hidden;
//     white-space: nowrap;
//     color: hsla(0,0%,96.5%,.8);
// }
// .player-badge-level, .player-badge-lock, .player-badge-name, .player-badge-platform {
//     display: table-cell;
//     vertical-align: middle;
// }
// //
// .endorsement-level, .player-badge-level-value, .player-level {
//     display: inline-block;
//     font-family: FuturaNo2D,century gothic,arial,sans-serif;
//     font-weight: 600;
// }
// .player-badge-level-value {
//     padding: 4px 10px;
//     border-radius: 3px;
//     line-height: 1;
//     font-weight: 700;
//     background-color: hsla(0,0%,96.5%,.8);
//     color: #3c4860;
// }
// //
// .player-badge-platform {
//     padding: 0 3%;
//     text-align: right;
//     width: 12%;
// }
// .player-badge-level, .player-badge-lock, .player-badge-name, .player-badge-platform {
//     display: table-cell;
//     vertical-align: middle;
// }
// .icon {
//     height: 24px;
//     fill: #f6f6f6;
//     stroke: #f6f6f6;
//     stroke-width: 0;
// }