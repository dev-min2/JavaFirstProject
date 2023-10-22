package com.minkyo.bookManagementPacket.bookHistory;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class RentBookVO implements Serializable {
	private int rentNo;
	private int memberUID;
	private int bookNo;
	private Timestamp rentDate;
	
	public int getRentNo() {
		return rentNo;
	}
	public void setRentNo(int rentNo) {
		this.rentNo = rentNo;
	}
	public int getMemberUID() {
		return memberUID;
	}
	public void setMemberUID(int memberUID) {
		this.memberUID = memberUID;
	}
	public int getBookNo() {
		return bookNo;
	}
	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	public Timestamp getRentDate() {
		return rentDate;
	}
	public void setRentDate(Timestamp rentDate) {
		this.rentDate = rentDate;
	}
	
	
}
