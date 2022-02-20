package com.newlecture.controller.admin.notice;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

// 클라이언트가 요청할 때 multipart 방식으로 요청했기 때문에, 서버 쪽에서도 multipath 방식으로 받아야 함.
// 서버가 multipath 방식으로 받도록 설정해주는 annotation
@MultipartConfig(		
//		location="/tmp",		// '파일 용량이 클 때에는 디스크를 쓰자'. 라는 의미 -> 이걸 사용 안하면 자바가 지정한 저장소로 저장됨.
		fileSizeThreshold=1024*1024,	// 바이트 단위. 1Mb를 넘을 때에는 디스크 사용
		maxFileSize=1024*1024*50,	// 파일 하나의 최대 크기. 5Mb
		maxRequestSize=1024*1024*50*5	//  한 번에 보낼 수 있는 전체 보내는 파일 크기
)
// 관리자 페이지에 대한 컨트롤러
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet{		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request 
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp")
		.forward(request, response);

	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		
		System.out.println("title : " + title);
		
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");	// true 또는 null 값이 옴
		boolean pub = false;
		
		if(isOpen!=null)
			pub=true;
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterID("newlec");
		
		NoticeService service = new NoticeService();
		service.insertNotice(notice);
		
		response.sendRedirect("list");
		
	}

}
