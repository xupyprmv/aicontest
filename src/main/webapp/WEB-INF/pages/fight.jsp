<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="/WEB-INF/pages/header.jsp"%>
<div class="container">
<c:forEach var="turn" items="${fight}">
<h3>${turn.key}</h3>
<table border="1px solid black;" width="100%" style="font-size: xx-small; text-align: center;">
    <col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" />
    <col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" />
    <col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" />
    <col width="20px" /><col width="20px" /><col width="20px" /><col width="20px" />

<tr>
<td colspan="2" style="background-color: black;"></td>
<c:forEach begin="0" end="8" varStatus="x">
<td colspan="2">&nbsp;${turn.value.field[x.index][4]}&nbsp;</td>
</c:forEach>
<td colspan="2" style="background-color: black;"></td>
</tr>

<tr>
<td colspan="1" style="background-color: black;"></td>
<c:forEach begin="0" end="9" varStatus="x">
<td colspan="2">&nbsp;${turn.value.field[x.index][3]}&nbsp;</td>
</c:forEach>
<td colspan="1" style="background-color: black;"></td>
</tr>

<tr>
<c:forEach begin="0" end="10" varStatus="x">
<td colspan="2">&nbsp;${turn.value.field[x.index][2]}&nbsp;</td>
</c:forEach>
</tr>

<tr>
<td colspan="1" style="background-color: black;"></td>
<c:forEach begin="0" end="9" varStatus="x">
<td colspan="2">&nbsp;${turn.value.field[x.index][1]}&nbsp;</td>
</c:forEach>
<td colspan="1" style="background-color: black;"></td>
</tr>

<tr>
<td colspan="2" style="background-color: black;"></td>
<c:forEach begin="0" end="8" varStatus="x">
<td colspan="2">&nbsp;${turn.value.field[x.index][0]}&nbsp;</td>
</c:forEach>
<td colspan="2" style="background-color: black;"></td>
</tr>

</table>
	<td>${event.sdate}</td>
</c:forEach>
</div>
<%@ include file="/WEB-INF/pages/footer.jsp"%>