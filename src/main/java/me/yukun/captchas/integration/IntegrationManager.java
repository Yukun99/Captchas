package me.yukun.captchas.integration;

import java.util.HashMap;
import java.util.Map;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class IntegrationManager {

  private static final Map<Player, Captcha> playerCaptchaMap = new HashMap<>();

  public static void registerIntegrationEvents(PluginManager pm, Plugin plugin) {
    if (Config.doIntegrationAuthMe()) {
      pm.registerEvents(new AuthMe(), plugin);
      Messages.printIntegrationEnabled("AuthMe");
    }
  }

  public static void queueCaptcha(Player player, Captcha captcha) {
    playerCaptchaMap.put(player, captcha);
  }

  protected static boolean isInQueue(Player player) {
    return playerCaptchaMap.containsKey(player);
  }

  public static void startCaptcha(Player player) {
    playerCaptchaMap.get(player).beginCaptcha();
    playerCaptchaMap.remove(player);
  }

  public static boolean isAuthenticated(Player player) {
    if (Config.doIntegrationAuthMe()) {
      return AuthMe.isAuthenticated(player);
    }
    return true;
  }
}
