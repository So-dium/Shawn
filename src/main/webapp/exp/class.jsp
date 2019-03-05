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

<link
	href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet">
<link href="exp/management.css" rel="stylesheet">
<script type="text/javascript"
	src="http://code.jquery.com/jquery-2.1.4.js"></script>
<script type="text/javascript"
	src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript"
	src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="exp/jquery.cookie.js"></script>
<script type="text/javascript" src="exp/class.js"></script>
<script type="text/javascript" src="exp/management.js"></script>
<script type="text/javascript" src="exp/user.js"></script>

</head>

<body>
	<nav class="navbar navbar-inverse" role="navigation">
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
			<li class="active"><a id="user-name" href="#">User Name</a></li>
			<li><a onclick="logout()" href="login.jsp">Log out</a></li>
			<li><a href="#">About Us</a></li>
		</ul>
	</div>
	</nav>

	<div class="container-fluid">
		<div class="row-fluid">
			<!--Sidebar content-->
			<div class="col-md-2">
				<div class="sidebar-nav">
					<ul class="nav nav-list nav-new">
						<li class="sidebar-nonactive" onclick="sidebar_click(this)"><a
							href="exp/course.jsp">课程管理</a></li>
						<li class="sidebar-active" onclick="sidebar_click(this)"><a
							href="exp/class.jsp">班级管理</a></li>
						<li class="sidebar-nonactive" onclick="sidebar_click(this)"><a
							href="exp/exp.jsp">实验管理</a></li>
						<li class="sidebar-nonactive" onclick="sidebar_click(this)"><a
							href="exp/student.jsp">学生管理</a></li>
						<li class="sidebar-nonactive" onclick="sidebar_click(this)"><a
							href="exp/teacher.jsp">教师管理</a></li>
					</ul>
				</div>
			</div>
			<div class="col-md-10" id="content">
				<!--Body content-->
				<h class="tittle">全部课程</h>

				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>编号</th>
								<th>课程名</th>
							</tr>
						</thead>
						<tbody id="tbody">
							<tr>
								<td>0</td>
								<td><button class="btn btn-link" onclick="Detail(this)">计算机网络
										1班</button></td>
								<td><button class="btn btn-link"
										onclick="Edit_Homepage(this);">编辑</button></td>
							</tr>
							<tr>
								<td>1</td>
								<td><button class="btn btn-link" onclick="Detail(this)">计算机网络
										2班</button></td>
								<td><button class="btn btn-link"
										onclick="Edit_Homepage(this);">编辑</button></td>
							</tr>
							<tr>
								<td>2</td>
								<td><button class="btn btn-link" onclick="Detail(this)">网络工程应用1班</button></td>
								<td><button class="btn btn-link"
										onclick="Edit_Homepage(this);">编辑</button></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div>
					<button class="btn btn-link" onclick="Add();">+ 添加班级</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="http://code.ciphertrick.com/demo/jquerysession/js/jquerysession.js"></script>

</body>
</html>
