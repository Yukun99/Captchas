package me.yukun.captchas.commands;

import org.bukkit.command.CommandSender;

public class CaptchaCommand {

  protected CommandSender sender;

  public CaptchaCommand(CommandSender sender) {
    this.sender = sender;
  }

  /**
   * Executes the command.
   */
  public boolean execute() {
    return false;
  }
}
