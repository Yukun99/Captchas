package me.yukun.captchas.gui;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.config.Messages;
import me.yukun.captchas.config.Players;
import me.yukun.captchas.integrations.IntegrationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class Captcha {

  private static final Random RANDOM = new Random();
  private static final Plugin PLUGIN = Objects.requireNonNull(
      Bukkit.getPluginManager().getPlugin("Captchas"));
  // Players on captcha cooldown
  private static final Set<Player> cooldowns = new HashSet<>();
  // General variables
  private final Player target;
  private final int slot;
  boolean isFirstJoin;
  private Inventory inventory;
  private int durationTimer;
  // Remaining grace time and chances
  private boolean isGraceTime = false;
  private int graceChances = 0;

  /**
   * Starts a captcha for specified player. Will not start a captcha if player is on cooldown.
   *
   * @param player      Player to start captcha for.
   * @param isFirstJoin Whether player has joined the server for the first time.
   */
  public Captcha(Player player, boolean isFirstJoin) {
    this.target = player;
    this.isFirstJoin = isFirstJoin;
    this.slot = RANDOM.nextInt(Config.getGUISize());
    if (!IntegrationManager.isAuthenticated(target)) {
      IntegrationManager.queueCaptcha(target, this);
    } else {
      beginCaptcha();
    }
  }

  /**
   * Check if specified player is on Captcha cooldown.
   *
   * @param player Player to be checked for cooldown status.
   * @return Whether specified player is on Captcha cooldown.
   */
  public static boolean isOnCooldown(Player player) {
    return cooldowns.contains(player);
  }

  /**
   * Begins captcha triggering procedure.
   */
  public void beginCaptcha() {
    setupTriggerCommands();
    setupGUI();
    setupTimes();
    setupGrace();
  }

  /**
   * Executes commands to be triggered on captcha start.
   */
  private void setupTriggerCommands() {
    if (!Config.doOnTrigger()) {
      return;
    }
    for (String command : Config.getOnTriggerCommands()) {
      String playerCommand = command.replaceAll("%player%", target.getName());
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand);
    }
  }

  /**
   * Sets up and saves captcha GUI inventory.
   */
  private void setupGUI() {
    inventory = Config.getGUI(target, slot);
  }

  /**
   * Setups up captcha times and starts captcha timers.
   */
  private void setupTimes() {
    startTimers();
  }

  /**
   * Starts captcha timers.
   */
  private void startTimers() {
    int warning = Config.getWarningTime(isFirstJoin);
    int duration = Config.getDurationTime(isFirstJoin);
    doWarning();
    Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, this::startCaptcha, warning * 20L);
    if (Config.doDuration()) {
      durationTimer = Bukkit.getScheduler()
          .scheduleSyncDelayedTask(PLUGIN, () -> clickWrong(true), (warning + duration) * 20L);
    }
  }

  /**
   * Starts warning timer and sends warning message if warning is enabled.
   */
  private void doWarning() {
    if (!Config.doWarning()) {
      return;
    }
    int warning = Config.getWarningTime(isFirstJoin);
    int duration = Config.getDurationTime(isFirstJoin);
    if (warning == 0) {
      return;
    }
    Messages.sendWarning(target, warning, duration);
  }

  /**
   * Sets up grace timer and tries.
   */
  private void setupGrace() {
    if (Config.doGraceTime()) {
      isGraceTime = true;
      Bukkit.getScheduler()
          .scheduleSyncDelayedTask(PLUGIN, () -> isGraceTime = false, Config.getGraceTime() * 20L);
    }
    if (Config.doGraceTries()) {
      graceChances = Config.getGraceTries();
    }
  }

  /**
   * Opens captcha inventory and starts captcha GUI tracking.
   */
  private void startCaptcha() {
    int duration = Config.getDurationTime(isFirstJoin);
    Messages.sendOpen(target, duration);
    target.openInventory(inventory);
    CaptchaListener.startCaptcha(this, inventory, slot);
  }

  /**
   * Stops captcha GUI tracking and closes captcha inventory.
   */
  private void endCaptcha() {
    if (Config.doDuration()) {
      Bukkit.getScheduler().cancelTask(durationTimer);
    }
    CaptchaListener.endCaptcha(inventory);
    target.closeInventory();
  }

  /**
   * Called when player has clicked on wrong item in captcha GUI or has no more time.<p> Sends
   * requisite messages to player too.
   *
   * @param ignoreGrace Whether wrong answer should ignore grace.
   */
  public void clickWrong(boolean ignoreGrace) {
    if (!ignoreGrace && useGrace()) {
      Messages.sendGrace(target);
      return;
    }
    int strikes = Players.addStrike(target);
    Messages.sendWrong(target, strikes);
    endCaptcha();
    if (Config.isWrongMaxWrong(strikes)) {
      punish();
    }
  }

  /**
   * Punishes player for reaching max number of strikes.
   */
  private void punish() {
    if (!Config.doWrongPunish()) {
      return;
    }
    Messages.sendPunish(target);
    for (String command : Config.getWrongPunishCommands()) {
      String playerCommand = command.replaceAll("%player%", target.getName());
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand);
    }
    if (Config.doFirstJoinExtraPunish()) {
      for (String command : Config.getFirstJoinExtraPunishCommands()) {
        String playerCommand = command.replaceAll("%player%", target.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand);
      }
    }
  }

  /**
   * Called when player has clicked on right item in captcha GUI.<p> Sends requisite messags to
   * player too.
   */
  public void clickRight() {
    int strikes = 0;
    if (Config.doCorrectClear()) {
      strikes = Players.removeStrike(target);
    }
    Messages.sendRight(target, Config.getCooldownTime(), strikes);
    endCaptcha();
    reward();
  }

  /**
   * Rewards player for getting captcha right.
   */
  private void reward() {
    if (Config.doCooldown()) {
      cooldowns.add(target);
      Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, () -> cooldowns.remove(target),
          Config.getCooldownTime() * 20L);
    }
    if (!Config.doCorrectReward()) {
      return;
    }
    for (String command : Config.getCorrectRewardCommands()) {
      String playerCommand = command.replaceAll("%player%", target.getName());
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand);
    }
  }

  /**
   * Tries to use grace to not get captcha wrong.
   *
   * @return Whether grace has been successfully used and player should be pardoned.
   */
  private boolean useGrace() {
    if (Config.doFirstJoinIgnoreGrace()) {
      return false;
    }
    if (isGraceTime) {
      return true;
    }
    if (graceChances > 0) {
      --graceChances;
      return true;
    }
    return false;
  }
}
