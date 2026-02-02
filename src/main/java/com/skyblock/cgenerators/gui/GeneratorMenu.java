package com.skyblock.cgenerators.gui;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.managers.GUIManager;
import com.skyblock.cgenerators.managers.LanguageManager;
import com.skyblock.cgenerators.models.Tier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratorMenu {
    
    private final CGenerators plugin;
    private final Player player;
    private final GUIManager gui;
    private final LanguageManager lang;
    private Inventory inventory;
    
    public GeneratorMenu(CGenerators plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.gui = plugin.getGUIManager();
        this.lang = plugin.getLanguageManager();
        createInventory();
    }
    
    private void createInventory() {
        String title = gui.getMenuTitle();
        int size = gui.getMenuSize();
        
        // MenuHolder ile inventory oluştur (daha güvenli tanımlama)
        inventory = Bukkit.createInventory(new MenuHolder(), size, title);
        
        // Boş slotları doldur
        if (gui.shouldFillEmptySlots()) {
            fillEmptySlots();
        }
        
        // Itemleri ekle
        setCurrentTierItem();
        setUpgradeButton();
        setInfoButton();
        setCloseButton();
    }
    
    private void fillEmptySlots() {
        try {
            Material fillMaterial = Material.valueOf(gui.getFillMaterial());
            String fillName = gui.getFillName();
            
            ItemStack fillItem = new ItemStack(fillMaterial);
            ItemMeta meta = fillItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(fillName);
                fillItem.setItemMeta(meta);
            }
            
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, fillItem);
            }
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz fill material: " + gui.getFillMaterial());
        }
    }
    
    private void setCurrentTierItem() {
        int slot = gui.getCurrentTierSlot();
        Tier currentTier = plugin.getTierManager().getPlayerCurrentTier(player);
        
        if (currentTier == null) return;
        
        try {
            Material material = Material.valueOf(gui.getCurrentTierMaterial());
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                // İsim
                String name = gui.getCurrentTierName()
                    .replace("{tier_name}", currentTier.getName())
                    .replace("{tier}", String.valueOf(currentTier.getLevel()));
                meta.setDisplayName(plugin.getConfigManager().colorize(name));
                
                // Lore
                List<String> lore = new ArrayList<>();
                for (String line : gui.getCurrentTierLore()) {
                    if (line.contains("{tier_drops}")) {
                        // Drop oranlarını ekle
                        for (Map.Entry<Material, Integer> entry : currentTier.getDrops().entrySet()) {
                            String dropLine = "  &7" + formatMaterialName(entry.getKey()) + ": &e%" + entry.getValue();
                            lore.add(plugin.getConfigManager().colorize(dropLine));
                        }
                        continue;
                    }
                    
                    line = line.replace("{tier}", String.valueOf(currentTier.getLevel()))
                               .replace("{tier_name}", currentTier.getName())
                               .replace("{tier_description}", currentTier.getDescription());
                    lore.add(plugin.getConfigManager().colorize(line));
                }
                
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            
            inventory.setItem(slot, item);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz materyal: " + gui.getCurrentTierMaterial());
        }
    }
    
    private void setUpgradeButton() {
        int slot = gui.getUpgradeSlot();
        Tier nextTier = plugin.getTierManager().getNextTier(player);
        
        ItemStack item;
        ItemMeta meta;
        
        if (nextTier == null) {
            // Maksimum tier
            try {
                Material material = Material.valueOf(gui.getMaxTierMaterial());
                item = new ItemStack(material);
                meta = item.getItemMeta();
                
                if (meta != null) {
                    meta.setDisplayName(gui.getMaxTierName());
                    
                    List<String> lore = new ArrayList<>();
                    for (String line : gui.getMaxTierLore()) {
                        lore.add(plugin.getConfigManager().colorize(line));
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            } catch (IllegalArgumentException e) {
                item = new ItemStack(Material.BARRIER);
            }
        } else {
            // Yükseltme mümkün
            try {
                Material material = Material.valueOf(gui.getUpgradeMaterial());
                item = new ItemStack(material);
                meta = item.getItemMeta();
                
                if (meta != null) {
                    // İsim
                    String name = gui.getUpgradeName();
                    meta.setDisplayName(name);
                    
                    // Lore
                    List<String> lore = new ArrayList<>();
                    String currency = plugin.getConfigManager().getCurrencySymbol();
                    
                    for (String line : gui.getUpgradeLore()) {
                        line = line.replace("{next_tier}", String.valueOf(nextTier.getLevel()))
                                   .replace("{cost}", String.format("%.0f", nextTier.getCost()))
                                   .replace("{currency}", currency);
                        lore.add(plugin.getConfigManager().colorize(line));
                    }
                    
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            } catch (IllegalArgumentException e) {
                item = new ItemStack(Material.EMERALD);
            }
        }
        
        inventory.setItem(slot, item);
    }
    
    private void setInfoButton() {
        int slot = gui.getInfoSlot();
        
        try {
            Material material = Material.valueOf(gui.getInfoMaterial());
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(gui.getInfoName());
                
                List<String> lore = new ArrayList<>();
                for (String line : gui.getInfoLore()) {
                    lore.add(plugin.getConfigManager().colorize(line));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            
            inventory.setItem(slot, item);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz materyal: " + gui.getInfoMaterial());
        }
    }
    
    private void setCloseButton() {
        int slot = gui.getCloseSlot();
        
        try {
            Material material = Material.valueOf(gui.getCloseMaterial());
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(gui.getCloseName());
                
                List<String> lore = gui.getCloseLore();
                if (!lore.isEmpty()) {
                    List<String> coloredLore = new ArrayList<>();
                    for (String line : lore) {
                        coloredLore.add(plugin.getConfigManager().colorize(line));
                    }
                    meta.setLore(coloredLore);
                }
                item.setItemMeta(meta);
            }
            
            inventory.setItem(slot, item);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Geçersiz materyal: " + gui.getCloseMaterial());
        }
    }
    
    private String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
    
    public void open() {
        player.openInventory(inventory);
        playSound(player, plugin.getConfigManager().getSound("open-menu"));
    }
    
    public void handleClick(int slot) {
        int upgradeSlot = gui.getUpgradeSlot();
        int closeSlot = gui.getCloseSlot();
        
        if (slot == closeSlot) {
            player.closeInventory();
            playSound(player, plugin.getConfigManager().getSound("click"));
            return;
        }
        
        if (slot == upgradeSlot) {
            handleUpgrade();
        }
    }
    
    private void handleUpgrade() {
        Tier nextTier = plugin.getTierManager().getNextTier(player);
        
        if (nextTier == null) {
            player.sendMessage(lang.getPrefix() + lang.getMessage("max-tier-reached"));
            playSound(player, plugin.getConfigManager().getSound("upgrade-fail"));
            return;
        }
        
        double cost = nextTier.getCost();
        String currency = plugin.getConfigManager().getCurrencySymbol();
        
        if (!plugin.getEconomyManager().hasEnoughMoney(player, cost)) {
            player.sendMessage(lang.getPrefix() + 
                lang.getMessage("not-enough-money",
                    "{cost}", String.format("%.0f", cost),
                    "{currency}", currency));
            playSound(player, plugin.getConfigManager().getSound("upgrade-fail"));
            return;
        }
        
        // Parayı çek
        plugin.getEconomyManager().withdrawMoney(player, cost);
        
        // Tier'ı yükselt
        plugin.getTierManager().upgradeTier(player);
        
        player.sendMessage(lang.getPrefix() + 
            lang.getMessage("upgrade-success", "{tier}", String.valueOf(nextTier.getLevel())));
        
        playSound(player, plugin.getConfigManager().getSound("upgrade-success"));
        
        // Menüyü güncelle
        player.closeInventory();
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            new GeneratorMenu(plugin, player).open();
        }, 5L);
    }
    
    private void playSound(Player player, String soundName) {
        if (!plugin.getConfigManager().isSoundsEnabled()) return;
        
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().warning("Geçersiz ses: " + soundName);
            }
        }
    }
    
    public Inventory getInventory() {
        return inventory;
    }
}
