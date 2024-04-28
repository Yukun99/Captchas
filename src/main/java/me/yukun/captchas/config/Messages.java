package me.yukun.captchas.config;

import static me.yukun.captchas.util.TextFormatter.applyColor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Messages {

  // Plugin info ping messages.
  private static final String VERSION = "Captchas v%version% loaded.";
  private static final String INTEGRATION = "%integration% integration enabled.";
  // Configuration related logging.
  private static final String FOLDER = "Config folder";
  private static final String EXISTS = " exists, skipping creation.";
  private static final String NOT_EXISTS = " not created, creating now.";
  private static final String COPY_ERROR = "&cERROR! %file% could not be created.";
  private static final String CONFIG_ERROR = "&cERROR! Config files could not load properly. Plugin will be disabled.";
  private static final String SAVE_ERROR = "&cERROR! %file% could not be saved.";
  private static final String VALIDATION_SUCCESS = "&aValidation success! %file% has no errors.";
  private static final String RELOAD = "&a%file% reloaded!";
  // Command reply messages.
  private static final String HELP_HEADER = "&b&l===============Captchas===============";
  private static final String HELP_COMMANDS = "&b&l----------Commands----------";
  private static final String HELP_ALIASES = "Command aliases: captchas, captcha, cpc";
  private static final String HELP_HELP = "/captchas help: Shows commands, aliases and permissions.";
  private static final String HELP_RELOAD = "/captchas reload: Reloads all configuration files.";
  private static final String HELP_PERMISSIONS = "&b&l----------Permissions----------";
  private static final String HELP_WILDCARD = "captchas.*: All permissions combined";
  private static final String HELP_ADMIN = "captchas.admin: Ability to use commands + admin.bypass";
  private static final String HELP_BYPASS = "captchas.bypass: Immunity to captcha checking.";
  private static final String HELP_FOOTER = "&b&l======================================";
  private static final String RELOAD_SUCCESS = "&aReload successful!";
  private static final String OPEN_SUCCESS = "&aCaptcha opened for specified player.";
  private static final String CLOSE_SUCCESS = "&aCaptcha closed for specified player.";
  // Prefix appended before all messages.
  private static String prefix = "&bCapt&echas&f >> &7";
  // Messages sent to players.
  private static String open;
  private static String warning;
  private static String grace;
  private static String right;
  private static String wrong;
  private static String punish;

  protected static void setup(FileConfiguration fileConfiguration) {
    prefix = fileConfiguration.getString("Prefix");
    open = prefix + fileConfiguration.getString("Open");
    warning = prefix + fileConfiguration.getString("Warning");
    grace = prefix + fileConfiguration.getString("Grace");
    right = prefix + fileConfiguration.getString("Right");
    wrong = prefix + fileConfiguration.getString("Wrong");
    punish = prefix + fileConfiguration.getString("Punish");
  }

  /**
   * Sends plugin version info to specified player.
   *
   * @param player Player to send plugin version info to.
   * @param plugin Plugin to get version info for.
   */
  public static void sendPluginVersion(Player player, Plugin plugin) {
    String message = prefix + VERSION.replaceAll("%version%", plugin.getDescription().getVersion());
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends plugin integration enabled status to specified player.
   *
   * @param player          Player to send plugin integration enabled status to.
   * @param integrationName Name of plugin that has integration enabled.
   */
  public static void sendIntegrationEnabled(Player player, String integrationName) {
    String message = prefix + INTEGRATION.replaceAll("%integration%", integrationName);
    player.sendMessage(applyColor(message));
  }

  /**
   * Sends config error message to specified player.
   *
   * @param player Player to send config error message to.
   */
  public static void sendConfigError(Player player) {
    player.sendMessage(applyColor(prefix + CONFIG_ERROR));
  }

  /**
   * Logging message during setup sent if config folder exists.
   */
  protected static void printFolderExists() {
    System.out.println(applyColor(prefix + FOLDER + EXISTS));
  }

  /**
   * Logging message during setup sent if config folder does not exist.
   */
  protected static void printFolderNotExists() {
    System.out.println(applyColor(prefix + FOLDER + NOT_EXISTS));
  }

  /**
   * Logging message during setup sent if specified file exists in config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileExists(String filename) {
    System.out.println(applyColor(prefix + filename + EXISTS));
  }

  /**
   * Logging message during setup sent if specified file does not exist in config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileNotExists(String filename) {
    System.out.println(applyColor(prefix + filename + NOT_EXISTS));
  }

  /**
   * Logging message during setup sent if specified file could not be copied to config folder.
   *
   * @param filename Filename of specified file.
   */
  protected static void printFileCopyError(String filename) {
    String message = prefix + COPY_ERROR.replaceAll("%file%", filename);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during setup sent if config files contain errors and could not load properly.
   */
  public static void printConfigError(Exception exception) {
    System.out.println(applyColor(prefix + CONFIG_ERROR));
    System.out.println(applyColor(prefix + exception.getMessage()));
  }

  /**
   * Logging message during setup sent if specified plugin integration is enabled.
   *
   * @param integrationName Name of plugin to integrate with.
   */
  public static void printIntegrationEnabled(String integrationName) {
    String message = prefix + INTEGRATION.replaceAll("%integration%", integrationName);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during shutdown sent if specified file could not be saved.
   *
   * @param filename Filename of specified file.
   */
  public static void printSaveError(String filename) {
    String message = prefix + SAVE_ERROR.replaceAll("%file%", filename);
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during setup sent if specified file is validated successfully.
   *
   * @param configType Configuration file type that was validated successfully.
   */
  public static void printValidationSuccess(ConfigTypeEnum configType) {
    String message = prefix + VALIDATION_SUCCESS.replaceAll("%file%", configType.toString());
    System.out.println(applyColor(message));
  }

  /**
   * Logging message during reloading sent if specified file is reloaded successfully.
   *
   * @param configType Configuration file type that was reloaded successfully.
   */
  public static void printReloaded(ConfigTypeEnum configType) {
    String message = prefix + RELOAD.replaceAll("%file%", configType.toString());
    System.out.println(applyColor(message));
  }

  /**
   * Send captcha opening message to player.
   *
   * @param player Player to send captcha opening message to.
   * @param time   Time in seconds player has to complete captcha in.
   */
  public static void sendOpen(Player player, int time) {
    player.sendMessage(applyColor(open.replaceAll("%time%", String.valueOf(time))));
  }

  /**
   * Send captcha opening warning message to player.
   *
   * @param player Player to send captcha opening warning message to.
   * @param warn   Time in seconds before captcha opens.
   * @param time   Time in seconds that player has to complete captcha after it opens.
   */
  public static void sendWarning(Player player, int warn, int time) {
    player.sendMessage(applyColor(warning.replaceAll("%warn%", String.valueOf(warn))
        .replaceAll("%time%", String.valueOf(time))));
  }

  /**
   * Send grace period message to player.
   *
   * @param player Player to send grace period message to.
   */
  public static void sendGrace(Player player) {
    player.sendMessage(applyColor(grace));
  }

  /**
   * Send right answer message to player.
   *
   * @param player   Player to send right answer message to.
   * @param cooldown Cooldown time before next captcha can open.
   * @param strikes  Number of strikes that the player has.
   */
  public static void sendRight(Player player, int cooldown, int strikes) {
    player.sendMessage(applyColor(right.replaceAll("%cooldown%", String.valueOf(cooldown))
        .replaceAll("%strikes%", String.valueOf(strikes))));
  }

  /**
   * Send wrong answer message to player.
   *
   * @param player  Player to send wrong answer message to.
   * @param strikes Number of strikes that the player has.
   */
  public static void sendWrong(Player player, int strikes) {
    player.sendMessage(applyColor(wrong.replaceAll("%strikes%", String.valueOf(strikes))));
  }

  /**
   * Send punish message to player.
   *
   * @param player Player to send punish message to.
   */
  public static void sendPunish(Player player) {
    player.sendMessage(applyColor(punish));
  }

  /**
   * Send commands help message to help command sender.
   *
   * @param commandSender Command sender to send commands help message to.
   */
  public static void sendHelp(CommandSender commandSender) {
    commandSender.sendMessage(applyColor(HELP_HEADER));
    commandSender.sendMessage(applyColor(HELP_COMMANDS));
    commandSender.sendMessage(applyColor(HELP_ALIASES));
    commandSender.sendMessage(applyColor(HELP_HELP));
    commandSender.sendMessage(applyColor(HELP_RELOAD));
    commandSender.sendMessage(applyColor(HELP_PERMISSIONS));
    commandSender.sendMessage(applyColor(HELP_WILDCARD));
    commandSender.sendMessage(applyColor(HELP_ADMIN));
    commandSender.sendMessage(applyColor(HELP_BYPASS));
    commandSender.sendMessage(applyColor(HELP_FOOTER));
  }

  /**
   * Send config reloaded message to player.
   *
   * @param commandSender CommandSender to send reloaded message to.
   */
  public static void sendReloadSuccess(CommandSender commandSender) {
    commandSender.sendMessage(applyColor(prefix + RELOAD_SUCCESS));
  }

  /**
   * Send captcha opening success message to player.
   *
   * @param sender CommandSender to send captcha opening success message to.
   */
  public static void sendOpenSuccess(CommandSender sender) {
    sender.sendMessage(applyColor(prefix + OPEN_SUCCESS));
  }

  /**
   * Send captcha closing success message to player.
   *
   * @param sender CommandSender to send captcha closing success message to.
   */
  public static void sendCloseSuccess(CommandSender sender) {
    sender.sendMessage(applyColor(prefix + CLOSE_SUCCESS));
  }
}
