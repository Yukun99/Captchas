package me.Yukun.Captchas;

public class Config {
	public static Integer getConfigInt(String path) {
		return Main.settings.getConfig().getInt(path);
	}

	public static Double getConfigDouble(String path) {
		return Main.settings.getConfig().getDouble(path);
	}

	public static Boolean getConfigBoolean(String path) {
		return Main.settings.getConfig().getBoolean(path);
	}

	public static String getConfigString(String path) {
		return Main.settings.getConfig().getString(path);
	}

	public static String getMessageString(String path) {
		return Main.settings.getMessages().getString(path);
	}

	public static void setConfigString(String path, String msg) {
		Main.settings.getConfig().set(path, (Object) msg);
		Main.settings.saveConfig();
	}
}
