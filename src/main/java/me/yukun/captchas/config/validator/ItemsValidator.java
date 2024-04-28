package me.yukun.captchas.config.validator;

import java.util.Objects;
import me.yukun.captchas.config.ConfigTypeEnum;
import me.yukun.captchas.config.FieldTypeEnum;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemsValidator implements IValidator {

  @Override
  public void validate(FileConfiguration items) throws ValidationException {
    validateSections(items);
    validateField(FieldTypeEnum.STRING, items, "Prefix");
    validateField(FieldTypeEnum.STRING, items, "Suffix");
    for (String materialName : Objects.requireNonNull(items.getConfigurationSection("Items"))
        .getKeys(false)) {
      try {
        validateItemStringField(items, materialName);
      } catch (ValidationException exception) {
        items.set("Items." + materialName, null);
        throw exception;
      }
    }
  }

  private void validateSections(FileConfiguration items) throws ValidationException {
    if (!items.isConfigurationSection("Items")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ITEMS, FieldTypeEnum.SECTION,
              "Items"));
    }
  }

  @Override
  public void validateStringField(FileConfiguration items, String field)
      throws ValidationException {
    if (!items.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.ITEMS, FieldTypeEnum.STRING, field));
    }
  }

  private void validateItemStringField(FileConfiguration items, String materialName)
      throws ValidationException {
    validateStringField(items, "Items." + materialName);
    if (Material.getMaterial(materialName) == null) {
      throw new ValidationException(
          "&cItem with material name " + materialName + " does not exist! Removing.");
    }
  }
}
