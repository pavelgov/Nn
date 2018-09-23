function showGraphic() {
    // var limit = 50;
    var y = 0;
    var data = [];
    var data1 = [];
    var dataSeries = {type: "line"};
    var dataSeries1 = {type: "line"};
    var dataPoints = [];
    var dataPoints1 = [];

    $.ajaxSetup({
        async: false //отключаем ассинхронное получение данных
    });


        $.ajax({          //отправляем запрос json на получение данных
            url: 'http://localhost:8080/hello',
            data: {getResult: 1},  //говорим дайт ответ сети
            async: false,
            dataType: 'json',
            success: function (json) {
                assignVariable(json);
            }
        });

    function assignVariable(data) {
        y = data; //перекладываем полученые данные в массив графика функции
    }


    for (var i = 0; i < y.length; i += 1) {
        dataPoints.push({
            x: i,
            y: y[i]
        });
    }
    dataSeries.dataPoints = dataPoints;
    data.push(dataSeries);



    var options = {
        zoomEnabled: true,
        animationEnabled: false,
        title: {
            text: "График функции"
        },
        axisY: {
            includeZero: false,
            lineThickness: 1
        },
        data: data  // random data
    };


    var chart = new CanvasJS.Chart("chartContainer", options);
   // var chart1 = new CanvasJS.Chart("chartContainer", options1);

    chart.render();
  //  chart1.render();
    y = [];
    data = [];
    dataSeries = {type: "line"};
    dataPoints = [];
    counter = 0;
    context = null;
    chart = null;

}