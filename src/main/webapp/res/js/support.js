// JavaScript Document
var host = window.location.host;
var root_path = window.location.pathname.split("/").slice(0,-1).join('/') + "/";

function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return null;
}

function push_alert(str){
	$("nav").after("<div class=\"alert alert-warning alert-dismissable\">" + "</div>");
	$(".alert").first().append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>" + str);
}
