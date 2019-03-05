// JavaScript Document
var a=1;
var user = new userControl();
var host = 'http://202.112.113.133:8081';
$(document).ready(function(e) {
	$.ajaxSetup({
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		complete : function(XMLHttpRequest,textStatus){
			var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
			if(sessionstatus == "timeout"){
				alert("session已过期，请重新登陆");
				window.location.replace("log/login.jsp");
			}
		}
	});
	
	if($("#sidebar-list").children().length == 0){
		alert("there's error when loading content, please login again.");
	}
	
	user.userInit();
	
	contentLoad();
});

function contentLoad(){
	showContent($("#sidebar-list").children().attr("id"), 0, "");
}

//set cookie "log" as "out" when logout, in case that password is remembered at login page
function logout(){
	$.cookie("log", "out", {expires: 7, path: '/'});
}

/**
 * 
 * @param id operation name
 * @param page the number of required page, default page is 0
 * @param select the name of select if has, default is ""
 */
function showContent(id, page, select){
	username = user.getUser();

	$("#content").empty();
	$("#content").append($("<h class='tittle'><i class='icon-spinner icon-spin icon-4x'></i>正在加载内容</h>"));
	//fetch data from server
	
	if(id==null)                     //由于facilities不是通过side-bar点击进入的，此处采取特殊的赋值方式
		id="facilities-management";
	
	fetchData(id, username, page, select,false);
}

//导入信息页面
function inputPage(){
	
	var username = user.getUser();
	var id=$(".sidebar-active").attr("id");
	
	$.ajax( {    
	    url:"InputInfo.action",
	    data:{
	    	user : username,
	    	id : id
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data) { 
	    	//display the result
	    	var body = $("#content");
	    	body.empty();
	    	
	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["select"] != null){    		
	    		//一般的选项应有选项标题，这里只需要下拉框，所以只用[1]
	    		createSingleselect(data["select"][1]["name"], data["select"][1]["value"], body , data["select"][1]["initValue"]);
	    	}
	    	
	    	if(data["guide"] != null){
	    		var h = $("<p></p>");
	    		h.appendTo(body);
	    		h.attr("class", "col-lg-12 col-md-12 col-sm-12 col-xs-12");
	    		h.html(data["guide"]["data"]);
	    	}
	    	
	    	if(data["infomation"] != null){
	    		
	    		var textarea = $("<textarea></textarea>");
	    		textarea.attr("class","col-lg-12 col-md-12 col-sm-12 col-xs-12 inputInfo");
	    		body.append(textarea);
	    	}
	    	
	    	if(data["button"] != null){
	    		var b = createButton(data["button"]);
	    		b.appendTo(body);
	    	}
	    	
	     },    
	     error : function() {
	    	 body_content = $("#content");
	    	 body_content.empty();
	    	 alert("error");
	     }
	}); 
}

//提交导入信息页面
function submitInputPage(){
	
	var username = user.getUser();
	var id=$(".sidebar-active").attr("id");
	
	//下拉框个数(页面上共有多少个可下拉列表)

	var select = $("select").val();
	var classId = $("select").val();
		
	var infomation = $(".inputInfo").val();
	
	flag = true;
	//学号语法检查
	idList = infomation.split("\n");
	
	for(i=0;i<idList.length;i++){
		for(j=0;j<idList[i].length;j++){
			if(isNaN(idList[i][j]) == true ){
				flag = false;
			}
		}
		if(idList[i].length > 10 || flag == false){
			msg = Messenger().post({
    			message : "输入不合法",
    			showCloseButton : true
    		});
			return;
		}
	}
	
	$.ajax( {    
	    url:"submitInfo.action",
	    data:{
	    	user : username,
	    	id : id,
	    	classId : classId,
	    	infomation : infomation
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data) { 
	    	
	    	//display the result
	    	msg = Messenger().post({
	    		message : data["data"],
	    		showCloseButton : true
	    	});
	    	
	    	var input = $(".inputInfo");
	    	input.val("");
	     },    
	     error : function() {
	    	 body_content = $("#content");
	    	 body_content.empty();
	    	 alert("error");
	     }
	}); 
}


/**
 * 
 * @param i the click item
 * @param key the kind of operation
 */
function showDetail(name, key , orderId){
	//user是用户名
	//id是side-bar active项
	//name是显示细节的对象名
	//key是管理内容的大类 如user,exp,course等
	
	//ajax need : user, id, id's name
	
	$(".sidebar-active").attr("class", "sidebar-nonactive");             
	
	switch(key){
	case "selfInfo" : $("#selfInfo").attr("class", "sidebar-active"); break;
	case "exp" : $("#exp-management").attr("class", "sidebar-active");break;
	case "course" : $("#course-management").attr("class", "sidebar-active"); break;
	case "class" : $("#class-management").attr("class", "sidebar-active"); break;
	case "group" : $("#group").attr("class", "sidebar-active");break;
	case "physicsMachines" : $("#physicsMachines-management").attr("class", "sidebar-active");break;
	case "score" : $("#score").attr("class", "sidebar-active"); break;
	case "my-exp" : $("#my-exp").attr("class", "sidebar-active");key = "exp"; break;
	}
	
	username = user.getUser();
	
	$("#content").empty();
	$("#content").append($("<h class='tittle'><i class='icon-spinner icon-spin icon-4x'></i>正在加载内容</h>"));
	
	if(key=="facilities")                     //由于facilities不是通过side-bar点击进入的，此处采取特殊的赋值方式
		id="facilities-management";
	else
		id = $(".sidebar-active").attr("id");
	
	$.ajax( {    
	    url:"showDetail",
	    data:{
	    	user : username,
	    	id : id,
	    	key : key,
	    	name : name,
	    	orderId : orderId
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data) { 
	    	//display the result
	    	var body = $("#content");
	    	body.empty();
	    	
	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["button"] != null){
	    		var b = createButton(data["button"]);
	    		b.appendTo(body);
	    	}
	    	if(data["password"] != null){
	    		createButton(data["password"]).appendTo(body);
	    	}
	    	
	    	if(data["data"] != null){
	    		createDetail(data["data"]).appendTo(body);
	    	}
	    	
	    	if(data["multiSelect"] != null){
	    		createFilter(data["multiSelect"]);
	    	}
	    	
	    	if(data["ResultTask"] != null){
	    		createResultTask(data["ResultTask"]);
	    	}
	    	
	    	
	    	$.cookie("click_id", id);
	    	if(data["id"] != null){
	    		$(".sidebar-active").attr("class", "sidebar-nonactive");
	    		$("#" + data["id"]).attr("class", "sidebar-active");
	    	
	    		//save cookie
	    		$.cookie("click_id", data["id"]);
	    	}
	    	
	    	//save cookie
	    	
	    	$.cookie("click_name", name);
	     },    
	     error : function() {
	    	 body_content = $("#content");
	    	 body_content.empty();
	    	 alert("error");
	     }
	}); 
}

/**
 * 构造详细信息表格
 * @param content
 * @returns
 */
function createDetail(content){
	var d_t = $("<table></table>");
	d_t.attr("class", "table table-hover table-striped detail-table");
	
	var collapse = new collapseUtil();
	
	$.each(content, function(index, value){
		var d_t_row = $("<tr></tr>");
		d_t_row.attr("class", "");
		d_t_row.appendTo(d_t);
		
		//head in div1, content in div2 as panel
		var d_t_row_th = $("<th></th>");
		d_t_row_th.attr("class", "col-lg-4 col-md-4 col-sm-4 col-xs-6");
		d_t_row_th.appendTo(d_t_row);
		d_t_row_th.html(value[0]["name"]);
		
		$.each(value, function(index, value){
			if(index != 0){
				var d_t_row_td = $("<td></td>");
				d_t_row_td.appendTo(d_t_row);
				
				if(value["class"] == "collapse"){
					if(value["name"].length != 0){
						collapse.createCollapse(value["name"]).appendTo(d_t_row_td);
					}
				}
				else if(value["class"] == "mutiselect"){
					if(value["name"].length != 0){
						createMutiSelect(value["name"], value["value"], d_t_row_td);
					}
				}
				else if(value["class"] == "singleselect"){
					if(value["name"].length != 0){
						createSingleselect(value["name"], value["value"], d_t_row_td,value["initValue"]);
					}
				}
				else{
					var d_t_row_td_a = $("<a></a>");
					d_t_row_td_a.appendTo(d_t_row_td);
					d_t_row_td_a.attr("class", value["class"]);
					d_t_row_td_a.attr("onclick", value["onclick"]);
					d_t_row_td_a.attr("value", value["value"]);
					d_t_row_td_a.html(value["name"]);
				}
			}
		});
	});
	
	return d_t;
}
function createFilter(data){
	//倒序的方法插入，注意prepend
	var content = $("#content");
	var table = $("<table></table>");
	table.attr("style","margin:70px 0 20 0;");
	table.appendTo(content);
	var tr = $("<tr></tr>");
	tr.attr("id","filter");
	tr.appendTo(table);
	var choose = $("<td></td>");
	var choosebutton = $("<button>选择</button>");
	choosebutton.attr("class","btn button-new");
	choosebutton.appendTo(choose);
	$("#filter").prepend(choose);
	choosebutton.attr("onclick","filter()");
	if(data["Exp"]!=null){
		var expList = $("<td></td>");
		expList.attr("style","padding:5px 10px;")
		$("#filter").prepend(expList);
		createMutiSelect(data["Exp"][1]["name"], data["Exp"][1]["value"], expList);
		var exp = $("<th></th>");
		exp.html("实验");
		exp.attr("style","padding:5px;font-size:20px");
		$("#filter").prepend(exp);
	}
	
}

function filter(){
	l=0;
	var array = new Array();
	//选择课程多选框中的内容
	var content = $("[value=BelongsToExpID]").val();
	if(content!=null){
		$.each(content, function(index, value){
			$.each($("[value=BelongsToExpID]").children("option"), function(i, val){
				if($(val).val() == value){
					array[l++] = $(val).html();
				}
			});
		});
	}
	
	tbody = $("#expinfo").find("tbody");
	var length = tbody.children().length;
	//选中的内容都在array中
	if($.inArray("全部", array) != -1 || array.length == 0){
		//全部小组都应该显示
		var tr = tbody.children().first();
		for(i=0;i<length;i++){
			tr.css("display","");
			tr = tr.next();
		}
	}
	else{
		//主意比对表格中每一行的内容
		var tr = tbody.children().first();
		for(i=0;i<length;i++){
			if ($.inArray(tr.find(".expname").html(), array) != -1) {
				
				tr.css("display","");
			}
			else{
				tr.css("display","none");
			}
			tr = tr.next();
		}
	}
}

function createResultTask(data){

	var content = $("#content");
	
	table = $("<table></table>");
	table.attr("class", "table table-striped");
	table.attr("id","expinfo");
	table.appendTo(content);
	thead = $("<thead></thead>");
	tr_head = $("<tr></tr>");
	tr_head.appendTo(thead);
	thead.appendTo(table);
	
	th = $("<th></th>");
	p = $("<p></p>");
	p.appendTo(th);
	p.html("小组成员");
	th.appendTo(tr_head);
	
	th = $("<th></th>");
	p = $("<p></p>");
	p.appendTo(th);
	p.html("实验实例id");
	p.hide();
	th.appendTo(tr_head);
	
	th = $("<th></th>");
	p = $("<p></p>");
	p.appendTo(th);
	p.html("实验名称");
	th.appendTo(tr_head);
	
//	th = $("<th></th>");
//	p = $("<p></p>");
//	p.appendTo(th);
//	p.html("任务");
//	th.appendTo(tr_head);
	
	th = $("<th></th>");
	p = $("<p></p>");
	p.appendTo(th);
	p.html("任务及得分");
	th.appendTo(tr_head);
	
	th = $("<th></th>");
	p = $("<p></p>");
	p.appendTo(th);
	p.html("");
	th.appendTo(tr_head);
	
	tbody = $("<tbody></tbody>");
	tbody.appendTo(table);
	
	collapse = new collapseUtil();
	var str="";
	$.each(data, function(index, value){
		if(index==0)
			str += value["caseid"];
		else
			str += " " + value["caseid"];
	});
	$.each(data, function(index, value){
		
		console.log(value);
		var tr=$("<tr></tr>");
		tr.appendTo(tbody);
		
		
		var td = $("<td></td>");
		collapse.createCollapse(value["groupmember"]).appendTo(td);
		td.appendTo(tr);

		var td = $("<td></td>");
		p = $("<p></p>");
		p.appendTo(td);
		p.html(value["caseid"]);
		p.hide();
		td.appendTo(tr);
		
		var td = $("<td></td>");
		p = $("<p></p>");
		p.appendTo(td);
		p.html(value["expname"]);
		p.attr("class", "expname");
		td.appendTo(tr);
		
//		var td = $("<td></td>");
//		collapse.createCollapse(value["tasklist"]).appendTo(td);
//		td.appendTo(tr);
		
		
		var td = $("<td></td>");
		collapse.createCollapse(value["scoreList"]).appendTo(td);
		td.appendTo(tr);
		
		
		var td = $("<td></td>");
		td.appendTo(tr);
		var butt=$("<button >评分</button>");
		butt.attr("class","btn btn-new")
		butt.click(function(){
			jumpToScore(index,str,"1",value["scoreList"].length);
			
		});
		butt.appendTo(td);
	});
}
function jumpToScore(index,str,taskid,tasknum){
	
	$.ajax({
		url:"score.action",
		data:{
			index : index,
			str : str,
			taskid : taskid,
			tasknum : tasknum
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	if(data["isSuccess"] == true){
	    		window.open( host + "/virnet/score.jsp");
	    	}
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}
function createSingleselect(data, value, father ,initValue){
	
	console.log(data,value);
	var select = $("<select></select>");
	select.attr("class", "form-control select select-primary select-block mbl");
	select.attr("value", value);
	
	$.each(data, function(index, value){
		var option = $("<option></option>");
		option.attr("value", value["value"]);
		if(value["selected"] != null){
			option.attr("selected", value["selected"]);
		}
		option.html(value["name"]);
		option.appendTo(select);
	});

	select.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	
	if(initValue != null)
		select.select2("val",initValue);
	
	if (value == 'realExpStuNum'){
		
		//由于修改了实验人数，因此必须修改session中的实验人数的值
		$.ajax({
			url:"realExpStuNum.action",
		    data:{
		    	'realExpStuNum': select.val() 
		    },    
		    type:'post',      
		    dataType:'json',    
		    success:function(data){
		    	console.log("modify session['realExpStuNum'] success");
		    },
		    error:function(data){
		    	alert("error");
		    }
		});
		
		select.on("change", function (e){
			//由于修改了实验人数，因此必须修改session中的实验人数的值
			$.ajax({
				url:"realExpStuNum.action",
			    data:{
			    	'realExpStuNum': select.val() 
			    },    
			    type:'post',      
			    dataType:'json',    
			    success:function(data){
			    	console.log("modify session['realExpStuNum'] success");
			    },
			    error:function(data){
			    	alert("error");
			    }
			});
		});
	}
}

function createMutiSelect(data, value, father){
	var select = $("<select></select>");
	select.attr("class", "form-control multiselect multiselect-primary");
	select.attr("multiple", "multiple");
	select.attr("value", value);
	
	$.each(data, function(index, value){
		var option = $("<option></option>");
		if(value["value"] != null)
			option.attr("value", value["value"]);
		if(value["selected"] != null){
			option.attr("selected", value["selected"]);
		}
		option.html(value["name"]);
		option.appendTo(select);
	});

	select.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
}

function createSelect_change(data, s, father){	
	var select = $("<select></select>");
	select.attr("class", "form-control select select-primary select-block mbl");
	
	//这里的class不是类   真的是课程的意思，很尴尬
	$.each(data, function(index, value){
		var option = $("<option></option>");
		option.attr("value", value["id"]);
		option.html(value["class"]);
		option.appendTo(select);
	});

	select.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	
	if(s != ""){
		select.val(s).trigger("change");
	}
	
	//学生管理 老师管理和实验员管理时，选择课程不需要刷新页面
	id=$(".sidebar-active").attr("id");
	if(id == "student" || id == "teacher" || id == "exp-staff")
		return;
	
	select.on("change", function (e){
		SelectClick(select.val());
	});
}

function createSelect(data, s, father){	
	var select = $("<select></select>");
	select.attr("class", "form-control select select-primary select-block mbl");
	
	//这里的class不是类   真的是课程的意思，很尴尬
	$.each(data, function(index, value){
		var option = $("<option></option>");
		option.attr("value", value["class"]);
		option.html(value["class"]);
		option.appendTo(select);
	});

	select.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	
	if(s != ""){
		select.val(s).trigger("change");
	}
	
	//学生管理 老师管理和实验员管理时，选择课程不需要刷新页面
	id=$(".sidebar-active").attr("id");
	if(id == "student" || id == "teacher" || id == "exp-staff")
		return;
	
	select.on("change", function (e){
		SelectClick(select.val());
	});
}
function SelectClick(name){	
	id = $(".sidebar-active").first().attr("id");
	showContent(id, 0, name);

}

function createTable(content){
	table = $("<table></table>");
	table.attr("class", "table table-striped");
	
	thead = $("<thead></thead>");
	tr_head = $("<tr></tr>");
	tr_head.appendTo(thead);
	
	$.each(content[0], function(index, value){
		th = $("<th></th>");
		
		p = $("<p></p>");
		p.appendTo(th);
		p.html(value["name"]);
		p.attr("class", value["class"]);
		
		th.appendTo(tr_head);
	});
	
	thead.appendTo(table);
	
	tbody = $("<tbody></tbody>");
	tbody.appendTo(table);
	
	collapse = new collapseUtil();
	
	$.each(content, function(index, value){
		if(index == 0){
			
		}
		else{
			tr = $("<tr></tr>");
			tr.appendTo(tbody);
			
			$.each(value, function(i, value){
				td = $("<td></td>");
				
				if(value["class"] == "collapse"){
					if(value["name"].length != 0){
						collapse.createCollapse(value["name"]).appendTo(td);
					}
				}
				else{
					p = $("<p></p>");
					p.appendTo(td);
					p.html(value["name"]);
					p.attr("class", value["class"]);
					p.attr("onclick", value["onclick"]);
				}
				
				td.appendTo(tr);
			});
		}
	});	
	
	return table;
}

function collapseUtil(){
	number = 0;
	
	this.createCollapse = function(content){
		panel = $("<div></div>");
		panel.attr("class", "panel-group");
		panel.attr("id", "accordion" + number);
		
		div = $("<div></div>");
		div.appendTo(panel);
		div.attr("class", "panel panel-info");
		
		head = $("<div></div>");
		head.attr("class", "panel-heading");
		head.appendTo(div);
		
		h = $("<h4></h4>");
		h.attr("class", "panel-title");
		h.appendTo(head);
		
		a = $("<a></a>");
		a.attr("data-toggle", "collapse");
		a.attr("data-parent", "#accordion" + number);
	    a.attr("href", "#collapse" + number);
	    a.attr("class", "collapsed");
	    a.html(content[0]["name"] + " ...");
	    a.appendTo(h);
	    
	    body = $("<div></div>");
	    body.attr("id", "collapse" + number);
	    body.attr("class", "panel-collapse collapse");
	    body.appendTo(div);
	    
	    tableInner = $("<div></div>");
	    tableInner.attr("class", "panel-body");
	    tableInner.appendTo(body);
	    
	    $.each(content, function(index, value){
	    	trInner = $("<div></div>");
	    	trInner.appendTo(tableInner);
	    	
	    	a0 = $("<a></a>");
	    	a0.html(value["name"]);
	    	a0.attr("class", value["class"]);
	    	a0.attr("onclick", value["onclick"]);
	    	a0.appendTo(trInner);
	    });
	    
	    number++;
	    return panel;
	};
}

/**
 * 创建button元素
 * @param data data["content"]
 * 					button inner html
 * @param data data["class"]
 * 					button class style
 * @param data data["click"]
 * 					button onclick function
 * @param data data["id"]
 * 					button id function
 * @returns {___anonymous_button_temp} jquery button
 */
function createButton(data){
	button_temp = $("<button></button>");
	
	button_temp.html(data["content"]);
	button_temp.attr("class", data["class"]);
	button_temp.attr("onclick", data["click"]);
	
	if(data["id"]!=null)
		button_temp.attr("id", data["id"]);
	
	return button_temp;
}

/**
 * 向服务器请求编辑指令，将当前页面内容改为可编辑页面
 */
function editContent(){
	var id = $.cookie("click_id");
	var name = $.cookie("click_name");
	var username = user.getUser();
	
	$.ajax({
		url:"edit.action",
	    data:{
			id : id,
			user : username,
			name : name
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	var body = $("#content");
	    	body.empty();
	    	
	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["data"] != null){
	    		createDetail(data["data"]).appendTo(body);
	    	}
	    	
	    	if(data["button"] != null){
	    		createButton(data["button"]).appendTo(body);
	    	}
	    	if(data["button_newTask"] != null){
	    		createButton(data["button_newTask"]).appendTo(body);
	    	}
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}

/**/
/**
 * 向服务器请求编辑指令，将当前页面内容改为可修改
 */
function changePassword(){
	var id = "changePassword";
	var name = $.cookie("click_name");
	var username = user.getUser();
	
	$.ajax({
		url:"edit.action",
	    data:{
			id : id,
			user : username,
			name : name
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	var body = $("#content");
	    	body.empty();
	    	
	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["data"] != null){
	    		createDetail(data["data"]).appendTo(body);
	    	}
	    	
	    	if(data["button"] != null){
	    		createButton(data["button"]).appendTo(body);
	    	}
	    	if(data["button_newTask"] != null){
	    		createButton(data["button_newTask"]).appendTo(body);
	    	}
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}

function JumpToExpPlatform(){
	window.location.href= host + '/virnet/exp.jsp';
}

function JumpToExpMonitor(){
	window.open(host + '/virnet/monitor.jsp');
}


/**
 * 在编辑状态下， 点击可编辑项。 从a变为input
 * @param i the click item
 */
function editable(i){
	var content = $(i).html();
	var value = $(i).attr("value");
	
	var input = $("<input></input>");
	input.attr("type", "text"); 
	input.attr("class", "form-control");
	input.attr("value",value);
	
//	input.bind("blur", function(){
//		var c = input.val();
//		var a = $("<a></a>");
//		a.html(c);
//		a.attr("class", "btn btn-link a edit");
//		a.attr("onclick", "editable(this);");
//		a.attr("value", value);
//		
//		input.replaceWith(a);
//	});
	
	$(i).replaceWith(input);
	input.focus();
	input.val(content);
}

/**
 * 在编辑状态下， 点击可编辑项且隐藏显示。 从a变为input
 * @param i the click item
 */
function editable_password(i){
	var content = $(i).html();
	var value = $(i).attr("value");
	
	var input = $("<input></input>");
	input.attr("type", "password"); 
	input.attr("class", "form-control");
	input.attr("value",value);
	
	$(i).replaceWith(input);
	input.focus();
	input.val(content);
}

/**
 * 在编辑页面提交信息
 */
function submit(){
	var id = $.cookie("click_id");
	var name = $.cookie("click_name");
	var username = user.getUser();
	var map = {};
	
	var isNull = false;
	
	//edit
	$.each($(".edit"), function(index, value){
		if( $(value).html() == ""){
			isNull = true;
			console.log("edit" + index);
		}
		map[$(value).attr('value')] = $(value).html();
	});
	
	//修改过的都会变成input 输入项
	var inputs = $("input");
	inputs.each(function(index, value){
		if($(value).hasClass("form-control") && $(value).val()== ""){
			isNull = true;
			console.log("input" + index);
		}
		map[$(value).attr('value')] = $(value).val();
	});
	
	if(isNull){
		msg = Messenger().post({
			message : "请将信息完整填入",
			showCloseButton : true
		});
		return;
	}
	
	//下拉框个数(页面上共有多少个可下拉列表)
	var length = $("select").length;

	for(var i = 0; i < length; i++)
	{
		var select = $($("select")[i]).val();
		
		console.log(select.length+"@@@");
		var array = new Array();
		var l = 0;
		//单选
		if(!(select instanceof Array)){
			$.each($($("select")[i]).children("option"), function(i, val){

				if($(val).val() == select){
					array[l++] = $(val).html();
					console.log(array[0]+"????")
				}
			});
		}
		//多选
		else{
			$.each(select, function(index, value){
				$.each($($("select")[i]).children("option"), function(i, val){

					if($(val).val() == value){
						array[l++] = $(val).html();
					}
				});
			});
		}
		map[$($("select")[i]).attr("value")] = array;
	}

	console.log(map);
	$.ajax( {    
	    url:"submit.action",
	    data:{
			id : id,
			name : name,
			user : username,
			data : map,
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	msg = Messenger().post({
	    		message : data["returndata"],
	    		showCloseButton : true
	    	});
	    	console.log(data);
	    	if(data["isSuccess"]==true && data["name"] != null && data["key"] != null){
	    		console.log("aaa");
	    		showDetail(data["name"], data["key"]);
	    	}
	    	else{
	    		//不刷新页面
	    	}
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}

/**
 * 新建任务
 */
function addtask(){
	var id = $.cookie("click_id");
	var name = $.cookie("click_name");
	var username = user.getUser();
	
	$.ajax({
		url:"addtask.action",
	    data:{
			id : id,
			user : username,
			name : name
	    },        
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	var body = $("#content");
	    	body.empty();
	    	
	    	//alert(data["tittle"]["data"]);

	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["data"] != null){
	    		createDetail(data["data"]).appendTo(body);
	    	}
	    	
	    	if(data["button"] != null){
	    		createButton(data["button"]).appendTo(body);
	    	}
	    	if(data["button_newTask"] != null){
	    		createButton(data["button_newTask"]).appendTo(body);
	    	}
	    	if(data["issuccess"] == false){
	    		alert("新增失败");
	    	}
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}


function addContent(id){
	var username = user.getUser();
	
	$.ajax({
		url:"add.action",
	    data:{
			id : id,
			user : username
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	$.cookie("click_name", "");
	    	$.cookie("click_id", $(".sidebar-active").attr("id"));
	    	
	    	var body = $("#content");
	    	body.empty();
	    	
	    	if(data["tittle"] != null){
	    		var h = $("<h></h>");
	    		h.appendTo(body);
	    		h.attr("class", "tittle");
	    		h.html(data["tittle"]["data"]);
	    	}
	    	
	    	if(data["data"] != null){
	    		createDetail(data["data"]).appendTo(body);
	    	}
	    	
	    	if(data["button"] != null){
	    		createButton(data["button"]).appendTo(body);
	    	}
	    	
	    	storeHistory("other", "other", {
			});
	    	
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
	
}

//function create_delete_form(key_class){//删除操作按钮点击后出现的完成按钮以及在每一行数据之后增加一个可勾选框,key_class为之后要取出的值的class
//	var trs = $("tbody tr");
//	
//	var l = $("<input></input>");
//	l.attr("type","checkbox");
//	l.attr("name","check");
//	l.attr("class","delete_checkbox");
//	td = $("<td></td>");
//	td.append(l);
//	trs.append(td);
//		
//	var finish_delete_btn = $("<button></button>");
//	finish_delete_btn.html("完成删除");
//	finish_delete_btn.attr("class","btn button-new");
//	finish_delete_btn.attr("onclick","delete_history('"+key_class+"');");
//	finish_delete_btn.appendTo($("#content"));
//	
//}
//
//function delete_history(key_class){//选取预约记录中已被勾选框选中所在行的记录的实验ID并形成数组返回给后台
//	var checkbox_list = $(".delete_checkbox");
//	var list = new Array();
//	var length = checkbox_list.length;
//	var i=0;
//	for(var j=0;j<length;j++){
//		var checkbox =  $(".delete_checkbox").eq(j);
//		
//		if(checkbox.is(':checked') == true)
//		{
//			console.log("!!!!");
//			Id = checkbox.parent().parent().find("."+key_class).html();
//			list[i++]=Id;	
//		}
//	}
////	$.each(checkbox_list,function(index,value){
////		var i=0;
////		console.log(value);
////		if(value.is(':checked') == "true")
////			{
////				Id = value.parent().parent().find(".App_Exp_Id").html();
////				list[i++]=Id;
////				
////			}
////	});
//	id = $(".sidebar-active").first().attr("id");
//	username = user.getUser();
//	$.ajax({
//		url:"delete.action",
//		data:{
//			user : username,
//			id : id,
//			list : list.toString()
//		},
//		type:'post',
//		dataType:'json',
//		success:function(data){
//	    	msg = Messenger().post({
//	    		message : data["data"],
//	    		showCloseButton : true
//	    	});
//	    	showContent(id, 0, "");
//	    },
//	    error:function(data){
//	    	alert("error");
//	    }
//	})
//}

/**
 * 侧边栏被点击时请求相应的显示内容
 * @param item onclick item
 */
function sidebar_click(item){
//	alert("sidebar_click");
	$(".sidebar-active").attr("class", "sidebar-nonactive");
	$(item).attr("class", "sidebar-active");
	
	//添加 正在加载代码
	
	if($(item).attr("id")=="selfInfo")
		showDetail(user.getUser(),"selfInfo");
	else if($(item).attr("id")=="exp-monitor"){
		JumpToExpMonitor();
	}
	else
		showContent($(item).attr("id"), 0, "");	
}


/**
 * 执行ajax操作， 向服务器请求侧边栏标签对应的内容
 * @param id sidebar name
 * @param user the current user
 * @param page required number of page, if not choose, set as 0
 * @param select 下拉框选择项，如果没有设置为""
 * @param isHistory 区分是不是back操作
 */
var intervalId = null;
function fetchData(id, user, page, select,isHistory){

	$.ajax( {    
	    url:"loadInfo.action",
	    data:{
			id : id,
			user : user,
			page : page,
			select : select
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data) { 
	    	//display the result
	    	body_content = $("#content");
	    	body_content.empty();
	    	
	    	if(data == null){
	    		return;
	    	}
	    	
	    	h = $("<h></h>");
	    	h.html($("#" + id).children().first().html());
	    	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	    	h.appendTo(body_content);
	    	
	    	if(data["button_delete"] != null){
		    	button_delete = createButton(data["button_delete"]);
		    	button_delete.appendTo(body_content);
	    	}
	    	
	    	
	    	if(data["button_new"] != null){
		    	button_new = createButton(data["button_new"]);
		    	button_new.appendTo(body_content);
	    	}
	    	
	    	if(data["select"] != null){
	    		s_select = createSelect(data["select"], select, body_content);
	    	}
	    	
	    	if(data["select_change"] != null){
	    		s_select = createSelect_change(data["select_change"], select, body_content);
	    	}
	    	
	    	if(data["data"] != null){
	    		table = createTable(data["data"]);
	    		table.appendTo(body_content);
	    	}
	    	
	    	if(data["page"]!=null){
	    		var pageutil = new PageUtil();
		    	pageutil.setPageNO(data["page"]);
		    	pageutil.setPageCurrent(page);
		    	content_page = pageutil.createPageUtil();
		    	if(content_page != null){
		    		content_page.appendTo(body_content);
		    	}
	    	}
	    	
	    	if(data["detail"] != null){
	    		createDetail(data["detail"]).appendTo(body_content);
	    	}
	    	
	    	if(data["button_switch"] != null){
		    	button_switch = createButton(data["button_switch"]);
		    	button_switch.appendTo(body_content);
	    	}
	    	
	    	$(".sidebar-active").attr("class", "sidebar-nonactive");
    		$("#" + id).attr("class", "sidebar-active");
	    	
    		if(! isHistory){
    			storeHistory(id, "fetchData", {
    				id : id,
    				user : user,
    				page : page,
    				select : select
    			});
    		}
	    	
	    	
	    	//监控页面的倒计时
	    	if(intervalId != null){
	    		window.clearInterval(intervalId);
	    	}
	    	if($(".retailTime")!= null){
	    		intervalId = window.setInterval(countDown,1000);
	    	}
	     },    
	     error : function() {
	    	 body_content = $("#content");
	    	 body_content.empty();
	    	 alert("error");
	     }
	}); 
}
/**
 * 执行ajax操作
 * 由于设备表不是从sidebar响应得到的（机柜表才是）
 * 因此此处另写一个专门给设备的函数
 * isHistory 区分是不是back操作
 * */
function fetchFacilitiesData(id, user, page, select, physicsMachinesName,isHistory){

	$.ajax( {    
	    url:"loadFacilitiesInfo.action",
	    data:{
			id : id,
			user : user,
			page : page,
			select : select,
			physicsMachinesName : physicsMachinesName
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data) { 
	    	//display the result
	    	body_content = $("#content");
	    	body_content.empty();
	    	
	    	if(data == null){
	    		return;
	    	}
	    	
	    	h = $("<h></h>");
	    	h.html(physicsMachinesName+"机柜管理");
	    	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	    	h.appendTo(body_content);
	    	
	    	if(data["button_new"] != null){
		    	button_new = createButton(data["button_new"]);
		    	button_new.appendTo(body_content);
	    	}
	    	
	    	if(data["select"] != null){
	    		s_select = createSelect(data["select"], select, body_content);
	    	}
	    	
	    	if(data["data"] != null){
	    		table = createTable(data["data"]);
	    		table.appendTo(body_content);
	    	}
	    	
	    	var pageutil = new PageUtil();
	    	pageutil.setPageNO(data["page"]);
	    	pageutil.setPageCurrent(page);
	    	content_page = pageutil.createPageUtil();
	    	if(content_page != null){
	    		content_page.appendTo(body_content);
	    	}
	    	
	    	if(data["detail"] != null){
	    		createDetail(data["detail"]).appendTo(body_content);
	    	}
	    	
	    	if(data["button_switch"] != null){
		    	button_switch = createButton(data["button_switch"]);
		    	button_switch.appendTo(body_content);
	    	}
	    	
	    	$(".sidebar-active").attr("class", "sidebar-nonactive");
    		$("#" + id).attr("class", "sidebar-active");
	    	
    		if (!isHistory){
    			storeHistory(id, "fetchFacilitiesData", {
    				id : id,
    				user : user,
    				page : page,
    				select : select,
    				physicsMachinesName : physicsMachinesName
    			});
    		}
	     },    
	     error : function() {
	    	 body_content = $("#content");
	    	 body_content.empty();
	    	 alert("error");
	     }
	}); 
}

function deleteTaskOrder(expId,expTaskOrder,expName){
	
	var info = "expId" + "@@" + expId + "##" + "expTaskOrder" + "@@" + expTaskOrder;
	
	msg = Messenger().post({
		  message: "确定删除该任务？",
		  actions: {
		    retry: {
		      label: '确定',
		      phrase: 'Retrying TIME',
		      delay: 10,
		      action: function(){
		    	  $.ajax({
		    		  url:'delete.action',
		    		  data:{
		    			  user:user.getUser(),
		    			  id:"task-management",
		    			  data:info
		    		  },
		    		  type:'post',      
		    		  dataType:'json',    
		    		  success:function(data) {
		    			  msg = Messenger().post({
		    		    		message : data["data"],
		    		    		showCloseButton : true
		    		      });
		    			  showDetail(expName, "exp",null);
		    		  }
		    	  });
		    	  return msg.cancel();
		      }
		    },
		    cancel: {
		      label:'取消',
		      action: function() {
		        return msg.cancel();
		      }
		    }
		  }
		});
}
function showDeleteExpButton(){
	if($("#showDeleteExpButton").html() == "- 删除实验"){
		$(".deleteButton").removeClass("hide");
		$("#showDeleteExpButton").html("取消");
		$("#showDeleteExpButton").css("background-color","#e00c1f");
	}
	else{
		$(".deleteButton").addClass("hide");
		$("#showDeleteExpButton").html("- 删除实验");
		$("#showDeleteExpButton").css("background-color","");
	}
	
}

function deleteExp(expName){
	
	var info = "expName" + "@@" + expName ;
	
	msg = Messenger().post({
		  message: "确定删除该实验？",
		  actions: {
		    retry: {
		      label: '确定',
		      phrase: 'Retrying TIME',
		      delay: 10,
		      action: function(){
		    	  $.ajax({
		    		  url:'delete.action',
		    		  data:{
		    			  user:user.getUser(),
		    			  id:"exp-management",
		    			  data:info
		    		  },
		    		  type:'post',      
		    		  dataType:'json',    
		    		  success:function(data) {
		    			  msg = Messenger().post({
		    		    		message : data["data"],
		    		    		showCloseButton : true
		    		      });
		    			  showContent("exp-management",0);
		    		  }
		    	  });
		    	  return msg.cancel();
		      }
		    },
		    cancel: {
		      label:'取消',
		      action: function() {
		        return msg.cancel();
		      }
		    }
		  }
		});
}
