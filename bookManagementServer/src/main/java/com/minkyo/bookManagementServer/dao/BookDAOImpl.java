package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

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
			
			pstmt.setInt(1, nextVal);
			pstmt.setString(2, vo.getBookTitle());
			pstmt.setString(3, vo.getBookAuthor());
			pstmt.setString(4, vo.getBookPublisher());
			pstmt.setString(5, vo.getBookImgPath());
			pstmt.setDate(6, new Date(System.currentTimeMillis()));
			
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
		String sql = "SELECT BOOKNO, BOOK_TITLE, BOOK_AUTHOR, BOOK_PUBLISHER, BOOK_REGIST_DATE "
				+ "FROM BOOK ORDER BY BOOK_TITLE ASC";
		try {
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				BookVO vo = new BookVO();
				vo.setBookNo(rs.getInt("BOOKNO"));
				vo.setBookTitle(rs.getString("BOOK_TITLE"));
				vo.setBookAuthor(rs.getString("BOOK_AUTHOR"));
				vo.setBookPublisher(rs.getString("BOOK_PUBLISHER"));
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
	public AbstractMap.SimpleEntry<BookVO, RentBookVO> selectOneBook(int bookNo, String bookTitle) {
		// TODO Auto-generated method stub
		AbstractMap.SimpleEntry<BookVO, RentBookVO> ret = null;
		BookVO bookVO = null;
		RentBookVO rentBookVO = null;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT A.BOOKNO, A.BOOK_TITLE, A.BOOK_AUTHOR, A.BOOK_PUBLISHER, A.BOOK_IMGPATH, A.BOOK_REGIST_DATE, B.RENTNO, B.MEMBER_UID, B.BOOKNO AS BB, B.RENT_DATE "
				+ "FROM BOOK A "
				+ "LEFT JOIN RENTBOOK B ON A.BOOKNO = B.BOOKNO "
				+ "WHERE A.BOOKNO = ? AND A.BOOK_TITLE = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookNo);
			pstmt.setString(2, bookTitle);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				bookVO = new BookVO();
				bookVO.setBookNo(rs.getInt("BOOKNO"));
				bookVO.setBookTitle(rs.getString("BOOK_TITLE"));
				bookVO.setBookAuthor(rs.getString("BOOK_AUTHOR"));
				bookVO.setBookPublisher(rs.getString("BOOK_PUBLISHER"));
				bookVO.setBookImgPath(rs.getString("BOOK_IMGPATH"));
				bookVO.setBookRegistDate(rs.getDate("BOOK_REGIST_DATE"));
				
				int rentNo = rs.getInt("RENTNO");
				if(!rs.wasNull()) {
					rentBookVO = new RentBookVO();
					rentBookVO.setRentNo(rentNo);
					rentBookVO.setMemberUID(rs.getInt("MEMBER_UID"));
					rentBookVO.setBookNo(rs.getInt("BB")); // bookVO의 BookNo를 넣어줘도 될듯.
					rentBookVO.setRentDate(rs.getDate("RENT_DATE"));
				}
				
				ret = new AbstractMap.SimpleEntry<BookVO, RentBookVO>(bookVO, rentBookVO); 
			}
			
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(conn);
		}
		
		return ret;
	}

	@Override
	public boolean insertRentBook(int memberUID, int bookNo, Date rentDate) {
		// TODO Auto-generated method stub
		boolean ret = true;

		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "INSERT INTO RENTBOOK VALUES(?,?,?,?)";
		String historySql = "INSERT INTO BOOKHISTORY (BOOKNO, MEMBER_UID, RENT_DATE, RENTNO) VALUES(?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);

			int nextVal = 0;
			String sequence = "SELECT BOOKMANAGEMENT.RENTBOOK_SEQ.NEXTVAL FROM DUAL";
			PreparedStatement ps = conn.prepareStatement(sequence);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				nextVal = rs.getInt(1);
			}

			pstmt.setInt(1, nextVal);
			pstmt.setInt(2, memberUID);
			pstmt.setInt(3, bookNo);
			pstmt.setDate(4, rentDate);

			ret = pstmt.executeUpdate() > 0 ? true : false;
			
			pstmt = conn.prepareStatement(historySql);
			pstmt.setInt(1, bookNo);
			pstmt.setInt(2, memberUID);
			pstmt.setDate(3, rentDate);
			pstmt.setInt(4, nextVal);
			
			pstmt.executeUpdate();

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

	@Override
	public boolean existRentBook(int bookNo) {
		// TODO Auto-generated method stub
		boolean ret = false;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT * FROM RENTBOOK WHERE BOOKNO = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookNo);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				ret = true;
			}
			
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(conn);
		}
		
		return ret;
	}

	@Override
	public boolean deleteRentBook(int rentNo) {
		boolean ret = true;

		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "DELETE FROM RENTBOOK WHERE RENTNO = ?";
		String historyUpdateSql = "UPDATE BOOKHISTORY SET RETURN_DATE = ? WHERE RENTNO = ?";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rentNo);
			ret = pstmt.executeUpdate() > 0 ? true : false;
			
			pstmt = conn.prepareStatement(historyUpdateSql);
			pstmt.setDate(1, new Date(System.currentTimeMillis()));
			pstmt.setInt(2, rentNo);
			pstmt.executeUpdate();

			conn.commit();
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
}
