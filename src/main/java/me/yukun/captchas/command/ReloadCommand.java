package me.yukun.captchas.command;

import me.yukun.captchas.config.FileManager;
import me.yukun.captchas.config.Messages;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends CaptchasCommand {

  public ReloadCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    FileManager.reload();
    Messages.sendReloadSuccess(sender);
    if (sender instanceof Player player) {
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }
    return true;
  }
}
