package me.yukun.captchas;

import me.yukun.captchas.command.CommandManager;
import me.yukun.captchas.config.Config;
import me.yukun.captchas.config.FileManager;
import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.CaptchaListener;
import me.yukun.captchas.integration.IntegrationManager;
import me.yukun.captchas.trigger.BreakBlockTrigger;
import me.yukun.captchas.trigger.CatchFishTrigger;
import me.yukun.captchas.trigger.FirstJoinTrigger;
import me.yukun.captchas.trigger.KillMobTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Captchas extends JavaPlugin implements Listener {

  private boolean isConfigErrored = false;

  @Override
  public void onEnable() {
    isConfigErrored = !FileManager.onEnable(this);
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    if (isConfigErrored) {
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }
    Objects.requireNonNull(getCommand("captchas")).setExecutor(new CommandManager());
    pm.registerEvents(new CaptchaListener(), this);
    if (Config.doFirstJoin()) {
      pm.registerEvents(new FirstJoinTrigger(), this);
    }
    if (Config.doTriggerCatchFish()) {
      pm.registerEvents(new CatchFishTrigger(), this);
    }
    if (Config.doTriggerKillMob()) {
      pm.registerEvents(new KillMobTrigger(), this);
    }
    if (Config.doTriggerBreakBlock()) {
      pm.registerEvents(new BreakBlockTrigger(), this);
    }
    IntegrationManager.registerIntegrationEvents(pm, this);
  }

  @Override
  public void onDisable() {
    if (isConfigErrored) {
      return;
    }
    FileManager.onDisable();
  }

  @EventHandler
  private void devJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.getName().equals("xu_yukun")) {
      return;
    }
    Messages.sendPluginVersion(player, this);
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    if (isConfigErrored) {
      Messages.sendConfigError(player);
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
    }
    if (Config.doIntegrationAuthMe()) {
      Messages.sendIntegrationEnabled(player, "AuthMe");
    }
  }

  @EventHandler
  private void adminJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.isOp() && !player.hasPermission("captchas.admin")) {
      return;
    }
    if (isConfigErrored) {
      Messages.sendConfigError(player);
    }
  }
}
