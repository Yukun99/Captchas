package me.yukun.captchas.integration;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AuthMe implements Listener {

  public static boolean isAuthenticated(Player player) {
    return AuthMeApi.getInstance().isAuthenticated(player);
  }

  @EventHandler
  private void playerAuthMeLoginEvent(LoginEvent e) {
    if (IntegrationManager.notInQueue(e.getPlayer())) return;
    IntegrationManager.startCaptcha(e.getPlayer());
  }

  @EventHandler
  private void playerAuthMeLeaveEvent(PlayerQuitEvent e) {
    if (IntegrationManager.notInQueue(e.getPlayer())) return;
    IntegrationManager.dequeueCaptcha(e.getPlayer());
  }
}
