package com.minkyo.bookManagementClient.bookService;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;

public class BookListPanel extends JPanel {
	class BookInfo {
		public static final int MAX_BOOK_IMG_WIDTH = 180;
		public static final int MAX_BOOK_IMG_HEIGHT = 245;
		public static final int DEFAULT_LABEL_WIDTH = 150;
		public static final int DEFAULT_LABEL_HEIGHT = 30;
		
		private JButton bookImg = null;
		private JLabel bookTitle = null;
		private JLabel bookAuthor = null;
		private JLabel bookIntroduce = null;
		private JLabel bookPublisher = null;
		private Font font = new Font("Serif",Font.PLAIN, 16);
		
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
	
	private BookPanelType pnType;
	private JButton backBtn = new JButton("돌아가기");
	private JButton bookRegistBtn = new JButton("도서등록");
	
	private final int MAX_ONEPAGE_BOOK_COUNT = 20;
	private JList bookList = null;
	private JList bookBackground = null;
	private DefaultListModel bookModelList = new DefaultListModel();	
	private JButton bookPrevBtn = new JButton("◀");
	private JButton bookNextBtn = new JButton("▶");
	
	private BookInfo bookInfo = null; 
	
	//private Map<String>
	
	public BookListPanel(BookPanelType pnType) {
		this.pnType = pnType;
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		
		bookInfo = new BookInfo();
		bookInfo.setBookImg(new JButton());
		bookInfo.getBookImg().setBounds(750,52,BookInfo.MAX_BOOK_IMG_WIDTH,BookInfo.MAX_BOOK_IMG_HEIGHT);
		
		backBtn.setBounds(20,52,100,50);
		bookRegistBtn.setBounds(20, 130, 100, 50);
		
		bookPrevBtn.setBounds(250,660, 70, 50);
		bookNextBtn.setBounds(630,660, 70,50);
		
		setBookInfo("test");
		setButtonListener();
		initJList();
		
		add(backBtn);
		add(bookRegistBtn);
		add(bookList);
		add(bookBackground);
		add(bookPrevBtn);
		add(bookNextBtn);
		add(bookInfo.getBookImg());
		add(bookInfo.getBookTitle());
		add(bookInfo.getBookPublisher());
		add(bookInfo.getBookAuthor());
		add(bookInfo.getBookIntroduce());

		// 이미지를 보이게하기 위해 order 앞으로 댕김
		this.setComponentZOrder(bookInfo.getBookImg(), 1);
		this.setComponentZOrder(bookInfo.getBookTitle(), 1);
		this.setComponentZOrder(bookInfo.getBookAuthor(), 1);
		this.setComponentZOrder(bookInfo.getBookPublisher(), 1);
		this.setComponentZOrder(bookInfo.getBookIntroduce(), 1);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
	}
	
	private void initJList() {
		bookList = new JList(bookModelList);
		bookBackground = new JList();
		bookBackground.setBounds(720,50,250,600);
		bookBackground.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookList.setBounds(250,50,450,600);
		bookList.setFont(new Font("Serif",Font.BOLD, 21));
		
		MouseListener mouseListener = new MouseAdapter() {
			@Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() >= 1) {
		           String selectedItem = (String) bookList.getSelectedValue();
		           // add selectedItem to your second list.
		           DefaultListModel model = (DefaultListModel) bookList.getModel();
		           
		           //책 정보 가져오기.
		         }
		    }
		};
		
		bookModelList.addElement("혼자 공부하는 자바");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookModelList.addElement("대충 아무책");
		bookList.addMouseListener(mouseListener);
	}

	private void setButtonListener() {
		ActionListener backBtn_action = (ActionEvent e) -> {
			// 로그인 가능한지 패킷 송신
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.MainPanel);
		};
		
		backBtn.addActionListener(backBtn_action);
	}
	
	private void setBookInfo(String bookTitle) {
		
		bookInfo.getBookImg().setBorderPainted(false);
		bookInfo.getBookImg().setBackground(new Color(255,255,255));
		
		Icon icon = Util.resize(
				new ImageIcon(Util.getImageFile("혼자공부하는자바.jpg")), 
				bookInfo.getBookImg().getWidth(),
				bookInfo.getBookImg().getHeight()
		);
		bookInfo.getBookImg().setIcon(icon);
		
		bookInfo.setBookTitle(new JLabel("혼자공부하는자바"));
		bookInfo.getBookTitle().setBounds(750, 310, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookInfo.getBookTitle().setFont(new Font("Serif", Font.BOLD, 16));
		
		bookInfo.setBookAuthor(new JLabel("저자 : 강남"));
		bookInfo.getBookAuthor().setBounds(750, 340, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookInfo.getBookAuthor().setFont(bookInfo.getFont());
		
		bookInfo.setBookPublisher(new JLabel("출판사 : 민교"));
		bookInfo.getBookPublisher().setBounds(750, 370, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookInfo.getBookPublisher().setFont(bookInfo.getFont());
		
		// 최대 13글자
		bookInfo.setBookIntroduce(new JLabel("<html><body>혼자공부할수있는자바책입니다.음ㄴ햐나ㅐㅎ언ㅇㅁ험ㄴ애ㅑ헌ㅇ햐ㅐ넝햐ㅐ</body></html>"));
		bookInfo.getBookIntroduce().setBounds(750, 140, BookInfo.DEFAULT_LABEL_WIDTH + 50, 600);
		bookInfo.getBookIntroduce().setFont(bookInfo.getFont());
	}

}

