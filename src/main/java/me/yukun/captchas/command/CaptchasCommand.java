package me.yukun.captchas.command;

import org.bukkit.command.CommandSender;

public class CaptchasCommand {

  protected CommandSender sender;

  public CaptchasCommand(CommandSender sender) {
    this.sender = sender;
  }

  /**
   * Executes the command.
   */
  public boolean execute() {
    return false;
  }
}
