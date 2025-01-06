package me.yukun.captchas.integration;

import com.nickuc.login.api.event.bukkit.auth.LoginEvent;
import com.nickuc.login.api.nLoginAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class NLogin implements Listener {

  private static final nLoginAPI api = com.nickuc.login.api.nLoginAPI.getApi();

  public static boolean isAuthenticated(Player player) {
    return api.isAuthenticated(player.getName());
  }

  @EventHandler
  private void PlayerNLoginLoginEvent(LoginEvent e) {
    if (IntegrationManager.notInQueue(e.getPlayer())) {
      return;
    }
    IntegrationManager.startCaptcha(e.getPlayer());
  }

  @EventHandler
  private void PlayerNLoginLeaveEvent(PlayerQuitEvent e) {
    if (IntegrationManager.notInQueue(e.getPlayer())) {
      return;
    }
    IntegrationManager.dequeueCaptcha(e.getPlayer());
  }
}
