package com.skyblock.cgenerators.managers;

import com.skyblock.cgenerators.CGenerators;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private final CGenerators plugin;
    private final FileConfiguration config;
    
    public ConfigManager(CGenerators plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    
    public boolean isDebug() {
        return config.getBoolean("settings.debug", false);
    }
    
    public String getLanguage() {
        return config.getString("settings.language", "tr");
    }
    
    public String getCustomCommand() {
        return config.getString("settings.custom-command", "");
    }
    
    public int getPlaceholderUpdateInterval() {
        return config.getInt("settings.placeholder-update-interval", 100);
    }
    
    public boolean isEconomyEnabled() {
        return config.getBoolean("economy.enabled", true);
    }
    
    public String getCurrencySymbol() {
        return config.getString("economy.currency-symbol", "₺");
    }
    
    public boolean isSoundsEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }
    
    public String getSound(String type) {
        return config.getString("sounds." + type, "UI_BUTTON_CLICK");
    }
    
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
}
