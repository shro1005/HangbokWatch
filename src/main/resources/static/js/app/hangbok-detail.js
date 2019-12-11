

const main = {
    init : function(){
        const _this = this;
        $('#btn-search').on('click', function (event) {
            _this.search();
            return false;
        });

        $('.refresh-button').on('click', function (event){
           _this.doRefresh();
           return false;
        });

        $('.favorite-button').on('click', function () {
            _this.doFavorite();
            return false;
        });

        // 엔터키 눌렀을 때 search 메소드 호
        $(document).keypress(function (e) {
            if (e.which == 13) {
                _this.search();
                return false;
            }
        });

        setContainerHeingt();
        drawProgressBar();
    },
    search : function () {
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

    },
    doRefresh : function () {

    },
    doFavorite : function () {

    }
};

const setContainerHeingt = () => {
    const objSet = document.getElementsByClassName("resultContainer-detail")[0];
    const objTarHeight = document.getElementsByClassName("player-detail-layout")[0].offsetHeight;
    // console.log(objTarHeight);
    objSet.style.height = objTarHeight + "px";
};

const drawProgressBar = () => {
    const divs = $('.progress-container');
    // console.log(divs);
    for (let i = 0 ; i < divs.length ; i++) {
        // const container = $('#')
        const winLoseGame = divs[i].id;
        // console.log(winLoseGame);
        const winGame = winLoseGame.substr(0, winLoseGame.indexOf("/"));
        const loseGame = winLoseGame.substr(winLoseGame.indexOf("/")+1, winLoseGame.length);
        // console.log(winGame, loseGame);
        let winRate = 0.0;
        if(winGame != '0' && loseGame != '0') {
            // console.log(parseFloat(winGame), parseFloat(loseGame));
            winRate = parseFloat(winGame) / (parseFloat(winGame) + parseFloat(loseGame));
            // console.log(winRate);
        }
        let bar = new ProgressBar.Line(divs[i], {
            strokeWidth: 2,
            easing: 'easeInOut',
            duration: 1500,
            color: 'rgba(232,110,208,1)',
            trailColor: '#3c4860',
            trailWidth: 2,
            svgStyle: {width: '100%', height: '100%'},
            text: {
                style: {
                    // Text color.
                    // Default: same as stroke color (options.color)
                    color: '#3c4860',
                    /* position: 'absolute', */
                    right: '0',
                    top: '30px',
                    padding: 0,
                    margin: 0,
                    transform: null
                },
                autoStyleContainer: false
            },
            from: {color: 'rgba(232,110,208,1)'},
            to: {color: 'rgba(232,110,208,1)'},   //#ED6A5A
            step: (state, bar) => {
                bar.setText(Math.round(bar.value() * 100) + ' %');
            }
        });

        bar.animate(winRate);  // Number from 0.0 to 1.0
    }
};


main.init();