<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="maxAge=20000">
<title>实验平台</title>
<link href="res/css/flat-ui.css" rel="stylesheet">
<link href="./res/css/bootstrap.min.css" rel="stylesheet">
<link href="./res/css/base.css" rel="stylesheet">
<link rel="shortcut icon" href="./res/images/favicon.ico"
	type="image/x-icon">
<link href="./res/css/score.css" rel="stylesheet">
<link href="./res/css/exp.css" rel="stylesheet">
<link href="virnet/virnet.css" rel="stylesheet">
<link href="res/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	media="screen">
<link rel="stylesheet" href="res/css/font-awesome.min.css">
<link href="virnet/timeManagement.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="res/css/messenger.css">
<link rel="stylesheet" type="text/css"
	href="res/css/messenger-theme-future.css">
<script src="./res/js/jquery.js"></script>
<script src="./res/js/json2.js"></script>
<script src="./res/js/modernizr.custom.js"></script>
<script src="./res/js/support.js"></script>
<script src="./res/js/sockjs.min.js"></script>
<script src="./res/js/score.js"></script>
<!-- [if IE]<script> push_alert("Please use Chrome or other webkit kernel browsers");</script>-->
</head>
<body class="cbp-spmenu-push">

	<!-- 暂定实验号为1 应由UserManagement.jsp传过来 
int expId = 1; %>
<!-- 暂定实验角色为stu /管理员则为  GM 应由UserManagement.jsp传过来 
String expRole="'stu'";%>
//String expRole="'GM'";%>
<!-- 暂定实验实例号为999 应由UserManagement.jsp传过来 -->
	<!--  int expCaseId= 999;%> -->

	<%
	String index = session.getAttribute("index").toString();
 	String taskid = session.getAttribute("taskid").toString();
	String str = session.getAttribute("str").toString();
	String tasknum = session.getAttribute("tasknum").toString();
%>


	<!-- 连接websocket之后,加载页面前首先运行start,获取设备信息与任务信息 -->
<body onload="score(<%=index%>,'<%=str%>',<%=taskid%>,<%=tasknum%>)">

	<div id=username style="display: none"></div>

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
				<li class="active"><a class="btn" id="user-name" onclick="">2013202448</a></li>
				<li><a onclick="logout()"
					href="http://localhost:8080/virnet/log/login.jsp">Log out</a></li>
			</ul>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="scorehead">
			<div class="last"></div>
			<div class="scoretitle"></div>
			<div class="next"></div>

		</div>
		<div class="topo">
			<div class="anstopotitle">
				<h4>拓扑图答案</h4>
			</div>
			<div class="topotitle">
				<h4>学生拓扑图</h4>
			</div>
			<canvas width="850" ; height="550" id="newexpCanvas"
				style="cursor: default;"></canvas>
			<canvas width="850" ; height="550" id="ansCanvas"
				style="cursor: default;"></canvas>
			<div class="toposcore">
				<input type="text" class="inputscore" />
				<!-- <button class="btn button-new">提交</button> -->
			</div>
		</div>
		<div class="ping">
			<div class="anspingtitle">
				<h4>ping状态答案</h4>
			</div>
			<div class="pingtitle">
				<h4>学生ping状态</h4>
			</div>
			<div class="pingtable"></div>
			<div class="ansping"></div>
			<div class="pingscore">
				<input type="text" class="inputscore" />
				<!-- <button class="btn button-new">提交</button> -->
			</div>
		</div>
		<div class="configure">
			<div class="ansconfiguretitle">
				<h4>配置文件答案</h4>
			</div>
			<div class="configuretitle">
				<h4>学生配置文件</h4>
			</div>
			<div class="selectmachane"></div>
			<div class="configurefilearea"></div>
			<div class="ansselect"></div>
			<div class="ansconfigure"></div>
			<div class="configurescore">
				<input type="text" class="inputscore" />
				<button class="btn button-new"
					onclick="handin(<%=index%>,<%=taskid%>,'<%=str%>')">提交</button>
			</div>
		</div>

	</div>
	<script src="./res/js/jtopo-0.4.8-min.js"></script>
	<script src="./res/js/topoCore.js"></script>
	<script src="./res/js/classie.js"></script>
	<script type="text/javascript" src="res/js/messenger.min.js"></script>
	<script type="text/javascript" src="res/js/messenger-theme-future.js"></script>
</body>
</html>