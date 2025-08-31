package me.yukun.captchas.config;

public enum ConfigTypeEnum {
  CONFIG("Config.yml"),
  MESSAGES("Messages.yml"),
  PLAYERS("Players.yml"),
  ITEMS("Items.yml");

  private final String filename;

  ConfigTypeEnum(String filename) {
    this.filename = filename;
  }

  @Override
  public String toString() {
    return this.filename;
  }
}
