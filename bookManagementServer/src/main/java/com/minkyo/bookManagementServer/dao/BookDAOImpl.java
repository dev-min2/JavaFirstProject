package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.minkyo.bookManagementPacket.BookList.BookVO;

import CommonUtils.DBConnectionPool;

public class BookDAOImpl implements BookDAO {
	private PreparedStatement pstmt; // 주입대상x
	
	@Override
	public boolean insertBook(BookVO vo) {
		// TODO Auto-generated method stub
		boolean ret = true;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "INSERT INTO BOOK VALUES(?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			
			int nextVal = 0;
			String sequence = "SELECT BOOKMANAGEMENT.BOOK_SEQ.NEXTVAL FROM DUAL";
			PreparedStatement ps = conn.prepareStatement(sequence);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				nextVal = rs.getInt(1);
			}
			
			pstmt.setLong(1, nextVal);
			pstmt.setString(2, vo.getBookTitle());
			pstmt.setString(3, vo.getBookAuthor());
			pstmt.setString(4, vo.getBookPublisher());
			pstmt.setString(5, vo.getBookIntroduce());
			pstmt.setString(6, vo.getBookImgPath());
			
			ret = pstmt.executeUpdate() > 0 ? true : false;
			
			conn.commit();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			if(conn != null) {
				try {
					conn.rollback();
				}
				catch(Exception e2) {
					e2.printStackTrace();
				}
			}
			ret = false;
		}
		finally {
			close(conn);
		}
		
		return ret;
	}
	
	private void close(Connection conn) {
		try {
			pstmt.close();
			conn.setAutoCommit(true);
			DBConnectionPool.getInstance().returnConnection(conn);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BookVO> selectAllBook() {
		List<BookVO> books = new ArrayList<BookVO>();
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT BOOKNO, BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER, BOOK_INTRODUCE, BOOK_REGIST_DATE "
				+ "FROM BOOK";
		try {
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				BookVO vo = new BookVO();
				vo.setBookNo(rs.getInt("BOOKNO"));
				vo.setBookTitle(rs.getString("BOOK_TITLE"));
				vo.setBookAuthor(rs.getString("BOOK_AUTHOR"));
				vo.setBookPublisher(rs.getString("BOOK_PUBLISHER"));
				vo.setBookIntroduce(rs.getString("BOOK_INTRODUCE"));
				vo.setBookRegistDate(rs.getDate("BOOK_REGIST_DATE"));
				
				books.add(vo);
			}
			
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(conn);
		}
		
		return books;
	}

	@Override
	public BookVO selectOneBook(int bookNo, String bookTitle) {
		// TODO Auto-generated method stub
		BookVO vo = null;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT BOOKNO, BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER, BOOK_INTRODUCE, BOOK_IMGPATH, BOOK_REGIST_DATE "
				+ "FROM BOOK WHERE BOOKNO = ? AND BOOK_TITLE = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookNo);
			pstmt.setString(2, bookTitle);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				vo = new BookVO();
				vo.setBookNo(rs.getInt("BOOKNO"));
				vo.setBookTitle(rs.getString("BOOK_TITLE"));
				vo.setBookAuthor(rs.getString("BOOK_AUTHOR"));
				vo.setBookPublisher(rs.getString("BOOK_PUBLISHER"));
				vo.setBookIntroduce(rs.getString("BOOK_INTRODUCE"));
				vo.setBookImgPath(rs.getString("BOOK_IMGPATH"));
				vo.setBookRegistDate(rs.getDate("BOOK_REGIST_DATE"));
			}
			
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(conn);
		}
		
		return vo;
	}
}
