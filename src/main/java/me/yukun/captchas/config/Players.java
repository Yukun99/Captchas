package me.yukun.captchas.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Players {

  private static final Map<UUID, Integer> playerStrikesMap = new HashMap<>();
  private static FileConfiguration players;

  protected static void setup(FileConfiguration fileConfiguration) {
    players = fileConfiguration;
    parseStrikes();
  }

  /**
   * Parses saved strikes from Players.yml into hashmap for access later.
   */
  private static void parseStrikes() {
    if (!players.isConfigurationSection("Players")) {
      return;
    }
    for (String stringUUID : Objects.requireNonNull(players.getConfigurationSection("Players"))
        .getKeys(false)) {
      int playerStrikes = players.getInt("Players." + stringUUID);
      playerStrikesMap.put(UUID.fromString(stringUUID), playerStrikes);
    }
  }

  /**
   * Saves player that has just joined the server to strikes and file configuration.
   *
   * @param player Player that just joined the server for the first time.
   */
  public static void saveFirstJoin(Player player) {
    playerStrikesMap.put(player.getUniqueId(), 0);
    players.set("Players." + player.getUniqueId(), 0);
  }

  /**
   * Checks if specified player has joined the server before.
   *
   * @param player Player to be checked.
   * @return Whether specified player has joined the server before.
   */
  public static boolean isFirstJoin(Player player) {
    return !playerStrikesMap.containsKey(player.getUniqueId());
  }

  /**
   * Adds a strike to specified player.
   *
   * @param player Player to add strike to.
   * @return Number of strikes specified player has after adding a strike.
   */
  public static int addStrike(Player player) {
    int strikes = playerStrikesMap.get(player.getUniqueId());
    playerStrikesMap.put(player.getUniqueId(), ++strikes);
    players.set("Players." + player.getUniqueId(), strikes);
    return strikes;
  }

  /**
   * Removes a strike from specified player.
   *
   * @param player Player to remove strike from.
   * @return Number of strikes specified player has after removing a strike.
   */
  public static int removeStrike(Player player) {
    int strikes = playerStrikesMap.get(player.getUniqueId());
    if (strikes == 0) {
      return strikes;
    }
    playerStrikesMap.put(player.getUniqueId(), --strikes);
    players.set("Players." + player.getUniqueId(), strikes);
    return strikes;
  }
}
