package com.minkyo.bookManagementClient.bookMain;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Util {
	private Util() {}
	
	public static void ErrDialog(Component co, String txt, int msgType ) {
		msgType = ( msgType < -1 || msgType > 3 ) ? JOptionPane.ERROR_MESSAGE : msgType;  
		JOptionPane.showMessageDialog(co, txt, "Message", msgType );
	}
	
	public static void resetTextField(JTextField... textFields) {
		if(textFields == null || textFields.length <= 0)
			return;
		
		for(JTextField text : textFields) {
			text.setText("");
		}
	}
	
	public static boolean createLocalDir(String localDirPath)
	{
		if(localDirPath.isEmpty())
			return false;
		
		String[] paths = localDirPath.split("\\\\");
		if(paths.length < 2)
			return false;
		
		try {
			String defaultDir = paths[0] + "\\" + paths[1];
			File folder = new File(defaultDir);
			if(!folder.exists())
				Files.createDirectory(folder.toPath());
		
			if(paths.length > 2) {
				StringBuilder builder = new StringBuilder(defaultDir);
				for(int i = 2; i < paths.length; ++i) {
					builder.append("\\" + paths[i]);
					folder = new File(builder.toString());
				
					if(!folder.exists())
						Files.createDirectory(folder.toPath());
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public static Image getImageFile(String fileName) {
		Image image = null;

		// 테스트환경은 하드코딩
		String imagePath = "C:\\javaFirstPrj\\bookManagementClient\\src\\main\\resources\\";
		imagePath += fileName;
		try {
			java.io.File file = new java.io.File(imagePath);
			image = ImageIO.read(file);
			if(image == null) {
				//java.io.File file2 = new java.io.File(fileName);
				image = new ImageIcon(Util.class.getResource(fileName)).getImage();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static Image getImageByFullPath(String path) {
		Image image = null;

		// 테스트환경은 하드코딩
		String imagePath = path;
		try {
			java.io.File file = new java.io.File(imagePath);
			image = ImageIO.read(file);
			if(image == null) {
				//java.io.File file2 = new java.io.File(fileName);
				//image = new ImageIcon(Util.class.getResource(fileName)).getImage();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	
	//http://www.java2s.com/example/java-utility-method/swing-icon/resize-icon-icon-int-width-int-height-8aaea.html
	public static ImageIcon resize(ImageIcon icon, int width, int height) {
        if (icon == null) {
            return icon;
        }

        if ((height <= 0 || height == icon.getIconHeight()) && (width <= 0 || width == icon.getIconWidth())) {
            return icon;
        }

        Image image = iconToImage(icon);

        if (height <= 0) {
            height = (int) (icon.getIconHeight() / (float) (icon.getIconWidth() / width));
        }

        if (width <= 0) {
            width = (int) (icon.getIconWidth() / (float) (icon.getIconHeight() / height));
        }

        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

	//http://www.java2s.com/example/java-utility-method/swing-icon/resize-icon-icon-int-width-int-height-8aaea.html
	public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }

        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);

        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return image;
    }
	
	public static boolean isBlank(String checkStr) {
		if(checkStr == null || checkStr.isEmpty())
			return true;
		
		String nonWhiteSpaceStr = checkStr.replace(" ", "");
		return nonWhiteSpaceStr.length() <= 0;
	}
	
	public static String Encrpyt(String pwd)  {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update(pwd.getBytes());
			byte[] pwdEncrypt = md.digest();
			
			// Byte to String
			StringBuilder sb = new StringBuilder(pwdEncrypt.length);
			for(byte b : pwdEncrypt)
				sb.append(String.format("%02x", b));
			result = sb.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
