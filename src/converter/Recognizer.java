package converter;

import java.util.regex.Pattern;

class Recognizer {
    public static boolean isXml(String s) {
        return Pattern.compile("[</>]+").matcher(s).find();
    }
    public static boolean isJson(String s) {
        return Pattern.compile("[{:}]+").matcher(s).find();
    }
}
