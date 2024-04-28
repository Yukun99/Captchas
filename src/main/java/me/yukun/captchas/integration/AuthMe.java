package me.yukun.captchas.integration;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMe implements Listener {

  public static boolean isAuthenticated(Player player) {
    return AuthMeApi.getInstance().isAuthenticated(player);
  }

  @EventHandler
  private void PlayerAuthMeLoginEvent(LoginEvent e) {
    if (!IntegrationManager.isInQueue(e.getPlayer())) {
      return;
    }
    IntegrationManager.startCaptcha(e.getPlayer());
  }
}
