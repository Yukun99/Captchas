package me.yukun.captchas.config;

import static me.yukun.captchas.util.TextFormatter.applyColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

  private static Map<Material, String> materialNameMap;
  private static List<Material> materials;

  protected static void setup(FileConfiguration fileConfiguration) {
    parseItems(fileConfiguration);
  }

  /**
   * Parses item and item display names from Items.yml.
   *
   * @param fileConfiguration Configuration file to parse item and item display names from.
   */
  private static void parseItems(FileConfiguration fileConfiguration) {
    Set<String> keys = Objects.requireNonNull(
        fileConfiguration.getConfigurationSection("Items")).getKeys(false);
    materialNameMap = new HashMap<>(keys.size());
    materials = new ArrayList<>(keys.size());
    String prefix = fileConfiguration.getString("Prefix");
    String suffix = fileConfiguration.getString("Suffix");
    for (String materialName : keys) {
      Material material = Material.getMaterial(materialName);
      String materialDisplayName = applyColor(
          prefix + fileConfiguration.getString("Items." + materialName) + suffix);
      materialNameMap.put(material, materialDisplayName);
      materials.add(material);
    }
  }

  /**
   * Get random list of ItemStacks for captcha GUI.
   *
   * @param guiSize Number of random items to get.
   * @return List of ItemStack to populate captcha GUI with.
   */
  public static ItemStack[] getCaptchaItems(int guiSize) {
    Collections.shuffle(materials);
    ItemStack[] result = new ItemStack[guiSize];
    for (int i = 0; i < guiSize; i++) {
      ItemStack item = new ItemStack(materials.get(i));
      ItemMeta itemMeta = item.getItemMeta();
      assert itemMeta != null;
      itemMeta.setDisplayName(materialNameMap.get(materials.get(i)));
      item.setItemMeta(itemMeta);
      result[i] = item;
    }
    return result;
  }
}
