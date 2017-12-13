package me.Yukun.Captchas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Api {
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("RankQuests");

	@SuppressWarnings("static-access")
	public Api(Plugin plugin) {
		this.plugin = plugin;
	}

	public static String replaceStrikes(Player player, String msg) {
		if (GUI.getWrong(player) != null) {
			return msg.replace("%wrong%", GUI.getWrong(player) + "");
		} else {
			return msg.replace("%wrong%", 0 + "");
		}
	}

	public static String replaceItemName(String msg, String name) {
		if (name.contains("PLATE") && name.contains("GOLD") && !name.contains("CHESTPLATE")) {
			name = name.replace("_", " ");
			name = name.replace("PLATE", "PRESSURE PLATE");
			name = name.replace("GOLD", "GOLDEN");
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.contains("PLATE") && name.contains("WOOD") && !name.contains("CHESTPLATE")) {
			name = name.replace("_", " ");
			name = name.replace("PLATE", "PRESSURE PLATE");
			name = name.replace("WOOD", "WOODEN");
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.contains("PLATE") && !name.contains("CHESTPLATE")) {
			name = name.replace("_", " ");
			name = name.replace("PLATE", "PRESSURE PLATE");
			msg = msg.replace("%item%", name);
			return msg;
		} else if (!name.equalsIgnoreCase("GOLD_INGOT") && !name.equalsIgnoreCase("GOLD_NUGGET")
				&& (!name.contains("PLATE") || name.contains("CHESTPLATE")) && name.contains("GOLD")) {
			name = name.replace("_", " ");
			name = name.replace("GOLD", "GOLDEN");
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.equalsIgnoreCase("WATCH")) {
			name = "CLOCK";
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.equalsIgnoreCase("FIREBALL")) {
			name = "FIRE CHARGE";
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.contains("WOOD")) {
			name = name.replace("_", " ");
			name = name.replace("WOOD", "WOODEN");
			msg = msg.replace("%item%", name);
			return msg;
		} else if (name.equalsIgnoreCase("PORK")) {
			msg = msg.replace("%item%", "RAW_PORKCHOP");
			return msg;
		} else {
			name = name.replace("_", " ");
			msg = msg.replace("%item%", name);
			return msg;
		}
	}
	
	static int getRandomNumber(int range) {
		Random random = new Random();
		return random.nextInt(range - 1);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItemInHand(Player player) {
		if (getVersion() >= 191) {
			return player.getInventory().getItemInMainHand();
		} else {
			return player.getItemInHand();
		}
	}

	public static ItemStack getItem(String name) {
		String[] msg = name.split(":");
		Material mat = Material.getMaterial(msg[0]);
		ItemStack item = new ItemStack(mat, 1, Short.parseShort(msg[1]));
		return item;
	}

	public static boolean containsItem(Player player, ItemStack item) {
		for (ItemStack items : player.getInventory().getContents()) {
			if (items.isSimilar(item) && (items.getAmount() + item.getAmount()) <= 64) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public static ItemStack getItem(Player player, ItemStack item) {
		if (Api.containsItem(player, item)) {
			for (ItemStack items : player.getInventory().getContents()) {
				if (items.isSimilar(item) && (items.getAmount() + item.getAmount()) <= 64) {
					return items;
				} else {
					continue;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void setItemInHand(Player player, ItemStack item) {
		if (getVersion() >= 191) {
			player.getInventory().setItemInMainHand(item);
		} else {
			player.setItemInHand(item);
		}
	}

	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static String removeColor(String msg) {
		msg = ChatColor.stripColor(msg);
		return msg;
	}



	public static String replacePHolders(String msg, Player player, String rank) {
		return msg
				.replace("%rank%", Main.settings.getConfig().getString("RankQuestOptions.Ranks." + rank + ".RankName"))
				.replace("%player%", player.getDisplayName());
	}

	public static Integer getVersion() {
		String ver = Bukkit.getServer().getClass().getPackage().getName();
		ver = ver.substring(ver.lastIndexOf('.') + 1);
		ver = ver.replaceAll("_", "").replaceAll("R", "").replaceAll("v", "");
		return Integer.parseInt(ver);
	}

	public static Integer getArgument(String Argument, ItemStack item, String Msg) {
		String arg = "0";
		Msg = Api.color(Msg).toLowerCase();
		String name = item.getItemMeta().getDisplayName().toLowerCase();
		if (Msg.contains(Argument.toLowerCase())) {
			String[] b = Msg.split(Argument.toLowerCase());
			if (b.length >= 1)
				arg = name.replace(b[0], "");
			if (b.length >= 2)
				arg = arg.replace(b[1], "");
		}
		return Integer.parseInt(arg);
	}

	public static String getString(String Argument, ItemStack item, String Msg) {
		String arg = "0";
		Msg = Api.color(Msg).toLowerCase();
		String name = item.getItemMeta().getDisplayName().toLowerCase();
		if (Msg.contains(Argument.toLowerCase())) {
			String[] b = Msg.split(Argument.toLowerCase());
			if (b.length >= 1)
				arg = name.replace(b[0], "");
			if (b.length >= 2)
				arg = arg.replace(b[1], "");
		}
		return arg;
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static Player getPlayer(String name) {
		return Bukkit.getServer().getPlayer(name);
	}

	public static Boolean hasArgument(String Argument, List<String> Msg) {
		for (String l : Msg) {
			l = Api.color(l).toLowerCase();
			if (l.contains(Argument.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static ItemStack makeItem(String itemtype, String name, ArrayList<String> lore, int amount) {
		ItemStack item = new ItemStack(Material.getMaterial(itemtype), amount);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(Api.color(name));
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}
}