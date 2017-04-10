

function loadDashboard(){
$.get('/records').done(function(data) {
         
     var resultData=data;
     var results = {"results":resultData};
     var resultModel = ko.mapping.fromJS(results);
     resultModel.created_date = ko.observable(new Date());
     ko.applyBindings(resultModel,document.getElementById("result-table"));
     $('#result-table').DataTable();    
         
     var pieChart = new customChart("overview-pie-chart","pie");    
     pieChart.add(data);
     pieChart.render();
     
     var lineChart = new customChart("overview-line-chart","line");    
     lineChart.add(data);
     lineChart.render();
     
     var columnChart = new customChart("overview-column-chart","column");    
     columnChart.add(data);
     columnChart.render();
  });

}


var customChart = function (containerStr,chartType) {
	var dps = [];
	var chart = new CanvasJS.Chart(containerStr, {
		zoomEnabled: true,
		height: 500,
		title : {
			text : "Results",
			fontSize : 20
		},
		axisX : {
			labelAngle: -45,
			labelFontSize: 12,
			gridThickness : 0
		},
		axisY : {
			labelFontSize: 12,
			gridThickness : 1
		},
		legend : {
			verticalAlign : 'bottom',
			horizontalAlign : "center"
		},
		data : [ {
			type : chartType,
			 dataPoints: dps
		} ]
	});
	var add = function(data) {
		if (data != '') {
			if (chartType == 'pie') {
				for (i = 0; i < data.length; i++) {
					var found = false;
					var j;
					for (j = 0; j < dps.length; j++) {
						if (dps[j].label == data[i].status) {
							dps[j].y = dps[j].y + 1;
							found = true;
							break;
						}
					}
					if (!found) {
						dps.push({ label : data[i].status, y : 1 });
					}
				}
			} else if(chartType == 'line'){
				var i;
				for (i = 0; i < data.length; i++) {
					var found = false;
					var j;
					var dt = new Date(data[i].created_date);
					for (j = 0; j < dps.length; j++) {
						if (isTodayDay(dt) && dps[j].x.getDay()  ===  dt.getDay() ) {
							dps[j].y = dps[j].y + 1;
							found = true;
							break;
						}
					}
					if (!found) {
						dps.push({ x : dt, y : 1 ,label: dt.toLocaleString('en-US', { hour: 'numeric', hour12: true })});
					}
				}
			} else if(chartType == 'column'){
				var i;
				for (i = 0; i < data.length; i++) {
					var found = false;
					var j;
					for (j = 0; j < dps.length; j++) {
						if ( dps[j].label  == data[i].sessionToken ) {
							dps[j].y = dps[j].y + 1;
							found = true;
							break;
						}
					}
					if (!found) {
						dps.push({ y : 1 , label: data[i].sessionToken});
					}
				}
			}
		 }
	};
	var render = function(){
		if(chartType == 'line') {
			chart.options.axisX.intervalType= "hour";		
			chart.options.title.text="Request per hour";
		} else if(chartType == 'column') {
		 chart.options.axisY.interval= Math.ceil(dps.length / 40);
		 chart.options.title.text="Request per session";
		}
		chart.render();
	}
	var customChart = {
			add: add,
			render: render
	};
	return customChart;
}

function isTodayDay(dt){
	var current = new Date();
	if(current.getDay() == dt.getDay()){
		return true;
	}
	return false;
}