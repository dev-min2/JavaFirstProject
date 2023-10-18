package com.minkyo.bookManagementClient.bookService;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
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

public class BookJoinPanel extends JPanel {
	private BookPanelType pnType;
	private static final long serialVersionUID = 1L;
	private JButton idDuplicateBtn = new JButton("아이디 중복검사");
	private JButton signupBtn = new JButton("회원 가입");
	private JButton prevBtn = new JButton("돌아가기");
	private JLabel idLabel = new JLabel("아이디");
	private JLabel pwdLabel = new JLabel("비밀번호");
	private JLabel pwdVerifyLabel = new JLabel("비밀번호 확인");
	private JLabel emailLabel = new JLabel("이메일");
	private JLabel nickNameLabel = new JLabel("닉네임");
	private JTextField idTextField = new JTextField(20);
	private JPasswordField passwordTextField = new JPasswordField(20);
	private JPasswordField passwordVerifyTextField = new JPasswordField(20);
	private JTextField emailTextField = new JTextField(20);
	private JTextField nickNameTextField = new JTextField(20);
	
	public BookJoinPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH, BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		idLabel.setBounds(380, 100, 100, 50);
		idLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		pwdLabel.setBounds(380, 180, 200, 50);
		pwdLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		pwdVerifyLabel.setBounds(380,260, 150, 50);
		pwdVerifyLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		emailLabel.setBounds(380,340,100,50);
		emailLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		nickNameLabel.setBounds(380,420,100,50);
		nickNameLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		
		final int textFieldMargin = 40;
		
		Rectangle idLabelRect = idLabel.getBounds();
		idTextField.setBounds(idLabelRect.x, idLabelRect.y + textFieldMargin, idLabelRect.width + 120, idLabelRect.height );
		Rectangle idTextFieldRect = idTextField.getBounds();
		idDuplicateBtn.setBounds(idTextFieldRect.x + 240, idTextFieldRect.y, 130,50);
		Rectangle pwdLabelRect = pwdLabel.getBounds();
		passwordTextField.setBounds(pwdLabelRect.x, pwdLabelRect.y + textFieldMargin, idLabelRect.width + 120, pwdLabelRect.height);
		Rectangle pwdVerifyLabelRect = pwdVerifyLabel.getBounds();
		passwordVerifyTextField.setBounds(pwdVerifyLabelRect.x, pwdVerifyLabelRect.y + textFieldMargin, idLabelRect.width + 120, pwdVerifyLabelRect.height);
		Rectangle emailRect = emailLabel.getBounds();
		emailTextField.setBounds(emailRect.x, emailRect.y + textFieldMargin, idLabelRect.width + 120, emailRect.height );
		Rectangle nickNameRect = nickNameLabel.getBounds();
		nickNameTextField.setBounds(nickNameRect.x, nickNameRect.y + textFieldMargin, idLabelRect.width + 120, nickNameRect.height);

		signupBtn.setBounds(nickNameRect.x - 60, nickNameRect.y + 150, 130, 50 );
		prevBtn.setBounds(nickNameRect.x + 200, nickNameRect.y + 150, 130, 50);
		
		setButtonListener();
		
		add(idLabel);
		add(pwdLabel);
		add(pwdVerifyLabel);
		add(emailLabel);
		add(idTextField);
		add(idDuplicateBtn);
		add(passwordTextField);
		add(passwordVerifyTextField);
		add(emailTextField);
		add(nickNameLabel);
		add(nickNameTextField);
		add(signupBtn);
		add(prevBtn);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.shareDefaultBackgroundImage,0,0, null);
	}
	
	private void setButtonListener() {
		ActionListener signupBtn_action = (ActionEvent e) -> {
			String idText = idTextField.getText();
			String pwdText = passwordTextField.getText();
			String pwdVerifyText = passwordVerifyTextField.getText();
			String emailText = emailTextField.getText();
			if(Util.isBlank(idText) || Util.isBlank(pwdText) || Util.isBlank(pwdVerifyText) || Util.isBlank(emailText))
			{
				Util.ErrDialog(this, "공란 확인해주세요.",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if(!pwdText.equals(pwdVerifyText))
			{
				Util.ErrDialog(this, "비밀번호 검증 확인해주세요.",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		};
		
		ActionListener duplicateBtn_action = (ActionEvent e) -> {
			if(Util.isBlank(idTextField.getText()))
			{
				Util.ErrDialog(this, "아이디 다시 입력해주세요.", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		};
		
		ActionListener prevBtn_action = (ActionEvent e) -> {
			Util.resetTextField(idTextField,passwordTextField,passwordVerifyTextField, emailTextField, nickNameTextField);
		
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.LoginPanel);
		};
		
		prevBtn.addActionListener(prevBtn_action);
		idDuplicateBtn.addActionListener(duplicateBtn_action);
		signupBtn.addActionListener(signupBtn_action);
	}
}
