package me.yukun.captchas.commands;

import me.yukun.captchas.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;


public class CommandManager implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof ConsoleCommandSender) || !Config.hasCommandPermissions(sender)) {
      return false;
    }
    CaptchaCommand captchaCommand;
    switch (args.length) {
      case 0:
        captchaCommand = new HelpCommand(sender);
        break;
      case 1:
        if (args[0].equals("reload")) {
          captchaCommand = new ReloadCommand(sender);
        } else {
          captchaCommand = new HelpCommand(sender);
        }
        break;
      default:
        captchaCommand = new CaptchaCommand(sender);
    }
    return captchaCommand.execute();
  }
}
