package me.yukun.captchas.config;

import static me.yukun.captchas.util.TextFormatter.applyColor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Config {

  private static final Set<Material> filterMaterials = new HashSet<>();
  private static FileConfiguration config;

  protected static void setup(FileConfiguration fileConfiguration) {
    config = fileConfiguration;
    parserFilterMaterials();
  }

  private static void parserFilterMaterials() {
    filterMaterials.clear();
    for (String materialName : config.getStringList("Filter.Blocks")) {
      filterMaterials.add(Material.getMaterial(materialName));
    }
  }

  private static boolean doIgnoreOpped(Player player) {
    return config.getBoolean("IgnoreOpped") && player.isOp();
  }

  public static boolean doIgnoreCaptchas(Player player) {
    if (doIgnoreOpped(player) && player.isOp()) {
      return true;
    }
    if (player.hasPermission("captchas.*")) {
      return true;
    }
    if (player.hasPermission("captchas.admin")) {
      return true;
    }
    return player.hasPermission("captchas.bypass");
  }

  public static boolean hasCommandPermissions(CommandSender sender) {
    if (sender.hasPermission("captchas.*")) {
      return true;
    }
    return sender.hasPermission("captchas.admin");
  }

  public static boolean doGraceTime() {
    return config.getBoolean("Grace.Time.Enable");
  }

  public static int getGraceTime() {
    return config.getInt("Grace.Time.Value");
  }

  public static boolean doGraceTries() {
    return config.getBoolean("Grace.Tries.Enable");
  }

  public static int getGraceTries() {
    return config.getInt("Grace.Tries.Value");
  }

  public static boolean doDuration() {
    return config.getBoolean("Duration.Enable");
  }

  public static int getDurationTime(boolean isFirstJoin) {
    int duration = config.getInt("Duration.Time");
    if (isFirstJoin && Config.doFirstJoinOverrideDuration()) {
      duration = Config.getFirstJoinOverrideDuration();
    }
    return duration;
  }

  public static boolean doCooldown() {
    return config.getBoolean("Cooldown.Enable");
  }

  public static int getCooldownTime() {
    return config.getInt("Cooldown.Time");
  }

  public static boolean doWarning() {
    return config.getBoolean("Warning.Enable");
  }

  public static int getWarningTime(boolean isFirstJoin) {
    int warning = config.getInt("Warning.Time");
    if (isFirstJoin && Config.doFirstJoinIgnoreWarningTime()) {
      warning = 0;
    }
    return warning;
  }

  public static boolean doCorrectClear() {
    return config.getBoolean("Correct.Clear");
  }

  public static boolean doCorrectReward() {
    return config.getBoolean("Correct.Reward.Enable");
  }

  public static List<String> getCorrectRewardCommands() {
    return config.getStringList("Correct.Reward.Commands");
  }

  public static boolean isWrongMaxWrong(int strikes) {
    if (doFirstJoinIgnoreStrikes()) {
      return true;
    }
    return strikes == config.getInt("Wrong.MaxWrong");
  }

  public static boolean doWrongPunish() {
    return config.getBoolean("Wrong.Punish.Enable");
  }

  public static List<String> getWrongPunishCommands() {
    return config.getStringList("Wrong.Punish.Commands");
  }

  public static boolean doFirstJoin() {
    return config.getBoolean("FirstJoin.Enable");
  }

  public static boolean doFirstJoinIgnoreGrace() {
    return doFirstJoin() && config.getBoolean("FirstJoin.Ignore.Grace");
  }

  public static boolean doFirstJoinIgnoreStrikes() {
    return doFirstJoin() && config.getBoolean("FirstJoin.Ignore.Strikes");
  }

  public static boolean doFirstJoinIgnoreWarningTime() {
    return doFirstJoin() && config.getBoolean("FirstJoin.Ignore.Warning");
  }

  public static boolean doFirstJoinOverrideDuration() {
    return doFirstJoin() && config.getBoolean("FirstJoin.OverrideDuration.Enable");
  }

  public static int getFirstJoinOverrideDuration() {
    return config.getInt("FirstJoin.OverrideDuration.Value");
  }

  public static boolean doFirstJoinExtraPunish() {
    return doFirstJoin() && config.getBoolean("FirstJoin.ExtraPunish.Enable");
  }

  public static List<String> getFirstJoinExtraPunishCommands() {
    return config.getStringList("FirstJoin.ExtraPunish.Commands");
  }

  public static boolean doOnTrigger() {
    return config.getBoolean("OnTrigger.Enable");
  }

  public static List<String> getOnTriggerCommands() {
    return config.getStringList("OnTrigger.Commands");
  }

  public static boolean doTriggerCatchFish() {
    return config.getBoolean("Triggers.CatchFish.Enable");
  }

  public static int getTriggerCatchFishChance() {
    return config.getInt("Triggers.CatchFish.Chance");
  }

  public static boolean doTriggerKillMob() {
    return config.getBoolean("Triggers.KillMob.Enable");
  }

  public static int getTriggerKillMobChance() {
    return config.getInt("Triggers.KillMob.Chance");
  }

  public static boolean doTriggerKillMobSpawnerOnly() {
    return config.getBoolean("Triggers.KillMob.SpawnerOnly");
  }

  public static boolean doTriggerBreakBlock() {
    return config.getBoolean("Triggers.BreakBlock.Enable");
  }

  public static int getTriggerBreakBlockChance() {
    return config.getInt("Triggers.BreakBlock.Chance");
  }

  public static boolean isTriggerMaterial(Material material) {
    if (!config.getBoolean("Filter.Enable")) {
      return true;
    }
    boolean result = !filterMaterials.contains(material);
    if (config.getBoolean("Filter.Invert")) {
      result = !result;
    }
    return result;
  }

  public static boolean doIntegrationAuthMe() {
    return config.getBoolean("Integration.AuthMe") && Bukkit.getPluginManager()
        .isPluginEnabled("AuthMe");
  }

  public static boolean doIntegrationLockLogin() {
    return config.getBoolean("Integration.LockLogin") && Bukkit.getPluginManager()
        .isPluginEnabled("LockLogin");
  }

  public static int getGUISize() {
    return config.getInt("GUI.Size");
  }

  private static String getGUIName() {
    return config.getString("GUI.Name");
  }

  /**
   * Get captcha GUI inventory filled with random items.
   *
   * @param player Player to get captcha GUI inventory for.
   * @return Captcha GUI inventory filled with random items.
   */
  public static Inventory getGUI(Player player, int slot) {
    ItemStack[] contents = Items.getCaptchaItems(getGUISize());
    String guiName = applyColor(
        getGUIName().replaceAll(
            "%item%",
            Objects.requireNonNull(contents[slot].getItemMeta()).getDisplayName()));
    Inventory result = Bukkit.createInventory(player, getGUISize(), guiName);
    result.setContents(contents);
    return result;
  }
}
