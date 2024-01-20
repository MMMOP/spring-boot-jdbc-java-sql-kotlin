package th.co.gosoft.rms.mm.pdm.pi.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String convNullToEmptyStr(String str) {
        String returnStr = "";

        try {
            if (str == null || "null".equalsIgnoreCase(str)) {
                returnStr = "";
            } else {
                returnStr = str;
            }
        } catch (Exception e) {
            return "";
        }

        return returnStr;
    }
}
