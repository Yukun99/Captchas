package me.yukun.captchas.config.validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import me.yukun.captchas.config.ConfigTypeEnum;
import me.yukun.captchas.config.FieldTypeEnum;
import org.bukkit.configuration.file.FileConfiguration;

public class MessagesValidator implements IValidator {

  private final Map<String, FieldTypeEnum> fields = new HashMap<>(7) {{
    put("Prefix", FieldTypeEnum.STRING);
    put("Open", FieldTypeEnum.STRING);
    put("Warning", FieldTypeEnum.STRING);
    put("Grace", FieldTypeEnum.STRING);
    put("Right", FieldTypeEnum.STRING);
    put("Wrong", FieldTypeEnum.STRING);
    put("Punish", FieldTypeEnum.STRING);
  }};

  private final Set<String> strikePlaceholderFields = new HashSet<>() {{
    add("Right");
    add("Wrong");
  }};

  private final Set<String> cooldownPlaceholderFields = new HashSet<>() {{
    add("Right");
  }};

  private final Set<String> warnPlaceholderFields = new HashSet<>() {{
    add("Warning");
  }};

  private final Set<String> timePlaceholderFields = new HashSet<>() {{
    add("Open");
    add("Warning");
  }};

  @Override
  public void validate(FileConfiguration messages) throws ValidationException {
    validateFields(messages);
  }

  private void validateFields(FileConfiguration messages) throws ValidationException {
    for (String field : fields.keySet()) {
      FieldTypeEnum fieldType = fields.get(field);
      validateField(fieldType, messages, field);
    }
  }

  @Override
  public void validateStringField(FileConfiguration messages, String field)
      throws ValidationException {
    if (!messages.isString(field)) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.STRING,
              field));
    }
    validateStrikeStringField(messages, field);
    validateCooldownStringField(messages, field);
    validateWarnStringField(messages, field);
    validateTimeStringField(messages, field);
  }

  private void validateStrikeStringField(FileConfiguration messages, String field)
      throws ValidationException {
    if (strikePlaceholderFields.contains(field)) {
      return;
    }
    if (Objects.requireNonNull(messages.getString(field)).contains("%strikes%")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.STRING,
              field));
    }
  }

  private void validateCooldownStringField(FileConfiguration messages, String field)
      throws ValidationException {
    if (cooldownPlaceholderFields.contains(field)) {
      return;
    }
    if (Objects.requireNonNull(messages.getString(field)).contains("%cooldown%")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.STRING,
              field));
    }
  }

  private void validateWarnStringField(FileConfiguration messages, String field)
      throws ValidationException {
    if (warnPlaceholderFields.contains(field)) {
      return;
    }
    if (Objects.requireNonNull(messages.getString(field)).contains("%warn%")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.STRING,
              field));
    }
  }

  private void validateTimeStringField(FileConfiguration messages, String field)
      throws ValidationException {
    if (timePlaceholderFields.contains(field)) {
      return;
    }
    if (Objects.requireNonNull(messages.getString(field)).contains("%time%")) {
      throw new ValidationException(
          ValidationException.getErrorMessage(ConfigTypeEnum.MESSAGES, FieldTypeEnum.STRING,
              field));
    }
  }
}
