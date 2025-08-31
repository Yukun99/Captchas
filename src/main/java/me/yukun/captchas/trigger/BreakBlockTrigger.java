package me.yukun.captchas.trigger;

import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class BreakBlockTrigger implements Listener, ITrigger {

  private static final Random RANDOM = new Random();

  @EventHandler
  private void playerBreakBlockEvent(BlockBreakEvent e) {
    if (cannotStart(e.getPlayer())) return;
    if (!Config.isTriggerMaterial(e.getBlock().getType())) return;
    if (RANDOM.nextInt(Config.getTriggerBreakBlockChance()) != 0) return;
    new Captcha(e.getPlayer(), false);
  }
}
