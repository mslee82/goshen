<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page session="false" %>
<html>
<head>
<%@ include file="/WEB-INF/include/header.jspf" %>
	<title>Main</title>
	<script type="text/javascript">
		var barChart = null;
		
		$(document).ready(function(){			
            fnDrawChart();
		});

		/**
		 * 차트 데이터 조회
		 */
		function fnDrawChart() {
			commonAjax.setUrl("<c:url value='/main/customerSalesTop5.do' />");
			commonAjax.setDataType("json");
			commonAjax.clearParam();
			commonAjax.setCallback("fnDrawChartCallback");
			commonAjax.ajax();
		}	

		/**
		 * 차트 데이터로 그리기
		 */
		function fnDrawChartCallback(response) {
			var chartlabel = [];
		 	var chartdata = [];
		 	var chartToolTipLabel = [];
		 	
		 	$.each(response.chart, function(j, val){
		 		if(gfn_nvl(val.name).length > 0){
		 			var vName = gfn_nvl(val.name).length > 9 ? val.name.substring(0, 9)+"..." : val.name;
		 	 		chartlabel.push(vName);
		 		}else{
		 			chartlabel.push("");
		 		}
		 		chartdata.push(val.value);
		 		chartToolTipLabel.push(gfn_nvl(val.name));
		 	});
		 	
		    var top5Data = {
	    		labels : chartlabel,
	    	    datasets : [{
	    	    	fillColor : "rgba(151,187,205,0.5)",
	                strokeColor : "rgba(151,187,205,0.8)",
	                highlightFill : "rgba(151,187,205,0.75)",
	                highlightStroke : "rgba(151,187,205,1)",
	                data : chartdata
	    	    }]
	       	}

		 	if(barChart != null){
		 		barChart.destroy();
		 	}
		 	
		 	var chartOption= {
	 			animation: true,
	 		    //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
	 		    scaleBeginAtZero : true,
	 		    scaleFontSize: 8,
	 		    //Boolean - Whether grid lines are shown across the chart
	 		    scaleShowGridLines : true,

	 		    //String - Colour of the grid lines
	 		    scaleGridLineColor : "1px solid rgba(223, 223, 223, 0.8)",

	 		    //Number - Width of the grid lines
	 		    scaleGridLineWidth : 1,

	 		    //Boolean - Whether to show horizontal lines (except X axis)
	 		    scaleShowHorizontalLines: true,

	 		    //Boolean - Whether to show vertical lines (except Y axis)
	 		    scaleShowVerticalLines: true,

	 		    //Boolean - If there is a stroke on each bar
	 		    barShowStroke : true,

	 		    //Number - Pixel width of the bar stroke
	 		    barStrokeWidth : 1,

	 		    //Number - Spacing between each of the X value sets
	 		    barValueSpacing : 5,

	 		    //Number - Spacing between data sets within X values
	 		    barDatasetSpacing : 1
	 		};
		 	var chartId = document.getElementById("chart01").getContext('2d');
		 	barChart = new Chart(chartId).Bar(top5Data, chartOption);
		}

	</script>
</head>
<body>
	<%@ include file="/WEB-INF/include/navi.jsp" %>		
	<div class="container_wrap">
		<canvas id="chart01" width="300" height="300"></canvas>
		<canvas id="myChart2" width="300" height="300"></canvas>
		<canvas id="myChart3" width="300" height="300"></canvas>
	</div>
</body>
</html>
