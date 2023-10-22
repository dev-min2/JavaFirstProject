package com.minkyo.bookManagementPacket.bookHistory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class RentBookVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 14001L;
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
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject(); // defaultWriteObject는 현재 자신 클래스의 멤버를 자동으로 직렬화
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject(); // defaultReadObject는 현재 자신 클래스의 멤버를 자동으로 역직렬화
	}
	
}
