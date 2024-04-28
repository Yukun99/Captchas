package me.yukun.captchas.command;

import me.yukun.captchas.config.Messages;
import me.yukun.captchas.gui.Captcha;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends CaptchaCommand {

  Player player;

  public OpenCommand(CommandSender sender, Player player) {
    super(sender);
    this.player = player;
  }

  @Override
  public boolean execute() {
    Captcha.openPlayerCaptcha(player);
    Messages.sendOpenSuccess(sender);
    return true;
  }
}
