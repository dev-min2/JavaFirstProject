package com.minkyo.bookManagementServer.service;

import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_REQ;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;

public interface MemberService {
	public boolean createUser(CREATE_USER_REQ packet);
	public MemberVO loginUser(LOGIN_USER_REQ packet);
	public boolean checkDuplicateID(DUPLICATE_ID_CHECK_REQ packet);
}
