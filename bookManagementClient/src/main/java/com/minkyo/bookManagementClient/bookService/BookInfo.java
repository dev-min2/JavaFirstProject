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
	private int bookNo = 0;
	private JLabel bookTitle = null;
	private JLabel bookAuthor = null;
	private JLabel bookIntroduce = null;
	private JLabel bookPublisher = null;
	private JTable historyTable = null;
	
	private JButton bookHistoryPrevBtn = new JButton("◀");
	private JButton bookHistoryNextBtn = new JButton("▶");
	private Font font = new Font("Serif",Font.PLAIN, 16);
	
	public int getBookNo() { return bookNo; }
	public void setBookNo(int bookNo) { this.bookNo = bookNo; }
	public JTable getHistoryTable() { return historyTable; }
	public void setHistoryTable(JTable table) { historyTable = table; }
	public JButton getHistoryPrevBtn() { return bookHistoryPrevBtn; }
	public void setHistoryPrevBtn(JButton btn) { bookHistoryPrevBtn = btn; }
	public JButton getHistoryNextBtn() { return bookHistoryNextBtn; }
	public void setHistoryNextBtn(JButton btn) { bookHistoryNextBtn = btn; }
	public Font getFont() { return font; }
	public JButton getBookImg() { return bookImg; }
	public void setBookImg(JButton btn) { bookImg = btn; }
	public JLabel getBookTitle() { return bookTitle; }
	public void setBookTitle(JLabel label) { bookTitle = label; }
	public JLabel getBookAuthor() { return bookAuthor; }
	public void setBookAuthor(JLabel label) { bookAuthor = label; }
	public JLabel getBookIntroduce() { return bookIntroduce; }
	public void setBookIntroduce(JLabel label) { bookIntroduce = label; }
	public JLabel getBookPublisher() { return bookPublisher; }
	public void setBookPublisher(JLabel label) { bookPublisher = label; } 
	
}
