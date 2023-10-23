package CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import CoreAcitive.ApplicationBeanLoader;

public final class Utils {
	private static final int WEEKID_OFFSET_MONDAY = 2;
	
	public static byte[] StringIpToBytes(String ip) {
		if(ip == null || ip.isEmpty())
			return null;
		
		String[] bytes = ip.split("\\.");
		byte[] ret = new byte[bytes.length];
		int cnt = 0;
		for(String s : bytes) {
			ret[cnt++] = (byte)Integer.parseInt(s);
		}
		
		return ret;
	}
	
	public static int getDayID(Calendar time) {
		if(time == null)
			return 0;
		
		int[] info = getTimeInfo(time);
		
		return (info[0] * 10000) + (info[1] * 100) + info[2];
	}

	
	// 해당주의 기준점이 되는 WeekID를 구한다.
	public static int getWeekID(Calendar time, int dayOfWeek) {
		if(time == null)
			return 0;
		
		if(dayOfWeek < Calendar.SUNDAY || dayOfWeek > Calendar.SATURDAY) {
			dayOfWeek = WEEKID_OFFSET_MONDAY;
		}
		
		int diffDayOfWeek = time.get(Calendar.DAY_OF_WEEK) - dayOfWeek;
		if(diffDayOfWeek < 1) {
			diffDayOfWeek += 7;
		}
		
		Calendar retTime = (Calendar)time.clone();
		retTime.add(Calendar.DAY_OF_WEEK, -diffDayOfWeek);
		
		return getDayID(retTime);
	}
	
	public static boolean isDouble(String value) {
		if(value == null || value.isEmpty())
			return false;
		
		try {
			Double.parseDouble(value);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String value) {
		if(value == null || value.isEmpty())
			return false;
		
		try {
			Integer.parseInt(value);
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private static int[] getTimeInfo(Calendar time) {
		return new int[] { time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH)};
	}
	
	// resources/staticFiles가 기본경로.
	public static Document getXmlDoc(String fileName) {
		if(fileName == null || fileName.isEmpty())
			return null;
		
		Document xml = null;
		InputSource is = null;
		
		String accessXmlPath = "resources/staticFiles/";
		accessXmlPath += fileName;
		try {
			// resource폴더가 jar로 나올 때 제거되어서 분리..
			java.io.File file = new java.io.File(accessXmlPath);
			if(!file.exists()) {
				InputStream inputStream = ApplicationBeanLoader.class.getClassLoader().getResourceAsStream(fileName);
				is = new InputSource(inputStream);
			}
			else 
				is = new InputSource(new FileReader(accessXmlPath));
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			xml.getDocumentElement().normalize();	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return xml;
	}

	
	// byte array 압축기능. -> 이미지 파일 버퍼의 크기가 너무커서 사용.
	// 압축 예제
    // byte[] compressedData = compress(uncompressedData, Deflater.BEST_COMPRESSION, false);
    // byte[] decompressedData = decompress(compressedData, false);
	
	// http://www.java2s.com/example/java-book/compressing-byte-arrays.html 해당 코드사용
//	public static byte[] compress(byte[] input, int compressionLevel, boolean GZIPFormat) throws IOException {
//		Deflater compressor = new Deflater(compressionLevel, GZIPFormat);
//		compressor.setInput(input);
//		compressor.finish();
//		
//	    ByteArrayOutputStream bao = new ByteArrayOutputStream();
//	    byte[] readBuffer = new byte[1024];
//	    int readCount = 0;
//	    
//	    while(!compressor.finished()) {
//	    	readCount = compressor.deflate(readBuffer);
//	    	if(readCount > 0) {
//	    		bao.write(readBuffer, 0, readCount);
//	    	}
//	    }
//	    
//	    compressor.end();
//	    
//	    return bao.toByteArray();
//	}
//	
//	public static byte[] decompress(byte[] input, boolean GZIPFormat) throws Exception {
//		Inflater decompressor = new Inflater(GZIPFormat);
//		
//		decompressor.setInput(input);
//		
//		ByteArrayOutputStream bao = new ByteArrayOutputStream();
//		byte[] readBuffer = new byte[1024];
//		int readCount = 0;
//		
//		while(!decompressor.finished()) {
//			readCount = decompressor.inflate(readBuffer);
//			if(readCount > 0) {
//				bao.write(readBuffer, 0, readCount);
//			}
//		}
//		
//		decompressor.end();
//		return bao.toByteArray();
//	}
}
