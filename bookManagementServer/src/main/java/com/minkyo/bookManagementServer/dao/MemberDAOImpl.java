package com.minkyo.bookManagementServer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.minkyo.bookManagementPacket.Member.MemberVO;

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
			pstmt.setString(2, vo.getMemberID());
			pstmt.setString(3, vo.getMemberPassword());
			pstmt.setString(4, vo.getMemberEmail());
			pstmt.setString(5, vo.getMemberNickName());
			pstmt.setBoolean(6, vo.isAdmin());
			pstmt.setDate(7, vo.getMemberCreateDate());
			
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
	public MemberVO loginUser(MemberVO vo) {
		// TODO Auto-generated method stub
		MemberVO retVO = null;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_PASSWORD = ?";
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getMemberID());
			pstmt.setString(2, vo.getMemberPassword());
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				retVO = new MemberVO();
				retVO.setMemberUID(rs.getInt(1));
				retVO.setMemberID(rs.getString(2));
				retVO.setMemberPassword(rs.getString(3));
				retVO.setMemberEmail(rs.getString(4));
				retVO.setMemberNickName(rs.getString(5));
				retVO.setAdmin(rs.getBoolean(6));
				retVO.setMemberCreateDate(rs.getDate(7));;
			}
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(conn);
		}
		
		return retVO;
	}

	@Override
	public boolean checkDuplicateID(MemberVO vo) {
		// TODO Auto-generated method stub
		boolean ret = false;
		
		Connection conn = DBConnectionPool.getInstance().getPoolConnection();
		String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID = ?";
		ResultSet rs;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getMemberID());
			
			rs = pstmt.executeQuery();
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
