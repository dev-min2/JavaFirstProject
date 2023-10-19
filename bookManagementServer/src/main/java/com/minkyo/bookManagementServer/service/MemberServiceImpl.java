package com.minkyo.bookManagementServer.service;


import java.sql.Date;

import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_REQ;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;
import com.minkyo.bookManagementServer.dao.MemberDAO;
import com.minkyo.bookManagementServer.dao.MemberDAOImpl;

public class MemberServiceImpl implements MemberService {

	private MemberDAO dao;
	
	public MemberServiceImpl(MemberDAOImpl dao) {
		this.dao = dao;
	}
	
	@Override
	public boolean createUser(CREATE_USER_REQ packet) {
		// TODO Auto-generated method stub
		boolean ret = false;
		
		if(packet.userID == null || packet.userID.isBlank())
			return ret;
		
		if(packet.userPassword == null || packet.userPassword.isBlank())
			return ret;
		
		MemberVO newVO = new MemberVO(); 
		newVO.setMemberID(packet.userID);
		newVO.setMemberPassword(packet.userPassword);
		newVO.setMemberEmail(packet.userEmail);
		newVO.setMemberCreateDate(new Date(System.currentTimeMillis()));
		newVO.setAdmin(false);
		newVO.setMemberNickName(packet.nickName);
		
		ret = dao.insertMember(newVO);
		
		return ret;
	}

	@Override
	public MemberVO loginUser(LOGIN_USER_REQ packet) {
		// TODO Auto-generated method stub
		if(packet.userID == null || packet.userID.isBlank())
			return null;
		
		if(packet.userPassword == null || packet.userPassword.isBlank())
			return null;
		
		MemberVO loginVO = new MemberVO();
		loginVO.setMemberID(packet.userID);
		loginVO.setMemberPassword(packet.userPassword);
		
		return dao.loginUser(loginVO);
	}

	@Override
	public boolean checkDuplicateID(DUPLICATE_ID_CHECK_REQ packet) {
		// TODO Auto-generated method stub
		if(packet.id == null || packet.id.isBlank())
			return false;
		
		MemberVO idDuplicateCheck = new MemberVO();
		idDuplicateCheck.setMemberID(packet.id);
		
		return dao.checkDuplicateID(idDuplicateCheck);
	}

}
