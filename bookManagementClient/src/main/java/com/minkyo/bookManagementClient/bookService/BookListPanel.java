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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;
import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_ACK;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_REQ;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.RENT_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.RENT_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.RETURN_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.RETURN_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_ACK;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;

import CommonUtils.Utils;
import PacketUtils.Packet;
import PacketUtils.PacketUtil;
import SockNet.NetClient;

public class BookListPanel extends EventPanel {
	private BookPanelType pnType;
	private JButton backBtn = new JButton("돌아가기");
	
	private static final int MAX_ONEPAGE_BOOK_COUNT = 20;
	private static final int MAX_TABLE_ROW_COUNT = 8;
	private JList bookList = null;
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
	private JButton imageChoiceBtn = null;
	private JButton submitBtn = null;
	private JLabel imageLabel = new JLabel("이미지 추가");
	private JFileChooser fileChooser = new JFileChooser();
	private JButton imgBtn = null; 
	private JButton returnBookBtn = new JButton("반납하기");
	
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
		returnBookBtn.setBounds(800,602,100,50);
		returnBookBtn.setVisible(false);
		
		bookRefreshBtn.setFont(new Font("Dialog",Font.BOLD, 18));
		
		initJList();
		setButtonListener();
		
		add(backBtn);
		add(bookList);
		add(bookPrevBtn);
		add(bookNextBtn);
		add(bookRentBtn);
		add(bookRefreshBtn);
		add(returnBookBtn);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
	}
	
	private void initJList() {
		bookList = new JList();
		DefaultListModel model = new DefaultListModel();
		bookModelList.add(model);

		bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookList.setBounds(250,50,450,600);
		bookList.setFont(new Font("Serif",Font.BOLD, 21));
	}

	private void setButtonListener() {
		ActionListener backBtnAction = (ActionEvent e) -> {
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
		
		ActionListener bookRefreshBtnAction = (ActionEvent e) -> {
			openEvent();
		};
		
		ActionListener bookRentBtnAction = (ActionEvent e) -> {
			if(selectedBookInfo == null) {
				Util.ErrDialog(this, "대여할 책을 선택해주세요.", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if(!selectedBookInfo.isCanRent()) {
				Util.ErrDialog(this, "이미 대여중인 도서입니다.", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			//대여하기 추가
			NetClient net = BookManagementMainFrame.getInstance().getNetClient();
			RENT_BOOK_REQ req = new RENT_BOOK_REQ();
			RENT_BOOK_ACK ack = null;
			
			MemberVO thisMember = BookManagementMainFrame.getInstance().getAccountInfo();
			
			req.bookNo = selectedBookInfo.getBookNo();
			req.memberUID = thisMember.getMemberUID();
			
			try {
				Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
				net.send(packet);
				
				ack = (RENT_BOOK_ACK)net.recv();
				if(ack.netError == NetError.NET_OK) {
					bookRentBtn.setText("대여중");
					
					String text = selectedBookInfo.getBookTitle().getText();
					String removeFront = text.replace("<html><body>", "");
					String removeBack = removeFront.replace("</body></html>", "");
					
					Util.ErrDialog(this, "대여완료. 도서명 : " + removeBack + ", 대여자 : " + thisMember.getMemberNickName(), JOptionPane.INFORMATION_MESSAGE);
				}
				else if(ack.netError == NetError.NET_ALREADY_RENT_BOOK) {
					Util.ErrDialog(this, "대여중인 도서 입니다.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(Exception e2) {
				e2.printStackTrace();
				return;
			}
			
		};
		
		ActionListener returnBookBtnAction = (ActionEvent e) -> {
			if(selectedBookInfo == null) {
				Util.ErrDialog(this, "도서 선택을 하지않았습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(selectedBookInfo.getRentNo() <= 0) {
				Util.ErrDialog(this, "반납이 불가능한 도서입니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			MemberVO thisMember = BookManagementMainFrame.getInstance().getAccountInfo();
			if(selectedBookInfo.getRentMemberUID() != thisMember.getMemberUID()) {
				Util.ErrDialog(this, "대여자만 반납이 가능합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			NetClient net = BookManagementMainFrame.getInstance().getNetClient();
			RETURN_BOOK_REQ req = new RETURN_BOOK_REQ();
			RETURN_BOOK_ACK ack = null;
			
			req.rentNo = selectedBookInfo.getRentNo();
			try {
				Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
				net.send(packet);
				
				ack = (RETURN_BOOK_ACK)net.recv();
				if(ack.netError == NetError.NET_OK) {
					bookRentBtn.setText("대여 가능");
					returnBookBtn.setVisible(false);
					
					String text = selectedBookInfo.getBookTitle().getText();
					String removeFront = text.replace("<html><body>", "");
					String removeBack = removeFront.replace("</body></html>", "");
					
					Util.ErrDialog(this, "반납완료. 도서명 : " + removeBack + ", 대여자 : " + thisMember.getMemberNickName(), JOptionPane.INFORMATION_MESSAGE);
				}
				else if(ack.netError == NetError.NET_ALREADY_RENT_BOOK) {
					Util.ErrDialog(this, "대여중인 도서 입니다.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(Exception e2) {
				e2.printStackTrace();
				return;
			}
		};
		
		MouseListener mouseListener = new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				String selectedItem = (String) bookList.getSelectedValue();
		        
				if(selectedBookInfo != null) {
					String text = selectedBookInfo.getBookTitle().getText();
					String removeFront = text.replace("<html><body>", "");
					String removeBack = removeFront.replace("</body></html>", "");
					
					if(selectedItem.equals(removeBack))
						return;
				}
		           
		        if(!bookVOByBookTitle.containsKey(selectedItem)) {
		        	Util.ErrDialog(BookListPanel.this, "존재하지 않는 책정보를 선택", JOptionPane.ERROR_MESSAGE);
		        	return;
		        }
		           
		        BookVO bookVO = bookVOByBookTitle.get(selectedItem);
		        selectedBookInfo = setBookInfo(bookVO);
		        
		        // 만약 대여중인 사람이 자기자신이라면. 반납버튼을 띄운다.
		        if(selectedBookInfo.getRentMemberUID() == BookManagementMainFrame.getInstance().getAccountInfo().getMemberUID())
		        	returnBookBtn.setVisible(true);
		        else
		        	returnBookBtn.setVisible(false);
		    }
		};
		
		bookList.addMouseListener(mouseListener);
		backBtn.addActionListener(backBtnAction);
		bookPrevBtn.addActionListener(bookPrevBtnAction);
		bookNextBtn.addActionListener(bookNextBtnAction);
		bookRentBtn.addActionListener(bookRentBtnAction);
		bookRefreshBtn.addActionListener(bookRefreshBtnAction);
		returnBookBtn.addActionListener(returnBookBtnAction);
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
		
//		bookIntroduce.setBounds(120,140, 300, 20);
//		introduceLabel.setBounds(75,133, 100, 30);
//		dialog.add(bookIntroduce);
//		dialog.add(introduceLabel);
		
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
			
			Image img = Util.iconToImage(imgBtn.getIcon());
			BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
			Graphics bg = bi.getGraphics();
			bg.drawImage(img, 0, 0, null);
			bg.dispose();
			
			NetClient net = BookManagementMainFrame.getInstance().getNetClient();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bi, "jpg", byteArrayOutputStream);
				//byte[] compressedData = Utils.compress(byteArrayOutputStream.toByteArray(), Deflater.BEST_COMPRESSION, false);
				req.imageBuffer = byteArrayOutputStream.toByteArray();
				
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
		imgBtn.setVisible(false);
	}
	
	private BookInfo setBookInfo(BookVO bookVO) {
		if(selectedBookInfo != null) {
			this.remove(selectedBookInfo.getBookTitle());
			this.remove(selectedBookInfo.getBookAuthor());
			this.remove(selectedBookInfo.getBookImg());
			this.remove(selectedBookInfo.getBookPublisher());
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
		
		NetClient net = BookManagementMainFrame.getInstance().getNetClient();
		ADDITIONAL_BOOK_INFO_REQ req = new ADDITIONAL_BOOK_INFO_REQ();
		ADDITIONAL_BOOK_INFO_ACK ack = null;
		
		req.bookNo = bookVO.getBookNo();
		req.bookTitle = bookVO.getBookTitle();
		
		localPath += "\\" + bookVO.getBookTitle() + ".jpg";
		if(!Util.existFile(localPath)) {
			req.existImageFile = false; // 없으면 이미지 요청.
		}
		else { 
			bookImage = Util.getImageByFullPath(localPath);
			if(bookImage == null)
				return null;
			
			req.existImageFile = true;
		}
		
		try {
			Packet packet = PacketUtil.convertPacketFromBytes(PacketUtil.genPacketBuffer(1, req));
			net.send(packet);
			ack = (ADDITIONAL_BOOK_INFO_ACK)net.recv();
			if(ack.netError != NetError.NET_OK) {
				return null;
			}

			if(!req.existImageFile) {
				//byte[] decompressedData = Utils.decompress(ack.imageBuffer, false);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(ack.imageBuffer));

				File outputfile = new File(localPath);
				if(!ImageIO.write(image, "jpg", outputfile))
					return null;

				bookImage = ImageIO.read(new File(localPath));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if(!ack.canRentBook) {
			if(ack.rentMemberUID == BookManagementMainFrame.getInstance().getAccountInfo().getMemberUID()) {
				bookRentBtn.setText("대여중");
			}
			else {
				bookRentBtn.setText("대여불가");
			}
			
			newBook.setRentMemberUID(ack.rentMemberUID);
			newBook.setCanRent(false);
		}
		else {
			bookRentBtn.setText("대여 가능");
			newBook.setCanRent(true);
		}
		
		Icon icon = Util.resize(
				new ImageIcon(bookImage), 
				imgBtn.getWidth(),
				imgBtn.getHeight()
		);
		imgBtn.setIcon(icon);

		newBook.setBookImg(imgBtn);
		newBook.setBookNo(bookVO.getBookNo());
		newBook.setRentNo(ack.rentNo);
		
		Font font = new Font("Serif",Font.PLAIN, 16);
		
		JLabel bookTitleLabel = new JLabel("<html><body>" + bookVO.getBookTitle() + "</body></html>");
		bookTitleLabel.setBounds(750, 300, BookInfo.DEFAULT_LABEL_WIDTH + 40, BookInfo.DEFAULT_LABEL_HEIGHT + 10);
		bookTitleLabel.setFont(new Font("Serif", Font.BOLD, 16));
		newBook.setBookTitle(bookTitleLabel);
		
		JLabel bookAuthorLabel = new JLabel("<html><body>" + "저자 : " + bookVO.getBookAuthor() + "</body></html>");
		bookAuthorLabel.setBounds(750, 335, BookInfo.DEFAULT_LABEL_WIDTH + 40, BookInfo.DEFAULT_LABEL_HEIGHT + 30);
		bookAuthorLabel.setFont(font);
		newBook.setBookAuthor(bookAuthorLabel);
		
		JLabel bookPublisherLabel = new JLabel("출판사 : " + bookVO.getBookPublisher());
		bookPublisherLabel.setBounds(750, 390, BookInfo.DEFAULT_LABEL_WIDTH + 40, BookInfo.DEFAULT_LABEL_HEIGHT);
		bookPublisherLabel.setFont(font);
		newBook.setBookPublisher(bookPublisherLabel);
		
		this.add(newBook.getBookImg());
		this.add(newBook.getBookTitle());
		this.add(newBook.getBookAuthor());
		this.add(newBook.getBookPublisher());

		this.setComponentZOrder(newBook.getBookImg(), 5);
		this.setComponentZOrder(newBook.getBookTitle(), 5);
		this.setComponentZOrder(newBook.getBookAuthor(), 5);
		this.setComponentZOrder(newBook.getBookPublisher(), 5);
		
		BookManagementMainFrame.getInstance().repaint();
		this.repaint();

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
				
				String fileName = file.getName();
				String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if(fileExtension.equals("jpg") || fileExtension.equals("jpeg")) {
					imgBtn.setIcon(Util.resize(new ImageIcon(image), 140, 170));
					imgBtn.setVisible(true);
				}
				else {
					Util.ErrDialog(this, "jpg확장자 파일만 업로드 가능합니다.", JOptionPane.INFORMATION_MESSAGE);
				}
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
					if((i % 19 == 0) && i != 0) {
						curModelIndex += 1;
						DefaultListModel model = new DefaultListModel();
						bookModelList.add(model);
					}
					int colIndex = i % 19;
					
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

