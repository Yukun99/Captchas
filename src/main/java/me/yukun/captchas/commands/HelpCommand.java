package me.yukun.captchas.commands;

import me.yukun.captchas.config.Messages;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CaptchaCommand {

  public HelpCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    Messages.sendHelp(sender);
    return false;
  }
}
