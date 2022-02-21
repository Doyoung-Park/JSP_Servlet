package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	public int removeNoticeAll(int[] ids) {	// 일괄 삭제
		// 배열로 id를 받
		return 0;	// 몇 개가 삭제되었는지를 반환. 
	}
	public int pubNoticeAll(int[] ids){ // 일괄 공개
	
		return 0;	// 몇 개를 공개했는지 반환 
	}
	public int insertNotice(Notice notice) { // 공지사항 추가
		
		int result=0;
		
		String params= "";
		
		String sql="INSERT INTO NOTICE(TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES(?,?,?,?,?)";
		

		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, notice.getTitle());
			st.setString(2,  notice.getContent());
			st.setString(3,  notice.getWriterID());
			st.setBoolean(4,  notice.getPub());
			st.setString(5, notice.getFiles());
			
			result = st.executeUpdate();
			
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;	// 정상적으로 추가되면 1 그렇지 않으면 0 반환
	}
	public int deleteNotice(int id) {	//

		return 0;
	}
	public int updateNotice(Notice notice) {	//새로 반영된 레코드가 몇 개의 레코드에 영향을 주었는지 리턴
	
		return 0;
	}
	public List<Notice> getNoticeNewestList(){  	// 새로운 공지사항을 리
	
		return null;
	}
	
	public List<NoticeView> getNoticeList() {		//	(기존에 사용한) 공지사항 리
		
		return getNoticeList("title","",1);
	}
	
	public List<NoticeView> getNoticeList(int page) {
		
		return getNoticeList("title","",page);
	}

	
	// 검색창에서 검색 기능 함수
	// field -> 'title' (제목) or 'writer_id' (작성자 아이디)
	// query -> 검색창에 입력한 검색어 => 이 검색어를 포함하는 값을 찾아야 함
	public List<NoticeView> getNoticeList(String field, String query, int page) {
		
		List<NoticeView> list= new ArrayList<>();
		
		String sql="SELECT * FROM ( "																		//*********************************
				+ "    SELECT ROWNUM NUM, N.*  "
				+ "    FROM (SELECT * FROM NOTICE_VIEW WHERE "+field+" LIKE ? ORDER BY REGDATE DESC) N "
				+ ") "
				+ " WHERE NUM BETWEEN ? AND ?";
		
		// 1, 11, 21, 31 -> 공식: an = 1+(page-1)*10
		// 10, 20, 30, 40 -> page * 10
		
		
		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1,  "%"+query+"%"); 	// 위에서의 1번째 물음표에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			st.setInt(2, 1+(page-1)*10);		// 위에서의 2번째 물음표(?)에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			st.setInt(3, page*10);		// 위에서의 3번째 ? 에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			
			
			ResultSet rs = st.executeQuery();

			while(rs.next()){ 
				 int id=rs.getInt("ID");
				 String title= rs.getString("TITLE");
				 String writerID = rs.getString("WRITER_ID");
				 Date regdate= rs.getDate("REGDATE");
				 String hit= rs.getString("HIT");
				 String files= rs.getString("FILES");
				 
//				 String content=rs.getString("CONTENT");
				 int cmtCount = rs.getInt("CMT_COUNT");
				 boolean pub = rs.getBoolean("PUB");
				 
				 NoticeView notice = new NoticeView(
						 id, 
						 title, 
						 writerID, 
						 regdate, 
						 hit, 
						 files, 
						 pub,
						 cmtCount 
						 );
				 
				 list.add(notice);
			}



			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list; 
	}
	
	public int getNoticeCount() {
		
		return getNoticeCount("title","");
	}
	
	public int getNoticeCount(String field, String query) {
		
		int count=0;
		
		String sql="SELECT COUNT(ID) COUNT FROM ( "
				+ "    SELECT ROWNUM NUM, N.*  "
				+ "    FROM (SELECT * FROM NOTICE WHERE "+field+" LIKE ? ORDER BY REGDATE DESC) N " 			//********************************************
				+ ")";
		
		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1,  "%"+query+"%"); 	// 위에서의 1번째 물음표에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			
			
			ResultSet rs = st.executeQuery();

			if(rs.next())
				count = rs.getInt("count");

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return count;
	}
	public Notice getNotice(int id) {
		
		Notice notice = null;
		
		
		String sql = "SELECT * FROM NOTICE WHERE ID=?";
		

		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,id); 	// 위에서의 1번째 물음표에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){ 
				 int nid=rs.getInt("ID");
				 String title= rs.getString("TITLE");
				 String writerID = rs.getString("WRITER_ID");
				 Date regdate= rs.getDate("REGDATE");
				 String hit= rs.getString("HIT");
				 String files= rs.getString("FILES");
				 String content=rs.getString("CONTENT");
				 boolean pub = rs.getBoolean("PUB");

				 notice = new Notice(
						 nid, title, writerID, regdate, hit, files, content, pub
						 );
			}

			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return notice;
	}

	public Notice getNextNotice(int id) {
		
		Notice notice =null;
		
		String sql="SELECT * FROM NOTICE "
				+ "WHERE ID=( "
				+ "    SELECT ID FROM NOTICE "
				+ "    WHERE REGDATE >(SELECT REGDATE FROM NOTICE WHERE ID=?) "
				+ "    AND ROWNUM=1 "
				+ ")";
		

		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,id); 	// 위에서의 1번째 물음표에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){ 
				 int nid=rs.getInt("ID");
				 String title= rs.getString("TITLE");
				 String writerID = rs.getString("WRITER_ID");
				 Date regdate= rs.getDate("REGDATE");
				 String hit= rs.getString("HIT");
				 String files= rs.getString("FILES");
				 String content=rs.getString("CONTENT");
				 boolean pub = rs.getBoolean("PUB");

				 notice = new Notice(
						 nid, title, writerID, regdate, hit, files, content, pub
						 );
			}


			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return notice;
	}

	public Notice getPrevNotice(int id) {
		
		Notice notice =null;
		
		
		String sql="SELECT ID FROM (SELECT * FROM NOTICE ORDER BY REGDATE DESC) "
				+ " WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID=?) "
				+ " AND ROWNUM = 1";
		

		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,id); 	// 위에서의 1번째 물음표에 두 번째 인자로 전달된 값을 사용하겠다는 뜻
			
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){ 
				 int nid=rs.getInt("ID");
				 String title= rs.getString("TITLE");
				 String writerID = rs.getString("WRITER_ID");
				 Date regdate= rs.getDate("REGDATE");
				 String hit= rs.getString("HIT");
				 String files= rs.getString("FILES");
				 String content=rs.getString("CONTENT");
				 boolean pub = rs.getBoolean("PUB");

				 notice = new Notice(
						 nid, title, writerID, regdate, hit, files, content, pub
						 );
			}


			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return notice;
	}
	public int deleteNoticeAll(int[] ids) {
		
		int result=0;
		
		String params= "";
		
		for(int i=0; i<ids.length; i++) {
			params +=ids[i];
			
			if( i<ids.length-1)		// 마지막이 되기 전까지 ',' 를 붙여줌 
				params += ",";
		}
		String sql="DELETE NOTICE WHERE ID IN ("+params+")";
		

		String url="jdbc:oracle:thin:@192.168.0.164:1521/xepdb1";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con= DriverManager.getConnection(url,"NEWLEC","12345");
			Statement st = con.createStatement();
		
			
			
			result = st.executeUpdate(sql);
			
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
		
	}
	
}
