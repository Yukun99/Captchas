package me.yukun.captchas.command;

import me.yukun.captchas.config.Messages;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CaptchasCommand {

  public HelpCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public boolean execute() {
    Messages.sendHelp(sender);
    return false;
  }
}
