package me.Yukun.Captchas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GUI implements Listener {
	static List<String> items = Main.settings.getItems().getStringList("Items");
	static HashMap<Inventory, Integer> chosenslot = new HashMap<Inventory, Integer>();
	static HashMap<Player, Inventory> cplayer = new HashMap<Player, Inventory>();
	static HashMap<Player, Integer> wrong = new HashMap<Player, Integer>();
	static HashMap<Player, Integer> timer = new HashMap<Player, Integer>();
	static ArrayList<Player> allowclose = new ArrayList<Player>();
	HashMap<Player, Integer> starttrack = new HashMap<Player, Integer>();
	Plugin plugin = Bukkit.getPluginManager().getPlugin("Captchas");
	int chance = Config.getConfigInt("CaptchaOptions.Chance");
	static int time = Config.getConfigInt("CaptchaOptions.Timeout");

	public static Integer getWrong(Player player) {
		if (wrong.containsKey(player)) {
			return wrong.get(player);
		} else {
			return null;
		}
	}

	public static void openCaptcha(Player player) {
		int guiSize = Config.getConfigInt("CaptchaOptions.GUISize") - 1;
		int chosen = Api.getRandomNumber(guiSize);
		Collections.shuffle(items);
		List<String> items2 = items.subList(0, guiSize + 1);
		String guiName = Api.color(Api.replaceItemName(Config.getConfigString("CaptchaOptions.GUIName"), items2.get(chosen)));
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
				timer.put(player, Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					public void run() {
						allowclose.remove(player);
						player.closeInventory();
						if (Config.getConfigInt("CaptchaOptions.Wrong") <= 1) {
							for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
							}
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
							timer.remove(player);
							return;
						} else {
							if (wrong.containsKey(player)) {
								wrong.put(player, wrong.get(player) + 1);
								player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
								if (wrong.get(player) >= Config.getConfigInt("CaptchaOptions.Wrong")) {
									for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
									}
									player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
									wrong.remove(player);
									timer.remove(player);
									return;
								}
								timer.remove(player);
								return;
							} else {
								wrong.put(player, 1);
								player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
								if (wrong.get(player) >= Config.getConfigInt("CaptchaOptions.Wrong")) {
									for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
									}
									player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
									wrong.remove(player);
									timer.remove(player);
									return;
								}
								timer.remove(player);
								return;
							}
						}
					}
				}, time * 20));
				return;
			} else {
				return;
			}
		}

	}

	@EventHandler
	public void CaptchaCloseEvent(InventoryCloseEvent e) {
		if (allowclose.contains(e.getPlayer())) {
			if (chosenslot.containsKey(e.getInventory())) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						e.getPlayer().openInventory(e.getInventory());
					}
				}, 1);
			}
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
				if (Config.getConfigBoolean("CaptchaOptions.Clear") == false) {
					if (wrong.containsKey(player)) {
						player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Right") + Api.replaceStrikes(player, Config.getMessageString("Messages.Clear"))));
						return;
					} else {
						player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Right")));
						return;
					}
				} else {
					if (wrong.containsKey(player)) {
						if (wrong.get(player) > 1) {
							wrong.put(player, wrong.get(player) - 1);
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Right") + Api.replaceStrikes(player, Config.getMessageString("Messages.Clear"))));
							return;
						} else {
							wrong.remove(player);
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Right") + Api.replaceStrikes(player, Config.getMessageString("Messages.Clear"))));
							return;
						}
					} else {
						player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Right")));
						return;
					}
				}
			} else {
				Player player = (Player) e.getWhoClicked();
				allowclose.remove(player);
				player.closeInventory();
				if (Config.getConfigInt("CaptchaOptions.Wrong") <= 1) {
					for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
					}
					player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
					player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
					return;
				} else {
					if (wrong.containsKey(player)) {
						wrong.put(player, wrong.get(player) + 1);
						player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
						if (wrong.get(player) >= Config.getConfigInt("CaptchaOptions.Wrong")) {
							for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
							}
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
							wrong.remove(player);
							return;
						}
						return;
					} else {
						wrong.put(player, 1);
						player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Wrong") + Api.replaceStrikes(player, Config.getMessageString("Messages.Strike"))));
						if (wrong.get(player) >= Config.getConfigInt("CaptchaOptions.Wrong")) {
							for (String line : Main.settings.getConfig().getStringList("CaptchaOptions.Commands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), line.replace("%player%", player.getName()));
							}
							player.sendMessage(Api.color(Config.getMessageString("Messages.Prefix") + Config.getMessageString("Messages.Punish")));
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
