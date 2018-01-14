package me.Yukun.Captchas;

public class Config
{
    public static Integer getConfigInt(final String path) {
        return Main.settings.getConfig().getInt(path);
    }
    
    public static Double getConfigDouble(final String path) {
        return Main.settings.getConfig().getDouble(path);
    }
    
    public static Boolean getConfigBoolean(final String path) {
        return Main.settings.getConfig().getBoolean(path);
    }
    
    public static String getConfigString(final String path) {
        final String msg = Main.settings.getConfig().getString(path);
        return msg;
    }
    
    public static String getMessageString(final String path) {
        final String msg = Main.settings.getMessages().getString(path);
        return msg;
    }
    
    public static void setConfigString(final String path, final String msg) {
        Main.settings.getConfig().set(path, (Object)msg);
        Main.settings.saveConfig();
    }
}
