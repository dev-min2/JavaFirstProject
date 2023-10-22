package com.minkyo.bookManagementClient.bookMain;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookService.BookHistoryPanel;
import com.minkyo.bookManagementClient.bookService.BookJoinPanel;
import com.minkyo.bookManagementClient.bookService.BookListPanel;
import com.minkyo.bookManagementClient.bookService.BookLoginPanel;
import com.minkyo.bookManagementClient.bookService.BookMainPanel;
import com.minkyo.bookManagementClient.bookService.EventPanel;
import com.minkyo.bookManagementPacket.Member.MemberVO;

import SockNet.NetClient;

public class BookManagementMainFrame extends JFrame {
	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;
	public static Image colorBackgroundImage = null;
	public static Image whiteColorBackgroundImage = null;
	
	private static BookManagementMainFrame inst = null;
	private NetClient net = new NetClient();
	private JPanel currentShowPanel = null; 
	
	private MemberVO myAccountInfo = null;
	private BookManagementMainFrame() {}
	
	public static BookManagementMainFrame getInstance() {
		if(inst == null)
			inst = new BookManagementMainFrame();
		return inst;
	}
	
	public NetClient getNetClient() {
		return net;
	}
	
	public MemberVO getAccountInfo() {
		return myAccountInfo;
	}
	
	public void setAccountInfo(MemberVO vo) {
		myAccountInfo = vo;
	}
	
	public void init() throws IOException {
		try {
			// 연결한 IP(도메인주소)와 Port번호를 입력.
			net.startToConnect("min.ddnsking.com", 9999);
		}
		catch(Exception e) {
			String error = "네트워크 연결에 실패했습니다." + e.getMessage();
			Util.ErrDialog(inst, error, JOptionPane.ERROR_MESSAGE );
			return;
		}
		
		colorBackgroundImage = Util.resize(
				new ImageIcon(Util.getImageFile("background3.jpg")), 
				BookManagementMainFrame.SCREEN_WIDTH,
				BookManagementMainFrame.SCREEN_HEIGHT
		).getImage();
		
		whiteColorBackgroundImage = Util.resize(
				new ImageIcon(Util.getImageFile("mainWhiteColor.jpg")), 
				BookManagementMainFrame.SCREEN_WIDTH,
				BookManagementMainFrame.SCREEN_HEIGHT
		).getImage();
		
		super.setTitle("도서관리 프로그램(전민교)");
		super.setIconImage(Util.resize(new ImageIcon(Util.getImageFile("bookIcon.jpg")), 100, 100).getImage());
		
		panelInit();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					net.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		super.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		super.setResizable(false);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(true);
		
	}
	
	public void changePanel(BookPanelType panelType) {
		this.remove(currentShowPanel);
		
		EventPanel changePanel = null;
		switch(panelType) {
			case LoginPanel:
				changePanel = new BookLoginPanel(panelType);
				break;
			case JoinPanel:
				changePanel = new BookJoinPanel(panelType);
				break;
			case MainPanel:
				changePanel = new BookMainPanel(panelType);
				break;
			case BookHistoryPanel:
				changePanel = new BookHistoryPanel(panelType);
				break;
			case BookListPanel:
				changePanel = new BookListPanel(panelType);
				break;
			default:
				Util.ErrDialog(inst, "없는 패널 타입으로 전환시도", JOptionPane.ERROR_MESSAGE);
				return;
		}
		
		currentShowPanel = changePanel;
		currentShowPanel.setVisible(true);
		
		changePanel.openEvent();
		
		currentShowPanel = changePanel;
		this.add(currentShowPanel);
		this.repaint();
		currentShowPanel.repaint();
	}
	
	private void panelInit() {
		BookLoginPanel loginPanel = new BookLoginPanel(BookPanelType.LoginPanel);
		currentShowPanel = loginPanel;
		this.add(loginPanel);
		loginPanel.setVisible(true);
	}
}
