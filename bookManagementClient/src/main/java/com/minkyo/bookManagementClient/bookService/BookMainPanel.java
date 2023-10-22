package com.minkyo.bookManagementClient.bookService;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;
import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.MY_BOOK_HISTORY_RENT_ACK;
import com.minkyo.bookManagementPacket.BookList.MY_BOOK_HISTORY_RENT_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;

import PacketUtils.Packet;
import PacketUtils.PacketUtil;
import SockNet.NetClient;

/*
 로그인 후 보게되는 첫 패널.
 
 // 개인정보?
 // 현재 대여한 것 책 제목, 책대여날짜, 반납날짜.
 // footer에는 요청게시판과 조회게시판
 */

public class BookMainPanel extends EventPanel {
	private BookPanelType pnType;
	private JButton requestBoardBtn = new JButton("도서 대여 이력");
	private JButton rentBoardBtn = new JButton("도서 조회");
	private JButton logoutBtn = new JButton("로그아웃");

//	private JButton returnBookPrevBtn = new JButton("◀");
//	private JButton returnBookNextBtn = new JButton("▶");
	
	private JButton rentBookPrevBtn = new JButton("◀");
	private JButton rentBookNextBtn = new JButton("▶");
	
	private JLabel nickNameLabel = new JLabel("이름");
	private JLabel rentCntLabel = new JLabel("대여중 도서 : ");
	private JLabel historyCntLabel = new JLabel("반납 도서 : ");
	
	//프로필
	
	//나의 대여기록
	private JLabel rentLabel = new JLabel("대여기록");
	private JTable myRentBookList = null;
	private List<DefaultTableModel> myRentBookModels = new ArrayList<DefaultTableModel>();
	private int curModelIndex = 0;
	
	public BookMainPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		requestBoardBtn.setBounds(160,70, 200, 250);
		rentBoardBtn.setBounds(400,70, 200, 250);
		logoutBtn.setBounds(880, 10, 100, 50);
	
		rentBookPrevBtn.setBounds(180,660,45,45);
		rentBookNextBtn.setBounds(800,660,45,45);
		
		nickNameLabel.setBounds(670,100, 100, 60);
		nickNameLabel.setFont(new Font("Serif", Font.BOLD, 26));
		
		rentCntLabel.setBounds(670,130,300,100);
		rentCntLabel.setFont(new Font("Serif", Font.BOLD, 18));
		historyCntLabel.setBounds(670,180,300,100);
		historyCntLabel.setFont(new Font("Serif", Font.BOLD, 18));
		
		initTable();
		
		setButtonListener();
		
		add(requestBoardBtn);
		add(rentBoardBtn);
		add(logoutBtn);
		add(myRentBookList.getTableHeader());
		add(myRentBookList);
		add(rentBookPrevBtn);
		add(rentBookNextBtn);
		add(rentLabel);
		add(nickNameLabel);
		add(rentCntLabel);
		add(historyCntLabel);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
		drawRentBookRect((Graphics2D)g);
		drawProfileBookRect((Graphics2D)g);
	}
	
	private void drawRentBookRect(Graphics2D g) {
		Stroke stroke1 = new BasicStroke(2f);
		g.setStroke(stroke1);
		g.drawRoundRect(160, 340, 700, 380, 30, 30);
	}
	
	private void drawProfileBookRect(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(660, 70, 200, 250);
	}
	
	private void initTable() {
		myRentBookModels.add(new DefaultTableModel());
		
		DefaultTableModel firstModel = myRentBookModels.get(curModelIndex);
		
		firstModel.addColumn("이름");
		firstModel.addColumn("도서제목");
		firstModel.addColumn("대여날짜");
		firstModel.addColumn("반납날짜");
		
		myRentBookList = new JTable(firstModel);
		myRentBookList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
		myRentBookList.setBounds(233,380, 557, 325);
		myRentBookList.getTableHeader().setBounds(233, 353, 557, 25);
		myRentBookList.getTableHeader().setFont(new Font("Serif",Font.BOLD, 12));
		myRentBookList.getTableHeader().setReorderingAllowed(false);
		myRentBookList.getTableHeader().setResizingAllowed(false);
		
		myRentBookList.setRowHeight(25);
		TableCellRenderer customRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// Check the condition for changing the font (e.g., row 2)
				c.setFont(new Font("Serif", Font.PLAIN, 15));

				return c;
			}
		};
		
		// 556
		myRentBookList.getColumnModel().getColumn(0).setWidth(60);
		myRentBookList.getColumnModel().getColumn(1).setWidth(248);
		myRentBookList.getColumnModel().getColumn(2).setWidth(124);
		myRentBookList.getColumnModel().getColumn(3).setWidth(124);
		
		for(int column = 0; column < myRentBookList.getColumnModel().getColumnCount(); ++column) {
			myRentBookList.getColumnModel().getColumn(column).setCellRenderer(customRenderer);
		}
		
		// 13개 까지 추가가능.
	}
	
	private void setButtonListener() {
		ActionListener request_board_action = (ActionEvent e) -> {
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.BookHistoryPanel);
		};
		
		ActionListener rent_board_action = (ActionEvent e) -> {
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.BookListPanel);
		};
		
		ActionListener rentBookPrevBtnAction = (ActionEvent e) -> {
			if(curModelIndex - 1 < 0)
				return;
			
			myRentBookList.setModel(myRentBookModels.get(--curModelIndex));
		};
		
		ActionListener rentBookNextBtnAction = (ActionEvent e) -> {
			if(curModelIndex + 1 >= myRentBookModels.size())
				return;

			myRentBookList.setModel(myRentBookModels.get(++curModelIndex));
		};
		
		ActionListener logoutBtnAction = (ActionEvent e) -> {
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.LoginPanel);
		};
		
		requestBoardBtn.addActionListener(request_board_action);
		rentBoardBtn.addActionListener(rent_board_action);
		rentBookPrevBtn.addActionListener(rentBookPrevBtnAction);
		rentBookNextBtn.addActionListener(rentBookNextBtnAction);
		logoutBtn.addActionListener(logoutBtnAction);
	}

	@Override
	public void openEvent() {
		// TODO Auto-generated method stub
//		String[] colors = {"Red", "Green", "Blue", "Yellow"};
//		
//		myRentBookModel.addRow(colors);
		
		MemberVO myAccountInfo = BookManagementMainFrame.getInstance().getAccountInfo();
		nickNameLabel.setText(myAccountInfo.getMemberNickName());
		
		NetClient net = BookManagementMainFrame.getInstance().getNetClient();
		
		MY_BOOK_HISTORY_RENT_REQ req = new MY_BOOK_HISTORY_RENT_REQ();
		MY_BOOK_HISTORY_RENT_ACK ack = null;
		
		req.memberUID = myAccountInfo.getMemberUID();
		try {
			Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
			net.send(packet);
			
			ack = (MY_BOOK_HISTORY_RENT_ACK)net.recv();
			List<String> titles = null;
			List<BookHistoryVO> historyVOs = null;
			if(ack.netError == NetError.NET_OK) {
				String nickName = ack.nickName;
				titles = ack.bookTitleList;
				historyVOs = ack.historyVOList;
				
				DefaultTableModel myRentBookModel = myRentBookModels.get(0);
				for(int idx = 0; idx < titles.size(); ++idx) {
					if(idx % 13 == 0 && idx != 0) {
						myRentBookModel = new DefaultTableModel();
						myRentBookModels.add(myRentBookModel);
						
						myRentBookModel.addColumn("이름");
						myRentBookModel.addColumn("도서제목");
						myRentBookModel.addColumn("대여날짜");
						myRentBookModel.addColumn("반납날짜");
					}
					
					String formattedRentString = "";
					String formattedReturnString = "";
					
					if(historyVOs.get(idx).rentDate != null) {
						long timeInMilliseconds = historyVOs.get(idx).rentDate.getTime();

				        // java.util.Date
				        Date date = new Date(timeInMilliseconds);

				        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
				        formattedRentString = format.format(date);	
					}
					
					if(historyVOs.get(idx).returnDate != null) {
						long timeInMilliseconds = historyVOs.get(idx).returnDate.getTime();

				        // java.util.Date
				        Date date = new Date(timeInMilliseconds);

				        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
				        formattedReturnString = format.format(date);	
					}
					
					myRentBookModel.addRow(new Object[] {nickName, titles.get(idx), formattedRentString, formattedReturnString});
				}
				
				myRentBookList.setModel(myRentBookModels.get(0));
				int curRentCnt = 0;
				int returnBookCnt = 0;
				if(historyVOs != null && historyVOs.size() > 0) {					
					for(BookHistoryVO vo : historyVOs) {
						if(vo.rentDate != null && vo.returnDate != null)
							++returnBookCnt;
						else
							++curRentCnt;
					}
				}
				
				rentCntLabel.setText(rentCntLabel.getText() + curRentCnt + "건");
				historyCntLabel.setText(historyCntLabel.getText() + returnBookCnt + "건");
			}
			else {
				Util.ErrDialog(this, "에러 발생.. 재로그인?", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		this.repaint();
	}

	private void initAddTable() {
	}
}
