package converter.util;

import java.util.regex.Pattern;

public class Recognizer
{
    public static boolean isXml(String s) {
        //return Pattern.compile("[</>]+").matcher(s).find();
        return Pattern.compile("<(?:\"[^\"]*\"['\"]*|'[^']*'['\"]*|[^'\">])+>").matcher(s).find();
    }

    public static boolean isJson(String s) {
        return Pattern.compile("[{:}]+").matcher(s).find();
        //return Pattern.compile("\\{\\s*\"[^\"]+\"\\s*:\\s*\"[^\"]+\"\\s*}").matcher(s).find();
    }
}
