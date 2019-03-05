//
//var menuRight = document.getElementById( 'cbp-spmenu-s2' ),
//showRightPush = document.getElementById( 'showRightPush' ),
//body = document.body;
//$("#cmdtbody").scrollTop(9999);	
///*
//showRightPush.onclick = function() {
//	if(showRightPush.value=="«")
//	{
//		showRightPush.value="»";
//		showRightPush.style.right="100px";
//	}
//	else if(showRightPush.value=="»")
//	{
//		showRightPush.value="«";
//		showRightPush.style.right="0px";
//	}
//	classie.toggle( this, 'active' );
//	classie.toggle( body, 'cbp-spmenu-push-toleft' );
//	classie.toggle( menuRight, 'cbp-spmenu-open' );
//};*/
//function show_date_time(){ 
//	var clock = document.getElementById("clock");
//	year=2016;
//	month=4;
//	date=25;
//	hour=0;
//	minute=0;
//	second=0;
//	window.setTimeout("show_date_time()", 1000); //每一秒执行一次show_date_time
//	var targetDay=new Date();//这个日期是可以修改的
//	targetDay.setFullYear(year);
//	targetDay.setMonth(month-1);					//0~11
//	targetDay.setDate(date);
//	targetDay.setHours(hour);
//	targetDay.setMinutes(minute);
//	targetDay.setSeconds(second);
//
//	var today=new Date(); 
//	differTime=(targetDay.getTime()-today.getTime()); 			//毫秒数相差
//	secDifferTime=Math.floor(differTime/1000);								//秒级
//	secPerDay=24*60*60;
//	day=Math.floor(secDifferTime/secPerDay)
//	secDifferTime=secDifferTime-secPerDay*day;					//减掉每天占用的
//	 
//	secPerHour=60*60;
//	hour=Math.floor(secDifferTime/secPerHour);
//	secDifferTime=secDifferTime-secPerHour*hour;
//
//	secPerMinute=60;
//	minute=Math.floor(secDifferTime/secPerMinute);
//	secDifferTime=secDifferTime-secPerMinute*minute;
//
//	second=secDifferTime;
//	 
//	clock.innerHTML="<font color=#A22900><p>"+hour+":"+minute+":"+second +"</p></font>" ; 
//	} 
//
//			
//$(document).ready(function(e) {
//	$("#cmdtbody").scrollTop(9999);
//	show_date_time();
//	//canvas = $("#expCanvas").get(0);
//	//topo.init(canvas);
//	//topo.show();
//});			