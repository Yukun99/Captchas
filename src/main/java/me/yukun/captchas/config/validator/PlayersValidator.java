package me.yukun.captchas.config.validator;

import java.util.Objects;
import me.yukun.captchas.config.ConfigTypeEnum;
import me.yukun.captchas.config.FieldTypeEnum;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayersValidator implements IValidator {

  @Override
  public void validate(FileConfiguration players) throws ValidationException {
    for (String uuid : Objects.requireNonNull(players.getConfigurationSection("Players"))
        .getKeys(false)) {
      try {
        validatePlayerUUID(uuid);
      } catch (ValidationException exception) {
        players.set("Players." + uuid, null);
        throw exception;
      }
      validateField(FieldTypeEnum.INTEGER, players, "Players." + uuid);
    }
  }

  /**
   * Validates player UUID against all players that have joined the server when parsing from file.
   *
   * @param uuid UUID of player saved in Players.yml.
   * @throws ValidationException If specified player UUID has never joined the server.
   */
  private void validatePlayerUUID(String uuid) throws ValidationException {
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getUniqueId().toString().equals(uuid)) {
        return;
      }
    }
    throw new ValidationException("Player with UUID " + uuid + " does not exist! Deleting.");
  }

  @Override
  public void validateIntegerField(FileConfiguration players, String field)
      throws ValidationException {
    if (!players.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.PLAYERS, FieldTypeEnum.INTEGER,
              field));
    }
  }
}
