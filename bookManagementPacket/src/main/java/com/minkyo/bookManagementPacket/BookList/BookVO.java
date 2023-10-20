package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

public class BookVO implements Serializable{
	private int bookNo;
	private String bookTitle;
	private String bookAuthor;
	private String bookPublisher;
	private String bookIntroduce;
	private String bookImgPath;
	
	public int getBookNo() {
		return bookNo;
	}
	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookPublisher() {
		return bookPublisher;
	}
	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}
	public String getBookIntroduce() {
		return bookIntroduce;
	}
	public void setBookIntroduce(String bookIntroduce) {
		this.bookIntroduce = bookIntroduce;
	}
	public String getBookImgPath() {
		return bookImgPath;
	}
	public void setBookImgPath(String bookImgPath) {
		this.bookImgPath = bookImgPath;
	}
}
