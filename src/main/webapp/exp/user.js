function userCheck(){
	user = $.cookie("user_recent");//登录用户
	$("#user-name").html(user);
}