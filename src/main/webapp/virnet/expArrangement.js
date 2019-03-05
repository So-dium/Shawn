function expArgEdit(){
	var top = $("#editExp").position().top;
	//createClassSelectorARG();
	var selector = $("<div><div>");
	var buttondiv = $("<div><div>");
	//按钮
	var button0 = $("<button id = 'newExp' class = 'btn button-new' onclick = 'createARG()'>+新建</button>" );
	var button1 = $("<button id = 'commit' class = 'btn button-new' onclick = 'commitEditARG()'>确定</button>" );
	var button2 = $("<button id = 'cancel' class = 'btn button-new' onclick = 'cancelEditARG()'>取消</button>" );
	button2.appendTo('#content');
	button1.appendTo('#content');
	//模式&操作 选择框  新建按钮
	
	selector.css({
		position:"absolute",
		top:top,
		left:"78%",
		width:"280px",
		height:"80px"
	})
	
	buttondiv.css({
		position:"absolute",
		top: top,
		left:"46%",
		width:"80px",
		height:"50px"
	})
	button0.appendTo(buttondiv);
	
	selector.attr("id","selector");
	selector.appendTo($("#content"));
	buttondiv.appendTo($("#content"));
	createOperationSelectorARG();
	createModeSelectorARG();
	$("#editExp").hide();
	eventListener(0);
}
function createModeSelectorARG(){
	//排课模式选择器
	var select = $("<select></select>");
	select.attr("id","modeSelect");
	select.attr("class","form-control select select-success select-block mbl away-from addDelARG");
	
	var option0 = $("<option></option>");
	option0.attr("value",0);
	option0.html("所有");
	option0.appendTo(select);
	
	var option1 = $("<option></option>");
	option1.attr("value",1);
	option1.html("固定模式");
	option1.appendTo(select);
	
	var option2 = $("<option></option>");
	option2.attr("value",2);
	option2.html("自由预约");
	option2.appendTo(select);
	
	select.appendTo($("#selector"));
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	
	select.on("change", function(){
		//更改操作类型  新增/删除
		var op = $("#modeSelect").val();
		showExpArrangeInMode(op);
	})
}
function createModeSelectorARGInEdit(father){
	//排课模式选择器
	var select = $("<select></select>");
	select.attr("id","modeSelectInEdit");
	select.attr("class","form-control select select-primary select-block mbl");
	
	var option1 = $("<option></option>");
	option1.attr("value",0);
	option1.html("自由预约");
	option1.appendTo(select);
	
	var option2 = $("<option></option>");
	option2.attr("value",1);
	option2.html("固定模式");
	option2.appendTo(select);
	
	select.appendTo(father);
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	
	select.on("change", function(){
		//更改操作类型  新增/删除
		var op = $("#modeSelectInEdit").val();
		if(op == 1){
			$('.addedARGtr').remove();
			//$('.arrange_tittle').hide();
			$('.tr_argNum').hide();
			$(".tr_arrange").hide();
			//$('.add_exp').hide();
		}
		else if(op == 0){
			$('.addedARGtr').remove();
			//$('.arrange_tittle').show();
			$('.tr_argNum').show();
			$(".tr_arrange").show();
			//$('.add_exp').show();
		}
	})
}
function showExpArrangeInMode(op){
	var trlist = $('tr');
	if(op == 0){
		trlist.css("display", "");
	}
	else if(op == 1){
		trlist.css("display", "");
		for(var i = 1; i<trlist.length; i++){
			if(trlist[i].innerHTML.indexOf('固定模式') == -1){
				trlist[i].style.display = 'none';
			}
		}
	}
	else if(op == 2){
		trlist.css("display", "");
		for(var i = 1; i<trlist.length; i++){
			if(trlist[i].innerHTML.indexOf('固定模式') > 0){
				trlist[i].style.display = 'none';
			}
		}
	}
}
function createOperationSelectorARG(){

	var select = $("<select></select>");
	select.attr("id","opeationSelector");
	select.attr("class", "form-control select select-success select-block mbl away-from addDelARG");
	
	var option = $("<option></option>");
	option.attr("value", 0);
	option.html("修改");
	option.appendTo(select);
	
	var option0 = $("<option></option>");
	option0.attr("value", 1);
	option0.html("删除");
	option0.appendTo(select);
	
	
	select.appendTo($("#selector"));
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	
	select.on("change",function(){
		var op = $('#opeationSelector').val();
		eventListener(op);
	});
}

function eventListener(op){
	/*var trList = $('tr');                     //所有tr加上arg_not_selected
	if(trList.length == 0) alert("Error");
	for (var i = 1; i < trList.length; i++){
		if(trList[i].classList.contains('arg_not_selected') == false){
			trList[i].classList.add('arg_not_selected');
		}
	}*/
	$('td').find('*').addClass('arg_not_selected');   //所有td及子元素加上arg_not_selected
	$('td').addClass('arg_not_selected');
	if(op == 0){
		$(".arg_not_selected").unbind();
		$('tr').each(function(){               //去除所有的高亮
			if($(this).hasClass('delARG')){
				$(this).removeClass('delARG');
			}
		});
		
		var selContainer = $('#selector');     //记录当前操做
		if(selContainer.hasClass('del_option')){
			selContainer.removeClass('del_option');
		}
		selContainer.addClass("add_option");
		
		$(".arg_not_selected").mouseenter(function(e){
			$(this).parents('tr').addClass("addHighlightARG");
		});
		$(".arg_not_selected").mouseout(function(e){
			$(this).parents('tr').removeClass("addHighlightARG");
		});
		$(".arg_not_selected").click(function(){
			if($(this).hasClass('clicked')){
				$(this).removeClass('clicked');
				return;
			}
			if($(this).is('p')){
				$(this).parent().addClass('clicked');
			}
			if(!$(this).parents('tr').hasClass("addARG")){
				$('tr').each(function(){               //去除所有的高亮
					if($(this).hasClass('addARG')){
						$(this).removeClass('addARG');
					}
				});
				$(this).parents('tr').addClass("addARG");
			}
			else{
				$('tr').each(function(){               //去除所有的高亮
					if($(this).hasClass('addARG')){
						$(this).removeClass('addARG');
					}
				});
				//$(this).parents('tr').removeClass("addARG");
			}
		});
	}
	if(op == 1){
		$(".arg_not_selected").unbind();
		$('tr').each(function(){               //去除所有的高亮
			if($(this).hasClass('addARG')){
				$(this).removeClass('addARG');
			}
		});
		
		var selContainer = $('#selector');      //记录当前操做
		if(selContainer.hasClass('add_option')){
			selContainer.removeClass('add_option');
		}
		selContainer.addClass("del_option");
		
		$(".arg_not_selected").mouseenter(function(e){
			$(this).parents('tr').addClass("delHighlightARG");
		});
		$(".arg_not_selected").mouseout(function(e){
			$(this).parents('tr').removeClass("delHighlightARG");
		});
		$(".arg_not_selected").click(function(){
			if($(this).hasClass('clicked')){
				$(this).removeClass('clicked');
				return;
			}
			if($(this).is('p')){
				$(this).parent().addClass('clicked');
			}
			if(!$(this).parents('tr').hasClass("delARG")){
				$('tr').each(function(){               //去除所有的高亮
					if($(this).hasClass('delARG')){
						$(this).removeClass('delARG');
					}
				});
				$(this).parents('tr').addClass("delARG");
			}
			else{
				$('tr').each(function(){               //去除所有的高亮
					if($(this).hasClass('delARG')){
						$(this).removeClass('delARG');
					}
				});
				//$(this).parents('tr').removeClass("delARG");
			}
		});
	}
}

function createExpTimeSelector(){
	var select = $("<select></select>");
	select.attr("id", "expTimeInHour");
	select.attr("class","form-control select select-success select-block mbl away-from addDelARG");
	
	var option0 = $("<option></option>");
	option0.attr("value",0);
	option0.html("8:00~10:00");
	option0.appendTo(select);
	
	var option1 = $("<option></option>");
	option1.attr("value",1);
	option1.html("10:00~12:00");
	option1.appendTo(select);
	
	var option2 = $("<option></option>");
	option2.attr("value",2);
	option2.html("12:00~14:00");
	option2.appendTo(select);
	
	var option3 = $("<option></option>");
	option3.attr("value",3);
	option3.html("14:00~16:00");
	option3.appendTo(select);
	
	var option4 = $("<option></option>");
	option4.attr("value",4);
	option4.html("16:00~18:00");
	option4.appendTo(select);
	
	var option5 = $("<option></option>");
	option5.attr("value",5);
	option5.html("18:00~20:00");
	option5.appendTo(select);
	
	var option6 = $("<option></option>");
	option6.attr("value",6);
	option6.html("20:00~22:00");
	option6.appendTo(select);
	
	var option7 = $("<option></option>");
	option7.attr("value",7);
	option7.html("22:00~24:00");
	option7.appendTo(select);

	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	return select;
}
function showTimeSpanARG(classinfo, date, target_id, editData){
	$.ajax({
		url : "showTimeSpan.action",
	    data : {
			classinfo : classinfo,
			date : date,
	    },    
	    type : 'post',      
	    dataType : 'json',
	    //async: false,
	    success : function(data){
	    	exist = data['exist'];
	    	occupied = data['occupied'];
	    	id = data['id'];
	    	expname = data['expname'];
	    	select = $("#"+target_id);
    		for(var i=0;i<exist.length;i++){
    			var start = exist[i]*2+8;
    			var end = exist[i]*2+10;
    			time = start+":00~"+end+":00";
    			var option = $("<option></option>");
    			option.attr("value", id[i]);
    			option.html(time);
    			option.appendTo(select);
    			for(var j=0;j<expname.length;j++){
    				if(exist[i] == occupied[j]){
    					option.prop('disabled', true);
			    		option.html(option.html() + '(' + expname[j] + ')');
    				}
    			}
    		}
	    	/*else if(op == 1){
	    		for(var i=0;i<exist.length;i++){
	    			var start = exist[i]*2+8;
	    			var end = exist[i]*2+10;
	    			time = start+":00~"+end+":00";
	    			var option = $("<option></option>");
	    			option.attr("value", id[i]);
	    			option.html(time);
	    			option.appendTo(select);
	    			for(var j=0;j<occupied.length;j++){
	    				if((occupied[j] >= editData['endTp'] || occupied[j] < editData['startTp']) 
	    						&& exist[i] == occupied[j]){
	    					option.prop('disabled', true);
				    		option.html(option.html() + '(' + expname[j] + ')');
	    				}
	    			}
	    		}
	    	}*/
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
}
function get_explist(classinfo, father){
	var select = $("<select></select>");
	select.attr("id","expSelector");
	select.attr("class","form-control select select-primary select-block mbl");
	//classinfo = 
	$.ajax({
		url : "getExplist.action",
	    data : {
			classinfo : classinfo,
	    },    
	    type : 'post',      
	    dataType : 'json',
	    async: false,
	    success : function(data){
	    	var expNamelist = data['name'];
	    	var expIdlist = data['id'];
	    	
	    	for(var i=0; i<expNamelist.length;i++){
	    		var option = $("<option></option>");
	    		option.html(expNamelist[i]);
	    		option.attr("value",expIdlist[i]);
	    		option.appendTo(select);
	    	}
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
	select.appendTo(father);
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	var first_val = select.find('option').eq(0).val();
	select.select2("val", first_val);
	select.on("change", function(){
		
	})
}
function createExpDateSelectorARG(classinfo, op, father, timespan_id){
	//排课日期选择器
	var select = $("<select></select>");
	select.attr("id", id);
	select.attr("class","form-control select select-primary select-block mbl");
	var option_head = $("<option></option>");
	option_head.attr("value", 0);
	option_head.html("请选择排课日期");
	option_head.appendTo(select);
	//ajax请求可以排课的日期
	$.ajax({
		url : "expDateARG.action",
	    data : {
			classinfo : classinfo,
	    },    
	    type : 'post',      
	    dataType : 'json',
	   // async: false,
	    success : function(data){
	    	for(var i=0; i<data['date'].length;i++){
	    		var option = $("<option></option>");
	    		option.attr('value', i+1);
	    		option.html(data['date'][i]);
	    		option.appendTo(select);
	    	}
	    	select.select2("val", 0);
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
	select.appendTo(father);
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	
	select.on("change", function(){
		if(op == 0){
			var value = select.val();
			if(value == 0) createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");;
			var text = select.find("option:selected").text();
			var td_timespan = father.parent('tr').next().find('td').eq(0).empty();
			createMutiSelect_ARGtimelist(td_timespan, timespan_id, null);
			showTimeSpanARG(classinfo, text, timespan_id, null);
			//perriodOccupy(classinfo, value, 0, null);
		}
		/*else if(op == 1){
			var value = $("#"+id).val();
			if(value == 0) createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");;
			var text = $("#expdate").find("option:selected").text();
			$("#td_timespan").empty();
			createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', null);
			showTimeSpanARG(classinfo, text, 1, getMyTimeSpan());
			//perriodOccupy(classinfo, value, 1, getMyTimespan());
		}*/
	})
}
function add_exp_onButton(classinfo){
	var op = $("#modeSelectInEdit").val();
	add_exp(classinfo, op);
}
function add_exp(classinfo, op){
	tbody = $("#tbody");
	var title_tr = $("<tr class = 'arrange_tittle addedARGtr'></tr>");
	var title_td = $("<td></td>");
	var str = $(".arrange_tittle").last().find('th').eq(0).html();
	var i = parseInt(str.replace(/[^0-9]/ig,""))+1;
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>第"+i+"个实验活动</th>").appendTo(title_td);
	title_td.appendTo(title_tr);
	$("<td></td>").appendTo(title_tr);
	title_tr.appendTo(tbody);
	
	var tr_expdate = $("<tr class = 'addedARGtr'></tr>");
	var td_expdate = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验日期</th>").appendTo(tr_expdate);
	createExpDateSelectorARG(classinfo, 0, td_expdate,'expTimespan'+i);
	td_expdate.appendTo(tr_expdate);
	tr_expdate.appendTo(tbody);
	
	var tr_timespan = $("<tr class = 'addedARGtr'></tr>");
	var td_timespan = $("<td class = 'td_timespan'></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验时段</th>").appendTo(tr_timespan);
	createMutiSelect_ARGtimelist(td_timespan, 'expTimespan'+i, "first_in");
	td_timespan.appendTo(tr_timespan);
	tr_timespan.appendTo(tbody);
	
	if(op == 0){ 				//自由预约才显示这一项
		var tr_argNum = $("<tr class = 'tr_argNum addedARGtr'></tr>");
		var td_argNum = $("<td></td>");
		$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验人数</th>").appendTo(tr_argNum);
		$("<input type='text' class='form-control' placeholder='请输入实验人数' id='argNum'>").appendTo(td_argNum);
		td_argNum.appendTo(tr_argNum);
		tr_argNum.appendTo(tbody);
	}
}
function createARG(){
	var classinfo = $('select').first().val();
	//alert(classinfo);
	//var classinfo = 1;
	var body_content = $("#content");
	body_content.empty();
	var b_commit = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'submitARG("+classinfo+",0)'>+确定</button>");
	var b_cancel = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'cancelEditARG()'>x取消</button>");
	var b_add = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'add_exp_onButton("+classinfo+")'>x新添实验活动</button>");
	var h = $("<h></h>");
	h.html("排课管理>>新建排课");
	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h.appendTo(body_content);
	var table = $("<table id = 'createArgTable' class = 'table table-hover table-striped detail-table' ></table>");
	var tbody = $("<tbody id = 'tbody'></tbody>").appendTo(table);
	
	table.appendTo(body_content);
	
	var tr3 = $("<tr class = 'add_exp'></tr>");
	var td3 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>课程实验</th>").appendTo(tr3);
	td3.appendTo(tr3);
	get_explist(classinfo, td3);
	tr3.appendTo(tbody);
	
	var tr_select = $("<tr></t>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>排课模式</th>").appendTo(tr_select);
	var td_select = $('<td></td>');
	createModeSelectorARGInEdit(td_select);
	td_select.appendTo(tr_select);
	tr_select.appendTo(tbody);
	
	var tr2 = $("<tr class = 'tr_arrange'></tr>");
	var td2 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>预约时间</th>").appendTo(tr2);
	td2.appendTo(tr2);
	$("<input type='text' class='form-control' placeholder='请选择预约时间' id='argtime'>").appendTo(td2);
	tr2.appendTo(tbody);
	
	var title_tr = $("<tr class = 'arrange_tittle'></tr>");
	var title_td = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>第1个实验活动</th>").appendTo(title_td);
	title_td.appendTo(title_tr);
	$("<td></td>").appendTo(title_tr);
	title_tr.appendTo(tbody);
	
	var tr_expdate = $("<tr></tr>");
	var td_expdate = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验日期</th>").appendTo(tr_expdate);
	//$("<input type='text' class='form-control' placeholder='请选择实验日期' id='expdate'> " ).appendTo(td_expdate);
	createExpDateSelectorARG(classinfo, 0, td_expdate,'expTimespan1');
	td_expdate.appendTo(tr_expdate);
	tr_expdate.appendTo(tbody);
	
	var tr_timespan = $("<tr></tr>");
	var td_timespan = $("<td class = 'td_timespan'></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验时段</th>").appendTo(tr_timespan);
	createMutiSelect_ARGtimelist(td_timespan, 'expTimespan1', "first_in");
	td_timespan.appendTo(tr_timespan);
	tr_timespan.appendTo(tbody);
	
	var tr_argNum = $("<tr class = 'tr_argNum'></tr>");
	var td_argNum = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验人数</th>").appendTo(tr_argNum);
	$("<input type='text' class='form-control' placeholder='请输入实验人数' id='argNum'>").appendTo(td_argNum);
	td_argNum.appendTo(tr_argNum);
	tr_argNum.appendTo(tbody);
	
	
	b_cancel.appendTo(body_content);
	b_commit.appendTo(body_content);
	b_add.appendTo(body_content);
	laydate.render({
		elem: '#expdate',
		type: 'date',
		//min: 0,
		theme: 'molv',
		done : function(value, date, endDate){
			td_timespan.empty();
			createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");
			perriodOccupy(classinfo, value, 0, null);
		}
	});
	laydate.render({
	   elem: '#argtime',
	   type: 'datetime',
	   range: true,
	   theme: 'molv',
	   min: 0
	});
}
//old not used
function createARG_old(){
	var classinfo = $('select').first().val();
	//alert(classinfo);
	//var classinfo = 1;
	var body_content = $("#content");
	body_content.empty();
	var b_commit = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'submitARG("+classinfo+",0)'>+确定</button>");
	var b_cancel = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'cancelEditARG()'>x取消</button>");
	var h = $("<h></h>");
	h.html("排课管理>>新建排课");
	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h.appendTo(body_content);
	var table = $("<table id = 'createArgTable' class = 'table table-hover table-striped detail-table' ></table>");
	var tbody = $("<tbody></tbody>").appendTo(table);
	
	table.appendTo(body_content);
	var tr_expdate = $("<tr></tr>");
	var td_expdate = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验日期</th>").appendTo(tr_expdate);
	//$("<input type='text' class='form-control' placeholder='请选择实验日期' id='expdate'> " ).appendTo(td_expdate);
	createExpDateSelectorARG(classinfo, 0, td_expdate);
	td_expdate.appendTo(tr_expdate);
	tr_expdate.appendTo(tbody);
	
	var tr_timespan = $("<tr></tr>");
	var td_timespan = $("<td id = 'td_timespan'></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验时段</th>").appendTo(tr_timespan);
	createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");
	td_timespan.appendTo(tr_timespan);
	tr_timespan.appendTo(tbody);
	
	var tr_select = $("<tr></t>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>排课模式</th>").appendTo(tr_select);
	var td_select = $('<td></td>');
	createModeSelectorARGInEdit(td_select);
	td_select.appendTo(tr_select);
	tr_select.appendTo(tbody);
	
	var tr2 = $("<tr id = 'tr_arrange'></tr>");
	var td2 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>预约时间</th>").appendTo(tr2);
	td2.appendTo(tr2);
	$("<input type='text' class='form-control' placeholder='请选择预约时间' id='argtime'>").appendTo(td2);
	tr2.appendTo(tbody);
	
	var tr_argNum = $("<tr id = 'tr_argNum'></tr>");
	var td_argNum = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验人数</th>").appendTo(tr_argNum);
	$("<input type='text' class='form-control' placeholder='请输入实验人数' id='argNum'>").appendTo(td_argNum);
	td_argNum.appendTo(tr_argNum);
	tr_argNum.appendTo(tbody);
	
	var tr3 = $("<tr></tr>");
	var td3 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>课程实验</th>").appendTo(tr3);
	td3.appendTo(tr3);
	get_explist(classinfo, td3);
	tr3.appendTo(tbody);
	
	
	
	b_cancel.appendTo(body_content);
	b_commit.appendTo(body_content);
	laydate.render({
		elem: '#expdate',
		type: 'date',
		//min: 0,
		theme: 'molv',
		done : function(value, date, endDate){
			td_timespan.empty();
			createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");
			perriodOccupy(classinfo, value, 0, null);
		}
	});
	laydate.render({
	   elem: '#argtime',
	   type: 'datetime',
	   range: true,
	   theme: 'molv',
	   min: 0
	});
}
function getMyTimespan(target_tr){
	var data = {};
	//找到第二个td 获取正在编辑的排课原有的实验时间
	var timeString = target_tr.find('td').eq(1).find('p').eq(0).html();
	var date = timeString.split(' ')[0];
	var startTime = timeString.split(' ')[1].split('~')[0];
	var startTp = (parseInt(startTime.split(':')[0]) - 8) / 2;
	var endTime = timeString.split(' ')[2];
	var endTp = (parseInt(endTime.split(':')[0]) - 8) / 2;
	data['date'] = date;
	data['startTp'] = startTp;
	data['endTp'] = endTp;
	return data;
}
function perriodOccupy(classinfo, date, op, data){
	$.ajax({
		url : "perriodOccupy.action",
	    data : {
			classinfo : classinfo,
			date : date,
	    },    
	    type : 'post',      
	    dataType : 'json',
	    async: false,
	    success : function(data){
	    	occupy = data['occupy'];
	    	name = data['expName'];
	    	name1 = name.split(',');
	    	select = $("select").eq(0);
	    	if(op == 0){
		    	for(var i=0; i<data['occupy'].length; i++){
		    		var num = occupy[i] + 1;
		    		option = select.find('option:nth-child('+ num +')');
		    		option.prop('disabled', true);
		    		option.html(option.html() + '(' + name1[i] + ')');
		    	}
	    	}
	    	else if (op == 1){
	    		for(var i=0; i<data['occupy'].length; i++){
		    		var num = occupy[i] + 1;
		    		if(occupy[i] >= data['endTp'] || occupy < data['startTp']){
			    		option = select.find('option:nth-child('+ num +')');
			    		option.prop('disabled', true);
			    		option.html(option.html() + '(' + name1[i] + ')');
		    		}
		    	}
	    	}
	    },
	    error : function(data){
	    	alert("error");
	    }
	});
}

function creatExpSelectARG(data){
	var select = $("<select></select>");
	//select.attr("id","expSelector");
	select.attr("class", "form-control select select-success select-block mbl away-from expSelector");
	
	$.each(data, function(index, value){
		var option = $("<option></option>");
		option.attr("value", value['value']);
		option.html(value['name']);
		option.appendTo(select);
	})
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
	select.select2("val", 0);
	
	return select;
}
function createMutiSelect_ARGtimelist(father, id, op){
	var select = $("<select></select>");
	select.attr("class", "form-control multiselect multiselect-primary");
	select.attr("multiple", "multiple");
	select.attr("id", id);
	//select.attr("value", value);
	if(op == "first_in"){
		for(var i=0;i<8;i++){
			var time = "";
			var start = i*2+8;
			var end = i*2+10;
			time = start+":00~"+end+":00";
			var option = $("<option></option>");
			option.attr("value", -1*i);
			option.html(time);
			option.appendTo(select);
		}
	}
	select.appendTo(father);
	
	select.select2({dropdownCssClass: 'dropdown-inverse'});
}

function cancelEditARG(){
	sidebar_click($('#exp-arrangement'));
}

function submitARG(classinfo, op){
	if(op == 0){
		var ModeOp = $("#modeSelectInEdit").val();
		//alert($('#expSelector').val());
		var expdateList = $(".expdate");
		var pidlist = new Array();
		var pidEachExpTd = $(".td_timespan");
		for(var i=0;i<pidEachExpTd.length;i++){
			pidlist.push(pidEachExpTd.find('select').eq(i).val())
		}
		var expmode = $("#modeSelectInEdit").val();
		var expernum = new Array();
		if(ModeOp == 0){
			var argtime = $("#argtime").val();
			expers = $(".tr_argNum").find('input');
			for(var i=0;i<expers.length;i++){
				expernum.push(expers[i].value)
			}
		}
		var expnum = $("#expSelector").val();
		var user = new userControl();
		var username = user.getUser();
		for(var i=0;i<expdateList.length;i++){
			var expdate = expdateList[i].val();
			if(expdate == 0){
				alert("请选择实验日期");
				return;
			}
		}
		//判断有无重复时段
		var sameFlag = false;
		var samei = 0, samej = 0;
		for(var i=0;i<pidlist.length-1;i++){
			temp = pidlist[i];
			for(var j=i+1;j<pidlist.length;j++){
				for(var ii=0;ii<temp.length;ii++){
					for(var jj=0;jj<pidlist[j].length;jj++){
						if(temp[ii]==pidlist[j][jj]) {
							if(!sameFlag){
								samei = i;
								samej = j;
							}
							sameFlag = true;
						}
					}
				}
			}
		}
		if(sameFlag) {
			samei = samei + 1;
			samej = samej +1;
			alert("不同实验活动选择的时段不能相同，请修改。错误提示:实验活动"+samei+
					"和实验活动"+samej+"选择了相同的时段");
			return;
		}
		var postPidList = new Array();
		for(var i=0;i<pidlist.length;i++){
			var temp = ""+pidlist[i][0];
			for(var j=1;j<pidlist[i].length;j++){
				temp += ","+pidlist[i][j];
			}
			postPidList.push(temp);
		}
		
		
		$.ajax({
			url : "newARG_2.action",
		    data : {
		    	user : username,
				classinfo : classinfo,
				pidlist : postPidList,
				expmode : expmode,
				argtime : argtime,
				expnum : expnum,
				expernum : expernum
		    },    
		    type : 'post',      
		    dataType : 'json',
		    success : function(data){
		    	alert("提交成功");
		    },
		    error : function(data){
		    	alert("error");
		    }
		});
	}
}
//old not uesd
function submitARG_old(classinfo, op){
	if(op == 0){
		//alert($('#expSelector').val());
		var expdate = $("#expdate").val();
		var pidlist = $("#expTimespan").val();
		var expmode = $("#modeSelectInEdit").val();
		var argtime = $("#argtime").val();
		var expnum = $("#expSelector").val();
		var expernum = $("#argNum").val();
		var user = new userControl();
		var username = user.getUser();
		if(expdate == 0){
			alert("请选择实验日期");
			return;
		}
		$.ajax({
			url : "newARG.action",
		    data : {
		    	user : username,
				classinfo : classinfo,
				pidlist : pidlist,
				expmode : expmode,
				argtime : argtime,
				expnum : expnum,
				expernum : expernum
		    },    
		    type : 'post',      
		    dataType : 'json',
		    success : function(data){
		    	alert("提交成功");
		    },
		    error : function(data){
		    	alert("error");
		    }
		});
	}
	else if (op == 1){
		//var expdate = $("#expdate").val();
		var pidlist = $("#expTimeSpan").val();
		var expmode = $("#modeSelectInEdit").val();
		var argtime = $("#argtimeEdit").val();
		var expnum = $("#expSelector").val();
		var expernum = $("#argNum").val();
		var user = new userControl();
		var username = user.getUser();
		if(expdate == 0){
			alert("请选择实验日期");
			return;
		}
		data = getMyTimespan($('.target').first());
		$.ajax({
			url : "editARG.action",
		    data : {
		    	user : username,
				classinfo : classinfo,
				pidlist : pidlist,
				expmode : expmode,
				argtime : argtime,
				expnum : expnum,
				expernum : expernum,
				oldDate : data['date'],
				oldStartTime : data['startTp'],
				oldEndTime : data['endTp']
		    },    
		    type : 'post',      
		    dataType : 'json',
		    success : function(data){
		    	alert("提交成功");
		    },
		    error : function(data){
		    	alert("error");
		    }
		});
	}
}

function commitEditARG(){
	var classinfo = $('select').first().val();
	//var classinfo = 2;
	var b_commit = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'submitARG("+classinfo+",1)'>+确定</button>");
	var b_cancel = $("<button id = 'create_commit' class = 'btn button-new' onclick = 'cancelEditARG()'>x取消</button>");
	var oldTable = $('.table').first();
	var body_content = $("#content");
	body_content.empty();
	var h = $("<h></h>");
	h.html("排课管理>>编辑排课");
	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h.appendTo(body_content);
	
	var h2 = $("<h></h>");
	h2.html("正在修改的排课项目>>");
	h2.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h2.appendTo(body_content);
	
	oldTable.appendTo(body_content);
	var trList = $('tr');
	$.each(trList, function(index, value){
		if(!$(value).hasClass('addARG') && index != 0){
			$(value).css("display", "none");
		}
	});
	var target_tr = $('.addARG').first();
	target_tr.addClass('target');
	target_tr.removeClass('addARG');
	
	var h3 = $("<h></h>");
	h3.html("填写新的排课项目>>");
	h3.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	h3.appendTo(body_content);
	
	var table = $("<table id = 'createArgTable' class = 'table table-hover table-striped detail-table' ></table>");
	var tbody = $("<tbody></tbody>").appendTo(table);
	
	data = getMyTimespan(target_tr);
	var tr_expdate = $("<tr></tr>");
	var td_expdate = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验日期</th>").appendTo(tr_expdate);
	//$("<input type='text' class='form-control' placeholder='请选择实验日期' id='expdateEdit'>").appendTo(td_expdate);
	//createExpDateSelectorARG(classinfo, 1, td_expdate)
	td_expdate.html(data['date']);
	td_expdate.appendTo(tr_expdate);
	tr_expdate.appendTo(tbody);

	var tr_timespan = $("<tr></tr>");
	var td_timespan = $("<td id = 'td_timespan'></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验时段</th>").appendTo(tr_timespan);
	createMutiSelect_ARGtimelist(td_timespan, "expTimeSpan", null);
	td_timespan.appendTo(tr_timespan);
	tr_timespan.appendTo(tbody);
	
	var tr_select = $("<tr></t>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>排课模式</th>").appendTo(tr_select);
	var td_select = $('<td></td>');
	createModeSelectorARGInEdit(td_select);
	td_select.appendTo(tr_select);
	tr_select.appendTo(tbody);
	
	var tr2 = $("<tr id = 'tr_arrange'></tr>");
	var td2 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>预约时间</th>").appendTo(tr2);
	td2.appendTo(tr2);
	$("<input type='text' class='form-control' placeholder='请选择预约时间' id='argtimeEdit'>").appendTo(td2);
	tr2.appendTo(tbody);
	
	var tr_argNum = $("<tr id = 'tr_argNum'></tr>");
	var td_argNum = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>实验人数</th>").appendTo(tr_argNum);
	$("<input type='text' class='form-control' placeholder='请输入实验人数' id='argNum' >").appendTo(td_argNum);
	td_argNum.appendTo(tr_argNum);
	tr_argNum.appendTo(tbody);
	
	var tr3 = $("<tr></tr>");
	var td3 = $("<td></td>");
	$("<th class = 'col-lg-4 col-md-4 col-sm-4 col-xs-6'>课程实验</th>").appendTo(tr3);
	td3.appendTo(tr3);
	get_explist(classinfo, td3);
	tr3.appendTo(tbody);
	
	table.appendTo(body_content);
	b_cancel.appendTo(body_content);
	b_commit.appendTo(body_content);
	
	//$("#td_timespan").empty();
	//createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', null);
	showTimeSpanARG(classinfo, data['date'], 1, data);
	
	laydate.render({
		elem: '#expdateEdit',
		type: 'date',
		//min: 0,
		done : function(value, date, endDate){
			td_timespan.empty();
			createMutiSelect_ARGtimelist(td_timespan, 'expTimespan', "first_in");
			perriodOccupy(classinfo, value, 1, getMyTimespan(target_tr));
		}
	});
	laydate.render({
	   elem: '#argtimeEdit',
	   type: 'datetime',
	   range: true,
	   min: 0
	});
}