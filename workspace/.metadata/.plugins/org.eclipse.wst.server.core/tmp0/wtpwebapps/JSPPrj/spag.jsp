<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!-- 입력코드 -->
<!-- ------------------------------------ -->
<!-- 출력코드 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%pageContext.setAttribute("result","bye"); %>
<body>
	
	<!--  <%=request.getAttribute("result") %>입니다. -->
	${requestScope.result} 입니다.<br>
	${names[1]} 입니다. <br>
	${notice.id} <br>
	${result}<br>
	${param.n/2} <br>
	${header.accept}
	
	
</body>
</html>