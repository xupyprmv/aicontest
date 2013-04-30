<!DOCTYPE html>
<%@ page pageEncoding="UTF-8"%>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/jquery-1.9.1.min.js"></script> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
	<title>КРФМЛИ : AIContest</title>
</head>
<script>
var timer = null;
function auto_reload()
{
	window.location = '${pageContext.request.contextPath}/${menuActiveItem}/';
}
</script>
<body ${(menuActiveItem=="arena"||menuActiveItem=="rating")?"onload=\"timer = setTimeout('auto_reload()',60000);\"":""}>
<%@ include file="/WEB-INF/pages/menu.jsp" %>

