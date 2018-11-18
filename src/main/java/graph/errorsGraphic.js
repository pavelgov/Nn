function showError() {

    var y1 = 0;
    var data1 = [];
    var dataSeries1 = {type: "line"};
    var dataPoints1 = [];

   $.ajaxSetup({
        async: false
    });


    $.ajax({
        url: 'http://localhost:8080/hello',
        data: {getErrors:1},
        async: false,
        dataType: 'json',
        success: function (json) {
            assignVariable(json);

        }
    });

    function assignVariable(data1) {
        y1 = data1;
    }


    for (var i = 0; i < y1.length; i += 1) {
        dataPoints1.push({
            x: i,
            y: y1[i]
        });
    }
    dataSeries1.dataPoints = dataPoints1;
    data1.push(dataSeries1);




    var options1 = {
        zoomEnabled: true,
        animationEnabled: false,
        title: {
            text: "График ошибки сети"
        },
        axisY: {
            includeZero: false,
            lineThickness: 1
        },
        data: data1  // random data
    };



    var chart1 = new CanvasJS.Chart("ErrorNetwork", options1);

    chart1.render();

}



