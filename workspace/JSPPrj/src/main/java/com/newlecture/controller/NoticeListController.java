package com.newlecture.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// list?f=title&q=a 의 형태로 브라우저에서 controller 로 전달됨.
		String field_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		
		// request의 parameter로 받은 값이 null일 수도 있음 ->
		// null 일 경우를 대비해 초기화값을 정해줌
		String field="title";
		if(field_ !=null)
			field=field_;
		
		String query ="";
		if(query_!=null)
			query=query_;
		
		NoticeService service= new NoticeService();
		List<Notice> list= service.getNoticeList(field, query,1);
		
		
		
		request.setAttribute("list", list);
		
		request
		.getRequestDispatcher("/WEB-INF/view/notice/list.jsp")
		.forward(request, response);

	}
}
