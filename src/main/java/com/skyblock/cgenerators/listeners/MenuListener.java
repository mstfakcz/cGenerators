package com.skyblock.cgenerators.listeners;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.gui.GeneratorMenu;
import com.skyblock.cgenerators.gui.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class MenuListener implements Listener {
    
    private final CGenerators plugin;
    
    public MenuListener(CGenerators plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Inventory clickedInventory = event.getInventory();
        InventoryHolder holder = clickedInventory.getHolder();
        
        // Yöntem 1: InventoryHolder kontrolü (en güvenli)
        if (holder instanceof MenuHolder) {
            // BU BİZİM MENÜMÜZ!
            event.setCancelled(true);
            
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            
            // Menü dışına (oyuncu envanterine) tıklama
            if (slot < 0 || slot >= event.getInventory().getSize()) {
                return;
            }
            
            // Debug
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().info("Menu click detected via Holder - Slot: " + slot);
            }
            
            // GeneratorMenu'ye tıklamayı ilet
            GeneratorMenu menu = new GeneratorMenu(plugin, player);
            menu.handleClick(slot);
            return;
        }
        
        // Yöntem 2: Başlık kontrolü (fallback)
        InventoryView view = event.getView();
        String clickedTitle = view.getTitle();
        String expectedTitle = plugin.getGUIManager().getMenuTitle();
        
        if (clickedTitle.equals(expectedTitle)) {
            // BU DA BİZİM MENÜMÜZ!
            event.setCancelled(true);
            
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            
            if (slot < 0 || slot >= event.getInventory().getSize()) {
                return;
            }
            
            // Debug
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().info("Menu click detected via Title - Slot: " + slot);
            }
            
            GeneratorMenu menu = new GeneratorMenu(plugin, player);
            menu.handleClick(slot);
        }
    }
}
