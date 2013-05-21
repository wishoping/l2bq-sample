Ext.require('Ext.chart.*');
Ext.require(['Ext.layout.container.Fit', 'Ext.window.MessageBox']);

// DAU RESTful Service Server Prefix
var REST_SERVER = "http://l2bq-test.appspot.com/rest/dau";

// DAU RESTful Service URI List
var REST_DAU_SERVICES = {
	daily_total : "/daily/total/1",
	hourly_total : "/hourly/total/1",
	daily_user_total : "/daily/eachuser/total/1",
	hourly_user_total : "/hourly/eachuser/total/1",
};

var store1 =  Ext.create('Ext.data.JsonStore', {
		fields : ['hour', 'loginCount'],
		data : [{"hour":"2013-05-19 23","loginCount":1},{"hour":"2013-05-20 00","loginCount":4},{"hour":"2013-05-20 01","loginCount":9},{"hour":"2013-05-20 02","loginCount":11},{"hour":"2013-05-20 03","loginCount":15},{"hour":"2013-05-20 04","loginCount":29},{"hour":"2013-05-20 05","loginCount":39},{"hour":"2013-05-20 06","loginCount":74},{"hour":"2013-05-20 07","loginCount":210},{"hour":"2013-05-20 08","loginCount":388},{"hour":"2013-05-20 09","loginCount":86},{"hour":"2013-05-20 10","loginCount":59},{"hour":"2013-05-20 11","loginCount":28},{"hour":"2013-05-20 12","loginCount":25},{"hour":"2013-05-20 13","loginCount":8},{"hour":"2013-05-20 14","loginCount":5},{"hour":"2013-05-20 15","loginCount":2},{"hour":"2013-05-20 16","loginCount":6},{"hour":"2013-05-20 17","loginCount":1}]
});

var downloadChart = function(chart){
    Ext.MessageBox.confirm('Confirm Download', 'Would you like to download the chart as an image?', function(choice){
        if(choice == 'yes'){
            chart.save({
                type: 'image/png'
            });
        }
    });
};

Ext.onReady(function() {
	var chart1 = Ext.create('Ext.chart.Chart',{
            animate: true,
            store: store1,
            insetPadding: 30,
            axes: [{
                type: 'Numeric',
                minimum: 0,
                position: 'left',
                fields: ['loginCount'],
                title: false,
                grid: true,
                label: {
                    renderer: Ext.util.Format.numberRenderer('0,0'),
                    font: '10px Arial'
                }
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['hour'],
                title: false,
                label: {
                    font: '11px Arial',
                    renderer: function(name) {
                        return name.substr(0, 3) + ' 07';
                    }
                }
            }],
            series: [{
                type: 'line',
                axis: 'left',
                xField: 'hour',
                yField: 'loginCount',
                listeners: {
                  itemmouseup: function(item) {
                      Ext.example.msg('Item Selected', item.value[1] + ' visits on ' + Ext.Date.monthNames[item.value[0]]);
                  }  
                },
                tips: {
                    trackMouse: true,
                    width: 80,
                    height: 40,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.get('hour'));
                        this.update(storeItem.get('loginCount'));
                    }
                },
                style: {
                    fill: '#38B8BF',
                    stroke: '#38B8BF',
                    'stroke-width': 3
                },
                markerConfig: {
                    type: 'circle',
                    size: 4,
                    radius: 4,
                    'stroke-width': 0,
                    fill: '#38B8BF',
                    stroke: '#38B8BF'
                }
            }]
        });
 

    var panel1 = Ext.create('widget.panel', {
        width: 600,
        height: 300,
        title: 'The Game Daily Access User Trends, 2013 (No styling)',
        renderTo: Ext.getBody(),
        layout: 'fit',
        tbar: [{
            text: 'Save Chart',
            handler: function(){ downloadChart(chart1); }
        }],
        items: chart1
    });
});