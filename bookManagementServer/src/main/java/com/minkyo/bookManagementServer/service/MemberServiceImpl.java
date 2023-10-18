package com.minkyo.bookManagementServer.service;


import java.sql.Date;

import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementServer.dao.MemberDAO;
import com.minkyo.bookManagementServer.dao.MemberDAOImpl;
import com.minkyo.bookManagementServer.dao.MemberVO;

public class MemberServiceImpl implements MemberService {

	private MemberDAO dao;
	
	public MemberServiceImpl(MemberDAOImpl dao) {
		this.dao = dao;
	}
	
	@Override
	public void createUser(CREATE_USER_REQ packet) {
		// TODO Auto-generated method stub
		
		MemberVO newVO = new MemberVO(); 
		newVO.setUserID(packet.userID);
		newVO.setUserPassword(packet.userPassword);
		newVO.setUserEmail(packet.userEmail);
		newVO.setCreateDate(new Date(System.currentTimeMillis()));
		newVO.setAdmin(false);
		newVO.setNickName(packet.nickName);
		
		dao.insertMember(newVO);
	}

}
