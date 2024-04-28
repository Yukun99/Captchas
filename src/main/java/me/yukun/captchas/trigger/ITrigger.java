package me.yukun.captchas.trigger;

import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;

public interface ITrigger {

  default boolean cannotStart(Player player) {
    if (Config.doIgnoreCaptchas(player)) {
      return true;
    }
    return Captcha.isOnCooldown(player);
  }
}
