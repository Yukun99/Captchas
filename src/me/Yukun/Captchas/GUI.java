package me.Yukun.Captchas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener {
	static List<String> items = Main.settings.getItems().getStringList("Items");
	static HashMap<Inventory, Integer> chosenslot = new HashMap<Inventory, Integer>();
	static HashMap<Player, Inventory> cplayer = new HashMap<Player, Inventory>();
	static HashMap<Player, Integer> wrong = new HashMap<Player, Integer>();
	static ArrayList<Player> allowclose = new ArrayList<Player>();
	HashMap<Player, Integer> starttrack = new HashMap<Player, Integer>();
	int chance = Api.getConfigInt("CaptchaOptions.Chance");

	public static Integer getWrong(Player player) {
		if (wrong.containsKey(player)) {
			return wrong.get(player);
		} else {
			return null;
		}
	}

	public static void openCaptcha(Player player) {
		int guiSize = Api.getConfigInt("CaptchaOptions.GUISize") - 1;
		int chosen = Api.getRandomNumber(guiSize);
		Collections.shuffle(items);
		List<String> items2 = items.subList(0, guiSize + 1);
		String guiName = Api
				.color(Api.replaceItemName(Api.getConfigString("CaptchaOptions.GUIName"), items2.get(chosen)));
		if (guiName != null) {
			Inventory captcha = Bukkit.createInventory(null, guiSize + 1, guiName);
			chosenslot.put(captcha, chosen);
			allowclose.add(player);
			// ***Item name debug code***
			// for (int i = 0; i <= guiSize; ++i) {
			// player.sendMessage(i + ": " + items2.get(i));
			// }
			for (int i = 0; i <= guiSize; ++i) {
				captcha.setItem(i, new ItemStack(Material.getMaterial(items2.get(i))));
			}
			if (captcha.getItem(guiSize).getType() != Material.AIR) {
				player.openInventory(captcha);
				return;
			} else {
				return;
			}
		}

	}

	@EventHandler
	public void CaptchaTriggerEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();
			if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
				Random random = new Random();
				int a = random.nextInt(chance);
				int b = random.nextInt(chance);
				if (a == b) {
					openCaptcha(player);
					player.sendMessage(
							Api.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Open")));
					return;
				}
			}
		}
	}

	@EventHandler
	public void CaptchaCloseEvent(InventoryCloseEvent e) {
		if (allowclose.contains(e.getPlayer())) {
			e.getPlayer().openInventory(e.getInventory());
		}
	}

	@EventHandler
	public void CaptchaClickEvent(InventoryClickEvent e) {
		if (chosenslot.containsKey(e.getInventory())) {
			e.setCancelled(true);
			if (e.getSlot() == chosenslot.get(e.getInventory())) {
				Player player = (Player) e.getWhoClicked();
				allowclose.remove(player);
				player.closeInventory();
				if (Api.getConfigBoolean("CaptchaOptions.Clear") == false) {
					if (wrong.containsKey(player)) {
						player.sendMessage(Api
								.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Right")
										+ Api.replaceStrikes(player, Api.getMessageString("Messages.Clear"))));
						return;
					} else {
						player.sendMessage(Api.color(
								Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Right")));
						return;
					}
				} else {
					if (wrong.containsKey(player)) {
						if (wrong.get(player) > 1) {
							wrong.put(player, wrong.get(player) - 1);
							player.sendMessage(Api.color(
									Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Right")
											+ Api.replaceStrikes(player, Api.getMessageString("Messages.Clear"))));
							return;
						} else {
							wrong.remove(player);
							player.sendMessage(Api.color(
									Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Right")
											+ Api.replaceStrikes(player, Api.getMessageString("Messages.Clear"))));
							return;
						}
					} else {
						player.sendMessage(Api.color(
								Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Right")));
						return;
					}
				}
			} else {
				Player player = (Player) e.getWhoClicked();
				if (Api.getConfigInt("CaptchaOptions.Wrong") <= 1) {
					for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
					}
					player.sendMessage(
							Api.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Wrong")
									+ Api.replaceStrikes(player, Api.getMessageString("Messages.Strike"))));
					player.sendMessage(Api
							.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Punish")));
					allowclose.remove(player);
					player.closeInventory();
					return;
				} else {
					allowclose.remove(player);
					player.closeInventory();
					if (wrong.containsKey(player)) {
						wrong.put(player, wrong.get(player) + 1);
						player.sendMessage(Api
								.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Wrong")
										+ Api.replaceStrikes(player, Api.getMessageString("Messages.Strike"))));
						if (wrong.get(player) >= Api.getConfigInt("CaptchaOptions.Wrong")) {
							for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
							}
							player.sendMessage(Api.color(
									Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Punish")));
							wrong.remove(player);
							return;
						}
						return;
					} else {
						wrong.put(player, 1);
						player.sendMessage(Api
								.color(Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Wrong")
										+ Api.replaceStrikes(player, Api.getMessageString("Messages.Strike"))));
						if (wrong.get(player) >= Api.getConfigInt("CaptchaOptions.Wrong")) {
							for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line);
							}
							player.sendMessage(Api.color(
									Api.getMessageString("Messages.Prefix") + Api.getMessageString("Messages.Punish")));
							wrong.remove(player);
							return;
						}
						return;
					}
				}
			}
		}
	}

}
