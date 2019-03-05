// JavaScript Document

$(document).ready(function(e) {
	logoutCheck();
	
	logFail();
	
	loginCheck();   
});

function logoutCheck(){
	logout = $.cookie("log");

	if(logout == "out"){
		$.cookie("remember", "false", {expires: 7, path: '/'});
	}
}

function logFail(){	
	if(state == 2){
		//登陆失败
		alert(statement);
	}
	else if(state == ""){
		
	}
	else if(state == 0){
		//没有用户
		$("#login-input").parent().parent().attr("class", "form-group has-error");
	}
	else if(state == 1){
		//密码错误
		$("#login-password").parent().parent().attr("class", "form-group has-error");
	}
	else if(state == 3){
		//没有对应的角色
		alert(statement);
	}
	else{
		//第一次登陆
		var remember;
		if($("input[name='remember']").prop('checked')){
			remember = "true";
		}
		else{
			remember = "false";
		}
		$.cookie("remember", remember, {expires: 7, path: '/'});
	}
	
	
}

function loginCheck(){
	remember = $.cookie("remember");//是否记住登录状态
	user = $.cookie("user_recent");//上一次登录用户
	password = $.cookie("password_recent");//对应密码
	
	//判断是否自动登录
	if(remember == "true"){//自动登录
		$("#login-input").val(user);
		$("#login-password").val(password);
		$("input[name='remember']").prop('checked', true);

		$("#login").submit();
		fail = "success";
		
	}
	else{//不自动登陆。填充账号
		$("#login-input").val(user);
		$("#login-password").val("");
		$("input[name='remember']").prop('checked', false);
	}
	
	$.cookie("log", "in", {expires: 7, path: '/'});
}

function login(user, password, remember_info){
	//获取登录表单内容
	user = $.trim($("#login-input").val());
	password = $.trim($("#login-password").val());
	remember_info = $("input[name='remember']").prop('checked');
	
	if(user != "" && password != ""){
		//存储cookie
		var remember;
		if(remember_info){
			remember = "true";
		}
		else{
			remember = "false";
		}
		
		$.cookie("remember", remember, {expires: 7, path: '/'});
		$.cookie("user_recent", user, {expires:7, path: '/'});
		$.cookie("password_recent", password, {expires:7, path: '/'});

		return true;
	}
	else{
		return false;
	}

}

function loginSuccess(){
	
}