package me.yukun.captchas.trigger;

import java.util.Random;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockTrigger implements Listener, ITrigger {

  private static final Random RANDOM = new Random();

  @EventHandler
  private void PlayerBreakBlockEvent(BlockBreakEvent e) {
    if (cannotStart(e.getPlayer())) {
      return;
    }
    if (!Config.isTriggerMaterial(e.getBlock().getType())) {
      return;
    }
    if (RANDOM.nextInt(Config.getTriggerBreakBlockChance()) != 0) {
      return;
    }
    new Captcha(e.getPlayer(), false);
  }
}
