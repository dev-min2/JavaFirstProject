package com.minkyo.bookManagementServer.service;

import com.minkyo.bookManagementServer.dao.MemberDAO;

public class MemberServiceImpl implements MemberService {

	private MemberDAO dao;
	
	public MemberServiceImpl(MemberDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void createUser() {
		// TODO Auto-generated method stub
	}

}
