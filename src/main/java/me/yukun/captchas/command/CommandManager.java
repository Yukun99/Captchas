package me.yukun.captchas.command;

import me.yukun.captchas.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof ConsoleCommandSender) || !Config.hasCommandPermissions(sender)) {
      return false;
    }
    CaptchasCommand captchaCommand;
    switch (args.length) {
      case 1 -> {
        if (args[0].equals("reload")) {
          captchaCommand = new ReloadCommand(sender);
          break;
        }
        captchaCommand = new HelpCommand(sender);
      }
      case 2 -> {
        if (Bukkit.getPlayer(args[1]) == null) {
          captchaCommand = new HelpCommand(sender);
          break;
        }
        Player player = Bukkit.getPlayer(args[1]);
        captchaCommand = switch (args[0]) {
          case "open" -> new OpenCommand(sender, player);
          case "close" -> new CloseCommand(sender, player);
          default -> new HelpCommand(sender);
        };
      }
      default -> captchaCommand = new HelpCommand(sender);
    }
    return captchaCommand.execute();
  }
}
