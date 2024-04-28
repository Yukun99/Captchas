package me.yukun.captchas.integrations;

import me.yukun.captchas.config.Config;
import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class IntegrationManager {

  public static void registerIntegrationEvents(PluginManager pm, Plugin plugin) {
    if (Config.doIntegrationAuthMe()) {
      pm.registerEvents(new AuthMe(), plugin);
      Messages.printIntegrationEnabled("AuthMe");
    }
  }

  public static boolean isAuthenticated(Player player) {
    if (!Config.doIntegrationAuthMe()) {
      return true;
    }
    return AuthMe.isAuthenticated(player);
  }

  public static void queueCaptcha(Player player, Captcha captcha) {
    if (!Config.doIntegrationAuthMe()) {
      return;
    }
    AuthMe.queueCaptcha(player, captcha);
  }
}
