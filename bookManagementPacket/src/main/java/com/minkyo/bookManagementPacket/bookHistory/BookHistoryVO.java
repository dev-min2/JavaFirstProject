package com.minkyo.bookManagementPacket.bookHistory;

import java.io.Serializable;
import java.sql.Timestamp;

public class BookHistoryVO implements Serializable {
	public int bookNo;
	public int memberUID;
	public Timestamp rentDate;
	public Timestamp returnDate;
	public int rentNo;
	
	public int getBookNo() {
		return bookNo;
	}
	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	public int getMemberUID() {
		return memberUID;
	}
	public void setMemberUID(int memberUID) {
		this.memberUID = memberUID;
	}
	public Timestamp getRentDate() {
		return rentDate;
	}
	public void setRentDate(Timestamp rentDate) {
		this.rentDate = rentDate;
	}
	public Timestamp getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}
	public int getRentNo() {
		return rentNo;
	}
	public void setRentNo(int rentNo) {
		this.rentNo = rentNo;
	}
	
}
