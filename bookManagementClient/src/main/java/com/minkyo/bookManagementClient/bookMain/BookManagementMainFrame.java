package com.minkyo.bookManagementClient.bookMain;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookService.BookJoinPanel;
import com.minkyo.bookManagementClient.bookService.BookListPanel;
import com.minkyo.bookManagementClient.bookService.BookLoginPanel;
import com.minkyo.bookManagementClient.bookService.BookMainPanel;
import com.minkyo.bookManagementClient.bookService.BookRequestBoardPanel;

import SockNet.NetClient;

public class BookManagementMainFrame extends JFrame {
	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;
	public static Image shareDefaultBackgroundImage = null;
	public static Image colorBackgroundImage = null;
	
	private static BookManagementMainFrame inst = null;
	private NetClient net = new NetClient();
	private HashMap<BookPanelType, JPanel> panelByType = new HashMap<>();
	private JPanel currentShowPanel = null; 
	
	private BookManagementMainFrame() {}
	
	public static BookManagementMainFrame getInstance() {
		if(inst == null)
			inst = new BookManagementMainFrame();
		return inst;
	}
	
	public NetClient getNetClient() {
		return net;
	}
	
	public void init() {
		try {
			// 연결한 IP(도메인주소)와 Port번호를 입력.
			net.startToConnect("localhost", 9999);
		}
		catch(Exception e) {
			String error = "네트워크 연결에 실패했습니다." + e.getMessage();
			Util.ErrDialog(inst, error, JOptionPane.ERROR_MESSAGE );
			return;
		}
		
		
		shareDefaultBackgroundImage = Util.resize(
				new ImageIcon(Util.getImageFile("rabbitBackgroundImg.jpg")), 
				BookManagementMainFrame.SCREEN_WIDTH,
				BookManagementMainFrame.SCREEN_HEIGHT
		).getImage();
		
		colorBackgroundImage = Util.resize(
				new ImageIcon(Util.getImageFile("defaultBackgroundColor.jpg")), 
				BookManagementMainFrame.SCREEN_WIDTH,
				BookManagementMainFrame.SCREEN_HEIGHT
		).getImage();
		
		super.setTitle("도서관리 프로그램(전민교)");
		super.setIconImage(Util.resize(new ImageIcon(Util.getImageFile("bookIcon.jpg")), 100, 100).getImage());
		
		panelInit();
		
		super.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		super.setResizable(false);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(true);
		
	}
	
	public void changePanel(BookPanelType panelType) {
		if(!panelByType.containsKey(panelType)) {
			Util.ErrDialog(inst, "없는 패널 타입으로 전환시도", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.remove(currentShowPanel);
		
		JPanel changePanel = panelByType.get(panelType);
		currentShowPanel.setVisible(false);
		changePanel.setVisible(true);
		
		currentShowPanel = changePanel;
		this.add(currentShowPanel);
		this.repaint();
		currentShowPanel.repaint();
	}
	
	private void panelInit() {
		BookLoginPanel loginPanel = new BookLoginPanel(BookPanelType.LoginPanel);
		BookJoinPanel joinPanel = new BookJoinPanel(BookPanelType.JoinPanel);
		BookListPanel listPanel = new BookListPanel(BookPanelType.BookListPanel);
		BookMainPanel mainPanel = new BookMainPanel(BookPanelType.MainPanel);
		BookRequestBoardPanel reqBoardPanel = new BookRequestBoardPanel(BookPanelType.BookRequestBoardPanel);
		
		panelByType.put(BookPanelType.LoginPanel, loginPanel);
		panelByType.put(BookPanelType.JoinPanel, joinPanel);
		panelByType.put(BookPanelType.BookListPanel, listPanel);
		panelByType.put(BookPanelType.MainPanel, mainPanel);
		panelByType.put(BookPanelType.BookRequestBoardPanel, reqBoardPanel);
		
		setUnvisiblePanels();
		
		currentShowPanel = loginPanel;
		this.add(loginPanel);
		loginPanel.setVisible(true);
	}
	
	private void setUnvisiblePanels() {
		panelByType.entrySet().stream().forEach(obj -> obj.getValue().setVisible(false));
	}
	
	//public void recv
}
