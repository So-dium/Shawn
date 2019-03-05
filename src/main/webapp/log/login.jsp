<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>中国人民大学虚拟网络实验室</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="res/css/bootstrap.min.css" rel="stylesheet">
<link href="log/login.css" rel="stylesheet">
<script src="res/js/jquery-3.2.1.min.js"></script>
<script src="res/js/bootstrap.min.js"></script>
<script type="text/javascript" src="res/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="res/js/jquery.cookie.js"></script>
<script type="text/javascript" src="log/loginCheck.js"></script>

</head>

<body>
	<script type="text/javascript">
  	  	state="${session.state}";
  	  	statement="${session.statement}";
  	</script>

	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target="#example-navbar-collapse">
			<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="#">Virnet</a>
	</div>
	<div class="collapse navbar-collapse" id="example-navbar-collapse">
		<ul class="nav navbar-nav navbar-right">
			<!--          <li class="active nav-li"><a href="login.jsp">log in</a></li> -->
		</ul>
	</div>
	</nav>

	<div class="container container-new">
		<div class="row visible-on">
			<div class="col-xs-12 col-sm-12">
				<div class="row">
					<div
						class="visible-lg visible-md visible-sm col-sm-6 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
						<div id="myCarousel" class="carousel slide carousel-new">
							<!-- 轮播（Carousel）指标 -->
							<ol class="carousel-indicators">
								<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
								<li data-target="#myCarousel" data-slide-to="1"></li>
								<li data-target="#myCarousel" data-slide-to="2"></li>
							</ol>
							<!-- 轮播（Carousel）项目 -->
							<div class="carousel-inner">
								<div class="item active">
									<img src="img/1.jpg" class="img-responsive" alt="First slide">
								</div>
								<div class="item">
									<img src="img/6.jpg" class="img-responsive" alt="Second slide">
								</div>
								<div class="item">
									<img src="img/7.jpg" class="img-responsive" alt="Third slide">
								</div>
							</div>
							<!-- 轮播（Carousel）导航 -->
							<a class="carousel-control left" href="#myCarousel"
								data-slide="prev">&lsaquo;</a> <a class="carousel-control right"
								href="#myCarousel" data-slide="next">&rsaquo;</a>
						</div>
					</div>
					<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
						<form id="login" class="form-horizontal form-signin"
							onsubmit="return login()" action="login.action" method="post">
							<div class="form-group">
								<label class="col-xs-2 control-label">账户</label>
								<div class="col-xs-10">
									<input id="login-input" name="userId" type="text"
										class="form-control" placeholder="请输入账号" required="required">
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label">密码</label>
								<div class="col-xs-10">
									<input id="login-password" name="password" type="password"
										class="form-control" placeholder="请输入密码" required="required">
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-offset-2 col-xs-10">
									<div class="checkbox">
										<label> <input type="checkbox" name="remember">
											自动登录
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-offset-2 col-xs-10">
									<button type="submit" class="btn btn-default btn-new">登录</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
