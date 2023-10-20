package com.minkyo.bookManagementClient.bookService;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;
import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_ACK;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_ACK;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;

import CommonUtils.Utils;
import PacketUtils.Packet;
import PacketUtils.PacketUtil;
import SockNet.NetClient;

public class BookListPanel extends JPanel {
	private BookPanelType pnType;
	private JButton backBtn = new JButton("돌아가기");
	
	private static final int MAX_ONEPAGE_BOOK_COUNT = 20;
	private static final int MAX_TABLE_ROW_COUNT = 8;
	private JList bookList = null;
	private JList bookBackground = null;
	private List<DefaultListModel> bookModelList = new ArrayList<DefaultListModel>();	
	private int curModelIndex = 0;
	
	private JButton bookPrevBtn = new JButton("◀");
	private JButton bookNextBtn = new JButton("▶");
	
	private JButton bookRentBtn = new JButton("대여하기");
	private JButton bookRefreshBtn = new JButton("⟲");
	
	private Map<String,BookVO> bookVOByBookTitle = new HashMap<String, BookVO>(); 
	private BookInfo selectedBookInfo = null;
	
	private boolean isOpenBookRegistDialog = false;
	
	private JButton bookRegistBtn = null;
	private JDialog dialog = null;
	private JTextField bookTitle = null;
	private JLabel titleLabel = null;
	private JTextField bookAuthor = null;
	private JLabel authorLabel = null;
	private JTextField bookPublisher = null;
	private JLabel publisherLabel = null;
	private JTextField bookIntroduce = null;
	private JLabel introduceLabel = null;
	private JButton imageChoiceBtn = null;
	private JButton submitBtn = null;
	private JLabel imageLabel = new JLabel("이미지 추가");
	private JFileChooser fileChooser = new JFileChooser();
	private JButton imgBtn = null; 
	
	//private Map<String>
	
	public BookListPanel(BookPanelType pnType) {
		this.pnType = pnType;
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		backBtn.setBounds(20,52,100,50);
		
		bookPrevBtn.setBounds(250,660, 70, 50);
		bookNextBtn.setBounds(630,660, 70,50);
		
		bookRentBtn.setBounds(325,660, 225, 50);
		bookRefreshBtn.setBounds(555, 660, 70, 50);
		
		bookRefreshBtn.setFont(new Font("Dialog",Font.BOLD, 18));
		
		initJList();
		setButtonListener();
		
		add(backBtn);
		add(bookList);
		add(bookBackground);
		add(bookPrevBtn);
		add(bookNextBtn);
		add(bookRentBtn);
		add(bookRefreshBtn);	
		
		this.setComponentZOrder(bookBackground, 3);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
	}
	
	private void initJList() {
		bookList = new JList();
		DefaultListModel model = new DefaultListModel();
		bookModelList.add(model);
		bookBackground = new JList();
		bookBackground.setBounds(720,50,250,600);
		bookBackground.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookList.setBounds(250,50,450,600);
		bookList.setFont(new Font("Serif",Font.BOLD, 21));
	}

	private void setButtonListener() {
		ActionListener backBtnAction = (ActionEvent e) -> {
			// 로그인 가능한지 패킷 송신
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.MainPanel);
		};
		
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
		
		MouseListener mouseListener = new MouseAdapter() {
			@Override
		    public void mouseClicked(MouseEvent e) {
		        if(e.getClickCount() >= 1) {
		           String selectedItem = (String) bookList.getSelectedValue();
		           //DefaultListModel model = (DefaultListModel) bookList.getModel();
		           
		           if(!bookVOByBookTitle.containsKey(selectedItem)) {
		        	   Util.ErrDialog(BookListPanel.this, "존재하지 않는 책정보를 선택", JOptionPane.ERROR_MESSAGE);
		        	   return;
		           }
		           
		           BookVO bookVO = bookVOByBookTitle.get(selectedItem);
		           setBookInfo(bookVO);
		        }
		    }
		};
		
		bookList.addMouseListener(mouseListener);
		backBtn.addActionListener(backBtnAction);
		bookPrevBtn.addActionListener(bookPrevBtnAction);
		bookNextBtn.addActionListener(bookNextBtnAction);
	}
	
	private void initRegistBookDialog() {
		bookRegistBtn = new JButton("도서등록");
		bookRegistBtn.setBounds(20, 130, 100, 50);
		add(bookRegistBtn);
		
		dialog = new JDialog();
		bookTitle = new JTextField();
		titleLabel = new JLabel("도서 제목:");
		bookAuthor = new JTextField();
		authorLabel = new JLabel("저자:");
		bookPublisher = new JTextField();
		publisherLabel = new JLabel("출판사:");
		bookIntroduce = new JTextField();
		introduceLabel = new JLabel("소개글:");
		imageChoiceBtn = new JButton("imageChoiceBtn!");
		submitBtn = new JButton("등록");
		imageLabel = new JLabel("이미지 추가");
		fileChooser = new JFileChooser();
		imgBtn = new JButton();
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosed(WindowEvent arg0) {
				isOpenBookRegistDialog = false;
                resetRegistDialog();
            }

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				isOpenBookRegistDialog = false;
                resetRegistDialog();
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
		
		dialog.setTitle("도서 등록");
		dialog.setSize(500, 400);
		dialog.setLayout(null);
		Dimension windowDimension = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation(windowDimension.width / 2 - 150, windowDimension.height / 2 - 180);
		
		bookTitle.setBounds(120,50,300,20);
		titleLabel.setBounds(60,43,100,30);
		dialog.add(titleLabel);
		dialog.add(bookTitle);
		
		bookAuthor.setBounds(120,80,300,20);
		authorLabel.setBounds(87, 73, 100, 30);
		dialog.add(authorLabel);
		dialog.add(bookAuthor);
		
		bookPublisher.setBounds(120,110, 300, 20);
		publisherLabel.setBounds(75,103, 100, 30);
		dialog.add(bookPublisher);
		dialog.add(publisherLabel);
		
		bookIntroduce.setBounds(120,140, 300, 20);
		introduceLabel.setBounds(75,133, 100, 30);
		dialog.add(bookIntroduce);
		dialog.add(introduceLabel);
		
		imageLabel.setBounds(51, 163, 100, 30);

		
		imageChoiceBtn.setBounds(72,193,40,40);
		dialog.add(imageChoiceBtn);
		dialog.add(imageLabel);
		
		submitBtn.setBounds(280,180,140,170);
		dialog.add(submitBtn);

		imgBtn.setBorderPainted(false);
		imgBtn.setBounds(130,180,140,170);
		imgBtn.setVisible(false);
		dialog.add(imgBtn);
		

		ActionListener bookRegistBtnAction = (ActionEvent e) -> {
			dialog.setVisible(true);
		};

		
		ActionListener imgAddBtnAction = (ActionEvent e3) -> {
			int retVal = fileChooser.showOpenDialog(getParent());
			if(retVal == JFileChooser.APPROVE_OPTION)
				choiceImage(dialog);
		};
		
		ActionListener submitBtnAction = (ActionEvent e4) -> {
			ADMIN_REGIST_BOOK_REQ req = new ADMIN_REGIST_BOOK_REQ();
			ADMIN_REGIST_BOOK_ACK ack = null;
			
			req.bookTitle = bookTitle.getText();
			req.bookAuthor = bookAuthor.getText();
			req.bookPublisher = bookPublisher.getText();
			req.bookIntroduce = bookIntroduce.getText();
			
			Image img = Util.iconToImage(imgBtn.getIcon());
			BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
			Graphics bg = bi.getGraphics();
			bg.drawImage(img, 0, 0, null);
			bg.dispose();
			
			NetClient net = BookManagementMainFrame.getInstance().getNetClient();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bi, "jpg", byteArrayOutputStream);
				byte[] compressedData = Utils.compress(byteArrayOutputStream.toByteArray(), Deflater.BEST_COMPRESSION, false);
				req.imageBuffer = compressedData;
				
				Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
				net.send(packet);
				
				ack = (ADMIN_REGIST_BOOK_ACK)net.recv();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} 
			
			if(ack.netError != NetError.NET_OK) {
				Util.ErrDialog(this, "도서 등록 실패", JOptionPane.ERROR_MESSAGE);
			}
			else {
				Util.ErrDialog(this, "도서 등록 성공", JOptionPane.INFORMATION_MESSAGE);
			}
			
			resetRegistDialog();
			dialog.setVisible(false);
		};
		
		imageChoiceBtn.addActionListener(imgAddBtnAction);
		submitBtn.addActionListener(submitBtnAction);
		bookRegistBtn.addActionListener(bookRegistBtnAction);
	}
	
	private void resetRegistDialog() {
		bookTitle.setText("");
		bookAuthor.setText("");
		bookPublisher.setText("");
		bookIntroduce.setText("");
		imgBtn.setVisible(false);
	}
	
	private BookInfo setBookInfo(BookVO bookVO) {
		if(selectedBookInfo != null) {
			this.remove(selectedBookInfo.getBookTitle());
			this.remove(selectedBookInfo.getBookAuthor());
			this.remove(selectedBookInfo.getBookImg());
			this.remove(selectedBookInfo.getBookPublisher());
			this.remove(selectedBookInfo.getBookIntroduce());
//			this.remove(selectedBookInfo.getHistoryTable());
//			this.remove(selectedBookInfo.getHistoryNextBtn());
//			this.remove(selectedBookInfo.getHistoryPrevBtn());
		}
		
		BookInfo newBook = new BookInfo();
		Image bookImage = null;
		
		JButton imgBtn = new JButton();
		imgBtn.setBorderPainted(false);
		imgBtn.setBounds(750,52,BookInfo.MAX_BOOK_IMG_WIDTH,BookInfo.MAX_BOOK_IMG_HEIGHT);
		imgBtn.setBackground(new Color(255,255,255));
		
		// 이미지 정보를 받아옴, 로컬에 있다면 로컬에서 읽어온다.
		String localPath = "C:\\bookManagementBookImg";
		
		if(!Util.existDir(localPath)) {
			if(!Util.createLocalDir(localPath))
				return null;
		}
		
		localPath += "\\" + bookVO.getBookTitle() + ".jpg";
		if(!Util.existFile(localPath)) {
			// 없으면 이미지 요청
			NetClient net = BookManagementMainFrame.getInstance().getNetClient();
			try {
				BOOK_IMAGE_REQ req = new BOOK_IMAGE_REQ();
				BOOK_IMAGE_ACK ack = null;
				
				req.bookNo = bookVO.getBookNo();
				req.bookTitle = bookVO.getBookTitle();
				
				Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
				net.send(packet);
				ack = (BOOK_IMAGE_ACK)net.recv();
				if(ack.netError != NetError.NET_OK) {
					return null;
				}
				
				byte[] decompressedData = Utils.decompress(ack.imageBuffer, false);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(decompressedData));

				File outputfile = new File(localPath);
				if(!ImageIO.write(image, "jpg", outputfile))
					return null;
				
				bookImage = ImageIO.read(new File(localPath));
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else { 
			bookImage = Util.getImageByFullPath(localPath);
			if(bookImage == null)
				return null;
		}
		Icon icon = Util.resize(
				new ImageIcon(bookImage), 
				imgBtn.getWidth(),
				imgBtn.getHeight()
		);
		imgBtn.setIcon(icon);

		newBook.setBookImg(imgBtn);
		newBook.setBookNo(bookVO.getBookNo());
		
		Font font = new Font("Serif",Font.PLAIN, 16);
		
		JLabel bookTitleLabel = new JLabel(bookVO.getBookTitle());
		bookTitleLabel.setBounds(750, 310, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookTitleLabel.setFont(new Font("Serif", Font.BOLD, 16));
		newBook.setBookTitle(bookTitleLabel);
		
		JLabel bookAuthorLabel = new JLabel("저자 : " + bookVO.getBookAuthor());
		bookAuthorLabel.setBounds(750, 340, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookAuthorLabel.setFont(font);
		newBook.setBookAuthor(bookAuthorLabel);
		
		JLabel bookPublisherLabel = new JLabel("출판사 : " + bookVO.getBookPublisher());
		bookPublisherLabel.setBounds(750, 370, BookInfo.DEFAULT_LABEL_WIDTH, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookPublisherLabel.setFont(font);
		newBook.setBookPublisher(bookPublisherLabel);
		
		JLabel bookIntroduceLabel = new JLabel("<html><body>" + bookVO.getBookIntroduce() + "</body></html>");
		bookIntroduceLabel.setBounds(750, 400, BookInfo.DEFAULT_LABEL_WIDTH + 50, 600);
		bookIntroduceLabel.setFont(font);
		newBook.setBookIntroduce(bookIntroduceLabel);
		
		add(imgBtn);
		add(bookTitleLabel);
		add(bookAuthorLabel);
		add(bookPublisherLabel);
		add(bookIntroduceLabel);
//		
		this.setComponentZOrder(imgBtn, 5);
		this.setComponentZOrder(bookTitleLabel, 5);
		this.setComponentZOrder(bookAuthorLabel, 5);
		this.setComponentZOrder(bookPublisherLabel, 5);
		this.setComponentZOrder(bookIntroduceLabel, 5);
		

//		
//		String[] header = {"이름","대여날짜","반납날짜"};
//		String[][] contents = { 
//				{"전민교","22/04/04","22/04/10"},
//				{"전민교","22/04/04","22/04/10"},
//				{"전민교","22/04/04","22/04/10"},
//				{"전민교","22/04/04","22/04/10"},
//				{"전민교","22/04/04","22/04/10"},
//				{"전민교","22/04/04","22/04/10"},
//		};
//		
//		bookInfo.setHistoryTable(new JTable(contents, header));
//		
//		JTable historyTable = bookInfo.getHistoryTable();
//		historyTable.setBounds(750,510, 200, 100);
//		historyTable.getTableHeader().setBounds(750, 480, 200, 30);
//		historyTable.getTableHeader().setFont(new Font("Serif",Font.BOLD, 12));
//		historyTable.getTableHeader().setReorderingAllowed(false);
//		historyTable.getTableHeader().setResizingAllowed(false);
//		
//		JButton bookHistoryPrevBtn = bookInfo.getHistoryPrevBtn();
//		JButton bookHistoryNextBtn = bookInfo.getHistoryNextBtn();
//		bookHistoryPrevBtn.setBounds(750,610,40,35);
//		bookHistoryPrevBtn.setFont(new Font("Serif",Font.BOLD, 7));
//		bookHistoryNextBtn.setBounds(910,610,40,35);
//		bookHistoryNextBtn.setFont(new Font("Serif",Font.BOLD, 7));
//
//		add(historyTable.getTableHeader());
//		add(bookInfo.getBookImg());
//		add(bookInfo.getBookTitle());
//		add(bookInfo.getBookPublisher());
//		add(bookInfo.getBookAuthor());
//		add(bookInfo.getBookIntroduce());
//		add(historyTable);
//		add(bookHistoryPrevBtn);
//		add(bookHistoryNextBtn);
//		this.setComponentZOrder(bookHistoryPrevBtn, 1);
//		this.setComponentZOrder(bookHistoryNextBtn, 1);
//
//		// 이미지를 보이게하기 위해 order 앞으로 댕김
//		this.setComponentZOrder(bookInfo.getBookImg(), 1);
//		this.setComponentZOrder(bookInfo.getBookTitle(), 1);
//		this.setComponentZOrder(bookInfo.getBookAuthor(), 1);
//		this.setComponentZOrder(bookInfo.getBookPublisher(), 1);
//		this.setComponentZOrder(bookInfo.getBookIntroduce(), 1);
//		this.setComponentZOrder(historyTable, 4);
//		this.setComponentZOrder(historyTable.getTableHeader(), 4);
		
		selectedBookInfo = newBook;
		return newBook;
	}
	
	private void choiceImage(JDialog dialog) {
		File file = fileChooser.getSelectedFile();
		if(file != null) {
			try {
				Image image = Util.getImageByFullPath(file.getAbsolutePath());
				if(image == null) {
					Util.ErrDialog(this, "이미지 파일이 아닙니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				imgBtn.setIcon(Util.resize(new ImageIcon(image), 140, 170));
				imgBtn.setVisible(true);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else
			Util.ErrDialog(this, "이미지를 불러오는데 실패 했습니다.", JOptionPane.ERROR_MESSAGE);

	}

	public void openEvent() {
		// TODO Auto-generated method stub
		MemberVO vo = BookManagementMainFrame.getInstance().getAccountInfo();
		if(vo != null && vo.isAdmin()) {
			initRegistBookDialog();
			bookRegistBtn.setVisible(true);
		}
		
		NetClient net = BookManagementMainFrame.getInstance().getNetClient();
		
		SELECT_ALL_BOOK_DATA_REQ req = new SELECT_ALL_BOOK_DATA_REQ();
		SELECT_ALL_BOOK_DATA_ACK ack = null;
		
		try {
			Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
			net.send(packet);
			
			ack = (SELECT_ALL_BOOK_DATA_ACK)net.recv();
			if(ack.netError == NetError.NET_OK && ack.bookList.size() > 0) {
				for(int i = 0; i < ack.bookList.size(); ++i) {
					if((i % 20 == 0) && i != 0) {
						curModelIndex += 1;
						DefaultListModel model = new DefaultListModel();
						bookModelList.add(model);
					}
					int colIndex = i % 20;
					
					BookVO bookVO = ack.bookList.get(i);
					bookModelList.get(curModelIndex).add(colIndex, bookVO.getBookTitle());
					
					bookVOByBookTitle.put(bookVO.getBookTitle(), bookVO);
				}
			}
			
			// 첫번째 모델 리스트부터 보여준다
			bookList.setModel(bookModelList.get(0));
			curModelIndex = 0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}

