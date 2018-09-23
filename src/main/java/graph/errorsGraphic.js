function showError() {

    var y = [];
    var data = [];
    var dataSeries = {type: "line"};
    var dataPoints = [];
    var counter = 0;
    $.ajaxSetup({
        async: false
    });

    var context;
    $.ajax({

        url: 'http://localhost:8080/hello',
        data: {getErrors:1},
        async: false,
        dataType: 'json',
        success: function (json) {
            assignVariable(json);

        }
    });

    function assignVariable(data) {
        context = data;
    }

    y = context;
    for (var i = 0; i < y.length; i += 1) {
        dataPoints.push({
            x: i,
            y: y[i]
        });
    }
    dataSeries.dataPoints = dataPoints;
    data.push(dataSeries);

//Better to construct options first and then pass it as a parameter
    var chart = new CanvasJS.Chart("ErrorNetwork", {
        animationEnabled: false,
        zoomEnabled: true,
        title: {
            text: "Размер ошибки сети "
        },
        axisY: {
            includeZero: false
        },
        data: data  // random generator below
    });

    chart.render();
    /* y = [];
     data = [];
     dataSeries = {type: "line"};
     dataPoints = [];
     counter = 0;
    context = null;
    chart = null;*/
}



