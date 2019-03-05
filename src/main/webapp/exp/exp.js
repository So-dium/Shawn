// JavaScript Document
page = "exp";
page_content = "实验模版";

content = new Array();
content[0] = new Array();
content[0][0] = "实验名称";
content[0][1] = "实验名称";
content[0][2] = "";
content[1] = new Array();
content[1][0] = "实验简介";
content[1][1] = "实验简介";
content[1][2] = "";
content[2] = new Array();
content[2][0] = "实验指导";
content[2][1] = "实验指导";
content[2][2] = "";

//实验的具体内容需要第二层决定填写什么

//确定每个content的class
classForm = new Array();
classForm[0] = "edit";
classForm[1] = "edit";
classForm[2] = "edit";

$(document).ready(function(e) {
	userCheck();
	
	Init();
});

function Init(){
	main = $("#tbody");
	
	//从服务器获取实验列表
	
}

function component(){
	//进入第二层
	b = $("<button></button>");
	b.html("编辑" + page_content);
	b.attr("class", "btn btn-default btn-next");
	b.appendTo(main);
}

function classBind(){
	$('.edit').bind('click', function(){ 
		$.session.set(page + "click", $(this).attr("id"));
		editable(this); 
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
}

function contentClear(){
	content[0][1] = "实验名称";
}