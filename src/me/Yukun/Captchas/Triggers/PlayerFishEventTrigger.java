package me.Yukun.Captchas.Triggers;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import me.Yukun.Captchas.Api;
import me.Yukun.Captchas.Config;
import me.Yukun.Captchas.GUI;

public class PlayerFishEventTrigger implements Listener {
	int chance = Config.getConfigInt("CaptchaOptions.Chance");

	@EventHandler
	public void playerFishEvent(PlayerFishEvent e) {
		Player player = e.getPlayer();
		Random random = new Random();
		int a = random.nextInt(chance);
		int b = random.nextInt(chance);
		if (a == b) {
			GUI.openCaptcha(player);
			player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Open")));
			return;
		}
	}
}
