<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="maxAge=20000">
<title>实验平台</title>
<link href="./res/css/bootstrap.min.css" rel="stylesheet">
<link href="./res/css/base.css" rel="stylesheet">
<link rel="shortcut icon" href="./res/images/favicon.ico"
	type="image/x-icon">
<link href="./res/css/exp.css" rel="stylesheet">
<script src="./res/js/jquery.js"></script>
<script src="./res/js/bootstrap.min.js"></script>
<script src="./res/js/json2.js"></script>
<script src="./res/js/modernizr.custom.js"></script>
<script src="./res/js/support.js"></script>
<script src="./res/js/sockjs.min.js"></script>
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
	Integer expId = Integer.parseInt(session.getAttribute("expId").toString());
	String expRole = "'" + session.getAttribute("expRole").toString() + "'";
	String workgroup = session.getAttribute("workgroup").toString();
	String username = session.getAttribute("username").toString();
%>


	<!-- 连接websocket之后,加载页面前首先运行start,获取设备信息与任务信息 -->
<body
	onload="start(<%=expId%>,<%=expRole%>,'<%=username%>',<%=workgroup%>)">

	<!-- loading图标，当程序加载的时候出现 -->
	<div id="cover" class="cover"></div>
	<div id="layout" class="layout">
		<div class="progress progress-striped active"
			style="height: 40px; padding: 5px; width: 100%">
			<div class="progress-bar progress-bar-info" id="loading"
				style="padding: 7px" role="progressbar" aria-valuenow="0"
				aria-valuemin="0" aria-valuemax="100" style="width: 0%;">

				<!-- progress-bar-success 绿色; progress-bar-info 蓝色; progress-bar-warning 橙色; progress-bar-danger 红色-->

			</div>
		</div>
	</div>

	<div id="waiting" class="waiting">等待其他组员点击开始实验</div>

	<div id="override"></div>
	<div id="alert"></div>
	<input id="btnshow000" class="btn btn-info" type="button" value="开始实验"
		onclick="showdiv()" style="display:none" />
	<input id="userName" type="hidden" value="<%=username%>">
	<%session.setAttribute("pageType","experiment");%>


	<div class="container-fluid">
		<div class="row">
			<div class="col-md-10">
				<div class="row-fluid">
					<h2 id="taskname"></h2>
					<div id="showExpUsers">
						<p id="showp">实验人员:</p>
						<p id="yourp"></p>
					</div>
				</div>
				<div class="row-fluid">
					<div class="col-md-6" id="canvasContainer" style="overflow: hidden">
						<canvas id="expCanvas" style="cursor: default;"></canvas>
						<div class="row">
							<div class="col-md-6">
								<div id="buttonGroup">
									<button class="btn btn-info opButton " onclick="topo.undo()">撤销</button>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="row">
							<div id="TabMain">
								<div class="col-md-1" style="padding-right: 0px;">
									<div class="tabItemContainer" id="tabItemContainer"></div>
								</div>
								<div class="col-md-10 full">
									<div id="tabBodyContainer">
										<div class="tabBodyItem tabBodyCurrent">
											<table id="tabobj">
												<tbody id="cmdtbody">
													<tr>
														<td><font id="fontsize1">All Rights Reserved
																(C) VIR_NETWORK.com<br>
														</font> <!-- 这里追加滚动显示内容-->
															<div id="parentDiv"></div>
															<div id="inputParent"></div></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="col-md-1 full">
									<div class="menuControlTab" id="menuControlTab"></div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6"></div>
							<div class="col-md-6">
								<div class="vote" id="vote">
									<!-- zyj -->
									<div class="row">
										<h2 id="inform"></h2>
									</div>
									<div class="row">
										<div class="col-md-4">
											<button class="btn btn-success" id="yes"
												style="display: none;">同意</button>
										</div>
										<div class="col-md-4">
											<button class="btn btn-danger" id="no" style="display: none;">拒绝</button>
										</div>
										<div class="col-md-4">
											<div class="time">
												<p id="timer" style="font-size: 36px;"></p>
											</div>
										</div>
									</div>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-2" id="chat">
				<div class="row">
					<div id="status">
						<div style="text-align: center; color: red" id="clock"></div>
						<!--<button  id = "release" class = "release" onclick="releaseEquipment()">释放资源</button> zyj-->
						<button id="next" class="release btn btn-info"></button>
						<!-- zyj -->
					</div>
				</div>

				<div class="row">
					<!-- 这里是输出结果面板-->
					<div id="historyMsg">
						<div id="emojiWrapper"></div>
						<div id="helpOptions" style="display: none;">
							<div class="row-fluid">
								<div class="col-md-4">
									<input id="option0" type="checkbox" />
								</div>
								<div class="col-md-8">
									<p>问题1</p>
								</div>
							</div>
							<div class="row-fluid">
								<div class="col-md-4">
									<input id="option1" type="checkbox" />
								</div>
								<div class="col-md-8">
									<p>问题2</p>
								</div>
							</div>
							<div class="row-fluid">
								<div class="col-md-4">
									<input id="option2" type="checkbox" />
								</div>
								<div class="col-md-8">
									<p>问题3</p>
								</div>
							</div>
							<div class="row-fluid">
								<div class="col-md-4"></div>
								<div class="col-md-8">
									<button id="sendHelpMsg" class="btn btn-info "
										onclick="sendHelpMsg();">发送</button>
								</div>
							</div>
						</div>
					</div>
					<!-- 这里是控制栏-->
				</div>
				<div class="row">
					<input id="colorStyle" class="btn" type="color" placeHolder='#000'
						title="font color" /> <input id="emoji" type="button"
						class="btn btn-info opButton" value="emoji" title="emoji" /> <input
						id="clearBtn" type="button" class="btn btn-info opButton"
						value="clear" title="clear screen" /> <input id="help"
						class="btn btn-info opButton" type="button" value="求助"
						title="help" onclick="help()" />
				</div>
				<div class="row">
					<textarea id="messageInput" placeHolder="enter to send"></textarea>
				</div>
				<div class="row">
					<button class="btn btn-info opButton" onclick="send()">SEND</button>
				</div>
				<!-- <button class="btn btn-info"  onclick="closeWebSocket()">CLOSE</button>  -->
			</div>
		</div>
	</div>
	<script src="./res/js/jtopo-0.4.8-min.js"></script>
	<script src="./res/js/topoCore.js"></script>
	<script src="./res/js/exp.js"></script>
	<script src="./res/js/classie.js"></script>
	<script src="./res/js/commuicate.js"></script>
</body>
</html>