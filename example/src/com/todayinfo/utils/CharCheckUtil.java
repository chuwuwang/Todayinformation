package com.todayinfo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符验证工具
 * 
 */
public class CharCheckUtil {

	/**
	 * 判断是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllDigit(String str) {
		if (!str.equals("") && str.length() > 0) {
			int len = 0;
			for (int idx = 0; idx < str.length(); idx++) {
				if (Character.isDigit(str.charAt(idx))) {
					len++;
				}
			}
			if (len == str.length()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 时间格式，去-
	 * 
	 * @param date
	 * @return
	 */
	public static String dateChange(String date) {
		StringBuilder sb = new StringBuilder();
		String[] dateStr = date.split("-");
		for (int i = 0; i < dateStr.length; i++) {
			sb.append(dateStr[i]);
		}
		return sb.toString();
	}

	/**
	 * 判断是否是数字和"*"号组成的
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isJustDigitStar(String str) {
		int len = 0;
		for (int idx = 0; idx < str.length(); idx++) {
			if (Character.isDigit(str.charAt(idx)) || str.charAt(idx) == '.') {
				len++;
			}
		}
		if (len == str.length()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是数字和"*"和“-”号组成的
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isJustDigitStar1(String str) {
		int len = 0;
		for (int idx = 0; idx < str.length(); idx++) {
			if (Character.isDigit(str.charAt(idx)) || str.charAt(idx) == '*'
					|| str.charAt(idx) == '-') {
				len++;
			}
		}
		if (len == str.length()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符长度
	 * 
	 * @param s
	 * @param charNum
	 * @return
	 */
	public static boolean allowMaxLenthOfString(String s, int charNum) {
		int num = 0;
		for (int i = 0; i < s.length(); i++) {
			String tmp = s.substring(i, i + 1);
			if (tmp.getBytes().length == 3) {
				num += 2;
			} else if (tmp.getBytes().length == 1) {
				num += 1;
			}
		}
		if (num <= charNum) {
			return true;
		}
		return false;
	}

	/**
	 * 判断中英文字符长度
	 * 
	 * @param s
	 * @param charNum
	 * @return
	 */
	public static boolean CheckStrType(String str) {
		boolean check = true;
		for (int i = 0; i < str.length(); i++) {
			String tmp = str.substring(i, i + 1);
			if (isChinese(tmp)) {
				check = true;
			} else if (isEnglish(tmp)) {
				check = true;
			} else {
				return false;
			}
		}
		return check;
	}

	/**
	 * 邮箱验证
	 * 
	 * @param str
	 * @return
	 */
	public static Boolean EmailCheck(String str) {

		if (str.toString().length() > 0
				&& !str.matches("^\\w+@\\w+\\.(com|cn)")) {
			return false;

		} else
			return true;
	}

	/**
	 * 日期验证
	 * 
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 手机号码验证
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean isPhoneNum(String phonenum) {
		Pattern pattern = Pattern.compile("^(1[0-9])\\d{9}$");
		Matcher m = pattern.matcher(phonenum);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 身份证号码验证
	 * 
	 * @param strID
	 * @return
	 */
	public static boolean isIDNumber(String strID) {
		Pattern pattern = Pattern
				.compile("((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))");
		Matcher m = pattern.matcher(strID);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 详细地址验证
	 * 
	 * @param address
	 * @return
	 */
	public static boolean isAddress(String address) {
		int i = 0, j = 0, k = 0, u = 0;
		int count = address.length();
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = pattern.matcher(address);
		while (m.find()) {
			i++;
		}
		for (int idx = 0; idx < count; idx++) {
			char c = address.charAt(idx);
			int tmp = (int) c;
			if ((tmp >= 'a' && tmp <= 'z') || (tmp >= 'A' && tmp <= 'Z')) {
				j++;
			}
			if (Character.isDigit(address.charAt(idx))) {
				k++;
			}
			if (c == ' ') {
				u++;
			}
		}
		if ((i + j + k + u) == count) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 除了“|”不能输和全部都是空格以外，其他的内容都可以通过
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isdouhao(String name) {
		int aa = name.indexOf("|");
		if (aa >= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 是否有逗号
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isdouhao1(String name) {
		int aa = name.indexOf("|");
		int bb = name.indexOf(" ");
		if (aa >= 0 && bb >= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 只能有一个“*”
	 * 
	 * @param str
	 * @return
	 */
	public static boolean xinhao(String str) {

		String[] arrg = str.split("\\*");
		if (arrg.length > 2) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 汉字验证
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isChinese(String name) {
		int j = 0;
		int i = name.length();
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
		Matcher m = pattern.matcher(name);
		while (m.find()) {
			j++;
		}
		if (i == j) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 英文姓名验证
	 * 
	 * @param english
	 * @return
	 */
	public static boolean isEnglish(String english) {
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		Matcher m = pattern.matcher(english);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 数字验证
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isData(String number) {
		Pattern pattern = Pattern.compile("^[0-9]*$");
		Matcher m = pattern.matcher(number);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 不能全是000
	 * 
	 * @param number
	 * @return
	 */
	public static boolean quanshiling(String number) {
		Pattern pattern = Pattern.compile("^0++$");
		Matcher m = pattern.matcher(number);
		if (m.matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

}
