// JavaScript Document
page = "course";
page_content = "课程信息";

content = new Array();
content[0] = new Array();
content[0][0] = "课程名称";
content[0][1] = "课程名称";
content[0][2] = "";
content[1] = new Array();
content[1][0] = "课程实验";
content[1][1] = "课程实验";
content[1][2] = "";
content[2] = new Array();
content[2][0] = "课程教师";
content[2][1] = "课程教师";
content[2][2] = "";
//实验的具体内容需要第二层决定填写什么

//确定每个content的class
classForm = new Array();
classForm[0] = "edit";
classForm[1] = "course-exp";
classForm[2] = "course-teacher";

$(document).ready(function(e) {
	userCheck();
	
	Init();
});


function Init(){
	main = $("#tbody");
	
	//从服务器获取实验列表
	
}

function component(){
	
}

function classBind(){
	$('.edit').bind('click', function(){ 
		$.session.set(page + "click", $(this).attr("id"));
		editable(this); 
	});
	
	$(".course-exp").bind('click', function(){
		
	});
	
	$(".course-teacher").bind("click", function(){
		
	});
}

function editable(i){
	td = $.session.get(page + 'click');
	$("#edit-input").val($("#" + td).html());	
	
	$(i).attr("data-toggle", "modal");
	$(i).attr("data-target", "#myModal");
}

function contentFromService(name){
	content[0][1] = name;
	
	content[1][1] = new Array();
	content[1][1][0] = "课程实验表";
	content[1][1][1] = new Array();
	content[1][1][1][0] = "asfd";
	content[1][1][1][1] = "dsfadsf";
	
	content[2][1] = new Array();
	content[2][1][0] = "课程教师组";
	content[2][1][1] = new Array();
	content[2][1][1][0] = "asfd";
	content[2][1][1][1] = "dsfadsf";
	
}

function contentClear(){
	content[0][1] = "课程名称";
}

function tdGenerate(td, content_td, className, number){
	panel = $("<div></div>");
	panel.attr("class", "panel-group");
	panel.attr("id", "accordion" + number);
	panel.appendTo(td);
	
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
	a.attr("data-parent", "#accordion");
    a.attr("href", "#collapse" + number);
    a.attr("class", "collapsed");
    a.html(content_td[0]);
    a.appendTo(h);
    
    body = $("<div></div>");
    body.attr("id", "collapse" + number);
    body.attr("class", "panel-collapse collapse");
    body.appendTo(div);
    
    tableInner = $("<div></div>");
    tableInner.attr("class", "panel-body");
    tableInner.appendTo(body);
    
   $.each(content_td[1], function(index, value){
    	trInner = $("<div></div>");
    	trInner.appendTo(tableInner);
    	
    	a0 = $("<a></a>");
    	a0.attr("class", className);
    	a0.html(value);
    	a0.appendTo(trInner);
    });
}