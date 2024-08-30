# Captchas v1.0.1e
Complete rewriting of old captchas plugin with bunch of QoL features for server admins.
Honestly very overengineered but I think this code could be reused.

## Setup
Place plugin into plugin folder and restart server. All players have to be logged after server restarts for plugin to work properly.
Default config files will be parsed and validated.

## Configuration
All configuration files are validated in code on plugin startup, errors will be displayed in console and server logs.
For Items.yml and Players.yml, any erroneous entries will be automatically removed by the plugin and saved on server stop.
