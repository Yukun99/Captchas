package me.yukun.captchas.trigger;

import java.util.Objects;
import java.util.Random;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class KillMobTrigger implements Listener, ITrigger {

  private static final Plugin PLUGIN = Objects.requireNonNull(
      Bukkit.getPluginManager().getPlugin("Captchas"));
  private static final Random RANDOM = new Random();

  @EventHandler
  private void SpawnerSpawnEvent(SpawnerSpawnEvent e) {
    e.getEntity().setMetadata("CAPTCHAS_SPAWNER", new FixedMetadataValue(PLUGIN, true));
  }

  @EventHandler
  private void PlayerKillMobEvent(EntityDeathEvent e) {
    if (!canTrigger(e)) {
      return;
    }
    Player killer = e.getEntity().getKiller();
    if (cannotStart(killer)) {
      return;
    }
    if (RANDOM.nextInt(Config.getTriggerKillMobChance()) != 0) {
      return;
    }
    new Captcha(killer, false);
  }

  private boolean canTrigger(EntityDeathEvent e) {
    if (e.getEntity() instanceof Player) {
      return false;
    }
    if (e.getEntity().getKiller() == null) {
      return false;
    }
    if (Config.doTriggerKillMobSpawnerOnly()) {
      return e.getEntity().hasMetadata("CAPTCHAS_SPAWNER");
    }
    return true;
  }
}
