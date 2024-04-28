package me.yukun.captchas.config.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.yukun.captchas.config.ConfigTypeEnum;
import me.yukun.captchas.config.FieldTypeEnum;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValidator implements IValidator {

  private final List<String> SECTIONS = new ArrayList<>(18) {{
    add("Grace");
    add("Grace.Time");
    add("Grace.Tries");
    add("Duration");
    add("Cooldown");
    add("Warning");
    add("Correct");
    add("Correct.Reward");
    add("Wrong");
    add("Wrong.Punish");
    add("FirstJoin");
    add("FirstJoin.Ignore");
    add("FirstJoin.OverrideDuration");
    add("FirstJoin.ExtraPunish");
    add("OnTrigger");
    add("Triggers");
    add("Triggers.CatchFish");
    add("Triggers.KillMob");
    add("Triggers.BreakBlock");
    add("Filter");
    add("Integration");
    add("GUI");
  }};

  private final Map<String, FieldTypeEnum> FIELDS = new HashMap<>(37) {{
    put("IgnoreOpped", FieldTypeEnum.BOOLEAN);
    put("Grace.Time.Enable", FieldTypeEnum.BOOLEAN);
    put("Grace.Time.Value", FieldTypeEnum.INTEGER);
    put("Grace.Tries.Enable", FieldTypeEnum.BOOLEAN);
    put("Grace.Tries.Value", FieldTypeEnum.INTEGER);
    put("Duration.Enable", FieldTypeEnum.BOOLEAN);
    put("Duration.Time", FieldTypeEnum.INTEGER);
    put("Cooldown.Enable", FieldTypeEnum.BOOLEAN);
    put("Cooldown.Time", FieldTypeEnum.INTEGER);
    put("Warning.Enable", FieldTypeEnum.BOOLEAN);
    put("Warning.Time", FieldTypeEnum.INTEGER);
    put("Correct.Clear", FieldTypeEnum.BOOLEAN);
    put("Correct.Reward.Enable", FieldTypeEnum.BOOLEAN);
    put("Correct.Reward.Commands", FieldTypeEnum.STRINGLIST);
    put("Wrong.MaxWrong", FieldTypeEnum.INTEGER);
    put("Wrong.Punish.Enable", FieldTypeEnum.BOOLEAN);
    put("Wrong.Punish.Commands", FieldTypeEnum.STRINGLIST);
    put("FirstJoin.Enable", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.Ignore.Grace", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.Ignore.Strikes", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.Ignore.Warning", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.OverrideDuration.Enable", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.OverrideDuration.Value", FieldTypeEnum.INTEGER);
    put("FirstJoin.ExtraPunish.Enable", FieldTypeEnum.BOOLEAN);
    put("FirstJoin.ExtraPunish.Commands", FieldTypeEnum.STRINGLIST);
    put("OnTrigger.Enable", FieldTypeEnum.BOOLEAN);
    put("OnTrigger.Commands", FieldTypeEnum.STRINGLIST);
    put("Triggers.CatchFish.Enable", FieldTypeEnum.BOOLEAN);
    put("Triggers.CatchFish.Chance", FieldTypeEnum.INTEGER);
    put("Triggers.KillMob.Enable", FieldTypeEnum.BOOLEAN);
    put("Triggers.KillMob.Chance", FieldTypeEnum.INTEGER);
    put("Triggers.KillMob.SpawnerOnly", FieldTypeEnum.BOOLEAN);
    put("Triggers.BreakBlock.Enable", FieldTypeEnum.BOOLEAN);
    put("Triggers.BreakBlock.Chance", FieldTypeEnum.INTEGER);
    put("Filter.Enable", FieldTypeEnum.BOOLEAN);
    put("Filter.Invert", FieldTypeEnum.BOOLEAN);
    put("Filter.Blocks", FieldTypeEnum.STRINGLIST);
    put("Integration.AuthMe", FieldTypeEnum.BOOLEAN);
    put("Integration.LockLogin", FieldTypeEnum.BOOLEAN);
    put("GUI.Size", FieldTypeEnum.INTEGER);
    put("GUI.Name", FieldTypeEnum.STRING);
  }};

  private final Set<String> PLAYER_PLACEHOLDER_FIELDS = new HashSet<>(4) {{
    add("Wrong.Punish.Commands");
    add("Correct.Reward.Commands");
    add("FirstJoin.ExtraPunish.Commands");
    add("OnTrigger.Commands");
  }};

  private final Set<String> ITEM_PLACEHOLDER_FIELDS = new HashSet<>(1) {{
    add("GUI.Name");
  }};

  @Override
  public void validate(FileConfiguration config) throws ValidationException {
    validateSections(config);
    validateFields(config);
  }

  private void validateSections(FileConfiguration config) throws ValidationException {
    for (String section : SECTIONS) {
      if (!config.isConfigurationSection(section)) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.SECTION,
                section));
      }
    }
  }

  private void validateFields(FileConfiguration config) throws ValidationException {
    for (String field : FIELDS.keySet()) {
      FieldTypeEnum fieldType = FIELDS.get(field);
      validateField(fieldType, config, field);
    }
  }

  @Override
  public void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.STRING, field));
    }
    if (ITEM_PLACEHOLDER_FIELDS.contains(field)) {
      return;
    }
    if (Objects.requireNonNull(config.getString(field)).contains("%item%")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.STRING, field));
    }
  }

  @Override
  public void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isInt(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.INTEGER, field));
    }
  }

  @Override
  public void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    if (!config.isBoolean(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.BOOLEAN, field));
    }
  }

  @Override
  public void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    if (PLAYER_PLACEHOLDER_FIELDS.contains(field)) {
      return;
    }
    List<String> fieldList = config.getStringList(field);
    for (String line : fieldList) {
      if (line.contains("%player%")) {
        throw new ValidationException(
            ValidationException.getErrorMessage(ConfigTypeEnum.CONFIG, FieldTypeEnum.STRINGLIST,
                field));
      }
    }
  }
}
