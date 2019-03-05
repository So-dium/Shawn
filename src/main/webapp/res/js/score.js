function score(index,str,taskid,tasknum){
	var caseid = str.split(' ');
	var last = $(".last");
	last.empty();
	
	if(index>0){
		var butt2 = $("<button></button>");
		butt2.html("<li class='icon-arrow-left'></li>上一份作业");
		butt2.appendTo(last);
		butt2.attr("onclick","score("+(index-1)+","+"'"+str+"'"+","+"1"+","+tasknum+")");
	}
	
	if(taskid > 1){
		var butt1 = $("<button></button>");
		butt1.html("<li class='icon-arrow-left'></li>上一个任务");
		butt1.appendTo(last);
		console.log("score("+index+","+"'"+str+"'"+","+(taskid-1)+","+tasknum+")");
		butt1.attr("onclick","score("+index+","+"'"+str+"'"+","+(taskid-1)+","+tasknum+")");
	}
	
	var next = $(".next");
	next.empty();
	
	if(index<caseid.length-1){
		var butt3 = $("<button></button>");
		butt3.attr("class","btn button-new");
		butt3.html("下一份作业<li class='icon-arrow-right'></li>");
		butt3.appendTo(next);
		butt3.attr("onclick","score("+(index+1)+","+"'"+str+"'"+","+"1"+","+tasknum+")");
	}
	
	if(taskid<tasknum){
		var butt4 = $("<button></button>");
		butt4.attr("class","btn button-new");
		butt4.html("下一个任务<li class='icon-arrow-right'></li>");
		butt4.appendTo(next);
		console.log("score("+index+","+"'"+str+"'"+","+(taskid+1)+","+tasknum+")");
		butt4.attr("onclick","score("+index+","+"'"+str+"'"+","+(taskid+1)+","+tasknum+")");
	}
	var config = $(".configurefilearea");
	config.empty();
	console.log(caseid[index]);
	console.log(taskid);
	$.ajax({
		url:"loadScoreInfo.action",
		data:{
			caseid : caseid[index],
			taskid : taskid
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	if(data["connect"]!=""){
	    		var connect = data["connect"].split(",");
		    	var obj = {
		    			position : data["position"],
		    			leftNUM_Str : connect[0],
		    			rightNUM_Str : connect[1],
		    			leftport_Str : connect[2],
		    			rightport_Str : connect[3]
		    	};
		    	console.log("学生答案拓扑图："+connect[0]+' '+connect[1]+' '+connect[2]+' '+connect[3]);
		    	console.log(data["position"]);
		    	console.log(data["equipmentname"]);
		    	console.log(data["equipmentorder"]);
		    	console.log(data["equipmentport"]);
	    	}
	    	
	    	
	    	if(data["task"]!=null){
	    		var title = $(".scoretitle");
	    		title.empty();
	    		var p = $("<h></h>");
	    		p.attr("class","tittle");
	    		p.html(data["expname"]+">>"+data["task"]);
	    		p.appendTo(title);
	    	}
	    	if(data["devicetype"]!=null){
	    		var tab = $(".selectmachane");
	    		tab.empty();
	    		var pingtable = $(".pingtable");
	    		pingtable.empty();
	    		var i1=1,i2=1,i3=0;
	    		var i4 = new Array('a','b','c','d');
	    		for(var i = 0; i<data["devicetype"].length; i++){
	    			var button = $("<button></button>");
	    			if(data["devicetype"][i]==1){
	    				button.html("RT" + i1);
	    				i1++;
	    			}
	    				
	    			if(data["devicetype"][i]==2){
	    				button.html("SW" + i2);
	    				i2++;
	    			}
	    				
	    			if(data["devicetype"][i]==3){
	    				button.html("SW" + i2);
	    				i2++;
	    			}
	    				
	    			if(data["devicetype"][i]==4){
	    				button.html("PC" + i4[i3]);
	    				i3++;
	    				
	    			}
	    				
	    			button.attr("onclick","showconfigure("+ caseid[index] +","+ taskid +","+ data["deviceorder"][i] +")")
	    			button.appendTo(tab);
	    		}
	    		p_table = $("<table></table>");
	    		p_table.attr("class", "table table-striped table-bordered table-hover table-responsive course-table");
	    		p_table.attr("id", "ping-table");
	    		p_table.appendTo(pingtable);
	    		p_thead = $("<thead></thead>");
	    		p_thead.appendTo(p_table);
	    		
	    		p_tr = $("<tr></tr>");
	    		p_tr.appendTo(p_thead);
	    		p_thead_content = new Array();
	    		p_thead_content[0]="";
	    		for(var j = 1; j <= i3; j++){
	    			p_thead_content[j]="PC"+i4[j-1];
	    		}
	    		for(var i = 0; i <= i3; i++){
	    			p_th = $("<th></th>");
	    			p_th.html(p_thead_content[i]);
	    			if(i == 0){
	    				p_th.attr("class", "courseTable-th col-lg-2 col-md-2 col-sm-2 col-xs-2");
	    			}
	    			else{
	    				p_th.attr("class", "courseTable-th");
	    			}
	    			
	    			p_th.appendTo(p_tr);
	    		}
	    		
	    		p_tbody = $("<tbody></tbody>");
	    		p_tbody.appendTo(p_table);
	    		
	    		p_tbody_content = new Array();
	    		for(var j = 0; j < i3; j++){
	    			p_tbody_content[j]="PC"+i4[j];
	    		}
	    		for(var i = 0; i < i3; i++){
	    			p_tb_tr = $("<tr></tr>");
	    			p_tb_tr.appendTo(p_tbody);
	    			
	    			p_th = $("<th></th>");
	    			p_th.html(p_tbody_content[i]);
	    			p_th.attr("class", "courseTable-th");
	    			p_th.appendTo(p_tb_tr);
	    			
	    			for(var j = 0; j < i3; j++){
	    				p_td = $("<th></th>");
	    				p_td.appendTo(p_tb_tr);
	    				p_td.html();
	    				p_td.attr("id", "pingtable-" + i + j);
	    			}
	    		}
	    		
	    	}
	    	if(data["pingstate"]!=""){
	    		console.log("qwer");
	    		console.log(data["pingstate"]);
	    		
	    		var min = data["pingsourse"][0];  
    			var len = data["pingsourse"].length;  
    			for (var i = 1; i < len; i++){  
    				if (data["pingsourse"][i] < min){  
    					min = data["pingsourse"][i];  
    				}  
    			}   
    			console.log(min);
	    		for(var i = 0; i<data["pingstate"].length; i++){
	    			var id1 = data["pingsourse"][i]*1-min
	    			var id2 = data["pingdest"][i]*1-min;
	    			var block1 = $("#pingtable-"+id1+id2);
	    			var block2 = $("#pingtable-"+id2+id1);
	    			if(data["pingstate"][i]=='1'){
	    				block1.html("<li class='icon-ok'></li>");
	    				block2.html("<li class='icon-ok'></li>");	    				
	    			}

	    		}
	    	}
	    	
	    	if((data["equipmentorder"]!="")&&(data["equipmentname"]!="")&&(data["connect"]!="")){
	    		var mycanv = $("#newexpCanvas");
	    		mycanv.replaceWith("<canvas width='850' ; height='550' id='newexpCanvas' style='cursor: default;'></canvas>");
	    		var canvas = $("#newexpCanvas").get(0);
				topo.init(canvas,data["equipmentname"],data["equipmentorder"],data["equipmentport"]);
	    		//topo.init();
				topo.show();
				topo.clone(obj);
	    	}
	    	else{
	    		var mycanv = $("#newexpCanvas");
	    		mycanv.replaceWith("<canvas width='850' ; height='550' id='newexpCanvas' style='cursor: default;'></canvas>");
	    		var canvas = $("#newexpCanvas").get(0);
				topo.init(canvas,data["equipmentname"],data["equipmentorder"],data["equipmentport"]);
	    		//topo.init();
				topo.show();
	    	}
	    }
	});
	console.log(caseid[index]);
	console.log(taskid);
	$.ajax({
		url:"loadAnsScoreInfo.action",
		data:{
			caseid : caseid[index],
			taskid : taskid
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	if(data["connect"]!=""){
	    		var connect = data["connect"].split(",");
		    	var obj = {
		    			position : data["position"],
		    			leftNUM_Str : connect[0],
		    			rightNUM_Str : connect[1],
		    			leftport_Str : connect[2],
		    			rightport_Str : connect[3]
		    	};
		    	console.log("标准答案拓扑图："+connect[0]+' '+connect[1]+' '+connect[2]+' '+connect[3]);
		    	console.log(data["position"]);
		    	console.log(data["equipmentname"]);
		    	console.log(data["equipmentorder"]);
		    	console.log(data["equipmentport"]);
	    	}
	    	
	    	if(data["devicetype"]!=null){
	    		var tab = $(".ansselect");
	    		tab.empty();
	    		var pingtable = $(".ansping");
	    		pingtable.empty();
	    		var i1=1,i2=1,i3=0;
	    		var i4 = new Array('a','b','c','d');
	    		for(var i = 0; i<data["devicetype"].length; i++){
	    			var button = $("<button></button>");
	    			if(data["devicetype"][i]==1){
	    				button.html("RT" + i1);
	    				i1++;
	    			}
	    				
	    			if(data["devicetype"][i]==2){
	    				button.html("SW" + i2);
	    				i2++;
	    			}
	    				
	    			if(data["devicetype"][i]==3){
	    				button.html("SW" + i2);
	    				i2++;
	    			}
	    				
	    			if(data["devicetype"][i]==4){
	    				button.html("PC" + i4[i3]);
	    				i3++;
	    				
	    			}
	    				
	    			button.attr("onclick","showAnsconfigure("+ caseid[index] +","+ taskid +","+ data["deviceorder"][i] +")")
	    			button.appendTo(tab);
	    		}
	    		p_table = $("<table></table>");
	    		p_table.attr("class", "table table-striped table-bordered table-hover table-responsive course-table");
	    		p_table.attr("id", "ping-anstable");
	    		p_table.appendTo(pingtable);
	    		p_thead = $("<thead></thead>");
	    		p_thead.appendTo(p_table);
	    		
	    		p_tr = $("<tr></tr>");
	    		p_tr.appendTo(p_thead);
	    		p_thead_content = new Array();
	    		p_thead_content[0]="";
	    		for(var j = 1; j <= i3; j++){
	    			p_thead_content[j]="PC"+i4[j-1];
	    		}
	    		for(var i = 0; i <= i3; i++){
	    			p_th = $("<th></th>");
	    			p_th.html(p_thead_content[i]);
	    			if(i == 0){
	    				p_th.attr("class", "courseTable-th col-lg-2 col-md-2 col-sm-2 col-xs-2");
	    			}
	    			else{
	    				p_th.attr("class", "courseTable-th");
	    			}
	    			
	    			p_th.appendTo(p_tr);
	    		}
	    		
	    		p_tbody = $("<tbody></tbody>");
	    		p_tbody.appendTo(p_table);
	    		
	    		p_tbody_content = new Array();
	    		for(var j = 0; j < i3; j++){
	    			p_tbody_content[j]="PC"+i4[j];
	    		}
	    		for(var i = 0; i < i3; i++){
	    			p_tb_tr = $("<tr></tr>");
	    			p_tb_tr.appendTo(p_tbody);
	    			
	    			p_th = $("<th></th>");
	    			p_th.html(p_tbody_content[i]);
	    			p_th.attr("class", "courseTable-th");
	    			p_th.appendTo(p_tb_tr);
	    			
	    			for(var j = 0; j < i3; j++){
	    				p_td = $("<th></th>");
	    				p_td.appendTo(p_tb_tr);
	    				p_td.html();
	    				p_td.attr("id", "pinganstable-" + i + j);
	    			}
	    		}
	    		
	    	}
	    	if(data["pingstate"]!=""){
	    		console.log("qwer");
	    		var min = data["pingsourse"][0];  
    			var len = data["pingsourse"].length;  
    			for (var i = 1; i < len; i++){  
    				if (data["pingsourse"][i] < min){  
    					min = data["pingsourse"][i];  
    				}  
    			}   
    			console.log(min);
	    		for(var i = 0; i<data["pingstate"].length; i++){
	    			var id1 = data["pingsourse"][i]*1-min
	    			var id2 = data["pingdest"][i]*1-min;
	    			var block1 = $("#pinganstable-"+id1+id2);
	    			var block2 = $("#pinganstable-"+id2+id1);
	    			if(data["pingstate"][i]=='1'){
						block1.html("<li class='icon-ok'></li>");
						block2.html("<li class='icon-ok'></li>");
					}
	    			
	    		}
	    	}
	    	
	    	if((data["equipmentorder"]!="")&&(data["equipmentname"]!="")&&(data["connect"]!="")){
	    		var mycanv = $("#ansCanvas");
	    		mycanv.replaceWith("<canvas width='850' ; height='550' id='ansCanvas' style='cursor: default;'></canvas>");
	    		var canvas = $("#ansCanvas").get(0);
				topo.init(canvas,data["equipmentname"],data["equipmentorder"],data["equipmentport"]);
	    		//topo.init();
				topo.show();
				topo.clone(obj);
	    	}
	    	else{
	    		var mycanv = $("#ansCanvas");
	    		mycanv.replaceWith("<canvas width='850' ; height='550' id='ansCanvas' style='cursor: default;'></canvas>");
	    		var canvas = $("#ansCanvas").get(0);
				topo.init(canvas,data["equipmentname"],data["equipmentorder"],data["equipmentport"]);
	    		//topo.init();
				topo.show();
	    	}
	    }
	});
}

function showAnsconfigure(caseid,taskid,order){
	$.ajax({
		url:"showAnsconfigure.action",
		data:{
			caseid : caseid,
			taskid : taskid,
			order : order
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	console.log(data["configfile"][0]);
	    	if(data["configfile"]!=null){
	    		var config = $(".ansconfigure");
	    		config.empty();
	    		
	    		var pl = data["configfile"][0].split('\n');
	    		for(var i = 0;i < pl.length;i++){
	    			var p = $("<p></p>");
	    			p.html(pl[i]);
	    			p.appendTo(config);
	    			
	    		}
	    		//p.html(data["configfile"][0]);
	    		//p.appendTo(config);
	    	}
	    	else{
	    		alert("error");
	    	}
	    }
	});
}
function showconfigure(caseid,taskid,order){
	$.ajax({
		url:"showconfigure.action",
		data:{
			caseid : caseid,
			taskid : taskid,
			order : order
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	console.log(data["configfile"][0]);
	    	if(data["configfile"]!=null){
	    		var config = $(".configurefilearea");
	    		config.empty();
	    		
	    		var pl = data["configfile"][0].split('\n');
	    		for(var i = 0;i < pl.length;i++){
	    			var p = $("<p></p>");
	    			p.html(pl[i]);
	    			p.appendTo(config);
	    			
	    		}
	    		//p.html(data["configfile"][0]);
	    		//p.appendTo(config);
	    	}
	    	else{
	    		alert("error");
	    	}
	    }
	});
}
function handin(index,taskid,str){
	var caseid = str.split(' ');
	var taskid = taskid;
	var input = $(".inputscore");
	var toposcore = input.eq(0).val();
	var pingscore = input.eq(1).val();
	var configurescore = input.eq(2).val();
	$.ajax({
		url:"HandinScore.action",
		data:{
			caseid : caseid[index],
			taskid : taskid,
			toposcore : toposcore,
			pingscore : pingscore,
			configurescore : configurescore
		},
		type:'post',      
	    dataType:'json',    
	    success:function(data){
	    	if(data["value"]==1){
	    		console.log("1");
	    		msg = Messenger().post({
		    		message : "提交成功",
		    		showCloseButton : true
		    	});
	    	}
	    	else{
	    		msg = Messenger().post({
		    		message : "提交失败",
		    		showCloseButton : true
		    	});
	    	}
	    }
	});
}