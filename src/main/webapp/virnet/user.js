
function userControl(){
	var username = "";
	
	this.userInit = function(){
		//need change : user name, link of user name
		username = $.cookie("user_recent");
		
		$("#user-name").html(username);
		$("#user-name").attr("onclick", "");
	};
	
	this.getUser = function(){
		username = $("#user-name").html();
		
		return username;
	};
}