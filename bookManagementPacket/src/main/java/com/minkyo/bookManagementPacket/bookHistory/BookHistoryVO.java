package com.minkyo.bookManagementPacket.bookHistory;

import java.io.Serializable;
import java.sql.Date;

public class BookHistoryVO implements Serializable {
	public int bookNo;
	public int userUID;
	public Date rentDate;
	public Date returnDate;
	public int getBookNo() {
		return bookNo;
	}
	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	public int getUserUID() {
		return userUID;
	}
	public void setUserUID(int userUID) {
		this.userUID = userUID;
	}
	public Date getRentDate() {
		return rentDate;
	}
	public void setRentDate(Date rentDate) {
		this.rentDate = rentDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	
}
