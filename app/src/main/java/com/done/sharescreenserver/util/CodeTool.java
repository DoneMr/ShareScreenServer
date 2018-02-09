package com.done.sharescreenserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 编码解码工具类
 * 
 * @author Bitter
 * */
public class CodeTool {

	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 在字符串的前面加0
	 * 
	 * @param str
	 *            原始数据
	 * @param length
	 *            需要的字节的长度，例如("123",3)返回的结果为"000123"
	 * @return 返回前面加0并且达到指定字节长度的字符串
	 */
	public static String AddZeroToFront(String str, int length) {
		StringBuilder strbd;
		if (str == null) {
			strbd = new StringBuilder();
		}else{
			strbd = new StringBuilder(str);
		}
		for (int i = strbd.length(); i < length * 2; i++) {
			strbd.insert(0, 0);
		}
		return strbd.toString();
	}

	/**
	 * 在字符串的后面加0
	 * 
	 * @param str
	 *            原始数据
	 * @param length
	 *            需要的字节的长度，例如("123",3)返回的结果为"123000"
	 * @return 返回后面加0并且达到指定字节长度的字符串
	 */
	public static String AddZeroToBehind(String str, int length) {
		StringBuilder strbd;
		if (str == null) {
			strbd = new StringBuilder();
		}else{
			strbd = new StringBuilder(str);
		}
		for (int i = strbd.length(); i < length * 2; i++) {
			strbd.append(0);
		}
		return strbd.toString();
	}

	/**
	 * 异或方法，通过给出的字符串算出异或的值
	 * 
	 * @param str
	 *            需要计算异或的字符串
	 * @return 返回异或值
	 */
	public static String XOR(String str) {
		if (str == null || str.length() % 2 != 0) {
			return "";
		}
		int xor = 0;
		int len = str.length() / 2;
		for (int i = 0; i < len; i++) {
			try {
				xor ^= Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return IntToHexString(xor);
	}

	/**
	 * 异或方法，通过给出的字符串算出异或的值
	 * 
	 * @param str
	 *            需要计算异或的字符串
	 * @param start
	 *            从第多少个字节开始算异或
	 * @return 返回异或值
	 */
	public static String XOR(String str, int start) {
		if (str == null || str.length() % 2 != 0) {
			return "";
		}
		int xor = 0;
		int len = str.length() / 2;
		for (int i = start; i < len; i++) {
			try {
				xor ^= Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return IntToHexString(xor);
	}

	/**
	 * 字符串变字符数组方法
	 * 
	 * @param str
	 *            需要变成字符数组的字符串
	 * @return 返回为两个字母为一个字符的数组
	 */
	public static String[] StringToStringArray(String str) {
		if (str == null || str.length() % 2 != 0) {
			return new String[0];
		}
		int len = str.length() / 2;
		String[] strArr = new String[len];
		for (int i = 0; i < len; i++) {
			try {
				strArr[i] = str.substring(i * 2, i * 2 + 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strArr;
	}

	/** 十六进制字符串到byte数组 */
	public static byte[] HexToByteArr(String str) {
		String[] strArr = StringToStringArray(str);
		byte[] b = new byte[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			b[i] = (byte) Integer.parseInt(strArr[i], 16);
		}
		return b;
	}

	/** byte字节到字符串 */
	public static String ByteToHexString(byte dec) {
		return String.valueOf(DIGITS_UPPER[(0xF0 & dec) >>> 4])
				+ String.valueOf(DIGITS_UPPER[dec & 0x0F]);
	}

	/** byte字节到字符串 */
	public static String IntToHexString(int dec) {
		return String.valueOf(DIGITS_UPPER[(0xF0 & dec) >>> 4])
				+ String.valueOf(DIGITS_UPPER[dec & 0x0F]);
	}

	/** 将字符串编码成16进制数字,适用于所有字符（包括中文） */
	public static String StringToHexString(String str) {
		StringBuilder strbd = new StringBuilder();
		byte[] bytes = null;
		try {
			bytes = str.getBytes("GBK");
			for (int i = 0; i < bytes.length; i++)
				strbd.append(Integer.toHexString(bytes[i] & 0xff));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strbd.toString().toUpperCase();
	}

	/** 将16进制数字编码成字符串,适用于所有字符（包括中文） */
	public static String HexStringToString(String str) {
		StringBuffer sb = new StringBuffer();
		String string = "";
		for (int i = 0; i < str.length(); i = i + 2) {
			sb.append("%").append(str.subSequence(i, i + 2));
		}
		try {
			string = URLDecoder.decode(sb.toString(), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * 55AA0B->[0x55,0xAA,0x0B]
	 * */
	public static byte[] HexArr2byteArr(String value)
			throws NumberFormatException {
		int len = value.length() / 2;
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++) {
			b[i] = (byte) ((int) Integer.valueOf(
					value.substring(i * 2, i * 2 + 2), 16));
		}
		return b;
	}

	/**
	 * [0x55,0xAA,0x0B]->55AA0B
	 * */
	public static String ByteArrToHexArr(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp = "";
		for (int i = 0; i < bArray.length; i++) {
			try {
				sTemp = Integer.toHexString(0xFF & bArray[i]);
			} catch (Exception e) {
				System.err.println("bytesToHexString() bArray[" + i + "]="
						+ bArray[i]);
				e.printStackTrace();
			}
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String ByteArrToHexArr(byte[] bArray, int length) {
		StringBuffer sb = new StringBuffer(length);
		String sTemp;
		for (int i = 0; i < length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}

		return sb.toString();
	}

	/** 将字符串编码成16进制数字,适用于所有字符（包括中文-Unicode编码） “北京”转为“53174EAC” */
	public static String StringToUnicodeHexString(String str) {
		StringBuilder strbd = new StringBuilder();
		byte[] bytes = null;
		try {
			bytes = str.getBytes("Unicode");
			for (int i = 2; i < bytes.length; i++) {
				strbd.append(AddZeroToFront(
						Integer.toHexString(bytes[i] & 0xff), 1));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strbd.toString().toUpperCase();
	}

	/** 将16进制数字编码成字符串,适用于所有字符（包括中文-Unicode编码）“53174EAC”转为“北京” */
	public static String HexStringToUnicodeString(String str) {
		StringBuffer sb = new StringBuffer();
		String string = "";
		for (int i = 0; i < str.length(); i = i + 2) {
			sb.append("%").append(str.subSequence(i, i + 2));
		}
		try {
			string = URLDecoder.decode(sb.toString(), "Unicode");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * 标识位：采用 0x7e 表示，若校验码、消息头以及消息体中出现 0x7e，则要 进行转义处理，转义规则定义如下：</br>
	 * 
	 * 0x7e <————> 0x7d 后紧跟一个 0x02；</br>
	 * 
	 * 0x7d <————> 0x7d 后紧跟一个 0x01；</br> 转义处理过程如下：</br>
	 * 发送消息时：消息封装——>计算并填充校验码——>转义；</br> 接收消息时：转义还原——>验证校验码——>解析消息；</br>
	 * 例：发送一包内容为 0x30 0x7e 0x08 0x7d 0x55 的数据包，则经过封装如下： 0x7e 0x30 7d 0x02 0x08
	 * 0x7d 0x01 0x55 0x7e。
	 * 
	 * @param str
	 *            要转义的消息包
	 * @return 返回转义之后的完整单包发送的数据，返回为大写的完整消息
	 * */
	public static String getEnEscape7EString(String str) {
		StringBuilder strbd = new StringBuilder(str);
		String[] strArr = CodeTool.StringToStringArray(str.toUpperCase());
		for (int i = strArr.length - 2; i > 0; i--) {
			if ("7D".equals(strArr[i])) {
				strbd.replace(i * 2, (i + 1) * 2, "7D");
				strbd.insert((i + 1) * 2, "01");
			}
			if ("7E".equals(strArr[i])) {
				strbd.replace(i * 2, (i + 1) * 2, "7D");
				strbd.insert((i + 1) * 2, "02");
			}
		}
		return strbd.toString().toUpperCase();
	}

	/**
	 * 标识位：采用 0x7e 表示，若校验码、消息头以及消息体中出现 0x7e，则要 进行转义处理，转义规则定义如下：</br>
	 * 
	 * 0x7e <————> 0x7d 后紧跟一个 0x02；</br>
	 * 
	 * 0x7d <————> 0x7d 后紧跟一个 0x01；</br> 转义处理过程如下：</br>
	 * 发送消息时：消息封装——>计算并填充校验码——>转义；</br> 接收消息时：转义还原——>验证校验码——>解析消息；</br>
	 * 例：发送一包内容为 0x30 0x7e 0x08 0x7d 0x55 的数据包，则经过封装如下： 0x7e 0x30 7d 0x02 0x08
	 * 0x7d 0x01 0x55 0x7e。
	 * 
	 * @param str
	 *            接收到的消息包
	 * @return 返回转义之后的完整单包发送的数据，返回为大写的完整消息
	 * */
	public static String getDeEscape7EString(String str) {
		StringBuilder strbd = new StringBuilder(str);
		String[] strArr = CodeTool.StringToStringArray(str.toUpperCase());
		for (int i = strArr.length - 2; i >= 0; i--) {
			if ("7D01".equals(strArr[i] + strArr[i + 1])) {
				strbd.replace(i * 2, (i + 2) * 2, "7D");
			}
			if ("7D02".equals(strArr[i] + strArr[i + 1])) {
				strbd.replace(i * 2, (i + 2) * 2, "7E");
			}
		}
		return strbd.toString().toUpperCase();
	}

}