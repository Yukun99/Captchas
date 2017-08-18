package me.Yukun.Captchas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {

	static SettingsManager instance = new SettingsManager();

	public static SettingsManager getInstance() {
		return instance;
	}

	Plugin p;

	FileConfiguration config;
	File cfile;

	FileConfiguration messages;
	File mfile;
	
	FileConfiguration items;
	File ifile;

	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		cfile = new File(p.getDataFolder(), "config.yml");
		if (!cfile.exists()) {
			try {
				File en = new File(p.getDataFolder(), "/config.yml");
				InputStream E = getClass().getResourceAsStream("/config.yml");
				copyFile(E, en);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(cfile);

		mfile = new File(p.getDataFolder(), "Messages.yml");
		if (!mfile.exists()) {
			try {
				File en = new File(p.getDataFolder(), "/Messages.yml");
				InputStream E = getClass().getResourceAsStream("/Messages.yml");
				copyFile(E, en);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		messages = YamlConfiguration.loadConfiguration(mfile);
		
		ifile = new File(p.getDataFolder(), "Items.yml");
		if (!ifile.exists()) {
			try {
				File en = new File(p.getDataFolder(), "/Items.yml");
				InputStream E = getClass().getResourceAsStream("/Items.yml");
				copyFile(E, en);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		items = YamlConfiguration.loadConfiguration(ifile);
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public FileConfiguration getMessages() {
		return messages;
	}
	
	public FileConfiguration getItems() {
		return items;
	}

	public void saveConfig() {
		try {
			config.save(cfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void saveMessages() {
		try {
			messages.save(mfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Messages.yml!");
		}
	}
	
	public void saveItems() {
		try {
			items.save(ifile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Items.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public void reloadMessages() {
		messages = YamlConfiguration.loadConfiguration(mfile);
	}
	
	public void reloadItems() {
		items = YamlConfiguration.loadConfiguration(ifile);
	}

	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}

	public static void copyFile(InputStream in, File out) throws Exception { // https://bukkit.org/threads/extracting-file-from-jar.16962/
		InputStream fis = in;
		FileOutputStream fos = new FileOutputStream(out);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
}
