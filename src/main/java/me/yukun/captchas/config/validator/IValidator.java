package me.yukun.captchas.config.validator;

import me.yukun.captchas.config.FieldTypeEnum;
import org.bukkit.configuration.file.FileConfiguration;

public interface IValidator {

  /**
   * Validate specified configuration file according to spec.
   *
   * @param config Configuration file to be verified.
   * @throws ValidationException If any field is missing or has wrong value type.
   */
  void validate(FileConfiguration config) throws ValidationException;

  /**
   * Validate specified field in specified configuration file.
   *
   * @param fieldType Type that specified field is supposed to be.
   * @param config    Configuration file that field is supposed to be in.
   * @param field     Field to be validated.
   * @throws ValidationException If field is missing, has wrong fieldType, or extra placeholders.
   */
  default void validateField(FieldTypeEnum fieldType, FileConfiguration config, String field)
      throws ValidationException {
    switch (fieldType) {
      case STRING -> validateStringField(config, field);
      case INTEGER -> validateIntegerField(config, field);
      case BOOLEAN -> validateBooleanField(config, field);
      case STRINGLIST -> validateStringListField(config, field);
      default -> {
      }
    }
  }

  /**
   * Validates a field with fieldType String.
   *
   * @param config Configuration file that field is supposed to be in.
   * @param field  Field to be evaluated.
   * @throws ValidationException If field is missing, has wrong fieldType, or extra placeholders.
   */
  default void validateStringField(FileConfiguration config, String field)
      throws ValidationException {
    throw new ValidationException("No string fields!");
  }

  /**
   * Validates a field with fieldType Int.
   *
   * @param config Configuration file that field is supposed to be in.
   * @param field  Field to be evaluated.
   * @throws ValidationException If field is missing, has wrong fieldType, or extra placeholders.
   */
  default void validateIntegerField(FileConfiguration config, String field)
      throws ValidationException {
    throw new ValidationException("No integer fields!");
  }

  /**
   * Validates a field with fieldType Boolean.
   *
   * @param config Configuration file that field is supposed to be in.
   * @param field  Field to be evaluated.
   * @throws ValidationException If field is missing, has wrong fieldType, or extra placeholders.
   */
  default void validateBooleanField(FileConfiguration config, String field)
      throws ValidationException {
    throw new ValidationException("No boolean fields!");
  }

  /**
   * Validates a field with fieldType StringList.
   *
   * @param config Configuration file that field is supposed to be in.
   * @param field  Field to be evaluated.
   * @throws ValidationException If field is missing, has wrong fieldType, or extra placeholders.
   */
  default void validateStringListField(FileConfiguration config, String field)
      throws ValidationException {
    throw new ValidationException("No stringlist fields!");
  }
}
