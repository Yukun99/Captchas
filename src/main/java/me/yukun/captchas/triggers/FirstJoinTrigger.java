package me.yukun.captchas.triggers;

import me.yukun.captchas.config.Players;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstJoinTrigger implements Listener, ITrigger {

  @EventHandler
  private void PlayerFirstJoinEvent(PlayerJoinEvent e) {
    if (cannotStart(e.getPlayer())) {
      return;
    }
    if (!Players.isFirstJoin(e.getPlayer())) {
      return;
    }
    Players.saveFirstJoin(e.getPlayer());
    new Captcha(e.getPlayer(), true);
  }
}
