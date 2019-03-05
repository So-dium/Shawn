
function showInChart(){
	//将页面改为表格模式
	
	//edit为bool型  判断是否为编辑状态,op是判断为新增还是删除操作
	
	body_content = $("#content");
	body_content.empty();
	
	h = $("<h></h>");
	h.html("课时管理");
	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h.appendTo(body_content);
	
	c_btn_new = $("<button></button>");	
	c_btn_new.html("< 课时安排 >");
	c_btn_new.attr("class", "btn button-new");
	c_btn_new.attr("id", "editTimeManagement");
	c_btn_new.attr("onclick", "editTimeManagement(0);");
	c_btn_new.appendTo(body_content);

	createDateSelector().appendTo(body_content);
	
	$('.form_date').datetimepicker({
	    language:  'zh-CN',
	    weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
	});
	
	createWeekSelector(body_content);
	createCourseTable().appendTo(body_content);
	username = user.getUser();
	queryCourseTableInfo(username);
	queryCourseTable(username, 0, 0 ,false);
	
	c_btn_s = $("<button></button>");	
	c_btn_s.html("< 在表格内显示 >");
	c_btn_s.attr("id", "showContent");
	c_btn_s.attr("class", "btn button-new");
	c_btn_s.attr("onclick", "showContent('time-management', 0);");
	c_btn_s.appendTo(body_content);
	
	user = new userControl();
	
}

function showCourseTableWithWeek(edit,op){
	
	//更新表格内容，op判断是新增还是删除操作
	//edit为bool型  判断是否为编辑状态
	
	var week = $("#week").val();
	var date = $("#courseTable-date").val();

	if(date == ""){
		date = 0;
	}

	user = new userControl();
	username = user.getUser();
	
	queryCourseTable(username, date, week, edit ,op);
}

/**
 * 
 * @param user
 * @param date if xxxx-xx-xx, think as choosing the date 
 * @param week if 0, think as default week, if -1, think as using date
 */
function queryCourseTable(user, date, week, edit , op){
	
	//edit为bool型  判断是否为编辑状态 op是判断为新增还是删除操作
	$.ajax({
		url : "showTimeArrange.action",
	    data : {
			user : user,
			date : date,
			week : week
	    },    
	    type : 'post',      
	    dataType : 'json',    
	    success : function(data){
	    	$("#course-table").replaceWith(createCourseTable());
	    	
	    	$("#week").val(data["week"]);
	    	$("#courseTable-date").val(data["date"]);
	    	
	    	datalist = data["data"];
	    	s = datalist.length;
	    	
	    	for(var i = 0; i < s; i++){
	    		//process each class
	    		classname = datalist[i]["class"];
	    		timelist = datalist[i]["timelist"];
	    		
	    		l = timelist.length;
	    		for(var j = 0; j < l; j++){
	    			$("#courseTable-" + timelist[j]).attr("class", "courseTable-selected");
	    			$("#courseTable-" + timelist[j]).html(classname);
	    		}	    		
	    	}
	    	console.log(op);
	    	if(edit && op == 0){
	    		addEventListener();
	    	}
	    	else if(edit && op == 1){
	    		delEventListener();
	    	}
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
}

function queryCourseTableInfo(user){
	//生成周数下拉框
	$.ajax({
		url : "courseTableInfo.action",
	    data : {
			user : user,
	    },    
	    type : 'post',      
	    dataType : 'json',    
	    success : function(data){
	    	totalweek = data["totalweek"];
	
	    	var select = $("#week");
	    	
	    	for(var i = 0; i < totalweek; i++){	    		
	    		var option = $("<option></option>");
	    		option.attr("value", (i + 1));
	    		option.html("第" + (i + 1) + "周");
	    		option.appendTo(select);
	    		option = null;
	    	}
	    },
	    error : function(data){
	    	
	    }
	});
}

function createCourseTable(){
	c_table = $("<table></table>");
	c_table.attr("class", "table table-striped table-bordered table-hover table-responsive course-table");
	c_table.attr("id", "course-table");
	
	c_thead = $("<thead></thead>");
	c_thead.appendTo(c_table);
	
	c_tr = $("<tr></tr>");
	c_tr.appendTo(c_thead);
	
	c_thead_content = new Array("", "一", "二", "三", "四", "五", "六", "日");
	for(var i = 0; i < 8; i++){
		c_th = $("<th></th>");
		c_th.html(c_thead_content[i]);
		if(i == 0){
			c_th.attr("class", "courseTable-th col-lg-2 col-md-2 col-sm-2 col-xs-2");
		}
		else{
			c_th.attr("class", "courseTable-th");
		}
		
		c_th.appendTo(c_tr);
	}
	
	c_tbody = $("<tbody></tbody>");
	c_tbody.appendTo(c_table);
	
	c_tbody_content = new Array("8 : 00 ~ 10 : 00", "10 : 00 ~ 12 : 00", "12 : 00 ~ 14 : 00", "14 : 00 ~ 16 : 00", "16 : 00 ~ 18 : 00", "18 : 00 ~ 20 : 00", "20 : 00 ~ 22 : 00", "22 : 00 ~ 24 : 00");
	
	for(var i = 0; i < 8; i++){
		c_tb_tr = $("<tr></tr>");
		c_tb_tr.appendTo(c_tbody);
		
		c_th = $("<th></th>");
		c_th.html(c_tbody_content[i]);
		c_th.attr("class", "courseTable-th");
		c_th.appendTo(c_tb_tr);
		
		for(var j = 0; j < 7; j++){
			c_td = $("<td></td>");
			c_td.appendTo(c_tb_tr);
			c_td.html();
			c_td.attr("id", "courseTable-" + i + (j + 1));
			c_td.attr("class", "notSelected");
		}
	}
	
	return c_table;
}

function createDateSelector(){
	c_d_div = $("<div></div>");
	c_d_div.attr("class", "col-lg-4 col-md-4");

	c_d_select = $("<div></div>");
	c_d_select.appendTo(c_d_div);
	c_d_select.attr("class", "input-group has-success date form_date");
	c_d_select.attr("data-date", "");
	c_d_select.attr("data-date-format", "dd MM yyyy");
	c_d_select.attr("data-link-field", "courseTable-date");
	c_d_select.attr("data-link-format", "yyyy-mm-dd");

	c_d_s_input = $("<input></input>");
	c_d_s_input.appendTo(c_d_select);
	c_d_s_input.attr("class", "form-control");
	c_d_s_input.attr("size", "16");
	c_d_s_input.attr("type", "text");
	c_d_s_input.attr("readonly", "");
	c_d_s_input.attr("id", "courseTable-date");
	c_d_s_input.attr("placeholder", "选择日期");

	c_d_s_input.on("change", function() {
		
		//当自主选择日期时，周数应显示无，并判断是否为编辑状态

		$("#week").select2("val", "-1");
		var op = $("#operation").val();

		if ($("#editTimeManagement").is(":hidden"))
			showCourseTableWithWeek(true, op);
		else {
			showCourseTableWithWeek(false, op);
		}
	});

	c_d_s_span1 = $("<span></span>");
	c_d_s_span1.appendTo(c_d_select);
	c_d_s_span1.attr("class", "input-group-addon");

	c_d_s_s1_span = $("<span></span>");
	c_d_s_s1_span.appendTo(c_d_s_span1);
	c_d_s_s1_span.attr("class", "glyphicon glyphicon-remove");

	c_d_s_span2 = $("<span></span>");
	c_d_s_span2.appendTo(c_d_select);
	c_d_s_span2.attr("class", "input-group-addon");

	c_d_s_s2_span = $("<span></span>");
	c_d_s_s2_span.appendTo(c_d_s_span2);
	c_d_s_s2_span.attr("class", "glyphicon glyphicon-calendar");

	c_d_input = $("<input></input>");
	c_d_input.appendTo(c_d_div);
	c_d_input.attr("type", "hidden");
	c_d_input.attr("id", "courseTable-date");

	return c_d_div;
}

function createWeekSelector(father){
	var c_w_div = $("<div></div>");
	c_w_div.attr("id","selector");
	var select = $("<select></select>");
	select.attr("id","week");
	select.attr("class", "form-control select select-success select-block mbl");
	
	var option = $("<option></option>");
	option.attr("value", 0);
	option.html("默认课表");
	option.appendTo(select);
	
	var option0 = $("<option></option>");
	option0.attr("value", -1);
	option0.html("无");
	option0.appendTo(select);
	
	select.appendTo(c_w_div);
	
	c_w_div.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.on("change", function(){
		
		//周数为默认列表时，日期应该清零
		
		$("#courseTable-date").val("");
		var op = $("#operation").val();
		if($("#editTimeManagement").is(":hidden"))
			showCourseTableWithWeek(true,op);
		else{
			showCourseTableWithWeek(false,op);
		}			
	});
	
	select = null;
	c_w_div = null;
	option = null;
	button = null;
}


function createOperationSelector(op){

	var select = $("<select></select>");
	select.attr("id","operation");
	select.attr("class", "form-control select select-success select-block mbl away-from addDel");
	
	var option = $("<option></option>");
	option.attr("value", 0);
	option.html("新增");
	option.appendTo(select);
	
	var option0 = $("<option></option>");
	option0.attr("value", 1);
	option0.html("删除");
	option0.appendTo(select);
	
	
	select.appendTo($("#selector"));
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", op);

	
	select.on("change", function(){
		//更改操作类型  新增/删除
		var op = $("#operation").val();
		showCourseTableWithWeek(true,op);
	});
	
	select = null;
	c_w_div = null;
	option = null;
}
//课时管理编辑
function editTimeManagement(op){	
	
	//先转换为表格模式
	if($("#courseTable-date").length <= 0)
		showInChart();
	
	$(".tittle").html("课时管理>>课时安排");
	$("#editTimeManagement").hide();
	$("#showContent").hide();
	
	button = $("<button></button>");
	button.attr("id","sumit");
	button.attr("class","btn button-new");
	button.html("取消");
	button.appendTo($("#content"));
	button.attr("onclick","cancel()");
	
	button = $("<button></button>");
	button.attr("id","sumit");
	button.attr("class","btn button-new");
	button.html("确定");
	button.appendTo($("#content"));
	button.attr("onclick","submitTimeManagement();")
	
	createClassSelector();
	createOperationSelector(op);
	
	showCourseTableWithWeek(true,op);
	if(op == 0){
		addEventListener();
	}
	else if(op == 1){
		delEventListener();
	}
	
		
}

//获得班级下拉框
function createClassSelector(){
	
	var user = new userControl();
	username = user.getUser();
	
	$.ajax({
		url : "classList.action",
	    data : {
			user : username,
	    },    
	    type : 'post',      
	    dataType : 'json', 
		success : function(data){
			
			var select = $("<select></select>");
			select.attr("class", " form-control select select-success select-block mbl away-from");
			select.attr("id", "class");
			
			for(var i=0;i<data.length;i++){
				var option = $("<option></option>");
				option.attr("value",data[i]["classId"]);
				option.html(data[i]["className"]);
				option.appendTo(select);
			}

			$("#selector").append(select);
			select.select2({dropdownCssClass: 'dropdown-inverse'});
			
			select.on("change", function(){
				//更改操作类型  新增/删除
				var op = $("#operation").val();
				showCourseTableWithWeek(true,op);
			});
		},
		error : function(){
			alert(error);
		}
	});
	
}
function cancel(){
	showInChart(false,-1);
	$(".tittle").html("课时管理");
}

function submitTimeManagement(){
	
	var week = $("#week").val();
	var date = $("#courseTable-date").val();
	var op = $("#operation").val();
	var classId = $("#class").val();
	
	if($(".add").length <= 0 && $(".del").length <= 0){
		alert("请在课程表上选择时段");
		return;
	}
	else if(week == -1 && date == ""){
		alert("请在选择正确的日期/周");
		return;
	}
	
	if(date == ""){
		date = 0;
	}
	if(op == 0){  //新增操作

		var array = new Array();
		var i = 0;
		$.each($(".add") ,function(index, value){
			var id = $(value).attr("id").split("-");
			var No = id[1];
			array[i++] = No;
		});

	}
	else if(op == 1){  //删除操作

		var array = new Array();
		var i = 0;
		$.each($(".del") ,function(index, value){
			var id = $(value).attr("id").split("-");
			var No = id[1];
			array[i++] = No;
		});

	}
	
	var user = new userControl();
	username = user.getUser();
	
	$.ajax({
		url : "editTimeArrange.action",
		data : {
			user 	: username,
			week 	: week,
			date 	: date,
			classId : classId,
			operation : op,
			period 	: array.join(",")
		},
		type : 'post',
		dataType : 'json',
		success : function(data){
			if(data["isSuccess"] == true)
				message = "更新成功";
			else
				message = "更新失败";
			msg = Messenger().post({
	    		message : message,
	    		showCloseButton : true
	    	});
			
			setTimeout(showInChart,500,false,-1);
			$(".tittle").html("课时管理");
			
		},
		error : function(){
			alert("error");
		}
	});
}

function addEventListener(){

	$(".notSelected").mouseenter(function(e){
		$(this).addClass("addHighlight");
	});
	$(".notSelected").mouseout(function(e){
		$(this).removeClass("addHighlight");
	});
	$(".notSelected").click(function(){
		
		if(!$(this).hasClass("add")){
			$(this).addClass("add");
		}
		else{
			$(this).removeClass("add");
		}
	});
}
function delEventListener(){

	$(".courseTable-selected").mouseenter(function(e){
		$(this).addClass("delHighlight");
	});
	$(".courseTable-selected").mouseout(function(e){
		$(this).removeClass("delHighlight");
	});
	$(".courseTable-selected").click(function(){
		
		if(!$(this).hasClass("del")){
			$(this).addClass("del");
		}
		else{
			$(this).removeClass("del");
		}
	});
}
