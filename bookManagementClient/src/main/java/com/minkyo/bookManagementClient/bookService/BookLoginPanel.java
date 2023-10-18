package com.minkyo.bookManagementClient.bookService;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;


public class BookLoginPanel extends JPanel {
	private JButton loginBtn = new JButton("로그인");
	private JButton joinBtn = new JButton("회원가입");
	private JLabel titleLabel = new JLabel("도서관리  프로그램");
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwLabel = new JLabel("패스워드");
	private JTextField idTextField = new JTextField(20);
	private JPasswordField passwordTextField = new JPasswordField(20);
	
	private BookPanelType pnType;
	
	public BookLoginPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		
		loginBtn.setBounds(320,530, 130, 80);
		joinBtn.setBounds(600,530, 130, 80);
		
		titleLabel.setBounds(270,60,600,100);
		titleLabel.setFont(new Font("Serif", Font.PLAIN, 60));
		
		idLabel.setBounds(300, 260, 200, 50);
		idLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		pwLabel.setBounds(300, 350, 200, 50);
		pwLabel.setFont(new Font("Serif", Font.PLAIN, 30));
		
		idTextField.setBounds(450,260,200,50);
		passwordTextField.setBounds(450,350,200,50);	
		
		setButtonListener();
		
		add(idLabel);
		add(pwLabel);
		add(idTextField);
		add(passwordTextField);
		add(loginBtn);
		add(joinBtn);
		add(titleLabel);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.shareDefaultBackgroundImage,0,0, null);
	}
	
	private void setButtonListener() {
		if(loginBtn == null || joinBtn == null) {
			return;
		}
		
		ActionListener loginBtn_action = (ActionEvent e) -> {
			if(idTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
				Util.ErrDialog(this, "아이디 혹은 비밀번호 다시 입력", JOptionPane.INFORMATION_MESSAGE );
				return;
			}
			
			// 로그인 가능한지 패킷 송신
		};
		
		ActionListener signupBtn_action = (ActionEvent e) -> {
			Util.resetTextField(idTextField, passwordTextField);
			
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.JoinPanel);
		};
		
		loginBtn.addActionListener(loginBtn_action);
		joinBtn.addActionListener(signupBtn_action);
	}
}
