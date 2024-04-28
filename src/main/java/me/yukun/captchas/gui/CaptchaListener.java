package me.yukun.captchas.gui;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class CaptchaListener implements Listener {

  private static final Map<Inventory, Integer> inventorySlotMap = new HashMap<>();
  private static final Map<Inventory, Captcha> inventoryCaptchaMap = new HashMap<>();
  private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("Captchas");

  protected static void startCaptcha(Captcha captcha, Inventory inventory, int slot) {
    inventorySlotMap.put(inventory, slot);
    inventoryCaptchaMap.put(inventory, captcha);
  }

  protected static void endCaptcha(Inventory inventory) {
    inventorySlotMap.remove(inventory);
  }

  @EventHandler
  private void playerLeaveEvent(PlayerQuitEvent e) {
    Inventory inventory = e.getPlayer().getOpenInventory().getTopInventory();
    if (inventorySlotMap.containsKey(inventory)) {
      inventoryCaptchaMap.get(inventory).clickWrong(true);
    }
  }

  @EventHandler
  private void captchaCloseEvent(InventoryCloseEvent e) {
    if (inventorySlotMap.containsKey(e.getInventory())) {
      assert plugin != null;
      Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
          () -> e.getPlayer().openInventory(e.getInventory()), 1);
    }
  }

  @EventHandler
  private void captchaClickEvent(InventoryClickEvent e) {
    if (inventorySlotMap.containsKey(e.getClickedInventory())) {
      e.setCancelled(true);
      if (e.getRawSlot() == inventorySlotMap.get(e.getClickedInventory())) {
        inventoryCaptchaMap.get(e.getClickedInventory()).clickRight();
      } else {
        inventoryCaptchaMap.get(e.getClickedInventory()).clickWrong(false);
      }
    }
  }

  @EventHandler
  private void captchaMoveItemEvent(InventoryMoveItemEvent e) {
    if (inventorySlotMap.containsKey(e.getSource())) {
      e.setCancelled(true);
    }
    if (inventorySlotMap.containsKey(e.getDestination())) {
      e.setCancelled(true);
    }
  }
}
