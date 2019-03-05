/**
 * The course table plug in
 * 
 * What we need?
 * 
 * 1. operation, whether use drop and drag or something else
 * 2. select time
 * 3. show different course in different color
 * 4. a cell with two or more course
 * 5. show weeks --- may be tool tip is better
 * 
 */


(function ($) {
	var courseTable = new Array();
	
	var defaultVal = {
		TotalWeek : 17,
		single : 0,
		onEdit : false,
		row : 8,
		col : 7,
		arrange : true,
		detail : null,
		table : null
	};
	
	$.fn.CourseTable = function(data){
		//suppose that the table perform as what we want --- id : course-table td-id : courseTable-xx(row, column)
		defaultVal.father = $(this);
		
		defaultVal = $.extend(defaultVal, data);
		
		init();
		
		process();
		
		show($(this));
		
		if(defaultVal.arrange){			
			addDropable($(this));
		}
		
	};
	
	init = function(){
		var r = defaultVal.row;
		var c = defaultVal.col;
		
		for(var i = 0; i < r; i++){
			courseTable[i] = new Array();
			for(var j = 0; j < c; j++){
				courseTable[i][j] = new $.simpleClass();
			}
		}
	};
	
	var process = function(){
		courseTable.splice(0, courseTable.length);
		init();
		
		d = defaultVal.detail;
			
		$(d).each(function(index, cinfo){
			var classname = cinfo["class"];
			var classid = cinfo["classid"];
			var classlist = cinfo["timelist"];
			
			$(classlist).each(function(index, timelist){
				var weekNO = timelist["weekNO"];
				var time = timelist["timelist"];
				
				$(time).each(function(index, t){
					var ccol = parseInt(t) % 10;
					var crow = (parseInt(t) - ccol) / 10;
					
					courseTable[crow][ccol - 1].add({
						ClassName : classname,
						ClassID : classid,
						WeekNO : weekNO
					});
				});
				
			});
		});
		
		var r = defaultVal.row;
		var c = defaultVal.col;
		
		for(var j = 0; j < c; j++){
			var content = null;
			
			for(var i = 0; i < r; i++){
				if(courseTable[i][j].equal(content)){
					courseTable[i][j].setShow(false);
					var number = 1;
					for(var k = i; k >= 0; k--){
						if(!courseTable[k][j].getShow())
							number++;
						else
							break;
					}
					
					if(k >= 0){
						courseTable[k][j].setRowspan(number);
					}
				}
				else{
					content = courseTable[i][j].getContent();
				}
			}
		}
	};
	
	var show = function(table){
		table.find(".courseTable-selected").removeClass("courseTable-selected").addClass("courseTable-item");
		
		addDropable(defaultVal.father);
		
		var r = defaultVal.row;
		var c = defaultVal.col;
		
		for(var i = 0; i < r; i++){
			
			for(var j = 1; j <= c; j++){
				var td = table.find("#courseTable-" + i + j).first();
				
				if(courseTable[i][j - 1].getShow()){
					if(td.length == 0){//create a new node
						td = $("<td></td>");
						td.attr("id", "courseTable-" + i + j);
						td.attr("class", "courseTable-item");
						
						var judge = false;
						for(var k = j - 1; k > 0; k--){
							if(courseTable[i][k - 1].getShow()){
								td.insertAfter($("#courseTable-" + i + k));
								judge = true;
								break;
							}
						}
						if(!judge){
							$("#courseTableRow-" + i).children().first().after(td);
						}
						
						td.appendTo();
					}
					
					td.attr("rowspan", courseTable[i][j - 1].getRowspan());
					td.empty();
					
					add(i, j, td);
				}
				else{
					if(td.length > 0){
						td.remove();
					}
				}
			}
		}
		
		addDropable(defaultVal.father);
	};
	
	var add = function(r, c, fnode){		
		$(courseTable[r][c - 1].getContent()).each(function(index, data){
			newContent(data.id, data.name, fnode);
		});
	};
	
	var newContent = function(id, content, fnode){
		var a = $("<a></a>");
		a.html(content);
		a.attr("class", "btn btn-info");
		a.attr("value", id);
		a.appendTo(fnode);
		
		var i = $("<i></i>");
		i.attr("class", "icon-remove");
		i.attr("style", "display :  none");
		
		i.click(function(){			
			deleteArrange(parseInt($(this).parent().parent().attr("id").replace(/[^0-9]/ig,"")), $(this).parent().parent().attr("rowspan"), parseInt($(this).parent().attr("value")));
			
			process();
			
			show($("#course-table"));
		});
		i.appendTo(a);
		
		a.mouseover(function(){
			$(this).find("i").fadeIn("fast");
		});
		
		a.mouseout(function(){
			$(this).find("i").fadeOut("fast");
		});
		
		fnode.attr("class", "courseTable-selected");
	}
	
	var deleteArrange = function(id, rowspan, classid){		
		var col = id % 10;
		var row = (id - col) / 10;
		
		var timelist = new Array();
		for(var i = 0; i < rowspan; i++){
			var time = (row + i) + "" + col;
			timelist.push(time);
			
			
		}
		
		$(defaultVal.detail).each(function(index, data){
			if(data.classid == classid){
				var removelist = new Array();
				
				$(data.timelist).each(function(i, d){
					var t = d.timelist;
					var l_t = t.length;
					
					var need_remove = new Array();
					
					var judge = true;
					for(var j = 0; j < l_t; j++){
						if($.inArray(t[j], timelist) == -1){
							judge = false;
						}
						else{
							need_remove.push(j);
							
						}
					}
					
					if(judge){
						removelist.push(d);
					}
					else{
						var l_n = need_remove.length;
						
						for(var j = l_n - 1; j >= 0; j--){
							defaultVal.detail[index].timelist[i].timelist.splice(need_remove[j], 1)
						}
						
						
					}
					
				});
				
				$(removelist).each(function(i, d){
					defaultVal.detail[index].timelist.splice($.inArray(d, defaultVal.detail[index].timelist), 1);
				});
			}
		});
	};

	var addDropable = function(father){
		$(".courseTable-item").droppable({
			activeClass: "drop-active",
			hoverClass: "ui-state-hover",
			accept: "#class-container > a",
			drop: function( event, ui ){
				defaultVal.onEdit = true;
				
				var self = $(this);
				var e = $(ui.draggable);
				
				var judge = true;
				
				if(self.children().length != 0){
					judge = false;
				}
				
				if(judge){
					selectTime(self, e.text(), e.attr("id"), father);
					
				}
			}
	    });
	};
	
	var selectTime = function(e, content, classid, father){
		var id = parseInt($(e).attr("id").replace(/[^0-9]/ig,""));

		var col = id % 10;
		var row = (id - col) / 10;
		
		$(e).addClass("selectTime");
		
		
		$(e).click(function(){
			clicksave($(this), classid, content, father);
		});
		
		for(var i = 0; i < defaultVal.row; i ++){
			for(var j = 1; j <= defaultVal.col; j++){
				if(j > col || (j == col && i > row)){
					//add event listener
					
					if($("#courseTable-" + i + j).length == 0){
						continue;
					}
					
					$("#courseTable-" + i + j).hover(function(){
						var id = parseInt($(this).attr("id").replace(/[^0-9]/ig,""));

						var colx = id % 10;
						var rowx = (id - col) / 10;
						
						var isbreak = false;
						for(var y = 1; y <= defaultVal.col; y++){
							if(isbreak){
								break;
							}
							for(var x = 0; x < defaultVal.row; x ++){
								if((y > col || (y == col && x >= row)) && (y < colx || (y == colx && x <= rowx))){
									var thing = $("#courseTable-" + x + y)
									
									if(thing.length == 0){
										continue;
									}
									
									if(!(x == row && y == col) && thing.children().length != 0){
										isbreak = true;
										
										break;
									}
									var oldclass = thing.attr("class");
									thing.attr("class", oldclass + " selectTime");
									
								}
							}
						}
						
						if($(this) != e){
						
							$(this).mouseout(function(oldclass){
								for(var x = 0; x < defaultVal.row; x ++){
									for(var y = 1; y <= defaultVal.col; y++){
										if(x == row && y == col){
											
										}
										else{
											
											var thing = $("#courseTable-" + x + y);
											
											if(thing.length > 0){
												thing.removeClass("selectTime");
											}
										}
										
									}
								}
								
							});
						}
					});
					
					$("#courseTable-" + i + j).click(function(){
						clicksave($(this), classid, content, father);
					});
				}
			}
		}
	};
	
	var save = function(id, name){	
		var l = defaultVal.detail.length;
		
		var i = 0;
		
		for(i = 0; i < l; i++){
			if(defaultVal.detail[i].classid == id){
				break;
			}
		}

		if(i == l){
			var newclass = {
				classid : id,
				class : name,
				timelist : new Array()
			}
			
			defaultVal.detail.push(newclass);
		}
		
		var timelist = new Array();
		$(".selectTime").each(function(index, data){
			timelist.push($(data).attr("id").replace(/[^0-9]/ig,""));
		});
		
		if(defaultVal.single != 0){
			defaultVal.detail[i].timelist.push({
				weekNO : defaultVal.single,
				timelist : timelist
			})
		}
		else{
			for(var j = 0; j < defaultVal.TotalWeek; j++){
				defaultVal.detail[i].timelist.push({
					weekNO : j + 1,
					timelist : timelist
				})
			}
		}
		
		process();
	}

	var clicksave = function(e, classid, content, father){
		var id = parseInt(e.attr("id").replace(/[^0-9]/ig,""));

		var col = id % 10;
		var row = (id - col) / 10;
		
		save(classid, content);
		
		$(".selectTime").removeClass("selectTime");
		$("#course-table td").unbind("click");
		$("#course-table td").unbind("mouseenter").unbind("mouseleave");
		
		show(father);
		
		defaultVal.onEdit = false;
	}
})(jQuery); 

(function ($) {
    $.simpleClass = function (arg) {
    	var show = true;
    	var rowspan = 1;
    	var classlist = new Array();
    	
    	var has = function(array, e){
    		var l = $(array).length;
    		
    		for(var i = 0; i < l; i++){
    			if(array[i] == e){
    				return true;
    			}
    		}
    		
    		return false;
    	}
    	
    	this.equal = function(content){
    		var finaljudge = true;
    		
    		if(content == null || content.length != classlist.length){
    			return false;
    		}
    		
    		$(content).each(function(index, data){
    			var l = classlist.length;
    			var judge = false;
    			for(var i = 0; i < l; i++){
    				var judge1 = false;
    				
    				if(data.id == classlist[i].id && classlist[i].week.length == data.week.length){
    					//check week
    					
    					var s = data.week.length;
    					for(var j = 0; j < s; j++){
    						var sc = classlist[i].week.length;
    						var judge2 = false;
    						for(var k = 0; k < sc; k++){
    							if(classlist[i].week[k] == data.week[j]){
    								judge2 = true;
    							}
    						}
    						
    						judge1 = judge2;
    						if(!judge2){
    							break;
    						}
    						
    					}
    				}
    				
    				if(judge1){
    					judge = true;
    					break;
    				}
    			}
    			
    			finaljudge = finaljudge && judge;
    		});
    		
    		return finaljudge;
    	};
    	
    	this.add = function(classinfo){
    		var judge = true;
    		
    		$(classlist).each(function(index, c){
    			if(c.id == classinfo.ClassID){
    				if(!has(c.week, classinfo.WeekNO)){
    					c.week.push(classinfo.WeekNO);
    				}
    				judge = false;
    			}
    		});
    		
    		if(judge){
    			var newclass = {
    				id : classinfo.ClassID,
    				name : classinfo.ClassName,
    				week : new Array()
    			}
    			
    			newclass.week.push(classinfo.WeekNO);
    			
    			classlist.push(newclass);
    		}
    		
    	};
    	
    	this.setShow = function(s){
    		show = s;
    	}
    	
    	this.getShow = function(){
    		return show;
    	}
    	
    	this.getContent = function(){
    		if(classlist.length == 0){
    			return null;
    		}
    		else{
    			return classlist;
    		}
    	}
    	
    	this.setRowspan = function(row){
    		rowspan = row;
    	}
    	
    	this.getRowspan = function(){
    		return rowspan;
    	}
    };

})(jQuery);