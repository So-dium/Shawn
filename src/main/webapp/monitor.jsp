<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>虚拟网络实验室教师监控平台</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<!-- Loading Bootstrap 
<link
	href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet">-->
<link href="res/css/bootstrap.min.css" rel="stylesheet">

<!-- Loading Flat UI -->
<link href="res/css/flat-ui.css" rel="stylesheet">
<link rel="shortcut icon" href="res/img/favicon.ico">

<!-- Loading Messenger -->
<link rel="stylesheet" type="text/css" href="res/css/messenger.css">
<link rel="stylesheet" type="text/css"
	href="res/css/messenger-theme-future.css">

<link href="res/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	media="screen">
<link rel="stylesheet" href="res/css/font-awesome.min.css">
<link href="virnet/virnet.css" rel="stylesheet">
<link href="virnet/monitor.css" rel="stylesheet">
</head>
<%	
		session.setAttribute("pageType","monitor");
        session.setAttribute("expRole","monitor");
  		String username = session.getAttribute("username").toString();
	%>
<body>

	<div id=username style="display: none"><%=username%></div>

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
			<li class="active"><a class="btn" id="user-name" onclick=""></a></li>
		</ul>
	</div>
	</nav>

	<div class="container-fluid">
		<div class="row-fluid">

			<div class="col-lg-10 col-md-10 col-sm-12 col-xs-12" id="content">
				<h class="tittle col-lg-12 col-md-12 col-sm-12 col-xs-12">教师监控平台</h>
				<table style="margin: 70px 0 20 0;">
					<tr id="filter">
						<td><button class="btn button-new" onclick='filter();'>选择</button></td>
					</tr>
				</table>

				<table id="monitorInfo" class="table table-striped">
					<thead>
						<tr>
							<th>
								<p>课程</p>
							</th>
							<th>
								<p>班级</p>
							</th>
							<th>
								<p>小组成员</p>
							</th>
							<th>
								<p>所选实验</p>
							</th>
							<th>
								<p>机柜IP</p>
							</th>
							<th class="timeHead">
								<p>
									剩余时间 <img src="./img/sort.png" class="sort"
										onclick="sort('.timeHead');" alt="" />
								</p>
							</th>
							<th class="problem0Head">
								<p>
									问题1 <img src="./img/sort.png" class="sort"
										onclick="sort('.problem0Head');" alt="" />
								</p>
							</th>
							<th class="problem1Head">
								<p>
									问题2 <img src="./img/sort.png" class="sort"
										onclick="sort('.problem1Head');" alt="" />
								</p>
							</th>
							<th class="problem2Head">
								<p>
									问题3 <img src="./img/sort.png" class="sort"
										onclick="sort('.problem2Head');" alt="" />
								</p>
							</th>
							<th class="helpHead">
								<p>
									求助 <img src="./img/sort.png" class="sort"
										onclick="sort('.helpHead');" alt="" />
								</p>
							</th>
							<th>
								<p>监控</p>
							</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div style="position: fixed; width: 15%; right: 2%; bottom: 0;">
				<p style="margin: 0 0 2px; text-align: center">消息窗口</p>
				<div id="historyMsg"></div>
				<textarea id="broadcastMessage"
					onfocus="if(value=='输入消息'){value=''}"
					onblur="if (value ==''){value='输入消息'}">输入消息</textarea>
				<button class="btn button-new" style="bottom: 0"
					onclick='broadcast();'>发送</button>
				<button id="clear" style="display: none;" class="btn button-new"
					onclick='clear();'>清屏</button>
				<button id="emoji" class="btn button-new" value="emoji"
					title="emoji">表情</button>
			</div>
			<div id="emojiWrapper"></div>
		</div>
	</div>

	<script type="text/javascript"
		src="res/js/jquery-2.1.4.js"></script>
	<script type="text/javascript"
		src="res/js/jquery.min.js"></script>
	<script type="text/javascript"
		src="res/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="res/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="res/js/bootstrap-datetimepicker.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="res/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript" src="res/js/flat-ui.min.js"></script>
	<script type="text/javascript" src="res/js/messenger.min.js"></script>
	<script type="text/javascript" src="res/js/messenger-theme-future.js"></script>
	<script type="text/javascript" src="res/js/support.js"></script>
	<script type="text/javascript" src="virnet/history.js"></script>
	<script type="text/javascript" src="virnet/monitor.js"></script>
</body>
</html>




