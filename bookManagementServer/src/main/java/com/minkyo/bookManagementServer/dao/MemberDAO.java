package com.minkyo.bookManagementServer.dao;

import com.minkyo.bookManagementPacket.Member.MemberVO;

public interface MemberDAO {
	public boolean insertMember(MemberVO vo);
	public MemberVO loginUser(MemberVO vo);
	public boolean checkDuplicateID(MemberVO vo);
}
