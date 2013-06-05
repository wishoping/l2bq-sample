function showGraph(divName, title, subTitle, x_series, y_series, xTitle, yTitle, seriesName)
{
	// http://www.highcharts.com/demo/spline-symbols
  $(divName).highcharts({
    chart: {
      type: 'spline'
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
    credits: {
        style: {
            color: '#9fa2a5'
        }
    },    
    tooltip: {
        crosshairs: true,
        shared: true
    },
    plotOptions: {
        series: {
            marker: {
                lineWidth: 1, // The border of each point (defaults to white)
                radius: 4 // The thickness of each point
            },

            lineWidth: 3, // The thickness of the line between points
            shadow: false
        }
    },
    series: [{
        marker: {
            symbol: 'circle'
        },
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

function _loadDashboard() {
    waitingDialog({title: "Please wait", message: "We are updating log data."});
    
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select count(*) as totalUsers from [l2bq_test.applog_signup]"},function(data){
	    var totalUsers = data.list[0].totalUsers;
	    $("#totalUsers").text(totalUsers);
	
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(*) as loginCount from [l2bq_test.applog_login] GROUP BY f_time ORDER BY f_time"},function(data){
	      showActiveUserGraph('#dauChart', data, 'DAU', '', 'Time', '# of User Login/hour', 'KaizinRumble');
	
	    },'jsonp');
	
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as f_time, count(*) as signupCount from [l2bq_test.applog_signup] GROUP BY f_time ORDER BY f_time"},function(data){
	      showNewUserGraph('#signupChart', data, 'New Users', '', 'Time', '# of New Users', 'KaizinRumble');
	    },'jsonp');
	
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP(CURRENT_DATE()), -1, \"MONTH\")) group by userId, day order by userId, day) group by userId) group by visitCount order by visitCount desc"},function(data){
	      showRetentionGraph('#retentionChart', data, 'Retention %', '', 'Day', '% Day 0', 'KaizinRumble', 30, totalUsers);
	    },'jsonp');
	
	    // retention_1
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -1, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_1").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_2
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -2, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_2").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_3
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -3, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_3").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_7
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -7, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_7").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_14
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -14, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_14").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_21
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -21, \"DAY\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_21").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_1mon
	    $.get("http://l2bq-test.appspot.com/rest/query", {"query":"select sum(userCount) as userCount from (select visitCount, count(visitCount) as userCount from (select userId, count(userId) as visitCount from (SELECT userId, STRFTIME_UTC_USEC(time*1000, \"%Y-%m-%d\") as day, count(userId) as loginCount FROM [l2bq_test.applog_login] where (time*1000) >= TIMESTAMP_TO_USEC(DATE_ADD(TIMESTAMP('2013-06-12'), -1, \"MONTH\")) group by userId, day order by userId, day) group by userId) group by visitCount)"},function(data){
	      $("#retention_1mon").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	    
	    closeWaitingDialog();
	    
	  },'jsonp');
}

function loadDashboard() {
    waitingDialog({title: "Please wait", message: "We are updating log data."});
    
	$.get("http://l2bq-test.appspot.com/rest/user/totalCount",function(data){
	    var totalUsers = data.list[0].totalUsers;
	    $("#totalUsers").text(totalUsers);
	
	    $.get("http://l2bq-test.appspot.com/rest/user/dau", function(data){
	      showActiveUserGraph('#dauChart', data, 'DAU', '', 'Time', '# of User Login/hour', 'KaizinRumble');
	
	    },'jsonp');
	
	    $.get("http://l2bq-test.appspot.com/rest/user/new_users",function(data){
	      showNewUserGraph('#signupChart', data, 'New Users', '', 'Time', '# of New Users', 'KaizinRumble');
	    },'jsonp');
	
	    $.get("http://l2bq-test.appspot.com/rest/user/retention",function(data){
	      showRetentionGraph('#retentionChart', data, 'Retention %', '', 'Day', '% Day 0', 'KaizinRumble', 30, totalUsers);
	    },'jsonp');
	
	    // retention_1
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/1",function(data){
	      $("#retention_1").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_2
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/2",function(data){
	      $("#retention_2").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_3
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/3", function(data){
	      $("#retention_3").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_7
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/7", function(data){
	      $("#retention_7").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_14
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/14", function(data){
	      $("#retention_14").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_21
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/day/21", function(data){
	      $("#retention_21").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	
	    // retention_1mon
	    $.get("http://l2bq-test.appspot.com/rest/user/retention/month/1", function(data){
	      $("#retention_1mon").text(Math.round (data.list[0].userCount / totalUsers * 10000 ) / 100 + "%");
	    },'jsonp');
	    
	    closeWaitingDialog();
	    
	  },'jsonp');
}

function _loadHttpLogs() {
    waitingDialog({title: "Please wait", message: "We are updating log data."});
    
	// Total HTTP Logs Count
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select count(*) as totalHttpLogs from [l2bq_test.http_access_log]"},function(data){
	    var totalHttpLogs = data.list[0].totalHttpLogs;
	    $("#totalHttpLogs").text(totalHttpLogs);
	},'jsonp');	
	
	
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select level, count(*) as levelCount from [l2bq_test.http_access_log] group by level"},function(data){
		for (var i in data.list) {
			var level = data.list[i].level;
			if (level == "Info") {
				$("#totalHttpLogsInfo").text(data.list[i].levelCount);
			}
			else if (level == "Warning") {
				$("#totalHttpLogsWarning").text(data.list[i].levelCount);
			}
			else if (level == "Critical") {
				$("#totalHttpLogsCritical").text(data.list[i].levelCount);
			}
			else if (level == "Debug") {
				$("#totalHttpLogsDebug").text(data.list[i].levelCount);
			}
			else {
				$("#totalHttpLogsUndefined").text(data.list[i].levelCount);
			}
		}
		
	    closeWaitingDialog();
	},'jsonp');	
	
	
	// 로그 정보들
	var limit = 5;
	
	// Critical
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select method, timestamp, resource, ip, versionId from [l2bq_test.http_access_log] where level = 'Critical' order by timestamp desc limit " + limit},function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr>";
			resultHtml += row;
		}
		$("#tableCriticalLogs").html(resultHtml);
	},'jsonp');	

	// Warning
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select method, timestamp, resource, ip, versionId from [l2bq_test.http_access_log] where level = 'Warning' order by timestamp desc limit " + limit},function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr>";
			resultHtml += row;
		}
		$("#tableWarningLogs").html(resultHtml);
	},'jsonp');	

	// Info
	$.get("http://l2bq-test.appspot.com/rest/query", {"query":"select method, timestamp, resource, ip, versionId from [l2bq_test.http_access_log] where level = 'Info' order by timestamp desc limit " + limit},function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr>";
			resultHtml += row;
		}
		$("#tableInfoLogs").html(resultHtml);
	},'jsonp');	
}

function loadHttpLogs() {
    waitingDialog({title: "Please wait", message: "We are updating log data."});
    
	// Total HTTP Logs Count
	$.get("http://l2bq-test.appspot.com/rest/http/totalCount", function(data){
	    var totalHttpLogs = data.list[0].totalHttpLogs;
	    $("#totalHttpLogs").text(totalHttpLogs);
	},'jsonp');	
	
	
	$.get("http://l2bq-test.appspot.com/rest/http/stats",function(data){
		for (var i in data.list) {
			var level = data.list[i].level;
			if (level == "Info") {
				$("#totalHttpLogsInfo").text(data.list[i].levelCount);
			}
			else if (level == "Warning") {
				$("#totalHttpLogsWarning").text(data.list[i].levelCount);
			}
			else if (level == "Critical") {
				$("#totalHttpLogsCritical").text(data.list[i].levelCount);
			}
			else if (level == "Debug") {
				$("#totalHttpLogsDebug").text(data.list[i].levelCount);
			}
			else {
				$("#totalHttpLogsUndefined").text(data.list[i].levelCount);
			}
		}
		
	    closeWaitingDialog();
	},'jsonp');	
	
	
	// 로그 정보들
	var limit = 5;
	
	// Critical
	$.get("http://l2bq-test.appspot.com/rest/http/logs/critical/" + limit, function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr onClick='javascript:showDetailedInfo(" + data.list[i].timestamp + "); return false;'><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr><tr id='detail_" + data.list[i].timestamp + "' style='display:none'></tr>";
			resultHtml += row;
		}
		$("#tableCriticalLogs").html(resultHtml);
	},'jsonp');	

	// Warning
	$.get("http://l2bq-test.appspot.com/rest/http/logs/warning/" + limit, function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr onClick='javascript:showDetailedInfo(" + data.list[i].timestamp + "); return false;'><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr><tr id='detail_" + data.list[i].timestamp + "' style='display:none'></tr>";
			resultHtml += row;
		}
		$("#tableWarningLogs").html(resultHtml);
	},'jsonp');	

	// Info
	$.get("http://l2bq-test.appspot.com/rest/http/logs/info/" + limit, function(data){
		var resultHtml = "";
		for (var i in data.list) {
			var date = new Date(data.list[i].timestamp / 1000);
			var row = "<tr onClick='javascript:showDetailedInfo(" + data.list[i].timestamp + "); return false;'><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr><tr id='detail_" + data.list[i].timestamp + "' style='display:none'></tr>";
			resultHtml += row;
		}
		$("#tableInfoLogs").html(resultHtml);
	},'jsonp');	
}

function showDetailedInfo(index) 
{
    var html = $("#detail_" + index).html();
    if (html.length != 0) {
    	if ($("#detail_" + index).is(":visible") == true) {
    		$("#detail_" + index).hide();	
    	}
    	else {
    		$("#detail_" + index).show();
    	}
    	return;
    }
    else {
        waitingDialog({title: "Please wait", message: "loading..."});
        
    	$.get("http://l2bq-test.appspot.com/rest/http/detail/" + index, function(data){
    		
    	    closeWaitingDialog();
    	    
    		// There is only one result which is matched with index
    		if (data.isSuccess == true) {
    			var resultHtml = "";
    			var item = data.list[0];
    			var appLogsHtml = "<ul class='ul_applog'>";
    			var app_logs = eval(item.app_logs);
    			for (var i in app_logs) {
    				var date = new Date(app_logs[i].timeUsec / 1000);
    				var row = "<li><code>" + app_logs[i].logLevel + "</code> <span class='label'>" + date + "</span><br><span class='applog_span'>" + app_logs[i].logMessage.replace("\n", "<br>") + "</div></li>";
    				appLogsHtml += row;
    			}
    			
    			appLogsHtml += "</ul>";
    			
    			resultHtml = '<td colspan=5><table style="table-layout: fixed; width: 100%" cellspacing="0" cellpadding="8" border="0"><tr><td colspan=2 style="word-wrap: break-word;">' + appLogsHtml + '</td></tr><tr><td width=150>httpStatus</td><td>' + item.httpstatus + '</td></tr><tr><td width=150>method</td><td>' + item.method + '</td></tr><tr><td width=150>httpVersion</td><td>' + item.httpversion + '</td></tr><tr><td width=150>instanceKey</td><td>' + item.instancekey + '</td></tr><tr><td width=150>apiMcycles</td><td>' + item.apimcycles + '</td></tr><tr><td width=150>cost</td><td>' + item.cost + '</td></tr><tr><td width=150>responseSize</td><td>' + item.responsesize + '</td></tr><tr><td width=150>mcycles</td><td>' + item.mcycles + '</td></tr><tr><td width=150>loadingRequest</td><td>' + item.loadingrequest + '</td></tr><tr><td width=150>pendingTimeUsec</td><td>' + item.pendingtimeusec + '</td></tr><tr><td width=150>latencyUsec</td><td>' + item.latencyusec + '</td></tr><tr><td width=150>timestamp</td><td>' + item.timestamp + '</td></tr><tr><td width=150>host</td><td>' + item.host + '</td></tr><tr><td width=150>path</td><td>' + item.path + '</td></tr><tr><td width=150>nickname</td><td>' + item.nickname + '</td></tr><tr><td width=150>ip</td><td>' + item.ip + '</td></tr><tr><td width=150>userAgent</td><td>' + item.useragent + '</td></tr><tr><td width=150>versionId</td><td>' + item.versionid + '</td></tr><tr><td width=150>level</td><td>' + item.level + '</td></tr></table></td>';

    			$("#detail_" + index).show();
    			$("#detail_" + index).html(resultHtml);

    		}
    	},'jsonp');
    }
}

function searchHTTPLogs(keyword)
{
	if ( keyword == null || keyword == undefined || keyword == "" ) {
		alert('Search Keyword is invalid.');
		return;
	}
		
	waitingDialog({title: "Please wait", message: "loading..."});

	var limit = 10;
	keyword = keyword.replace(" ", "%20");

	// Clear previous search result 
	$("#tableSearchLogs").html("");

	$.get("http://l2bq-test.appspot.com/rest/http/search/" + keyword + "/" + limit, function(data){
    		
    	    closeWaitingDialog();

    		// There is only one result which is matched with index
    		if (data.isSuccess == true) {
				var resultHtml = "";
				for (var i in data.list) {
					var date = new Date(data.list[i].timestamp / 1000);
					var row = "<tr onClick='javascript:showDetailedInfo(" + data.list[i].timestamp + "); return false;'><td>" + date + "</td><td>" + data.list[i].ip + "</td><td>" + data.list[i].method + "</td><td>" + data.list[i].resource.substring(0,100) + "</td><td>" + data.list[i].versionId + "</td></tr><tr id='detail_" + data.list[i].timestamp + "' style='display:none'></tr>";
					resultHtml += row;
				}
				if ( resultHtml == "" ) {
					alert('There is no search result.');
				} else {
					$("#tableSearchLogs").html(resultHtml);	
				}
				
    		} else {
    			alert('There is no search result.');
    		}
    	},'jsonp');

}
