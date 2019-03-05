var status = document.getElementById('status');
var color = document.getElementById('colorStyle');
var messageinput = document.getElementById('messageInput');
var sendbutton = document.getElementById('sendBtn');
var historyMessage = document.getElementById('historyMsg');
var clearScreen = document.getElementById('clearBtn');
var websocket = null;
var showingTabItem = 0; // 正在显示的cmd窗口编号
var EquipmentAmount = null;
var keyArr = [];
var name_str, num_str, port_str;
var PROGRESSTIME = 1000;
var autoProgressTimeout;
var messageJson = {
	"flag" : "experiment",
	"experimentStatus" : "default",
	"type" : "default",
	"cabinet_num" : "default",
	"color" : "default",
	"content" : "default",
	"user" : "default",
	"lock" : "unlock",
	"equipmentControler" : "default", // 设备控制者
	"equipmentNumber" : "default", // 设备数量
	"equipmentName" : "default", // 设备名串
	"inputEquipmentNumber" : "default", // 当前操作的设备号
	"equipmentNumStr" : "default", // 设备序号串
	"equipmentPortStr" : "default", // 设备序号串对应下的各设备可用端口号串
	"position" : "default", // 设备的位置 设备的X，Y坐标以空格相隔，设备间以逗号相隔
	"leftNUM_Str" : "default", // 左端设备序号串，“##”隔开
	"rightNUM_Str" : "default", // 右端设备序号串，“##”隔开
	"leftport_Str" : "default", // 左端设备端口序号串，“##”隔开
	"rightport_Str" : "default", // 右端设备端口序号串，“##”隔开
	"success" : "default",
	"expId" : "default", // 实验Id
	"expTaskOrder" : "default", // 当前操作的任务序号
	"expTaskNum" : "default", // 本实验的任务个数（包含初始）
	"expRole" : "default", // 实验角色 学生或管理员
	"websocketSession" : "default", // 标志目标客户端
	"isMyself" : "default",
	"TaskName" : "default",
	"expName" : "default",
};
function initWebSocket() {
	// 判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {

		websocket = new WebSocket("ws://" + host + root_path + "websck");

	} else {

		websocket = new SockJS("http://" + host + root_path + "sockjs/websck");

	}
}

this.initWebSocket();
this.initialEmoji();
var canvas = document.getElementById("expCanvas");
var tab = document.getElementById("tabBodyContainer");
// alert(canvasContainer.clientWidth);
canvas.width = canvasContainer.clientWidth * 0.95;
// alert(canvas.clientWidth);
canvas.height = ((canvasContainer.clientWidth * 0.95 * 650) / 850);
tab.style.height = ((canvasContainer.clientWidth * 0.95 * 650) / 850);
var parentDiv = document.getElementById("parentDiv");
var fontsize1 = document.getElementById("fontsize1");
parentDiv.style.height = tab.style.height - fontsize1.style.height;

// 连接发生错误的回调方法
websocket.onerror = function(event) {

};

// 连接成功建立的回调方法
websocket.onopen = function(event) {
	console.log("establish success");
}
function showStartBtn(){
	document.getElementById("btnshow000").style.display = "block";
}
// 接收到消息的回调方法
websocket.onmessage = function(event) {
	var object = JSON.parse(event.data);
	console.log(object)
	if (object.type == "error") {
		document.getElementById("btnshow000").style.display = "none";
		document.getElementById("alert").innerHTML = object.content;
		document.getElementById("alert").style.display = "block";
	}
	if (object.type == "connectestablish") {
		if (document.getElementById("btnshow000") != null ) {
			showStartBtn();
		}else{
			setTimeout(showStartBtn(), 250);	
		}
		
		
	}
	if (object.type == "requestEquipment") {
		console.log("ok");
		progress(0);
		var temp = object.equipmentName;
		messageJson.equipmentName = object.equipmentName;
		messageJson.equipmentNumber = object.equipmentNumber;
		messageJson.expTaskNum = object.expTaskNum;
		messageJson.expTaskOrder = object.expTaskOrder;
		messageJson.TaskName = object.TaskName;
		messageJson.expName = object.expName;
		EquipmentAmount = object.equipmentNumber;
		var keyValueArr = temp.split('##');
		for (var i = 0; i < keyValueArr.length; i++) {
			keyArr[i] = keyValueArr[i];
		}
		showCmdTab(object.equipmentNumber, keyArr);
		showHideCmdTab(object.equipmentNumber);
		topoAndConfigureButton();
		showNext();// zyj
		
	} else if (object.type == "timeRemain") {// zyj
		$("#clock").html(timeCal(object.content));
		countDown();
	} else if (object.type == "sendEquipment") {
		
		document.getElementById("override").style.display = "none";
		document.getElementById("alert").style.display = "none";
		
		// 给jtopo需要的数据赋值
		messageJson.name_str = object.equipmentName;
		messageJson.num_str = object.equipmentNumStr;
		messageJson.port_str = object.equipmentPortStr;
		messageJson.cabinet_num = object.cabinet_num;
		num_str = object.equipmentNumStr;
		port_str = object.equipmentPortStr;
		name_str = object.equipmentName;
		canvas = $("#expCanvas").get(0);
		topo.init(canvas, name_str, num_str, port_str);
		topo.show();
	}

	else if (object.type == "communication") {
		setMessageInnerHTML(event.data);
	} else if (object.type == "agreement")// zyj
	{
		setAgreementInnerHTML(event.data);
	} else if (object.type == "agree")// zyj
	{
		setAgreeInnerHTML(event.data);
	} else if (object.type == "refuse")// zyj
	{
		setRefuseInnerHTML(event.data);
	} else if (object.type == "alert")// zyj
	{
		alert(object.content);
	} else if (object.type == "openbutton") {
		$("#topoSave").attr("disabled", false);
		$("#configureSave").attr("disabled", false);
		$("#next").attr("disabled", false);
	} else if (object.type == "permit")// zyj
	{
		console.log(object.content);
		if (object.content == "agree exit"
				|| object.content == "agree noSaveToExit") {
			releaseEquipment();
		} else if (object.content == "goodbye") {
			self.location = 'http://202.112.113.133:8081/virnet/virnet/virnet.jsp';
		} else if (object.content == "agree next"
				|| object.content == "agree noSaveToNext") {
			messageJson.expTaskOrder = object.expTaskOrder;
			messageJson.TaskName = object.TaskName;
			messageJson.expName = object.expName;
			showNext();
		} else if (object.content == "agree savetopo") {
			var i = parseInt(messageJson.expTaskOrder);
			topo.submit(i);
		} else if (object.content == "agree saveconfigure") {
			if (messageJson.leftNum_Str == "default"
					|| messageJson.rightNum_Str == "default"
					|| messageJson.leftport_Str == "default"
					|| messageJson.leftport_Str == "default") {
				alert("请先提交拓扑再保存配置");
				return;
			} else {
				var i = parseInt(messageJson.expTaskOrder);
				saveConfigureFile(i);
			}
			// pingVerify(i);
		}
	} else if (object.type == "saveConfigureFile") {
		if (object.success == "plzRelease") {
				alert("请释放所有设备和PC之后再保存");
		}  else if (object.success == false){
			alert("保存失败");
		}
	} else if (object.type == "command") {
		setCommandInnerHTML(event.data);
	} else if (object.type == "lock") {
		console.log(object);
		changeButtonNum = object.inputEquipmentNumber;
		if (object.lock == "unlock") { // 由于释放与控制是异步操作，所以不能立即开放控制功能，要确保控制断开之后
			show_controller(object.equipmentControler,
					object.inputEquipmentNumber, object.lock);
			if (object.isMyself == true) {
				document.getElementById("cancelBtn" + changeButtonNum).disabled = true;
				$("#cancelBtn" + changeButtonNum).removeClass("btn-info");
				document.getElementById("dosform" + changeButtonNum).style.display = "none";
				document.getElementById("dosform" + changeButtonNum).disable = true;
			}
			
			/*
			 * 此处采用了buttonIndex的形参是为了传值，避免在函数执行之前changeButtonNum被篡改
			 * */
			setTimeout(function(buttonIndex) {
				enableButton(buttonIndex);
			}, 2000,changeButtonNum);
		}
		if (object.lock == "lock") { // 立即禁止其他组员控制本设备
			disableButton(changeButtonNum);
			show_controller(object.equipmentControler,
					object.inputEquipmentNumber, object.lock);
			if (object.isMyself == true) { // 本人有释放权限

				// 确保设备已控制再开放释放权限
				setTimeout(
						function(buttonIndex) {
							document
									.getElementById("dosform" + buttonIndex).style.display = "";
							document
									.getElementById("dosform" + buttonIndex).disable = false;
							document
									.getElementById("editBtn" + buttonIndex).name = "show";
							document.getElementById("cancelBtn"
									+ buttonIndex).disabled = false;
							$("#cancelBtn" + buttonIndex).addClass(
									"btn-info");
						}, 2000,changeButtonNum);
			}
		}
	} else if (object.type == "topolock") {
		if (object.lock == "unlock") {
			disableTopo();
		}
		if (object.lock == "lock") {
			enableTopo();
		}
	} else if (object.type == "topoedit") {
		if (object.success == true) {
			alert("提交成功");
		} else {
			alert("提交失败");
		}
	}

	else if (object.type == "equipConnectionInfo") {
		messageJson.position = object.position;
		messageJson.leftNUM_Str = object.leftNUM_Str;
		messageJson.rightNUM_Str = object.rightNUM_Str;
		messageJson.leftport_Str = object.leftport_Str;
		messageJson.rightport_Str = object.rightport_Str;
		topo.clone(object);
	} else if (object.type == "release") {
		if (object.success == true) {
			alert("释放成功");
			document.getElementById("release").disabled = true;
			document.getElementById("release").style.background = 'grey';
			closeWebSocket();
		} else {
			alert("释放失败");
		}
	} else if (object.type == "topoSaveToDatabase") {
		progress(0);
		if (object.success == true) {
			alert("保存成功");
		} else {
			alert("保存失败");
		}

	}
	// else if(object.type == "saveConfigureFile")
	// {
	//		  
	// if(object.success == true)
	// {
	// alert("保存成功");
	// }
	// else if(object.success == "plzRelease"){
	// alert("请释放所有设备和PC之后再保存");
	// }
	// else{alert("保存失败");}
	// progress(0);
	// }
	else if (object.type == "pingVerify") {
		if (object.success == true) {
			clearTimeout(autoProgressTimeout);
			progress(100);
			alert("验证完成");
		} else if (object.success == "plzRelease") {
			alert("请释放所有设备和PC之后再保存");
		} else {
			alert("验证被中断");
		}
		// 关闭加载页面
		progress(0);
	}

	else if (object.type == "progress") {

		console.log("object.content:" + object.content);
		if (object.replyType == "queue") {
			progress(object.content);
		} else {
			var progressContent = object.content.split("##");
			clearTimeout(autoProgressTimeout);
			autoProgress(progressContent[0], progressContent[1], PROGRESSTIME);
			if (object.replyType == "PingVerify") {
				var PCping = object.reply.split("##");
				$("#waiting")
						.html(
								"PC"
										+ String.fromCharCode(PCping[0]
												.charCodeAt() + 48)
										+ "正在和PC"
										+ String.fromCharCode(PCping[1]
												.charCodeAt() + 48)
										+ "进行ping检测");
			} else if (object.replyType == "ExperimentSave") {
				$("#waiting").html("正在保存设备配置");
			}
		}
	}
	// else if(object.type == "getOperationHistory"){
	// //复制操作命令历史给新的成员
	// var operationHistory = "";
	// for( i=0; i<parseInt(object.equipmentNumber); i++){
	// if( i != parseInt(object.equipmentNumber)-1 )
	// operationHistory = operationHistory + $("#showTab"+i).html() + "###";
	// else
	// operationHistory = operationHistory + $("#showTab"+i).html() ;
	//			  
	// console.log("showTab"+i+":"+$("#showTab"+i).html())
	// }
	// //显示控制设备图片信息 lrc
	// var showImageHistory = "";
	// for( i=1; i<=parseInt(object.equipmentNumber); i++){
	// var showImage = $('#showImage'+i);
	// if(showImage.css("display") != "none" ){
	// var imageURL = showImage.css("background");
	// showImageHistory = showImageHistory + i + showImage.css("top") + "###" +
	// imageURL + "###";
	// }
	//				  
	// }
	// //显示实验员 lrc
	// var showExpUsers = "";
	// showExpUsers = showExpUsers + object.user;
	// showExpUsers = $("#showp").html();
	//		  
	// messageJson.type = "getOperationHistory";
	// messageJson.content = operationHistory;
	// messageJson.more_content = showImageHistory + showExpUsers;
	// messageJson.websocketSession = object.websocketSession;
	// var mess = JSON.stringify(messageJson);
	// websocket.send(mess);
	// }
	else if (object.type == "loadOperationHistory") {
		// 复制操作命令历史给新的成员
		var history = object.content.split("###");
		for (i = 0; i < parseInt(object.equipmentNumber); i++) {
			temp = history[i];
			temp = temp.replace(/</g, "&lt;");
			temp = temp.replace(/>/g, "&gt;");
			var command = temp.replace(/\r\n/g, "<br/>");
			command = command.replace(/\n/g, "<br/>");
			var jtp = document.getElementById('showTab' + i);
			var msgToShow = document.createElement('font');
			msgToShow.style.color = '#FFFFFF';
			msgToShow.id = 'fontsize1';
			msgToShow.innerHTML = command;
			jtp.appendChild(msgToShow);
			cmdtbody = document.getElementById("cmdtbody");
			cmdtbody.scrollTop = cmdtbody.scrollHeight;
		}
		// //复制控制图片历史给新的成员 lrc
		// var showImageAndP = object.more_content.split('###');
		// for(i=0;i<showImageAndP.length-1;i=i+2){
		// var showImageNum = showImageAndP[i][0];
		// var elseString = "";
		// for(j=1;j<showImageAndP[i].length;j++){
		// elseString = elseString + showImageAndP[i][j];
		// }
		// var imageURL = showImageAndP[i+1];
		// $('#showImage'+showImageNum).css({
		// "background":imageURL,
		// "top":elseString,
		// "display":"block"
		// });
		// }
		// //显示实验人员 lrc
		// var userNum = object.user_num;
		// var showpHtml = showImageAndP[showImageAndP.length-1];
		// if(object.expRole == "last") {
		// showpHtml = showpHtml + userNum + "." + object.user;
		// messageJson.type = "addExpUser";
		// messageJson.user_num = userNum;
		// messageJson.user = object.user;
		// messageJson.websocketSession = object.websocketSession;
		// var mess = JSON.stringify(messageJson);
		// websocket.send(mess);
		// }
		// $("#showp").html(showpHtml);
		// userNum = "你是第" + userNum + "号实验员";
		// var p = $("<p></p>");
		// p.html(userNum);
		// p.appendTo($("#showExpUsers"));
	} else if (object.type == "ready") {
		messageJson.experimentStatus = "start";
		messageJson.type = "";
		messageJson.flag = "experiment";
		var mess = JSON.stringify(messageJson);
		websocket.send(mess);
		console.log("send");
		messageJson.experimentStatus = "";
	} else if (object.type == 'getAllUsers') {
		var showp = $("#showp");
		var yourp = $("#yourp");
		var alluser = object.content;
		showp.html(showp.html() + alluser);
		var yourUserNum = object.user;
		yourp.html('你是第' + yourUserNum + '号实验员');
	} else if (object.type == 'addExpUsers') {
		var addUserName = object.content;
		var showp = $("#showp");
		var num = parseInt($("#yourp").html().replace(/[^0-9]/ig, "")) + 1;
		var str = '' + num + '.' + addUserName + ';';
		showp.html(showp.html() + str);
	}
}
function show_controller(equipmentControler, inputEquipmentNumber, lock) {
	var controller = equipmentControler;
	var imageNum = inputEquipmentNumber;
	var aBlockHeight = $('#tabItema0').outerHeight();
	// imageNum = parseInt(imageNum) + 1;
	var imageDiv = $('#showImage' + imageNum);
	if (lock == "lock") {
		// 改背景图片
		var imageURL = "./res/images/controller/" + controller + ".png";
		imageDiv.css("background", "url(" + imageURL + ")");
		imageDiv.css("top", (aBlockHeight * inputEquipmentNumber + 3) + "px");
		imageDiv.css("display", "inline");
	} else if (lock == "unlock") {
		imageDiv.css("display", "none");
	}
}
// 连接关闭的回调方法
var clocknum;
websocket.onclose = function() {
	if (clocknum != null)
		clearInterval(clocknum);
	console.log("已断开连接");
}

// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	alert("websocket close!");
	websocket.close();
}

// 关闭连接
function closeWebSocket() {
	websocket.close();
}
function start(expId, expRole, username, workgroup) {
	messageJson.expId = expId;
	messageJson.expRole = expRole;
	messageJson.user = document.getElementById('userName').value;
	console.log("expId" + expId);
	console.log("expRole" + expRole);
	console.log("username" + username);
	console.log("workgroup" + workgroup);
}

// 发送消息
function send() {
	var message = messageinput.value;
	messageinput.value = "";
	messageInput.focus();
	messageJson.color = color.value;
	messageJson.content = message;
	messageJson.type = "communication";
	messageJson.user = document.getElementById('userName').value;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}
// 编辑锁定
function editLock() {
	messageJson.lock = "lock";
	messageJson.type = "lock";
	messageJson.inputEquipmentNumber = showingTabItem;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}
// 解除锁定
function releaseLock() {
	messageJson.lock = "unlock";
	messageJson.type = "lock";
	messageJson.inputEquipmentNumber = showingTabItem;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function editTopoLock() {
	// 为编辑拓扑取锁
	messageJson.lock = "lock";
	messageJson.type = "topolock";
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

/**
 * 
 */
function releaseTopoLock() {
	// 为编辑拓扑解锁
	messageJson.lock = "unlock";
	messageJson.type = "topolock";
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function disableButton(number) {
	document.getElementById("editBtn" + number).disabled = true;
	$("#editBtn" + number).removeClass("btn-info");
	document.getElementById("dosform" + number).disable = true;
}
function enableButton(number) {
	document.getElementById("editBtn" + number).disabled = false;
	document.getElementById("cancelBtn" + number).disabled = true;
	$("#cancelBtn" + number).removeClass("btn-info");
	$("#editBtn" + number).addClass("btn-info");
	document.getElementById("dosform" + number).disable = true;
}
function enableTopo() {
	$(".canvasButtonGroup button").each(function(index, e) {
		// 注释掉是因为第一个解锁按钮没了
		// if (index == 0) {
		// e.disabled = true;
		// e.style.background = 'grey';
		// }
		// else{
		e.disabled = false;
		e.style.background = '#3bb3e0';
		// }
	});
}

function disableTopo() {
	$(".canvasButtonGroup button").each(function(index, e) {
		if (index == 0) {
			e.disabled = false;
			e.style.background = '#3bb3e0';
		} else {
			e.disabled = true;
			e.style.background = 'grey';
		}
	});
}

// 清屏
clearScreen.addEventListener('click', function() {
	document.getElementById('historyMsg').innerHTML = '';
}, false);

// 表情控制项
document.getElementById('emoji').addEventListener('click', function(e) {
	var emojiwrapper = document.getElementById('emojiWrapper');
	emojiwrapper.style.display = 'block';
	e.stopPropagation();
}, false);

document.body.addEventListener('click', function(e) {
	var elem = e.target;
	var emojiwrapper = document.getElementById('emojiWrapper');
	var helpOptions = document.getElementById('helpOptions');
	if (elem != emojiwrapper) {
		emojiwrapper.style.display = 'none';
	}
	;
});

document.getElementById('emojiWrapper').addEventListener(
		'click',
		function(e) {
			var target = e.target;
			if (target.nodeName.toLowerCase() == 'img') {
				var messageInput = document.getElementById('messageInput');
				messageInput.focus();
				messageInput.value = messageInput.value + '[emoji:'
						+ target.title + ']';
			}
			;
		}, false);

function emojiWrapper() {
	var emojiWrapper = document.getElementById('emojiWrapper');
	if (emojiWrapper.style.display == 'none') {
		emojiWrapper.style.display = '';
		$("#emoji").css("box-shadow", "0px 0px 14px 4px #2534f6 outset");
	} else {
		$("#emoji").css("box-shadow", "");
		emojiWrapper.style.display = 'none';
	}
}

// 显示表情的函数
function initialEmoji() {
	var emojiContainer = document.getElementById('emojiWrapper'), docFragment = document
			.createDocumentFragment();
	for (var i = 1; i <= 60; i++) {
		var emojiItem = document.createElement('img');
		emojiItem.src = 'emotions/emoji/' + i + '.png';
		emojiItem.title = i;
		docFragment.appendChild(emojiItem);
	}
	;
	emojiContainer.appendChild(docFragment);
}
function showEmoji(msg) {
	var match, result = msg, reg = /\[emoji:\d+\]/g, emojiIndex, totalEmojiNum = document
			.getElementById('emojiWrapper').children.length;
	while (match = reg.exec(msg)) {
		emojiIndex = match[0].slice(7, -1);
		if (emojiIndex > totalEmojiNum) {
			result = result.replace(match[0], '[X]');
		} else {
			result = result.replace(match[0],
					'<img class="emoji" src="emotions/emoji/' + emojiIndex
							+ '.png" />');// todo:fix this in chrome it will
			// cause a new request for the image
		}
		;
	}
	;
	return result;
}

// 聊天消息显示窗口
function setMessageInnerHTML(msg) {
	var msgToDisplay = document.createElement('p'), date = new Date()
			.toTimeString().substr(0, 5);
	var obj = JSON.parse(msg);
	msgToDisplay.style.color = obj.color || '#000';
	obj.content = this.showEmoji(obj.content);
	msgToDisplay.id = 'fontsize1';
	msgToDisplay.innerHTML = obj.user
			+ '<span class="timespan" style="color: red;">(' + date
			+ '): </span>' + obj.content;
	historyMessage.appendChild(msgToDisplay);
	historyMessage.scrollTop = historyMessage.scrollHeight;
}

var num;
function setAgreementInnerHTML(msg) {// zyj
	$("#topoSave").attr("disabled", true);
	$("#configureSave").attr("disabled", true);
	$("#next").attr("disabled", true);
	var obj = JSON.parse(msg);
	var inform = $("#inform");
	var s = document.getElementById("timer");
	var yes = $("#yes");
	var no = $("#no");
	if (obj.content == "savetopo")
		inform.html(obj.user + "发起了提交拓扑");
	else if (obj.content == "saveconfigure")
		inform.html(obj.user + "发起了保存配置");
	else if (obj.content == "next")
		inform.html(obj.user + "发起了下一个任务");
	else if (obj.content == "exit")
		inform.html(obj.user + "发起了退出请求");
	else if (obj.content == "noSaveToNext")
		inform.html(obj.user + "发起了下一个任务,没有保存");
	else if (obj.content == "noSaveToExit")
		inform.html(obj.user + "发起了退出请求，没有保存");
	else {
	}
	yes.css("display", "block");
	no.css("display", "block");
	s.innerHTML = 10;
	num = window.setInterval(timer, 1000, obj.content);
	yes.attr("onclick", "agree('" + obj.content + "')");
	no.attr("onclick", "refuse('" + obj.content + "')");
}

function agree(operation) {// zyj
	clearVote();
	var message = operation;
	messageJson.content = "agree " + message;
	messageJson.type = "agree";
	messageJson.user = document.getElementById('userName').value;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function refuse(operation) {// zyj
	clearVote();
	var message = operation;
	messageJson.content = "refuse " + message;
	messageJson.type = "refuse";
	messageJson.user = document.getElementById('userName').value;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function timer(operation) {// zyj
	var s = document.getElementById("timer");
	if (s.innerHTML == "") {
		return;
	}
	if (s.innerHTML == 0) {
		refuse(operation);
	} else
		s.innerHTML = s.innerHTML * 1 - 1;
}
function setAgreeInnerHTML(msg) {// zyj
	setMessageInnerHTML(msg);
}
function setRefuseInnerHTML(msg) {// zyj
	$("#topoSave").attr("disabled", false);
	$("#configureSave").attr("disabled", false);
	$("#next").attr("disabled", false);
	clearVote();
	setMessageInnerHTML(msg);
}
function clearVote() {// zyj
	var inform = $("#inform");
	var s = document.getElementById("timer");
	var yes = $("#yes");
	var no = $("#no");
	inform.html("");
	yes.css("display", "none");
	no.css("display", "none");
	s.innerHTML = "";
	if (num != null)
		clearInterval(num);
}
// 控制台信息显示窗口
function setCommandInnerHTML(msg) {

	var jsonCommand = JSON.parse(msg);
	var temp = jsonCommand.content;
	var equipNumber = jsonCommand.inputEquipmentNumber;
	temp = temp.replace("<", "&lt;");
	temp = temp.replace(">", "&gt;");
	var command = temp.replace(/\r\n/g, "<br/>");
	command = command.replace(/\n/g, "<br/>");
	var jtp = document.getElementById('showTab' + equipNumber);
	var msgToShow = document.createElement('font');
	msgToShow.style.color = '#FFFFFF';
	msgToShow.id = 'fontsize1';
	msgToShow.innerHTML = command;
	jtp.appendChild(msgToShow);
	// $("#cmdtbody").scrollTop(9999);
	tabBodyContainer = document.getElementById("tabBodyContainer");
	// alert(tabBodyContainer.scrollTop + " " + tabBodyContainer.scrollHeight );
	tabBodyContainer.scrollTop = tabBodyContainer.scrollHeight;
	// alert(tabBodyContainer.scrollTop + " " + tabBodyContainer.scrollHeight );
}

// 控制台输入窗口
function cmdSend() {
	var cmdMessageInput = document.getElementById('dosform' + showingTabItem);
	var message = cmdMessageInput.value;
	cmdMessageInput.value = "";
	cmdMessageInput.focus();
	messageJson.content = message;
	messageJson.type = "command";
	messageJson.inputEquipmentNumber = showingTabItem;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}
function releaseEquipment() {
	messageJson.type = "release";
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

// 将拓扑连接信息发送到一层NTC
function topoSend(position, leftNUM_Str, rightNUM_Str, leftport_Str,
		rightport_Str) {
	messageJson.position = position;
	messageJson.leftNUM_Str = leftNUM_Str;
	messageJson.rightNUM_Str = rightNUM_Str;
	messageJson.leftport_Str = leftport_Str;
	messageJson.rightport_Str = rightport_Str;
	messageJson.type = "topoedit";
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

// 保存拓扑信息到数据库
function topoSaveToDatabase(position, leftNUM_Str, rightNUM_Str, leftport_Str,
		rightport_Str, expTaskOrder) {
	messageJson.position = position;
	messageJson.leftNUM_Str = leftNUM_Str;
	messageJson.rightNUM_Str = rightNUM_Str;
	messageJson.leftport_Str = leftport_Str;
	messageJson.rightport_Str = rightport_Str;
	messageJson.position = position;
	messageJson.expTaskOrder = expTaskOrder;
	messageJson.type = "topoSaveToDatabase";
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

// 遮罩层控制函数
function showdiv() {
	document.getElementById("btnshow000").style.display = "none";
	messageJson.flag = "arrange";
	messageJson.arrangeStatus = "true";
	var mess = JSON.stringify(messageJson);

	if (websocket.readyState == 1) {
		websocket.send(mess);
		messageJson.experimentStatus = "";
	} else {
		console.log("wait for websocket establish");
		setTimeout(showdiv(), 250);
	}

}
// 这里是动态生成cmd窗口的逻辑
function showCmdTab(number, name_Str) {
	var parent = document.getElementById("parentDiv");
	var liParent = document.getElementById("tabItemContainer");
	var inputParent = document.getElementById("inputParent");
	var buttonParent = document.getElementById("menuControlTab");
	var SW_name = 0;
	var RT_name = 0;
	var PC_name = 0;
	var PC_count = new Array('a', 'b', 'c', 'd');

	for (var i = 0; i < number; i++) {
		var newli = document.createElement('li');
		var newa = document.createElement('a');
		// lrc start
		var wrapperDiv = document.createElement('div');
		var newImage = document.createElement('div');
		newImage.setAttribute("id", "showImage" + i);
		newImage.setAttribute("class", 'showControlImage');
		liParent.appendChild(newImage);
		// lrc end
		if (name_Str[i] == "SW3" || name_Str[i] == "SW2") {
			SW_name++;
			newa.innerHTML = "SW" + SW_name;
		} else if (name_Str[i] == "RT") {
			RT_name++;
			newa.innerHTML = "RT" + RT_name;
		} else if (name_Str[i] == "PC") {
			PC_name++;
			newa.innerHTML = "PC" + PC_count[PC_name - 1];
		}
		newa.setAttribute("id", "tabItema" + i);
		newa.setAttribute("value", i);
		if (i == 0) {
			newa.setAttribute("class", "tabItemCurrent");
		}
		newli.appendChild(newa);
		liParent.appendChild(newli);

		var newDiv = document.createElement('div');
		newDiv.setAttribute("id", "showTab" + i);
		newDiv.setAttribute("class", "showTab");
		newDiv.style.display = 'none';
		if (i == 0) {
			newDiv.style.display = '';
		}
		parent.appendChild(newDiv);

		// input框自动生成
		var newInput = document.createElement('input');
		newInput.setAttribute("type", "text");
		newInput.setAttribute("id", "dosform" + i);
		newInput.setAttribute("class", "dosform");
		newInput.setAttribute("autocomplete", "off");
		newInput.setAttribute("name", "input");
		newInput.setAttribute("size", "64");
		newInput.setAttribute("maxlength", "100");
		newInput.setAttribute("onkeypress",
				"if (event.keyCode == 13){cmdSend();}");
		newInput.style.display = 'none';
		inputParent.appendChild(newInput);

		// 编辑、撤销按钮动态生成
		var newEditButton = document.createElement('button');
		newEditButton.setAttribute("id", "editBtn" + i);
		newEditButton.setAttribute("class", "btn btn-info");
		newEditButton.setAttribute("onclick", "editLock()");
		newEditButton.innerHTML = "编辑";
		newEditButton.style.display = 'none';
		if (i == 0) {
			newEditButton.style.display = '';
		}

		var newReleaseButton = document.createElement('button');
		newReleaseButton.setAttribute("id", "cancelBtn" + i);
		newReleaseButton.setAttribute("class", "btn");
		newReleaseButton.setAttribute("onclick", "releaseLock()");
		newReleaseButton.setAttribute("disabled", "true");
		newReleaseButton.innerHTML = "释放";
		newReleaseButton.style.display = 'none';
		if (i == 0) {
			newReleaseButton.style.display = '';
		}

		buttonParent.appendChild(newEditButton);
		buttonParent.appendChild(newReleaseButton);
	}
}
// 显示或隐藏cmd窗口的逻辑
function showHideCmdTab(order) {
	for (var i = 0; i < order; i++) {
		document.getElementById('tabItema' + i).onclick = function(e) {
			var target = e.target;
			var id = target.id;
			var cc = id.substring(id.length - 1, id.length);
			for (var j = 0; j < order; j++) {
				if (j == cc) {
					document.getElementById('showTab' + j).style.display = '';
					if (document.getElementById('editBtn' + j).name == "show") {
						document.getElementById('dosform' + j).style.display = '';
					}
					document.getElementById('editBtn' + j).style.display = '';
					document.getElementById('cancelBtn' + j).style.display = '';
					document.getElementById('tabItema' + j).className = 'tabItemCurrent';
					showingTabItem = cc;
				} else {
					document.getElementById('showTab' + j).style.display = 'none';
					document.getElementById('dosform' + j).style.display = 'none';
					document.getElementById('editBtn' + j).style.display = 'none';
					document.getElementById('cancelBtn' + j).style.display = 'none';
					document.getElementById('tabItema' + j).className = '';
				}
			}
		}
	}
}
// function pingVerify(taskOrder){
//	
//	
// setTimeout(function(){
// messageJson.type = "pingVerify";
// messageJson.expTaskOrder = taskOrder;
// var mess = JSON.stringify(messageJson);
// websocket.send(mess);
// } , 1000);
// }

// 按照任务号保存配置文件
function saveConfigureFile(taskOrder) {

	messageJson.type = "saveConfigureFile";
	messageJson.expTaskOrder = taskOrder;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

// 动态生成拓扑及配置操作按钮（按任务分类）
function topoAndConfigureButton() {// zyj

	var expId = messageJson.expId;
	var taskNum = parseInt(messageJson.expTaskNum);
	var expRole = messageJson.expRole;
	var i = $("#TopoSaveButton").val();
	// 按照任务个数动态生成按钮,循环中taskNum加1是因为任务0不在任务表中
	// for(var i=0;i<taskNum+1;i++){

	// if(expRole == "monitor") //监控员不需要保存按钮
	// break;

	// if(i==0&&expRole=="stu") //学生没有权限更改初始拓扑，配置，ping状态
	// continue;

	// 拓扑
	if (expRole == "GM") {
		var buttonParent = document.getElementById("buttonGroup");
		var newTopoButton = document.createElement('button');
		newTopoButton.setAttribute("id", "inittopoSave");
		// 这里复用了editBtn的样式，以后可修改
		newTopoButton.setAttribute("class", "btn btn-info opButton");
		newTopoButton.setAttribute("onclick", "topo.submit('0')");
		newTopoButton.innerHTML = "保存初始拓扑";
		buttonParent.appendChild(newTopoButton);
	}
	var buttonParent1 = document.getElementById("buttonGroup");
	var newTopoButton = document.createElement('button');
	newTopoButton.setAttribute("id", "topoSave");
	// 这里复用了editBtn的样式，以后可修改
	newTopoButton.setAttribute("class", "btn btn-info opButton");
	newTopoButton.setAttribute("onclick", "agreement('savetopo')");
	newTopoButton.setAttribute("value", 1);
	newTopoButton.innerHTML = "提交拓扑";

	/*
	 * if(i==0) newTopoButton.innerHTML = "保存初始拓扑"; else newTopoButton.innerHTML =
	 * "保存拓扑为任务"+i;
	 */

	buttonParent1.appendChild(newTopoButton);

	// 配置
	var buttonParent2 = document.getElementById("buttonGroup");
	var newConfigureButton = document.createElement('button');
	newConfigureButton.setAttribute("id", "configureSave");
	// 这里复用了editBtn的样式，以后可修改
	newConfigureButton.setAttribute("class", "btn btn-info opButton");
	newConfigureButton.setAttribute("onclick", "agreement('saveconfigure')");
	newConfigureButton.innerHTML = "保存配置";
	/*
	 * if(i==0) newConfigureButton.innerHTML = "保存初始配置"; else
	 * newConfigureButton.innerHTML = "保存配置为任务"+i;
	 */

	buttonParent2.appendChild(newConfigureButton);

	// ping验证
	/*
	 * var buttonParent3 = document.getElementById("pingVerifyButton"); var
	 * newPingVerifyButton = document.createElement('button');
	 * newPingVerifyButton.setAttribute("id", "pingVerify");
	 * //这里复用了editBtn的样式，以后可修改 newPingVerifyButton.setAttribute("class",
	 * "editBtn"); newPingVerifyButton.setAttribute("onclick", "pingVerify(" + i +
	 * ")");
	 * 
	 * if(i==0) newPingVerifyButton.innerHTML = "验证初始状态ping"; else
	 * newPingVerifyButton.innerHTML = "验证任务"+i+"ping";
	 * 
	 * buttonParent3.appendChild(newPingVerifyButton);
	 */

}
// }
function help() {
	var helplist = document.getElementById('helpOptions');
	if (helplist.style.display == 'none') {
		helplist.style.display = '';
		$("#help").css("box-shadow", "0px 0px 14px 4px #2534f6 outset");
	} else {
		$("#help").css("box-shadow", "");
		helplist.style.display = 'none';
	}
}

function sendHelpMsg() {

	var helplist = document.getElementById('helpOptions');
	messageJson.type = "help";
	messageJson.color = color.value;
	var problemList = "";
	for (var i = 0; i < 3; i++) {
		if ($("#option" + i).is(":checked")) {
			problemList = problemList + i + "#";
		}
	}
	// 去除#号
	problemList = problemList.substring(0, problemList.length - 1);
	messageJson.problemList = problemList;

	var mess = JSON.stringify(messageJson);
	websocket.send(mess);

	$("#help").css("box-shadow", "");
	helplist.style.display = 'none';
}

// 加载进度条 变量为进度值
function progress(value) {

	if (value != 0) {// 开启加载页面
		document.getElementById("cover").style.display = "block";
		document.getElementById("layout").style.display = "block";
		document.getElementById("waiting").style.display = "block";
	} else {
		//document.getElementById("override").style.display = "none";
		document.getElementById("cover").style.display = "none";
		document.getElementById("layout").style.display = "none";
		document.getElementById("waiting").style.display = "none";
	}
	var progress = $("#layout").first().find("#loading");

	// if(value > 100){
	// value = 100;
	// }else if(value < 0){
	// return;
	// }

	progress.attr("aria-valuenow", value);
	progress.css("width", value + "%");
	progress.html(value + "%");
}

function autoProgress(count, end, time) {
	if (count >= end) {
		return;
	} else {
		autoProgressTimeout = setTimeout(autoProgress,
				Math.random() * 2 * time, ++count, end, time);
		progress(count);
		return;
	}
}

function showNext() {// zyj
	var taskNum = parseInt(messageJson.expTaskNum);
	var next = document.getElementById("next");
	var i = parseInt(messageJson.expTaskOrder);
	var taskname = messageJson.TaskName;
	var expname = messageJson.expName;
	var html = "你正在进行实验:" + expname + " 当前任务：" + taskname;
	console.log(i);
	console.log(taskNum);
	$("#taskname").html(html);
	if (i == taskNum) {
		$("#next").html("结束");
		next.setAttribute("onclick", "agreement('exit')");
	} else {
		$("#next").html("下一个任务");
		next.setAttribute("onclick", "agreement('next')");
	}
}

function agreement(operation) {// zyj
	var message = operation;
	messageJson.content = message;
	messageJson.type = "agreement";
	messageJson.user = document.getElementById('userName').value;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function countDown() {

	// $("#clock").each(function(index, value) {
	time = $("#clock").html().split(":");
	// 超过一个小时
	if (time.length == 3) {

		if (parseInt(time[2], 10) > 0) {
			if (parseInt(time[2], 10) - 1 < 10)
				$("#clock").html(
						time[0] + ":" + time[1] + ":0"
								+ (parseInt(time[2], 10) - 1));
			else
				$("#clock").html(
						time[0] + ":" + time[1] + ":"
								+ (parseInt(time[2], 10) - 1));
		} else if (parseInt(time[1], 10) > 0) {
			if (parseInt(time[1], 10) - 1 < 10)
				$("#clock").html(
						time[0] + ":0" + (parseInt(time[1], 10) - 1) + ":59");
			else
				$("#clock").html(
						time[0] + ":" + (parseInt(time[1], 10) - 1) + ":59");
		} else if (parseInt(time[0], 10) - 1 > 0) {
			if (parseInt(time[0], 10) - 1 < 10)
				$("#clock").html("0" + (parseInt(time[0], 10) - 1) + ":59:59");
			else
				$("#clock").html((parseInt(time[0], 10) - 1) + ":59:59");
		} else
			$("#clock").html("59:59");
	}
	// 一个小时之内
	else if (time.length == 2) {

		// 倒计时间到
		if (parseInt(time[1], 10) == 0 && parseInt(time[0], 10) == 0) {
			return;
		} else if (parseInt(time[1], 10) > 0) {
			if (parseInt(time[1], 10) - 1 < 10)
				$("#clock").html(time[0] + ":0" + (parseInt(time[1], 10) - 1));
			else
				$("#clock").html(time[0] + ":" + (parseInt(time[1], 10) - 1));
		} else if (parseInt(time[0], 10) > 0) {
			if (parseInt(time[0], 10) - 1 < 10)
				$("#clock").html("0" + (parseInt(time[0], 10) - 1) + ":59");
			else
				$("#clock").html((parseInt(time[0], 10) - 1) + ":59");
		}
	}
	// });
	clocknum = setTimeout(countDown, 1000);
	return;
}

// 计算时间差
function timeCal(endTime) {
	var stopTime = new Date(endTime.replace(/\-/g, "/"));
	var currentTime = new Date();
	var retailSecond = (stopTime.getTime() - currentTime.getTime()) / 1000;

	console.log(currentTime);
	console.log(stopTime);
	console.log(retailSecond);

	var retail = "";
	if (Math.floor(retailSecond / 3600) != 0) {
		if (Math.floor(retailSecond / 3600) < 10)
			retail = retail + "0" + Math.floor(retailSecond / 3600) + ':';
		else
			retail = retail + Math.floor(retailSecond / 3600) + ':';
		retailSecond = retailSecond % 3600;
	}
	if (Math.floor(retailSecond / 60) != 0) {
		if (Math.floor(retailSecond / 60) < 10)
			retail = retail + "0" + Math.floor(retailSecond / 60) + ':';
		else
			retail = retail + Math.floor(retailSecond / 60) + ':';
		retailSecond = retailSecond % 60;
	}
	retail = retail + parseInt(retailSecond);
	return retail;
}
