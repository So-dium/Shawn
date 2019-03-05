function groupArrange(classId){

	var username = user.getUser();
	$.ajax({
		url : 'groupArrange.action',
		data : {
			user : username,
			id : "groupArrange",
			ClassId : classId
		},
		type : 'post',
		dataType : 'json',
		success : function(data){
			body_content = $("#content");
	    	body_content.empty();
	    	
	    	if(data == null){
	    		return;
	    	}
	    	
	    	h = $("<h></h>");
	    	h.html(data["tittle"]["data"]);
	    	h.attr("class", "tittle col-lg-12 col-md-12 col-sm-12 col-xs-12");
	    	h.appendTo(body_content);
	    	
	    	if(data["button_new"] != null){
		    	button_new = createButton(data["button_new"]);
		    	button_new.appendTo(body_content);
	    	}
	    	button_new.hide();
	    	
	    	if(data["select"] != null){    		
	    		//一般的选项应有选项标题，这里只需要下拉框，所以只用[1]
	    		createSingleselect(data["select"][1]["name"], data["select"][1]["value"], body_content);
	    	}
	    	
	    	var select = $("select").first();
	    	select.on("change",function(){
	    		groupArrange(select.val())
	    	});
	    	if(classId != 0)
	    		select.select2("val",classId);
	    	
	    	if(data["data"] != null){
//	    		console.log(data["data"]);
	    		table = createTable(data["data"]);
	    		table.appendTo(body_content);
	    	}
		},
		error : function(){
			
		}
	});
}
function choose(i){

	var content = $(i).html();
	var value = $(i).val();
	console.log(value);
	if(value == 0){
		$(i).removeClass("btn-fade");
		$(i).addClass("btn-new");
		$(i).val(1);
		$(i).html("已选");
	}
	else{
		$(i).addClass("btn-fade");
		$(i).removeClass("btn-new");
		$(i).val(0);
		$(i).html("未选");
	}
	var choose = $(".btn-new");
	if(choose.length == 3)//20180901蒋家盛修改预约组队人数为3人
		$(".button-new").first().show();
	else
		$(".button-new").first().hide();
}

function submitGroupArrange(){
	
	var groupInfo = "";
	var choose = $(".btn-new");
	for(var i=0;i<choose.length;i++){
		var row = choose.eq(i).parent().parent();
		var stuId = row.find(".stuId").first().html();
		groupInfo = groupInfo + stuId + "##";
	}
	console.log(groupInfo);
	
	var classId = $("select").first().val();
	console.log("班级："+classId);
	
	$.ajax({
		url:"submitGroupArrange.action",
		data:{
			stuId:groupInfo,
			classId:classId
		},
		type:"post",
		dataType:"json",
		success:function(data){
			msg = Messenger().post({
	    		message : data["data"],
	    		showCloseButton : true
	    	});
			groupArrange(classId);
		},
		error:function(){
			alert("error");
		}
	});
}








