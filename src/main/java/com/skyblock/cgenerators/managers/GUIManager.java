package com.skyblock.cgenerators.managers;

import com.skyblock.cgenerators.CGenerators;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class GUIManager {
    
    private final CGenerators plugin;
    private FileConfiguration guiConfig;
    
    public GUIManager(CGenerators plugin) {
        this.plugin = plugin;
        loadGUIConfig();
    }
    
    public void loadGUIConfig() {
        File guiFile = new File(plugin.getDataFolder(), "gui.yml");
        
        if (!guiFile.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("GUI config yüklendi!");
        }
    }
    
    public String getMenuTitle() {
        return colorize(guiConfig.getString("menu.title", "&6&lJeneratör Yükseltme"));
    }
    
    public int getMenuSize() {
        return guiConfig.getInt("menu.size", 27);
    }
    
    public boolean shouldFillEmptySlots() {
        return guiConfig.getBoolean("menu.fill-empty-slots", true);
    }
    
    public String getFillMaterial() {
        return guiConfig.getString("menu.fill-material", "GRAY_STAINED_GLASS_PANE");
    }
    
    public String getFillName() {
        return colorize(guiConfig.getString("menu.fill-name", " "));
    }
    
    public int getCurrentTierSlot() {
        return guiConfig.getInt("slots.current-tier", 13);
    }
    
    public int getUpgradeSlot() {
        return guiConfig.getInt("slots.upgrade", 22);
    }
    
    public int getInfoSlot() {
        return guiConfig.getInt("slots.info", 4);
    }
    
    public int getCloseSlot() {
        return guiConfig.getInt("slots.close", 26);
    }
    
    // Current Tier Item
    public String getCurrentTierMaterial() {
        return guiConfig.getString("current-tier-item.material", "COBBLESTONE");
    }
    
    public boolean isCurrentTierGlowing() {
        return guiConfig.getBoolean("current-tier-item.glowing", true);
    }
    
    public String getCurrentTierName() {
        return colorize(guiConfig.getString("current-tier-item.name", "{tier_name}"));
    }
    
    public List<String> getCurrentTierLore() {
        return guiConfig.getStringList("current-tier-item.lore");
    }
    
    // Next Tier Item
    public String getNextTierName() {
        return colorize(guiConfig.getString("next-tier-item.name", "{tier_name}"));
    }
    
    public List<String> getNextTierLore() {
        return guiConfig.getStringList("next-tier-item.lore");
    }
    
    // Upgrade Button
    public String getUpgradeMaterial() {
        return guiConfig.getString("upgrade-button.material", "EMERALD");
    }
    
    public String getUpgradeName() {
        return colorize(guiConfig.getString("upgrade-button.name", "&a&lYÜKSELT"));
    }
    
    public List<String> getUpgradeLore() {
        return guiConfig.getStringList("upgrade-button.lore");
    }
    
    // Max Tier Button
    public String getMaxTierMaterial() {
        return guiConfig.getString("max-tier-button.material", "BARRIER");
    }
    
    public String getMaxTierName() {
        return colorize(guiConfig.getString("max-tier-button.name", "&c&lMAKSİMUM SEVİYE"));
    }
    
    public List<String> getMaxTierLore() {
        return guiConfig.getStringList("max-tier-button.lore");
    }
    
    // Info Button
    public String getInfoMaterial() {
        return guiConfig.getString("info-button.material", "BOOK");
    }
    
    public String getInfoName() {
        return colorize(guiConfig.getString("info-button.name", "&e&lBilgi"));
    }
    
    public List<String> getInfoLore() {
        return guiConfig.getStringList("info-button.lore");
    }
    
    // Close Button
    public String getCloseMaterial() {
        return guiConfig.getString("close-button.material", "BARRIER");
    }
    
    public String getCloseName() {
        return colorize(guiConfig.getString("close-button.name", "&c&lKapat"));
    }
    
    public List<String> getCloseLore() {
        return guiConfig.getStringList("close-button.lore");
    }
    
    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public void reload() {
        loadGUIConfig();
    }
}
