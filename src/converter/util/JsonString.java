package converter.util;

public class JsonString
{
    public static boolean isOpeningTitled(String s)
    {
        return s.matches("\"[^\"]+\": *\\{") || s.matches("\"[^\"]+\": *\\[");
    }

    public static boolean isOpening(String s)
    {
        return s.equals("{") || s.equals("[") || isOpeningTitled(s);
    }

    public static boolean isClosing(String s)
    {
        return s.equals("}") || s.equals("]");
    }

    public static boolean isPair(String s)
    {
        return !isOpening(s) && !isClosing(s) && s.contains(":");
    }

    // NOTE: If the last double-quote missing, i.e. there is only 3 of them.
    public static boolean isPairBroken(String s)
    {
        return isPair(s) && s.matches("(\"[^\"]+){3}");
    }

    public static String trimQuotes(String s)
    {
        return s.trim().replaceAll("\"", "");
    }

    public static String trimBrackets(String s) {
        return s.replaceFirst("^\\[", "").replaceAll("]$", "");
    }

    public static String trimCurlyBrackets(String s) {
        return s.replaceFirst("^\\{", "").replaceAll("}$", "");
    }

    public static String wrapBrackets(String s) {
        return '[' + s + ']';
    }
}
