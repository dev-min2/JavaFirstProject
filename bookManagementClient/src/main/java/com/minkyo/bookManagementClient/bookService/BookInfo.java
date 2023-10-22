package com.minkyo.bookManagementClient.bookService;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

// 도서 조회 게시판에서 볼 수 있는 거
public class BookInfo {
	public static final int MAX_BOOK_IMG_WIDTH = 180;
	public static final int MAX_BOOK_IMG_HEIGHT = 245;
	public static final int DEFAULT_LABEL_WIDTH = 150;
	public static final int DEFAULT_LABEL_HEIGHT = 30;
	
	private JButton bookImg = null;
	private int rentMemberUID = 0; // rent한 Member의 UID;
	private int bookNo = 0;
	private int rentNo = 0;
	private boolean canRent = false;
	private JLabel bookTitle = null;
	private JLabel bookAuthor = null;
	private JLabel bookPublisher = null;
	
	private Font font = new Font("Serif",Font.PLAIN, 16);
	
	public int getBookNo() { return bookNo; }
	public void setBookNo(int bookNo) { this.bookNo = bookNo; }
	public Font getFont() { return font; }
	public JButton getBookImg() { return bookImg; }
	public void setBookImg(JButton btn) { bookImg = btn; }
	public JLabel getBookTitle() { return bookTitle; }
	public void setBookTitle(JLabel label) { bookTitle = label; }
	public JLabel getBookAuthor() { return bookAuthor; }
	public void setBookAuthor(JLabel label) { bookAuthor = label; }
	public JLabel getBookPublisher() { return bookPublisher; }
	public void setBookPublisher(JLabel label) { bookPublisher = label; } 
	public boolean isCanRent() { return canRent; }
	public void setCanRent(boolean canRent) { this.canRent = canRent; }
	public int getRentMemberUID() { return rentMemberUID; }
	public void setRentMemberUID(int rentMemberUID) { this.rentMemberUID = rentMemberUID; }
	public int getRentNo() { return rentNo; }
	public void setRentNo(int rentNo) { this.rentNo = rentNo; }
	
}
