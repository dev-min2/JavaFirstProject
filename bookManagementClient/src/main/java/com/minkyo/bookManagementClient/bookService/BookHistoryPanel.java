package com.minkyo.bookManagementClient.bookService;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;
import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.BOOK_HISTORY_RENT_ACK;
import com.minkyo.bookManagementPacket.BookList.BOOK_HISTORY_RENT_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_ACK;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;

import PacketUtils.Packet;
import PacketUtils.PacketUtil;
import SockNet.NetClient;

// 우선 책정보를 받아와야함.
// 리스트패널처럼 진입시에 책정보만 우선받아오고, 해당 책 클릭시 


public class BookHistoryPanel extends EventPanel {
	private BookPanelType pnType;
	private JButton backBtn = new JButton("돌아가기");
	private int curModelIndex = 0;
	private JList bookList = null;
	private List<DefaultListModel> bookModelList = new ArrayList<DefaultListModel>();	
	private Map<String, Integer> bookNoByTitle = new HashMap<>(); 
	
	private JButton bookPrevBtn = new JButton("◀");
	private JButton bookNextBtn = new JButton("▶");
	
	// 클릭됐을 때 호출.
	private JTable myRentBookList = null;
	private List<DefaultTableModel> myRentBookModels = new ArrayList<DefaultTableModel>();
	private int curTableModelIndex = 0;
	
	private JButton rentBookPrevBtn = new JButton("◀");
	private JButton rentBookNextBtn = new JButton("▶");
	
	public BookHistoryPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		backBtn.setBounds(20,52,100,50);
		bookPrevBtn.setBounds(810,210, 50, 50);
		bookNextBtn.setBounds(810,270, 50, 50);
		
		rentBookPrevBtn.setBounds(810,595,50,50);
		rentBookNextBtn.setBounds(810,655,50,50);
		
		initJList();
		initTable();
		setButtonListener();
		
		add(backBtn);
		add(bookList);
		add(bookPrevBtn);
		add(bookNextBtn);
		add(myRentBookList);
		add(myRentBookList.getTableHeader());
		add(rentBookPrevBtn);
		add(rentBookNextBtn);
	}
	
	private void initJList() {
		bookList = new JList();
		DefaultListModel model = new DefaultListModel();
		bookModelList.add(model);

		bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookList.setBounds(250,50,550,270); // 최대 9개
		bookList.setFont(new Font("Serif",Font.BOLD, 21));
	}
	
	private void initTable() {
		myRentBookModels.add(new DefaultTableModel());
		
		DefaultTableModel firstModel = myRentBookModels.get(curModelIndex);
		
		firstModel.addColumn("대여자");
		firstModel.addColumn("도서제목");
		firstModel.addColumn("대여날짜");
		firstModel.addColumn("반납날짜");
		
		myRentBookList = new JTable(firstModel);
		myRentBookList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
		myRentBookList.setBounds(250,380, 550, 325);
		myRentBookList.getTableHeader().setBounds(250, 353, 550, 25);
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
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
	}

	@Override
	public void openEvent() {
		// TODO Auto-generated method stub
		
		MemberVO vo = BookManagementMainFrame.getInstance().getAccountInfo();
		
		curModelIndex = 0;
		bookModelList.clear();
		bookModelList.add(new DefaultListModel());
		
		NetClient net = BookManagementMainFrame.getInstance().getNetClient();
		
		SELECT_ALL_BOOK_DATA_REQ req = new SELECT_ALL_BOOK_DATA_REQ();
		SELECT_ALL_BOOK_DATA_ACK ack = null;
		
		try {
			Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
			net.send(packet);
			
			ack = (SELECT_ALL_BOOK_DATA_ACK)net.recv();
			if(ack.netError == NetError.NET_OK && ack.bookList.size() > 0) {
				for(int i = 0; i < ack.bookList.size(); ++i) {
					if((i % 9 == 0) && i != 0) {
						curModelIndex += 1;
						DefaultListModel model = new DefaultListModel();
						bookModelList.add(model);
					}
					int colIndex = i % 9;
					
					BookVO bookVO = ack.bookList.get(i);
					bookModelList.get(curModelIndex).add(colIndex, bookVO.getBookTitle());
					
					bookNoByTitle.put(bookVO.getBookTitle(), bookVO.getBookNo());
				}
			}
			
			// 첫번째 모델 리스트부터 보여준다
			bookList.setModel(bookModelList.get(0));
			curModelIndex = 0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		this.repaint();
	}
	
	private void setButtonListener() {
		ActionListener bookPrevBtnAction = (ActionEvent e) -> {
			if(curModelIndex - 1 < 0)
				return;
			
			bookList.setModel(bookModelList.get(--curModelIndex));
		};
		
		ActionListener bookNextBtnAction = (ActionEvent e) -> {
			if(curModelIndex + 1 >= bookModelList.size())
				return;
			
			bookList.setModel(bookModelList.get(++curModelIndex));
		};
		
		ActionListener backBtnAction = (ActionEvent e) -> {
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.MainPanel);
		};
		
		ActionListener rentBookPrevBtnAction = (ActionEvent e) -> {
			if(curTableModelIndex - 1 < 0)
				return;
			
			myRentBookList.setModel(myRentBookModels.get(--curModelIndex));
		};
		
		ActionListener rentBookNextBtnAction = (ActionEvent e) -> {
			if(curModelIndex + 1 >= myRentBookModels.size())
				return;

			myRentBookList.setModel(myRentBookModels.get(++curModelIndex));
		};
		
		MouseListener mouseListener = new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				String selectedItem = (String)bookList.getSelectedValue();
				if(!bookNoByTitle.containsKey(selectedItem)) {
					Util.ErrDialog(BookHistoryPanel.this, "없는 도서를 선택했습니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				showBookHistory(bookNoByTitle.get(selectedItem));
		    }
		};
		
		bookPrevBtn.addActionListener(bookPrevBtnAction);
		bookNextBtn.addActionListener(bookNextBtnAction);
		bookList.addMouseListener(mouseListener);
		backBtn.addActionListener(backBtnAction);
		rentBookPrevBtn.addActionListener(rentBookPrevBtnAction);
		rentBookNextBtn.addActionListener(rentBookNextBtnAction);
	}
	
	private void showBookHistory(int bookNo) {
		curTableModelIndex = 0;
		myRentBookModels.clear();
		myRentBookModels.add(new DefaultTableModel());
		
		myRentBookModels.get(0).addColumn("대여자");
		myRentBookModels.get(0).addColumn("도서제목");
		myRentBookModels.get(0).addColumn("대여날짜");
		myRentBookModels.get(0).addColumn("반납날짜");
		
		NetClient net = BookManagementMainFrame.getInstance().getNetClient();
		BOOK_HISTORY_RENT_REQ req = new BOOK_HISTORY_RENT_REQ();
		BOOK_HISTORY_RENT_ACK ack = null;
		
		req.bookNo = bookNo;
		try {
			Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
			net.send(packet);
			
			ack = (BOOK_HISTORY_RENT_ACK)net.recv();
			if(ack.netError == NetError.NET_OK) {
				List<String> nickNames = ack.memberNicknameList;
				List<BookHistoryVO> historyVOs = ack.historyVOList;
				List<BookVO> bookVOs = ack.bookVOList;
				
				DefaultTableModel myRentBookModel = myRentBookModels.get(0);
				for(int idx = 0; idx < nickNames.size(); ++idx) {
					if(idx % 13 == 0 && idx != 0) {
						myRentBookModel = new DefaultTableModel();
						myRentBookModels.add(myRentBookModel);
						
						myRentBookModel.addColumn("대여자");
						myRentBookModel.addColumn("도서제목");
						myRentBookModel.addColumn("대여날짜");
						myRentBookModel.addColumn("반납날짜");
					}
					
					String formattedRentString = "";
					String formattedReturnString = "";
					String nickName = nickNames.get(idx);
					String bookTitle = bookVOs.get(idx).getBookTitle();
					
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
					
					myRentBookModel.addRow(new Object[] {nickName, bookTitle, formattedRentString, formattedReturnString});
				}
				
				myRentBookList.setModel(myRentBookModels.get(0));
			}
			else {
				Util.ErrDialog(this, "에러 발생", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		this.repaint();
	}
}
