package me.yukun.captchas.trigger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.yukun.captchas.gui.Captcha.isInCaptcha;

public class CaptchaFreezeTrigger implements Listener {

  @EventHandler
  private void playerCaptchaBreakBlockEvent(BlockBreakEvent e) {
    if (isInCaptcha(e.getPlayer())) e.setCancelled(true);
  }

  @EventHandler
  private void playerCaptchaCatchFishEvent(PlayerFishEvent e) {
    if (isInCaptcha(e.getPlayer())) e.setCancelled(true);
  }

  @EventHandler
  private void playerCaptchaInteractEvent(PlayerInteractEvent e) {
    if (isInCaptcha(e.getPlayer())) e.setCancelled(true);
  }

  @EventHandler
  private void playerCaptchaMoveEvent(PlayerMoveEvent e) {
    if (isInCaptcha(e.getPlayer())) e.setCancelled(true);
  }

  @EventHandler
  private void playerCaptchaDamageEvent(EntityDamageByEntityEvent e) {
    if (!(e.getDamager() instanceof Player)) return;
    if (isInCaptcha((Player) e.getDamager())) e.setCancelled(true);
  }

  @EventHandler
  private void playerCaptchaDropItemEvent(PlayerDropItemEvent e) {
    if (isInCaptcha(e.getPlayer())) e.setCancelled(true);
  }
}
