function PageUtil(){
	var pageNO = 1;
	var pageCurrent = 1;
	
	this.createPageUtil = function(){
		if(pageNO == 1){
			//only one page, do nothing
			return null;
		}
		else{
			var div = $("<div></div>");
			div.attr("class", "pagination pagination-success");
			
			PageLeft().appendTo(div);
			
			ul = $("<ul></ul>");
									
			for(var i = 1; i <= pageNO; i++){
				PageContent(i).appendTo(ul);
			}
			ul.appendTo(div);
			
			PageRight().appendTo(div);
		}		
		return div;
	};
	
	this.getPageNO = function(){
		return pageNO;
	};
	
	this.setPageNO = function(number){
		pageNO = number >= 1? number : 1;
	};
	
	this.getPageCurrent = function(){
		return pageCurrent;
	};
	
	this.setPageCurrent = function(number){
		pageCurrent = number >= 1? number : 1;
	};
	
	function PageLeft(){
		var a = $("<a></a>");
		if(pageCurrent == 1){
			
		}
		else{
			a.attr("onclick", "fowardPage(this);");
		}
		a.attr("class", "btn btn-success previous");
		a.html("Previous");
		
		return a;
	}
	
	function PageRight(){
		var a = $("<a></a>");
		if(pageCurrent == pageNO){
			
		}
		else{
			a.attr("onclick", "backwardPage(this);");
		}
		a.attr("class", "btn btn-success next");
		a.html("Next");
		
		return a;
	}
	
	function PageContent(number){
		li = $("<li></li>");
		if(number == pageCurrent){
			//if it's current page, set active, and disabled on click event
			li.attr("class", "active");
		}
		else{
			//if it isn't current page, not active, and set on click event
			li.attr("onclick", "turnToPage(this);");
		}
				
		a = $("<a></a>");
		a.html(number);
		a.appendTo(li);
		
		return li;
	}	
}

function turnToPage(i){
	page = $(i).children().first().html();
	id = $(".sidebar-active").first().attr("id");
		
	showContent(id, page , $("select").val());
}

function fowardPage(i){
	page = $(i).siblings("ul").children(".active").children("a").html() - 1;
	id = $(".sidebar-active").first().attr("id");
	
	showContent(id, page , $("select").val());
}

function backwardPage(i){
	page = $(i).siblings("ul").children(".active").children("a").html() - 1 + 2;
	id = $(".sidebar-active").first().attr("id");
	
	showContent(id, page, $("select").val());
}
