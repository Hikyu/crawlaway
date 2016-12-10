package space.kyu.crawlaway.utils;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
	private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

	public static String getCharset(String contentType) {
		Matcher matcher = patternForCharset.matcher(contentType);
		if (matcher.find()) {
			String charset = matcher.group(1);
			if (Charset.isSupported(charset)) {
				return charset;
			}
		}
		return null;
	}

	public static boolean isNotEmpty(CharSequence cs) {
		 return !(cs == null && cs.length() == 0);
	}

	public static boolean isNotBlank(CharSequence cs) {
		int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return true;
            }
        }
        return false;
	}
}
