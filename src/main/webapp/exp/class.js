// JavaScript Document
page = "class";
page_content = "班级信息";

var content = new Array();
content[0] = new Array();
content[0][0] = "班级名称";
content[0][1] = "班级名称";
content[0][2] = "";
content[1] = new Array();
content[1][0] = "所属课程";
content[1][1] = "所属课程";
content[1][2] = "";
content[2] = new Array();
content[2][0] = "班级教师";
content[2][1] = "班级教师";
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
	
}

function classBind(){
	
}

function contentFromService(name){
	content[0][1] = name;
}

function contentClear(){
	content[0][1] = "班级名称";
}