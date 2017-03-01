package com.wjb.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.google.gdata.util.common.base.StringUtil;

public class StringParamUtils {

	public final static int LOWER_CASE = -1;
	public final static int UPPER_CASE = 1;
	public final static int NOTHING = 0;
	
	public static List<String> getListOfTokens(String string, String delims, boolean noEmpties, boolean applyTrim, int typeOfCase) {
		List<String> lista = new ArrayList<String>();
		if (!StringUtil.isEmptyOrWhitespace(string)) {
			StringTokenizer stk = new StringTokenizer(typeOfCase == 0 ? string : (typeOfCase > 0 ? string.toUpperCase() : string.toLowerCase()), delims);
			while(stk.hasMoreTokens()) {
				String token = stk.nextToken();
				if(!noEmpties || noEmpties && !StringUtil.isEmptyOrWhitespace(token)) {
					String tokRes = applyTrim ? token.trim() : token;
					tokRes = typeOfCase == 0 ? tokRes : (typeOfCase > 0 ? tokRes.toUpperCase() : tokRes.toLowerCase());
					lista.add(tokRes);
				}
			}
		}
		return lista;
	}
	
	public static String lastSubstring(String str, int count) {
		int len = str.length();
		return count <= len ? str.substring(str.length() - count) : str;
	}
	
	public static Integer defaultIfNoInteger(String strValue, Integer defaultValue) {
		if (StringUtil.isEmptyOrWhitespace(strValue)) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(strValue.trim());
        } catch (Throwable e) {
        	return defaultValue;
        }
	}
	
	public static Double defaultIfNoDouble(String strValue, Double defaultValue) {
		if (StringUtil.isEmptyOrWhitespace(strValue)) {
			return defaultValue;
		}
		try {
			return Double.valueOf(strValue.trim());
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static Long defaultIfNoLong(String strValue, Long defaultValue) {
		if (StringUtil.isEmptyOrWhitespace(strValue)) {
			return defaultValue;
		}
		try {
			return Long.valueOf(strValue.trim());
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static Boolean defaultIfNoBoolean(String strValue, Boolean defaultValue) {
		if (StringUtil.isEmptyOrWhitespace(strValue)) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(strValue.trim());
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static String defaultIfEmptyOrNull(String strValue, Object defaultValue) {
		return (StringUtil.isEmptyOrWhitespace(strValue) ? (String) defaultValue : strValue);
	}
	
	public static String defaultIfEmptyOrNullAndTrim(String strValue, Object defaultValue) {
		String result = defaultIfEmptyOrNull(strValue, defaultValue);
		return (result == null ? result : result.trim());
	}
	
	public static String nullIfEmptyOrEqual(String strValue, String compareValue) {
		return (StringUtil.isEmptyOrWhitespace(strValue) || strValue.equals(compareValue.trim())) ? null : strValue.trim();
	}
	
	public static Calendar parseCalendar(String strDate, String inputPattern) {
		return parseCalendar(strDate, inputPattern, null);
	}
	
	public static Calendar parseCalendar(String strDate, String inputPattern, String timeZone) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(parseDate(strDate, inputPattern, timeZone));
		return (Calendar) gc;
	}

	public static Calendar parseCalendarWithTZ(String strDate, String inputPattern, TimeZone timeZone) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(parseDateWithTZ(strDate, inputPattern, timeZone));
		return (Calendar) gc;
	}
	
	public static Date parseDate(String strDate, String inputPattern, String timeZoneID) {
		return parseDateWithTZ(strDate, inputPattern, StringUtil.isEmptyOrWhitespace(timeZoneID) ? null : TimeZone.getTimeZone(timeZoneID));
	}
	
	public static Date parseDateWithTZ(String strDate, String inputPattern, TimeZone timeZoneID) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(inputPattern);
			if (timeZoneID != null) {
				sdf.setTimeZone(timeZoneID);
			}
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseDate(String strDate, String inputPattern) {
		return parseDate(strDate, inputPattern, null);
	}
	
	public static String formatTime(String patronFormat) {
		return formatTime(patronFormat, null);
	}
	
	public static String formatTimeWithTZ(Date date, String patronFormat, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(patronFormat);
		if (timeZone != null) {
			sdf.setTimeZone(timeZone);
		}
		return sdf.format(date);
	}
	public static String formatTime(Date date, String patronFormat, String timeZone) {
		return formatTimeWithTZ(date, patronFormat, StringUtil.isEmptyOrWhitespace(timeZone) ? null : TimeZone.getTimeZone(timeZone));
	}
	
	public static String formatTime(String patronFormat, String timeZone) {
		Calendar cal = GregorianCalendar.getInstance();
		return formatTime(cal.getTime(), patronFormat, timeZone);
	}
	
	public static String stringToGuions(String stringValue) {
		if (stringValue != null) {
			return stringValue.replaceAll(" ", "_");
		} else {
			return stringValue;
		}
	}

	public static String guionsTotring(String stringValue) {
		if (stringValue != null) {
			return stringValue.replaceAll("_", " ");
		} else {
			return stringValue;
		}
	}
	
	public static String getDomainFromEmail(String emailAddress) {
		if (!StringUtil.isEmptyOrWhitespace(emailAddress)) {
			String newEmailAddress = emailAddress.trim();
			int pos = newEmailAddress.indexOf("@");
			return pos > 0 ? newEmailAddress.substring(pos + 1) : StringUtil.EMPTY_STRING;
		} else {			
			return StringUtil.EMPTY_STRING;
		}
	}
	
	public static String getStringUTF8(String strValue) {
		if (StringUtil.isEmptyOrWhitespace(strValue)) {
			return strValue;
		} else {
			try {
				return new String(strValue.getBytes("UTF-8"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return strValue;
			}
		}
	}
	
	public static String encode(String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}

	public static String decode(String string) {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}
	
	public static String mergeElements(String strList1, String strList2, String separator) {
		
		String strList = "|";
		
		List<String> list1 = getListOfTokens(strList1.toLowerCase(), "|", true, true, NOTHING);		
		for (String elemIn1 : list1) {
			if (!strList.contains("|" + elemIn1 + "|")) {
				strList = String.format("%s%s|", strList, elemIn1);
			}
		}
		List<String> list2 = getListOfTokens(strList2.toLowerCase(), "|", true, true, NOTHING);		
		for (String elemIn2 : list2) {
			if (!strList.contains("|" + elemIn2 + "|")) {
				strList = String.format("%s%s|", strList, elemIn2);
			}
		}
		if (strList.length() > 1) {
			strList = strList.substring(1, strList.length() - 1 );
		} else {
			strList = StringUtil.EMPTY_STRING;
		}
		return strList;
	}
	
	public static String removeParameterFromUrl(String strUrl, String parameter) throws MalformedURLException {
		int pos = -1, to = -1;
		if ((pos = strUrl.indexOf(String.format("&%s=", parameter))) != -1
				|| (pos = strUrl.indexOf(String.format("?%s=", parameter))) != -1) {
			to = strUrl.indexOf("&", pos + 1);
			if (to != -1) {
				to = to + 1;
				pos = pos +1;
			}
		} else {
			return strUrl;
		}
		return strUrl.substring(0, pos) + (to != -1 ? strUrl.substring(to, strUrl.length()) : "");
	}
	
	public static boolean areEquals(String str1, String str2) {
		return str1 == null && str2 == null || str1 != null && str1.equalsIgnoreCase(str2) || str2 != null && str2.equalsIgnoreCase(str1);
	}
}
