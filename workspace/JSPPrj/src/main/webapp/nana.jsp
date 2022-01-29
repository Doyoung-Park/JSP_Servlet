<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%

//	int cnt=Integer.parseInt(request.getParameter("cnt"));
	// request.Parameter() : 사용자로부터 값을 입력받음 (문자열) -->정수형태로 바꿔주는 것이
	// Integer.parseInt() 
	String cnt_=request.getParameter("cnt");	// 임시변수에 받음
	int cnt=100;		// 기본값
		
	if(cnt_!=null && !cnt_.equals(""))
		cnt=Integer.parseInt(cnt_);

    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<% for(int i=0; i<cnt;i++){ %>
	안녕 servlet!!<br>
	<%} %>
</body>
</html>