<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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

<!-- Loading Bootstrap 
   	<link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">    -->
<link href="res/css/bootstrap.min.css" rel="stylesheet">

<!-- Loading Flat UI -->
<link href="res/css/flat-ui.css" rel="stylesheet">
<link rel="shortcut icon" href="res/img/favicon.ico">
<!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
<!--[if lt IE 9]>
      <script src="res/js/vendor/html5shiv.js"></script>
      <script src="res/js/vendor/respond.min.js"></script>
    <![endif]-->

<!-- Loading Messenger -->
<link rel="stylesheet" type="text/css" href="res/css/messenger.css">
<link rel="stylesheet" type="text/css"
	href="res/css/messenger-theme-future.css">

<link href="res/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	media="screen">
<link rel="stylesheet" href="res/css/font-awesome.min.css">
<link href="virnet/virnet.css" rel="stylesheet">
<link href="virnet/timeManagement.css" rel="stylesheet">
<link href="virnet/expArrangement.css" rel="stylesheet">
</head>

<body>
	<nav class="navbar navbar-inverse" role="navigation">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target="#navbar-collapse">
			<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand">Virnet</a>
	</div>
	<div class="collapse navbar-collapse" id="navbar-collapse">
		<ul class="nav navbar-nav navbar-right">
			<li class="active"><a class="btn" id="user-name">User Name</a></li>
			<li><a onclick="logout()" href="log/login.jsp">Log out</a></li>
		</ul>
	</div>
	</nav>

	<div class="container-fluid">
		<div class="row-fluid">
			<!--Side bar content-->
			<div class="col-md-2">
				<div class="sidebar-nav">
					<ul id="sidebar-list" class="nav nav-list nav-new">
						<s:iterator value="#session.powerlist">
							<li id=<s:property value="idname" />
								class=<s:property value="classname"/>
								onclick="sidebar_click(this)"><a class="btn"><s:property
										value="powerinfo" /></a></li>
						</s:iterator>
					</ul>
				</div>
			</div>

			<!--Body content-->
			<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12" id="content">

			</div>
		</div>
	</div>

	<!--<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.js"></script> -->
	<script type="text/javascript" src="virnet/laydate.js"></script>
	<script type="text/javascript" src="res/js/jquery-3.2.1.js"></script>
	<script type="text/javascript" src="virnet/page.js"></script>
	<script type="text/javascript" src="virnet/user.js"></script>
	<script type="text/javascript" src="virnet/virnet.js"></script>
	<script type="text/javascript" src="virnet/order.js"></script>
	<script type="text/javascript" src="virnet/groupArrange.js"></script>
	<script type="text/javascript" src="res/js/jquery.js"></script>
	<script type="text/javascript" src="res/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="res/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="res/js/bootstrap-datetimepicker.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="res/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript" src="virnet/timeManagement.js"></script>
	<script type="text/javascript" src="res/js/flat-ui.min.js"></script>
	<script type="text/javascript" src="res/js/messenger.min.js"></script>
	<script type="text/javascript" src="res/js/messenger-theme-future.js"></script>
	<script type="text/javascript" src="virnet/history.js"></script>
	<script type="text/javascript" src="virnet/expArrangement.js"></script>
</body>
</html>
