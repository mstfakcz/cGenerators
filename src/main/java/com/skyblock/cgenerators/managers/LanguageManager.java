package com.skyblock.cgenerators.managers;

import com.skyblock.cgenerators.CGenerators;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LanguageManager {
    
    private final CGenerators plugin;
    private FileConfiguration languageConfig;
    private String currentLanguage;
    
    public LanguageManager(CGenerators plugin) {
        this.plugin = plugin;
        loadLanguage();
    }
    
    public void loadLanguage() {
        currentLanguage = plugin.getConfig().getString("settings.language", "tr");
        
        File languageFile = new File(plugin.getDataFolder(), "locale/" + currentLanguage + ".yml");
        
        // Eğer dil dosyası yoksa, default'tan oluştur
        if (!languageFile.exists()) {
            plugin.saveResource("locale/" + currentLanguage + ".yml", false);
        }
        
        try {
            languageConfig = YamlConfiguration.loadConfiguration(languageFile);
            
            // Default değerleri yükle
            InputStream defConfigStream = plugin.getResource("locale/" + currentLanguage + ".yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
                languageConfig.setDefaults(defConfig);
            }
            
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().info("Dil dosyası yüklendi: " + currentLanguage);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Dil dosyası yüklenemedi: " + currentLanguage);
            e.printStackTrace();
            
            // Fallback olarak tr.yml kullan
            if (!currentLanguage.equals("tr")) {
                currentLanguage = "tr";
                loadLanguage();
            }
        }
    }
    
    public String getMessage(String path) {
        if (languageConfig == null) {
            return "Message not found: " + path;
        }
        
        String message = languageConfig.getString(path);
        if (message == null) {
            message = path;
        }
        
        return colorize(message);
    }
    
    public String getMessage(String path, String... replacements) {
        String message = getMessage(path);
        
        for (int i = 0; i < replacements.length - 1; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }
        
        return message;
    }
    
    public String getPrefix() {
        return getMessage("prefix");
    }
    
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public void reload() {
        loadLanguage();
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
