package me.yukun.captchas.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import me.yukun.captchas.config.validator.ConfigValidator;
import me.yukun.captchas.config.validator.ItemsValidator;
import me.yukun.captchas.config.validator.MessagesValidator;
import me.yukun.captchas.config.validator.PlayersValidator;
import me.yukun.captchas.config.validator.ValidationException;
import me.yukun.captchas.util.IOExceptionConsumer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Class that manages configuration file I/O.
 */
public class FileManager {

  private static FileManager fileManager;
  private final File dataFolder;

  private final Map<ConfigTypeEnum, FileConfiguration> fileConfigurationMap = new HashMap<>();
  // Processing maps
  private final Map<ConfigTypeEnum, Consumer<ConfigTypeEnum>> validateConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      try {
        new ConfigValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      try {
        new MessagesValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.PLAYERS, configType -> {
      try {
        new PlayersValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
    put(ConfigTypeEnum.ITEMS, configType -> {
      try {
        new ItemsValidator().validate(fileManager.fileConfigurationMap.get(configType));
        Messages.printValidationSuccess(configType);
      } catch (ValidationException e) {
        Messages.printConfigError(e);
      }
    });
  }};
  private final Map<ConfigTypeEnum, Consumer<ConfigTypeEnum>> setupConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Config.setup(fileManager.fileConfigurationMap.get(configType));
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Messages.setup(fileManager.fileConfigurationMap.get(configType));
    });
    put(ConfigTypeEnum.PLAYERS, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Players.setup(fileManager.fileConfigurationMap.get(configType));
    });
    put(ConfigTypeEnum.ITEMS, configType -> {
      validateConfigMap.get(configType).accept(configType);
      Items.setup(fileManager.fileConfigurationMap.get(configType));
    });
  }};
  // Files and FileConfigurations
  private File configFile;
  private File messagesFile;
  private File playersFile;
  private File itemsFile;
  private final Map<ConfigTypeEnum, IOExceptionConsumer<FileConfiguration>> saveConfigMap = new HashMap<>(
      4) {{
    put(ConfigTypeEnum.PLAYERS, (players) -> players.save(playersFile));
    put(ConfigTypeEnum.ITEMS, (items) -> items.save(itemsFile));
  }};
  private final Map<ConfigTypeEnum, Function<ConfigTypeEnum, FileConfiguration>> reloadConfigMap = new HashMap<>() {{
    put(ConfigTypeEnum.CONFIG, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(configFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
      return fileConfigurationMap.get(ConfigTypeEnum.CONFIG);
    });
    put(ConfigTypeEnum.MESSAGES, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(messagesFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
      return fileConfigurationMap.get(ConfigTypeEnum.MESSAGES);
    });
    put(ConfigTypeEnum.PLAYERS, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(playersFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
      return fileConfigurationMap.get(ConfigTypeEnum.PLAYERS);
    });
    put(ConfigTypeEnum.ITEMS, configType -> {
      fileConfigurationMap.put(configType, YamlConfiguration.loadConfiguration(itemsFile));
      setupConfigMap.get(configType).accept(configType);
      Messages.printReloaded(configType);
      return fileConfigurationMap.get(ConfigTypeEnum.ITEMS);
    });
  }};

  /**
   * Constructor for a new FileManager instance.
   *
   * @param plugin Plugin to create FileManager instance for.
   * @throws Exception If config files cannot be loaded properly.
   */
  private FileManager(Plugin plugin) throws Exception {
    createFolder(plugin);
    dataFolder = plugin.getDataFolder();

    configFile = createFile("Config.yml");
    messagesFile = createFile("Messages.yml");
    playersFile = createFile("Players.yml");
    itemsFile = createFile("Items.yml");

    fileConfigurationMap.put(ConfigTypeEnum.CONFIG,
        YamlConfiguration.loadConfiguration(configFile));
    fileConfigurationMap.put(ConfigTypeEnum.MESSAGES,
        YamlConfiguration.loadConfiguration(messagesFile));
    fileConfigurationMap.put(ConfigTypeEnum.PLAYERS,
        YamlConfiguration.loadConfiguration(playersFile));
    fileConfigurationMap.put(ConfigTypeEnum.ITEMS, YamlConfiguration.loadConfiguration(itemsFile));
  }

  /**
   * Instantiates the FileManager instance to be used by the plugin.
   *
   * @param plugin Plugin to be instantiated from.
   * @return Whether config files have created successfully.
   */
  public static boolean onEnable(Plugin plugin) {
    try {
      fileManager = new FileManager(plugin);
      for (ConfigTypeEnum configType : fileManager.setupConfigMap.keySet()) {
        fileManager.setupConfigMap.get(configType).accept(configType);
      }
      return true;
    } catch (Exception e) {
      Messages.printConfigError(e);
      return false;
    }
  }

  public static void onDisable() {
    for (ConfigTypeEnum configType : fileManager.saveConfigMap.keySet()) {
      try {
        fileManager.saveConfigMap.get(configType)
            .accept(fileManager.fileConfigurationMap.get(configType));
      } catch (IOException e) {
        Messages.printSaveError(configType.toString());
      }
    }
  }

  public static void reload() {
    for (ConfigTypeEnum configType : fileManager.reloadConfigMap.keySet()) {
      fileManager.reloadConfigMap.get(configType).apply(configType);
    }
  }

  /**
   * Creates config folder if it doesn't exist.
   *
   * @param plugin Plugin to create config folder for.
   */
  private void createFolder(Plugin plugin) {
    if (plugin.getDataFolder().mkdir()) {
      Messages.printFolderNotExists();
    } else {
      Messages.printFolderExists();
    }
  }

  /**
   * Attempts to copy default config file into config folder. Does not copy file if config file
   * already exists in config folder.
   *
   * @param filename Filename of file to be created.
   * @return File instance pointing to the specified filename in config folder.
   * @throws Exception If default config file does not copy successfully.
   */
  private File createFile(String filename) throws Exception {
    File file = new File(dataFolder, filename);
    if (file.exists()) {
      Messages.printFileExists(filename);
      return file;
    }
    Messages.printFileNotExists(filename);
    File defaultFile = new File(dataFolder, "/" + filename);
    InputStream inputStream = getClass().getResourceAsStream("/" + filename);
    try {
      copyFile(inputStream, defaultFile);
    } catch (Exception copyException) {
      Messages.printFileCopyError(filename);
      throw copyException;
    }
    return file;
  }

  /**
   * Copies files from inside the jar to outside. Adapted from\
   * <a href="https://bukkit.org/threads/extracting-file-from-jar.16962/">thread</a>
   *
   * @param in  Where to copy file from.
   * @param out Where to copy file to.
   * @throws Exception If file does not get copied successfully.
   */
  private void copyFile(InputStream in, File out) throws Exception {
    try (InputStream fis = in; FileOutputStream fos = new FileOutputStream(out)) {
      byte[] buf = new byte[1024];
      int i;
      while ((i = fis.read(buf)) != -1) {
        fos.write(buf, 0, i);
      }
    }
  }
}
