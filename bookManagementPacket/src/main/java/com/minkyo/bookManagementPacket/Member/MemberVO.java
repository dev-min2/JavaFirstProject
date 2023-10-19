package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;
import java.sql.Date;

public class MemberVO implements Serializable {
	private long memberUID;
	private String memberID;
	private String memberPassword;
	private String memberEmail;
	private String memberNickName;
	private boolean isAdmin;
	private Date memberCreateDate;
	public long getMemberUID() {
		return memberUID;
	}
	public void setMemberUID(long memberUID) {
		this.memberUID = memberUID;
	}
	public String getMemberID() {
		return memberID;
	}
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
	public String getMemberPassword() {
		return memberPassword;
	}
	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}
	public String getMemberEmail() {
		return memberEmail;
	}
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	public String getMemberNickName() {
		return memberNickName;
	}
	public void setMemberNickName(String memberNickName) {
		this.memberNickName = memberNickName;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Date getMemberCreateDate() {
		return memberCreateDate;
	}
	public void setMemberCreateDate(Date memberCreateDate) {
		this.memberCreateDate = memberCreateDate;
	}

	
}