package me.yukun.captchas.integrations;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import java.util.HashMap;
import java.util.Map;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMe implements Listener {

  private static final Map<Player, Captcha> playerCaptchaMap = new HashMap<>();

  public static boolean isAuthenticated(Player player) {
    return AuthMeApi.getInstance().isAuthenticated(player);
  }

  public static void queueCaptcha(Player player, Captcha captcha) {
    playerCaptchaMap.put(player, captcha);
  }

  @EventHandler
  private void PlayerAuthMeLoginEvent(LoginEvent e) {
    if (!playerCaptchaMap.containsKey(e.getPlayer())) {
      return;
    }
    playerCaptchaMap.get(e.getPlayer()).beginCaptcha();
    playerCaptchaMap.remove(e.getPlayer());
  }
}
