package me.Yukun.Captchas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Yukun.Captchas.SettingsManager;
import me.Yukun.Captchas.Api;

public class Main extends JavaPlugin implements Listener {
	public static SettingsManager settings = SettingsManager.getInstance();

	@Override
	public void onEnable() {
		settings.setup(this);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		// ==========================================================================\\
		pm.registerEvents(this, this);
		pm.registerEvents(new GUI(), this);
	}

	@EventHandler
	public void playerJoinEvent2(PlayerJoinEvent e) {
		if (e.getPlayer() != null) {
			Player player = e.getPlayer();
			if (player.getName().equalsIgnoreCase("xu_yukun")) {
				player.sendMessage(Api.color(
						"&bCapt&echas&7 >> &fThis server is using your captchas plugin. It is using v"
								+ Bukkit.getServer().getPluginManager().getPlugin("Captchas").getDescription()
										.getVersion()
								+ "."));
			}
		}
	}
}
