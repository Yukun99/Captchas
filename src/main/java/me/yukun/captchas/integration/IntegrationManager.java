package me.yukun.captchas.integration;

import me.yukun.captchas.config.Config;
import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class IntegrationManager {

  private IntegrationManager() {}

  private static final Map<Player, Captcha> playerCaptchaMap = new HashMap<>();

  public static void registerIntegrationEvents(PluginManager pm, Plugin plugin) {
    if (Config.doIntegrationAuthMe()) {
      pm.registerEvents(new AuthMe(), plugin);
      Messages.printIntegrationEnabled("AuthMe");
    }
    if (Config.doIntegrationNLogin()) {
      pm.registerEvents(new NLogin(), plugin);
      Messages.printIntegrationEnabled("nLogin");
    }
  }

  public static void queueCaptcha(Player player, Captcha captcha) {
    playerCaptchaMap.put(player, captcha);
  }

  protected static void dequeueCaptcha(Player player) {
    playerCaptchaMap.get(player).clickWrong(true);
    playerCaptchaMap.remove(player);
  }

  public static boolean isInQueue(Player player) {
    return playerCaptchaMap.containsKey(player);
  }

  protected static boolean notInQueue(Player player) {
    return !playerCaptchaMap.containsKey(player);
  }

  public static void startCaptcha(Player player) {
    playerCaptchaMap.get(player).beginCaptcha();
    playerCaptchaMap.remove(player);
  }

  public static boolean isAuthenticated(Player player) {
    if (Config.doIntegrationAuthMe()) {
      return AuthMe.isAuthenticated(player);
    }
    if (Config.doIntegrationNLogin()) {
      return NLogin.isAuthenticated(player);
    }
    return true;
  }
}
