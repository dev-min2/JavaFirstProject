package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import CommonUtils.DBConnectionPool;

public class MemberDAOImpl implements MemberDAO {
	private PreparedStatement pstmt; // 주입대상x
	
	@Override
	public boolean insertMember(MemberVO vo) {
		boolean ret = true;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "INSERT INTO MEMBER VALUES(?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getUserID());
			pstmt.setString(2, vo.getUserPassword());
			pstmt.setString(3, vo.getUserEmail());
			pstmt.setString(4, vo.getNickName());
			pstmt.setBoolean(5, vo.isAdmin());
			pstmt.setDate(6, vo.getCreateDate());
			
			int b = pstmt.executeUpdate();
			
			conn.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			ret = false;
		}
		finally {
			
		}
		
		return true;
	}
	
	public void close(Connection conn) {
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
