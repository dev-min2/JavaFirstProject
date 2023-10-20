package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
