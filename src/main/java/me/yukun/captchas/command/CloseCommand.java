package me.yukun.captchas.command;

import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CloseCommand extends CaptchasCommand {

  Player player;

  public CloseCommand(CommandSender sender, Player player) {
    super(sender);
    this.player = player;
  }

  @Override
  public boolean execute() {
    Captcha.closePlayerCaptcha(player);
    Messages.sendCloseSuccess(sender);
    return true;
  }
}
