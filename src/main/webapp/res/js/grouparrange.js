/*var status = document.getElementById('status');
var color = document.getElementById('colorStyle');
var messageinput = document.getElementById('messageInput');
var sendbutton = document.getElementById('sendBtn');
var historyMessage = document.getElementById('historyMsg');
var clearScreen = document.getElementById('clearBtn');
var showTab = document.getElementById('showTab');*/
var websocket = null;
var messageJson = { 
					"flag":"arrange",
					"userName":"default",
					"defaultGroup":"1",
					"finalGroup":"default",
					"arrangeStatus":"default",
					"ready":"default"
				  };
//判断当前浏览器是否支持WebSocket
  if ('WebSocket' in window) {	  
  	websocket= new WebSocket("ws://" + host + root_path + "websck");
  	console.log("ws://" + host + root_path + "websck");
  }
  else {  
    websocket = new SockJS("http://" + host + root_path + "sockjs/websck");	
}
  
//连接发生错误的回调方法
websocket.onerror = function(event){
	alert('连接服务器失败'); 
};

//连接成功建立的回调方法
websocket.onopen = function(event){
	document.getElementById("sendBtn").style.display = "";
}

//接收到消息的回调方法
websocket.onmessage = function(event){
  var object = JSON.parse(event.data);
  if(object.ready == "true")
	{
	  document.getElementById('userName').value = object.userName;
	  document.getElementById('groupID').value = object.finalGroup;
	  document.getElementById('submitform').submit(); 
	}
}

//连接关闭的回调方法
websocket.onclose = function(){
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
  //websocket.close();
}

//发送消息
function sendStatus(){
  messageJson.arrangeStatus = "true";
  var mess = JSON.stringify(messageJson);
  websocket.send(mess);
}

function showAndHideThings(){
	document.getElementById("loader").style.display="";
	sendStatus();
}

