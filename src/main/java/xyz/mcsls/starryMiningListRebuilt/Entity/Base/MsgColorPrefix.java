package xyz.mcsls.starryMiningListRebuilt.Entity.Base;

public class MsgColorPrefix {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    //获取颜色字符串
    public static String getColorStr(String color, String str) {
        return color + str + ANSI_RESET;
    }
}