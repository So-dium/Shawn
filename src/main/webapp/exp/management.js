// JavaScript Document
function sidebar_click(i){
	
	$(i).parent("ul").children("li").attr("class", "sidebar-nonactive");
	$(i).attr("class", "sidebar-active");
}

function logout(){
	$.cookie("log", "out", {expires: 7, path: '/'});
}

function Detail(i){	
	main = $("#content");

	main.empty();
	
	h = $("<h></h>");
	h.html($(i).html());
	h.attr("class", "tittle");
	h.appendTo(main);
	
	s = $("<small></small>");
	b = $("<button></button>");
	b.attr("class", "btn btn-link");
	b.attr("onclick", "Edit_SubPage(this)");
	b.appendTo(s);
	b.html("编辑");
	s.appendTo(main);
	
	//修改content的class属性
	$.each(classForm, function(index, value){
		content[index][2] = classForm[index];
	});
	
	//从服务器获取数据填入数组中
	contentFromService($(i).html());
	
	createTable(main, content);
	
	//重置content的内容
	contentClear();

	$.each(content, function(index, value){
		content[index][2] = "";
	});
}

function Edit_Homepage(i){
	$.session.set(page + "Name", $(i).parent("td").prev().children("button").html());
	
	Edit(i);
}

function Edit_SubPage(i){
	$.session.set(page + "Name", $(i).parent("small").prev().html());
	
	Edit(i);
}

function Edit(i){
	main = $("#content");

	main.empty();
	
	h = $("<h></h>");
	h.html("编辑");
	h.attr("class", "tittle");
	h.appendTo(main);
	
	//修改content的class属性
	$.each(classForm, function(index, value){
		content[index][2] = classForm[index];
	});
	
	//从服务器获取content的内容
	contentFromService($.session.get(page + "Name"));
	
	createTable(main, content);
	
	//设置class绑定事件
	classBind();
	
	//各个模块特有功能
	component();
	
	//保存实验
	b = $("<button></button>");
	b.html("提交");
	b.appendTo(main);
	b.attr("class", "btn btn-default btn-lg btn-new");
	
	//重置content的内容
	contentClear();

	$.each(content, function(index, value){
		content[index][2] = "";
	});
	
}

function Add(){
	main = $("#content");

	main.empty();
	
	h = $("<h></h>");
	h.html("新" + page_content);
	h.attr("class", "tittle");
	h.appendTo(main);
	
	//修改content的class属性
	$.each(classForm, function(index, value){
		content[index][2] = classForm[index];
	});
	
	
	createTable(main, content);
	
	//设置class绑定事件
	classBind();
	
	//各个模块特有功能
	component();
	
	//保存实验
	b = $("<button></button>");
	b.html("提交");
	b.appendTo(main);
	b.attr("class", "btn btn-default btn-lg btn-new");
	
	//重置content的内容
	contentClear();

	$.each(content, function(index, value){
		content[index][2] = "";
	});
}

function createTable(main, content){
	table = $("<table></table>");	
	table.appendTo($(main));
	table.attr("class", "table table-striped");
	
	number = 0;
	$.each(content, function(index, value){
		tr = $("<tr></tr>");
		tr.appendTo(table);
		
		th = $("<th></th>");
		th.html(value[0]);
		
		td = $("<td></td>");
		if(value[2] == "edit"){
			a = $("<a></a>");
			a.html(value[1]);
			a.appendTo(td);
			a.attr("class", value[2]);
			a.attr("id", page + "-a" + number);
		}
		else{
			tdGenerate(td, value[1], value[2], number);
		}
		number++;
		
		th.appendTo(tr);
		td.appendTo(tr);
	});	
}

function inputsave(){
	td = $.session.get(page + 'click');
	if($("#edit-input").val() != ""){
		$("#" + td).html($("#edit-input").val());
	}
	
	$('#myModal').modal("hide");
}