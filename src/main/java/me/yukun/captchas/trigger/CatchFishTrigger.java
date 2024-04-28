package me.yukun.captchas.trigger;

import java.util.Random;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class CatchFishTrigger implements Listener, ITrigger {

  private static final Random RANDOM = new Random();

  @EventHandler
  private void PlayerCatchFishEvent(PlayerFishEvent e) {
    if (cannotStart(e.getPlayer())) {
      return;
    }
    if (e.getCaught() == null) {
      return;
    }
    if (RANDOM.nextInt(Config.getTriggerCatchFishChance()) != 0) {
      return;
    }
    new Captcha(e.getPlayer(), false);
  }
}
