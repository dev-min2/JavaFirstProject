package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import CommonUtils.DBConnectionPool;

public class MemberDAOImpl implements MemberDAO {
	private PreparedStatement pstmt; // 주입대상x
	
	@Override
	public boolean insertMember(MemberVO vo) {
		boolean ret = true;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "INSERT INTO MEMBER VALUES(?,?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			
			int nextVal = 0;
			String sequence = "SELECT BOOKMANAGEMENT.MEMBER_SEQ.NEXTVAL FROM DUAL";
			PreparedStatement ps = conn.prepareStatement(sequence);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				nextVal = rs.getInt(1);
			}
			
			pstmt.setLong(1, nextVal);
			pstmt.setString(2, vo.getUserID());
			pstmt.setString(3, vo.getUserPassword());
			pstmt.setString(4, vo.getUserEmail());
			pstmt.setString(5, vo.getNickName());
			pstmt.setBoolean(6, vo.isAdmin());
			pstmt.setDate(7, vo.getCreateDate());
			
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
