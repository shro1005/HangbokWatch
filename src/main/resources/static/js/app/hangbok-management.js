const management = {
    init : function() {
        const _this = this;
        $('#btn-search-tap').on('click', function (event) {
            _this.search("");

            return false;
        });

        getSeasonData();
        getJobData();

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
        location.href = "/search/" + playerName;
    }
};

const getSeasonData = () => {
    const season_list = $("#season_list").html();
    const template = Handlebars.compile(season_list);

    const item ={
        seasons : []
    };

    $.ajax({
        type: 'POST',
        url: '/getSeasonData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        async : false
    }).done(function (datas) {
        // console.log("showPlayerList => " + datas);
        // console.log("/showPlayerList => 결과 내역 사이즈 : " + datas.length);
        count = datas.length;
        if(datas.length != 0) {
            $.each(datas, function (idx, data) {
                item.seasons.push({season : data.season, startDate : data.startDate, endDate : data.endDate});
            });
        }
    });

    const seasons = template(item);
    $('.season-list-container').append(seasons);
};

const modifySeason = (obj) => {
    const parent_tr = $(obj).parent().parent('tr.season_data');
    const season_td = parent_tr.children('td.season');
    const start_date_td = parent_tr.children('td.start-date');
    const end_date_td = parent_tr.children('td.end-date');
    const mgt_button_td = parent_tr.children('td.mgt-button');

    let season = season_td.text().trim();
    let start_date = start_date_td.text().trim();
    let end_date = end_date_td.text().trim();

    season_td.empty(); season_td.append($('<input class="season_input" align="left" value="'+season+'">'));
    start_date_td.empty(); start_date_td.append($('<input class="start_date_input" align="left" value="'+start_date+'">'));
    end_date_td.empty(); end_date_td.append($('<input class="end_date_input" align="left" value="'+end_date+'">'));

    mgt_button_td.empty(); mgt_button_td.append($('<div class="save" href="javascript:void(0);" onclick="saveSeason(this); return false;" style="cursor: pointer">저장</div>'));

};

const verifyingData = (inputData) => {
    const season_check = /[0-9]/g;
    const date_check = /(2[0-9]{3})([0-1]{1}[0-9]{1})([0-3]{1}[0-9]{1})([0-2]{1}[0-9]{1})([0-6]{1}[0-9]{1})([0-6]{1}[0-9]{1})/;
    if(!season_check.test(inputData.season)) {
        alert("시즌 정보는 숫자만 입력 가능 합니다.");
        return false;
    }

    if(!season_check.test(inputData.startDate)) {
        alert("시작 일시가 날짜 형식과 다릅니다.");
        return false;
    }

    if(inputData.startDate.length != 14) {
        alert("시작 일시는 14자리 숫자여야 합니다..");
        return false;
    }

    if(!season_check.test(inputData.endDate)) {
        alert("종료 일시가 날짜 형식과 다릅니다.");
        return false;
    }

    if(inputData.endDate.length != 14) {
        alert("종료 일시는 14자리 숫자여야 합니다..");
        return false;
    }

    return true;
};

const saveSeason = (obj) => {
    const parent_tr = $(obj).parent().parent('tr.season_data');
    const season_td = parent_tr.children('td.season');
    const start_date_td = parent_tr.children('td.start-date');
    const end_date_td = parent_tr.children('td.end-date');
    const mgt_button_td = parent_tr.children('td.mgt-button');

    let season = season_td.children().val().trim();
    let start_date = start_date_td.children().val().trim();
    let end_date = end_date_td.children().val().trim();

    // console.log(season, start_date, end_date);

    const inputData = {season : season, startDate : start_date, endDate : end_date};
    if(verifyingData(inputData)) {
        $.ajax({
            type: 'POST',
            url: '/saveSeasonData',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(inputData),
            async : false
        }).done(function (data) {
            if("Y" == data.isSuccess) {
                season_td.empty(); season_td.text(data.season);
                start_date_td.empty(); start_date_td.text(data.startDate);
                end_date_td.empty(); end_date_td.text(data.endDate);

                mgt_button_td.empty(); mgt_button_td.append($('<div class="modify inner-button" href="javascript:void(0);" onclick="modifySeason(this); return false;" style="cursor: pointer">수정</div>'));
            }else {
                alert("시즌 정보 등록 및 수정에 실패했습니다. \n정상적인 데이터를 등록해 주세요.");
            }
        });
    }
};

const moreSeason = () => {
    const season_list = $("#add_season_list").html();
    const template = Handlebars.compile(season_list);

    const item ={
        seasons : [{season : "add_season"}]
    };
    const seasons = template(item);
    $('.season-tbody:last').append(seasons);
};

const getJobData = () => {
    const job_list = $("#job_list").html();
    const template = Handlebars.compile(job_list);

    const items = {
        jobs: []
    };

    let target = [];
    $.ajax({
        type: 'POST',
        url: '/getJobData',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        async : false
    }).done(function (datas) {
        // console.log("showPlayerList => " + datas);
        // console.log("/showPlayerList => 결과 내역 사이즈 : " + datas.length);
        count = datas.length;
        if(datas.length != 0) {
            $.each(datas, function (idx, data) {
                items.jobs.push({
                    jobName: data.jobName,
                    status: data.status,
                    lastStartTime: data.lastStartTime,
                    lastEndTime: data.lastEndTime,
                    index: idx + 1
                });
                if("COMPLETED"==data.status || "FAILED"==data.status) {

                }else {
                    target.push("job-"+(idx+1));
                }
            });
        }


    });

    const jobs = template(items);
    $('.job-list-container').append(jobs);

    $.each(target, function (i, job) {
        console.log(job);
        const mgt_button_td = $("."+job).parent();
        mgt_button_td.empty(); mgt_button_td.append($('<div class="processing inner-button" href="javascript:void(0);">진행중</div>'));
    });

};

const resumeJob = (obj) => {
    const parent_tr = $(obj).parent().parent('tr.job-data');
    const job_name_td = parent_tr.children('td.job-name');
    const job_status = parent_tr.children('td.job-status');
    const st_td = parent_tr.children('td.last-start-time');
    const et_td = parent_tr.children('td.last-end-time');
    const mgt_button_td = parent_tr.children('td.mgt-button');

    const jobName = job_name_td.text().trim();

    const inputData = {
        jobName : jobName
    };

    const flag = confirm(jobName + " 배치를 재기동 하시겠습니까?");

    if(!flag) {return;}

    mgt_button_td.empty(); mgt_button_td.append($('<div class="processing inner-button" href="javascript:void(0);" >진행중</div>'));

    $.ajax({
        type: 'POST',
        url: '/resumeJob',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(inputData),
        async : false
    }).done(function (data) {
        if('Y' == data.isNormal) {
            alert(data.jobName + " 배치 재기동이 완료됐습니다.");
        }else {
            alert(data.jobName + " 배치 재기동에 실패했습니다. ");
        }
        job_status.empty(); job_status.text(data.status);
        st_td.empty(); st_td.text(data.lastStartTime);
        et_td.empty(); et_td.text(data.lastEndTime);

        mgt_button_td.empty(); mgt_button_td.append($('<div class="resume inner-button" href="javascript:void(0);" onclick="resumeJob(this); return false;" style="cursor: pointer">재기동</div>'));
    });
};

management.init();