(function() {
    var appBody = el('app-body')
    var tableHeader = el('step1-table-header')
    var tableBody = el('step1-table-body')
    var sendCsvBtn = el('send-csv')
    var testDataFile = el('test_data_file')
    var progressImg = el('progress-img')

    testDataFile.addEventListener('change', function(e1) {
        var file = e1.target.files[0];
        var fileReader = new FileReader();
        fileReader.onloadend = function (e2) {
            csvData = e2.target.result;
        };
        fileReader.readAsText(file);
    })

    el('show-csv')
        .addEventListener('click', function(e) {
            initTable();
            try {
                drawTable(csvData);
            } catch(e) {
                alert('csv 데이터 형식이 바르지 않습니다.');
                initTable();
            }
        });

    // drawTable(csvData)
    function drawTable(csvStr) {
        var tr = document.createElement('tr')
        console.log('>>', csvStr)
        var csvStrArr = csvStr.split('\n');
        var heads = csvStrArr.shift(1).split(',');
        var bodys = csvStrArr;
        for(var i=0; i<heads.length; i++) {
            var th = ce('th', trimAndremoveDoubleQuote(heads[i]));
            tr.appendChild(th);
        }

        tableHeader.appendChild(tr);
        for(var i=1; i < bodys.length; i++ ) {
            var t = bodys[i];
            var cols = t.split(",")
            if(cols.length != heads.length) {
                continue;
            }
            var tr = document.createElement('tr')
            tr.appendChild(ce('td', trimAndremoveDoubleQuote(cols[0])));
            tr.appendChild(ce('td', cols[1]));
            tr.appendChild(ce('td', trimAndremoveDoubleQuote(cols[2])));
            tr.appendChild(ce('td', trimAndremoveDoubleQuote(cols[3])));
            tableBody.appendChild(tr);
        } 
    }   

    sendCsvBtn
        .addEventListener('click', function() {
            if(testDataFile.files[0] === undefined) {
                alert('csv 파일을 입력해주세요');
                return;
            }
            sendCsvToServer();
        })

    function sendCsvToServer() {
        var httpRequest= new XMLHttpRequest();
        var formdata = new FormData()
        formdata.append('test_data_file', el('test_data_file').files[0]);
        httpRequest.open('POST', 'http://35.194.117.145:8080/upload_test_data', true);
        httpRequest.send(formdata);

        toggleProgress();

        httpRequest.onload = function(e) {
            toggleProgress();
            var text = e.target.response;
            var json = JSON.parse(text);
            step2Draw(json);
            el('tab_result').click();
        }

        httpRequest.onerror = function(e) {
            alert('오류가 발생했습니다. csv 파일을 확인해주세요');
            toggleProgress();
        }
    }

    var toggleProgress = (function() {
        var isProgress = false;
        return function() {
            if(isProgress) {
                appBody.style.opacity = "1";
                progressImg.style.display = 'none';
            } else {
                appBody.style.opacity = "0.1";
                progressImg.style.display = 'block';
            }
            isProgress = !isProgress;
        }
    })()
})()
