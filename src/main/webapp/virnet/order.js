function addOrderRecord(id,ClassId,ExpId,ExpArrangeId){
	var username = user.getUser();
	$.ajax({
		url:"addOrder.action",
	    data:{
			id : id,
			user : username,
			ClassId : ClassId,
			ExpId : ExpId,
			ExpArrangeId : ExpArrangeId
	    },    
	    type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	
	    	if(data["isSuccess"] != null && !data["isSuccess"]){
	    		alert("当前没有可预约的实验");
	    		return;
	    	}
	    	$.cookie("click_name", "");
	    	$.cookie("click_id", $(".sidebar-active").attr("id"));
	    	
	    	var body = $("#content");
	    	body.empty();
	    	console.log(data);
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
	    	var select = $("select");
	    	
	    	if(ClassId != -1)
	    		$(select[0]).select2("val",ClassId);
	    	if(ExpId != -1)
	    		$(select[1]).select2("val",ExpId);
	    	
	    	//给预约班级 预约实验 和  预约时段下拉框增加eventListener 当这些框的值改变时 更改其他下拉框的内容	    	
	    	$(select[0]).on("change",function(){
	    		
	    		var newClassId = $(select[0]).val();
	    		addOrderRecord(id,newClassId,-1,-1);
	    	});
	    	
	    	$(select[1]).on("change",function(){
	    		
	    		var newClassId = $(select[0]).val();
	    		var newExpId = $(select[1]).val();
	    		addOrderRecord(id,newClassId,newExpId,-1);
	    	});
	    	
	    	$(select[2]).on("change",function(){
	    		
	    		var newExpArrangeId = $(select[2]).val();
	    		addOrderRecord(id,ClassId,ExpId,newExpArrangeId);
	    		
	    	});
	    	
	    	$(select[3]).on("change",function(){

	    		var orderWay = $(select[3]).val();
	    		console.log("orderway:"+orderWay);
	    		//增加预约组员的输入栏
	    		if(orderWay == 1){
	    			
	    			var d_t = $(".table").first();
	    			
	    			for(var n=1;n<3;n++){//20180901蒋家盛修改预约组队人数为3人
	    				var d_t_row = $("<tr></tr>");
		    			d_t_row.attr("class", "memberControl");
		    			d_t_row.appendTo(d_t);
		    			
		    			var d_t_row_th = $("<th></th>");
		    			d_t_row_th.attr("class", "col-lg-4 col-md-4 col-sm-4 col-xs-6");
		    			d_t_row_th.appendTo(d_t_row);
		    			d_t_row_th.html("成员"+n);
		    			
		    			var d_t_row_td = $("<td></td>");
						d_t_row_td.appendTo(d_t_row);
		    				    			
		    			var input = $("<input></input>");
		    			input.attr("type", "text"); 
		    			input.attr("class", "form-control widthControl");
		    			input.attr("value","member"+n);
		    			
		    			input.appendTo(d_t_row_td);
		    			input.focus();
		    			input.val("");
	    			}
	    		}
	    		//
	    		if(orderWay == 0){
	    			member = $(".memberControl");
	    			for(var n=0 ; n < member.length ; n++){
	    				$(member[n]).remove();
	    			}
	    		}
	    	});
	    },
	    error:function(data){
	    	alert("error");
	    }
	});
}


function submitOrderDetail(){

	var username = user.getUser();
	var map = {};
	var check = true;
	//下拉框个数(页面上共有多少个可下拉列表)
	var length = $("select").length;
	console.log(length+"!!!");
	for(var i = 0; i < length; i++)
	{
		var select = $($("select")[i]).val();
		

//		var array = new Array();
		var l = 0;
		var content;
		//单选
		$.each($($("select")[i]).children("option"), function(i, val){
			if($(val).val() == select){
				content = $(val).val();
			}
		});

		map[$($("select")[i]).attr("value")] = content;
	}
	
	member = new Array();
	var duplicate = false;
	//输入框
	var inputs = $(".memberControl").find($("input"));
	inputs.each(function(index, value){
		if($(value).val() == ""){
			alert("成员不能为空");
			check = false;
		}
		else if($(value).val() == username){
			alert("不必输入本人学号");
			check = false;
		}
		for(var i=0;i<=member.length;i++){

			if($(value).val() == member[i]){
				duplicate = true;
				check = false;	
				break;
			}
		}
		member.push($(value).val());
		map[$(value).attr('value')] = $(value).val();
	});
	
	if(duplicate)
		alert("不能输入重复学生");

	console.log(map);
	if (check) {
		$.ajax({
			url : "submitOrder.action",
			data : {
				user : username,
				data : map,
			},
			type : 'post',
			dataType : 'json',
			success : function(data) {
				console.log(data);
				msg = Messenger().post({
					message : data["returnData"],
					showCloseButton : true
				});
				if(data["isSuccess"] == true){

					showContent("my-exp",0,data["className"]);
				}
					
			},
			error : function(data) {
				alert("error");
			}
		});
	}
}

//倒计时
function countDown(){
	
	$(".retailTime").each(function(index, value) {
		time = $(value).html().split(":");
		// 超过一个小时
		if (time.length == 3) {

			if (parseInt(time[2],10) > 0){
				if(parseInt(time[2],10) - 1 < 10)
					$(value).html(time[0] + ":" + time[1] + ":0" + (parseInt(time[2],10) - 1));
				else
					$(value).html(time[0] + ":" + time[1] + ":" + (parseInt(time[2],10) - 1));
			}	
			else if (parseInt(time[1],10) > 0){
				if(parseInt(time[1],10) - 1 < 10)
					$(value).html(time[0] + ":0" +( parseInt(time[1],10) - 1 )+ ":59");
				else
					$(value).html(time[0] + ":" +( parseInt(time[1],10) - 1 )+ ":59");
			}
			else if (parseInt(time[0],10) - 1 > 0 ){
				if(parseInt(time[0],10) - 1 < 10)
					$(value).html("0" + (parseInt(time[0],10) - 1) + ":59:59");
				else
					$(value).html((parseInt(time[0],10) - 1) + ":59:59");
			}
			else
				$(value).html("59:59");
		}
		// 一个小时之内
		else if (time.length == 2) {
			
			//倒计时间到
			if(parseInt(time[1],10) == 0 && parseInt(time[0],10) == 0)
				showContent("my-exp",0,$(".select").first().val());

			if (parseInt(time[1],10) > 0){
				if(parseInt(time[1],10) - 1 < 10)
					$(value).html(time[0] + ":0" + (parseInt(time[1],10) - 1));
				else
					$(value).html(time[0] + ":" + (parseInt(time[1],10) - 1));
			}	
			else if (parseInt(time[0],10) > 0 ){
				if(parseInt(time[0],10) - 1 < 10)
					$(value).html("0" + (parseInt(time[0],10) - 1) + ":59");
				else
					$(value).html((parseInt(time[0],10) - 1) + ":59");
			}
		}
//		// 一分钟之内
//		else if (time.length == 1) {
//			if (parseInt(time[0],10) > 0){
//				if(parseInt(time[0],10) - 1 < 10)
//					$(value).html("0" + parseInt(time[0],10) - 1);
//				else
//					$(value).html(parseInt(time[0],10) - 1);
//			}
//			else
//				$(value).html("00");
//		}
	});
}
function confirmThisOrder(user, orderId , confirmOrNot){
	$.ajax({
		url : "confirmOrder.action",
		data : {
			user : user,
			orderId : orderId,
			confirmOrNot : confirmOrNot
		},
		type : 'post',
		dataType : 'json',
		success : function(data){
			if(data["isSuccess"] == true){
				showContent("my-exp",0,$("select").first().val());
			}
		},
		eror : function(){
			console.log("error");
		}
	});
}

function deleteThisOrder(orderId){
	msg = Messenger().post({
		  message: "确定删除该预约？",
		  actions: {
		    retry: {
		      label: '确定',
		      phrase: 'Retrying TIME',
		      delay: 10,
		      action: function(){
		    	  $.ajax({
		    		  url:'deleteOrder.action',
		    		  data:{
		    			  orderId : orderId
		    		  },
		    		  type:'post',      
		    		  dataType:'json',    
		    		  success : function(data){
		    			if(data["isSuccess"] == true){
		    				showContent("my-exp",0,$("select").first().val());
		    			}
		    		  },
		    		  error : function(){
		    			console.log("error");
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