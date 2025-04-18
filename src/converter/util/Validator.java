package converter.util;

import java.util.regex.Pattern;

// TODO: Implement methods properly.
public class Validator
{
    public static boolean isXml(String s) {
        return Pattern.compile("[</>]+").matcher(s).find();
    }
    public static boolean isJson(String s) {
        return Pattern.compile("[{:}]+").matcher(s).find();
    }
}
