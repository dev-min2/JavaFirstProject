package com.minkyo.bookManagementServer.service;

import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;

public interface MemberService {
	public void createUser(CREATE_USER_REQ packet);
}
