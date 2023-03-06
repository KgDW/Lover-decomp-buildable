package dev.blackhig.zhebushigudu.lover.util;

public class NotificationManager
{
    public static void raw(final String message) {
        ChatUtil.printChatMessage(message);
    }
    
    public static void info(final String message) {
        raw("[Info]" + message);
    }
    
    public static void warn(final String message) {
        raw(color("6") + "[Warning]" + color("r") + message);
    }
    
    public static void error(final String message) {
        ChatUtil.printErrorChatMessage(color("c") + "[Error]" + color("r") + message);
    }
    
    public static void fatal(final String message) {
        ChatUtil.printErrorChatMessage(color("4") + "[Fatal]" + color("r") + message);
    }
    
    public static void debug(final String message) {
        raw(color("a") + "[Debug]" + color("r") + message);
    }
    
    public static String color(final String color) {
        return ChatUtil.SECTIONSIGN + color;
    }
}
