package me.yukun.captchas.config.validator;

import me.yukun.captchas.config.ConfigTypeEnum;
import me.yukun.captchas.config.FieldTypeEnum;

public class ValidationException extends Exception {

  public ValidationException(String errorMessage) {
    super(errorMessage);
  }

  public static String getErrorMessage(ConfigTypeEnum configType, FieldTypeEnum fieldType,
      String item) {
    return "Error in " + configType + ": " + fieldType + " not found at " + item + ".";
  }
}
