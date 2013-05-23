function showGraph(divName, title, subTitle, x_series, y_series, xTitle, yTitle, seriesName)
{
  $(divName).highcharts({
    chart: {
      type: 'line'
    },
    title: {
      text: title
    },
    subtitle: {
      text: subTitle
    },
    xAxis: {
      categories: x_series
    },
    yAxis: {
      title: {
        text: yTitle
      }
    },
    tooltip: {
      enabled: false
    },
    plotOptions: {
      line: {
        dataLabels: {
          enabled: true
        },
        enableMouseTracking: false
      }
    },
    series: [{
      name: seriesName,
      data: y_series
    }]
  });

}

function showActiveUserGraph(divName, jsonObject, title, subTitle, xTitle, yTitle, seriesName)
{
 var x_series = new Array();
  var y_series = new Array();


  for (var i in jsonObject.list) {
    x_series[i] = jsonObject.list[i].f_time;
    y_series[i] = parseInt(jsonObject.list[i].loginCount);
  }

  showGraph(divName, title, subTitle, x_series, y_series, xTitle, yTitle, seriesName);
}

function showRetentionGraph(divName, jsonObject, title, subTitle, xTitle, yTitle, seriesName, maxX, total)
{
  var x_series = new Array();
  var y_series = new Array();

  var sum = 0;

  for (var i = maxX; i >= 0 ; i--) {
    x_series[i] = i;
    y_series[i] = 0;
  }

  for (var i in jsonObject.list) {
    y_series[jsonObject.list[i].visitCount] = jsonObject.list[i].userCount;
  }      

  for (var i = maxX; i >= 0 ; i--) {
    y_series[i] += sum;
    sum = y_series[i];
    y_series[i] = Math.round (sum / total * 10000 ) / 100;
  }

  showGraph(divName, title, subTitle, x_series, y_series, xTitle, yTitle, seriesName);
}


function showNewUserGraph(divName, jsonObject, title, subTitle, xTitle, yTitle, seriesName)
{
  var x_series = new Array();
  var y_series = new Array();


  for (var i in jsonObject.list) {
    x_series[i] = jsonObject.list[i].f_time;
    y_series[i] = parseInt(jsonObject.list[i].signupCount);
  }

  showGraph(divName, title, subTitle, x_series, y_series, xTitle, yTitle, seriesName);
}
