package me.Yukun.Captchas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Yukun.Captchas.SettingsManager;
import me.Yukun.Captchas.Triggers.BlockBreakEventTrigger;
import me.Yukun.Captchas.Triggers.EntityDamageByEntityEventTrigger;
import me.Yukun.Captchas.Triggers.PlayerFishEventTrigger;
import me.Yukun.Captchas.Api;

public class Main extends JavaPlugin implements Listener {
	public static SettingsManager settings = SettingsManager.getInstance();
	public static Plugin plugin = Bukkit.getPluginManager().getPlugin("Captchas");

	@Override
	public void onEnable() {
		settings.setup(this);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		// ==========================================================================\\
		pm.registerEvents(this, this);
		pm.registerEvents(new GUI(), this);
		if (Config.getConfigBoolean("CaptchaOptions.Events.BlockBreakEvent")) {
			pm.registerEvents(new BlockBreakEventTrigger(), this);
		}
		if (Config.getConfigBoolean("CaptchaOptions.Events.EntityDamageByEntityEvent")) {
			pm.registerEvents(new EntityDamageByEntityEventTrigger(), this);
		}
		if (Config.getConfigBoolean("CaptchaOptions.Events.PlayerFishEvent")) {
			pm.registerEvents(new PlayerFishEventTrigger(), this);
		}
	}

	public static Plugin getPlugin() {
		return plugin;
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
