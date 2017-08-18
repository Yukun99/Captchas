package me.Yukun.Captchas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

	/*
	 * Experience handler method from
	 * https://gist.github.com/RichardB122/8958201b54d90afbc6f0/
	 */
	public static int getTotalExperience(Player player) {
		int experience = 0;
		int level = player.getLevel();
		if (level >= 0 && level <= 15) {
			experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
			int requiredExperience = 2 * level + 7;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		} else if (level > 15 && level <= 30) {
			experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
			int requiredExperience = 5 * level - 38;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		} else {
			experience = (int) Math.ceil(((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220)));
			int requiredExperience = 9 * level - 158;
			double currentExp = Double.parseDouble(Float.toString(player.getExp()));
			experience += Math.ceil(currentExp * requiredExperience);
			return experience;
		}
	}

	/*
	 * Experience handler method from
	 * https://gist.github.com/RichardB122/8958201b54d90afbc6f0/
	 */
	public static void takeTotalXP(Player player, int amount) {
		if (getVersion() >= 181) {
			int total = getTotalExperience(player) - amount;
			player.setTotalExperience(0);
			player.setTotalExperience(total);
			player.setLevel(0);
			player.setExp(0);
			for (; total > player.getExpToLevel();) {
				total -= player.getExpToLevel();
				player.setLevel(player.getLevel() + 1);
			}
			float xp = (float) total / (float) player.getExpToLevel();
			player.setExp(xp);
		} else {
			player.giveExp(-amount);
		}
	}

	public static int getRandomNumber(int range) {
		Random random = new Random();
		return random.nextInt(range - 1);
	}

	public static Location getRandomLoc(Location location) {
		Location location2 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
		Random random = new Random();
		int xpos = random.nextInt(2);
		int zpos = random.nextInt(2);
		double x = random.nextInt(2) + 0.5;
		double z = random.nextInt(2) + 0.5;
		if (xpos == 1 && zpos == 1) {
			int add = random.nextInt(2);
			if (add == 1) {
				location2 = location2.add(x, 0, z);
			} else {
				location2 = location2.add(x + 1, 0, z + 1);
			}
			return location2;
		} else if (xpos == 1 && zpos == 0) {
			int add = random.nextInt(2);
			if (add == 1) {
				location2 = location2.add(x, 0, -z);
			} else {
				location2 = location2.add(x + 1, 0, -z);
			}
			return location2;
		} else if (xpos == 0 && zpos == 1) {
			int add = random.nextInt(2);
			if (add == 1) {
				location2 = location2.add(-x, 0, z);
			} else {
				location2 = location2.add(-x, 0, z + 1);
			}
			return location2;
		} else if (xpos == 0 && zpos == 0) {
			location2 = location2.add(-x, 0, -z);
			return location2;
		}
		return location2;
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

	public static String replaceSpawnerNPholders(String msg, int tier, String mobname) {
		String msg1 = msg.replace("%mob%", mobname).replace("%tier%", tier + "");
		return Api.color(msg1);
	}

	public static void setConfigString(String path, String msg) {
		Main.settings.getConfig().set(path, msg);
		Main.settings.saveConfig();
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

	public static String getConfigString(String path) {
		String msg = Main.settings.getConfig().getString(path);
		return msg;
	}

	public static Boolean getConfigBoolean(String path) {
		return Main.settings.getConfig().getBoolean(path);
	}

	public static Integer getConfigInt(String path) {
		Integer msg = Main.settings.getConfig().getInt(path);
		return msg;
	}

	public static Double getConfigDouble(String path) {
		String msg = Main.settings.getConfig().getString(path);
		if (Api.isDouble(msg)) {
			return Double.parseDouble(msg);
		} else {
			return null;
		}
	}

	public static String getMessageString(String path) {
		String msg = Main.settings.getMessages().getString(path);
		return msg;
	}

	public static String getItemsString(String path) {
		String msg = Main.settings.getItems().getString(path);
		return msg;
	}

	public static int getItemsInt(String path) {
		String msg = Main.settings.getItems().getString(path);
		if (isInt(msg)) {
			return Integer.parseInt(msg);
		} else {
			return 0;
		}
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