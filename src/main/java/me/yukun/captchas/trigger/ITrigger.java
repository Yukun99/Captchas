package me.yukun.captchas.trigger;

import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;

import static me.yukun.captchas.gui.Captcha.cannotTriggerCaptcha;

public interface ITrigger {

  default boolean cannotStart(Player player) {
    if (Config.doIgnoreCaptchas(player)) {
      return true;
    }
    if (Captcha.isOnCooldown(player)) {
      return true;
    }
    return cannotTriggerCaptcha(player);
  }
}
