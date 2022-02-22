package com.newlecture.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
		
	//	System.out.println("title : " + title);
		
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");	// true 또는 null 값이 옴
		
		Collection<Part> parts = request.getParts();	// 다중 파일을 받기 위한 collection
		StringBuilder builder = new StringBuilder();
		for(Part p: parts) {
			if(!p.getName().equals("file")) continue;	// p의 이름이 file이 아니면 pass
			
			if(p.getSize() == 0) continue;		// 받은 파일의 크기가 0일 경우, 즉 데이터가 없는 경우
			
			Part filePart = p;	
			
			String fileName = filePart.getSubmittedFileName();
			builder.append(fileName);		//	 파일 이름
			builder.append(",");		// 구분자
			
			InputStream fis = filePart.getInputStream();	// 바이너리 스트림 설정
			
			// 파일이 저장되는 경로는 상대 경로가 될 수 없음. 절대 경로로 설정해야 함
			String realPath = request.getServletContext().getRealPath("/upload");	// 저장 위치의 절대 경로를 이 코드를 통해 쉽게 알아낼 수 있음
			System.out.println(realPath);	
			
		//	fis.read();		// 1바이트씩 읽어들임. 다 읽어들인 후에는 -1을 리턴
			
			String filePath = realPath + File.separator + fileName;
			FileOutputStream fos = new FileOutputStream(filePath);
			
			
			byte[] buf = new byte[1024];
			int size = 0;
			while((size=fis.read(buf))!= -1) 
				fos.write(buf, 0,size);
			
			fos.close();
			fis.close();
		}
		builder.delete(builder.length()-1, builder.length());
		
		
		boolean pub = false;
		
		if(isOpen!=null)
			pub=true;
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterID("newlec");
		notice.setFiles(builder.toString());
		
		NoticeService service = new NoticeService();
		service.insertNotice(notice);
		
		response.sendRedirect("list");
		
	}

}
