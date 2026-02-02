package com.skyblock.cgenerators;

import com.skyblock.cgenerators.commands.GeneratorsCommand;
import com.skyblock.cgenerators.commands.GeneratorsAdminCommand;
import com.skyblock.cgenerators.listeners.CobbleGenerateListener;
import com.skyblock.cgenerators.listeners.MenuListener;
import com.skyblock.cgenerators.managers.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public class CGenerators extends JavaPlugin {
    
    private static CGenerators instance;
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private GUIManager guiManager;
    private EconomyManager economyManager;
    private TierManager tierManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Config dosyalarını oluştur
        saveDefaultConfig();
        createDefaultFiles();
        
        // Manager'ları başlat
        configManager = new ConfigManager(this);
        languageManager = new LanguageManager(this);
        guiManager = new GUIManager(this);
        
        // Economy'yi başlat
        economyManager = new EconomyManager(this);
        if (!economyManager.setupEconomy()) {
            getLogger().severe(languageManager.getMessage("errors.vault-not-found"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Tier Manager'ı başlat
        tierManager = new TierManager(this);
        tierManager.loadTiers();
        
        // Komutları kaydet
        getCommand("cgenerators").setExecutor(new GeneratorsCommand(this));
        getCommand("cgeneratorsadmin").setExecutor(new GeneratorsAdminCommand(this));
        
        // Custom command'i kaydet
        registerCustomCommand();
        
        // Event listener'ları kaydet
        getServer().getPluginManager().registerEvents(new CobbleGenerateListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        
        getLogger().info("cGenerators v" + getDescription().getVersion() + " başarıyla yüklendi!");
        getLogger().info("Dil: " + languageManager.getCurrentLanguage());
    }
    
    @Override
    public void onDisable() {
        getLogger().info("cGenerators kapatılıyor...");
    }
    
    private void createDefaultFiles() {
        // locale klasörünü oluştur
        File localeDir = new File(getDataFolder(), "locale");
        if (!localeDir.exists()) {
            localeDir.mkdirs();
        }
        
        // Varsayılan dil dosyalarını oluştur
        saveResource("locale/tr.yml", false);
        saveResource("locale/en.yml", false);
        
        // GUI config'i oluştur
        File guiFile = new File(getDataFolder(), "gui.yml");
        if (!guiFile.exists()) {
            saveResource("gui.yml", false);
        }
    }
    
    private void registerCustomCommand() {
        String customCommand = configManager.getCustomCommand();
        
        // Eğer custom command boş değilse
        if (customCommand != null && !customCommand.trim().isEmpty()) {
            // '/' karakterini kaldır
            String commandName = customCommand.replace("/", "").trim();
            
            if (!commandName.isEmpty()) {
                try {
                    // CommandMap'i reflection ile al
                    CommandMap commandMap = getCommandMap();
                    
                    if (commandMap != null) {
                        // Özel Command sınıfı oluştur
                        Command generatorCmd = new Command(commandName) {
                            @Override
                            public boolean execute(CommandSender sender, String label, String[] args) {
                                if (!(sender instanceof Player)) {
                                    sender.sendMessage(languageManager.getMessage("player-only"));
                                    return true;
                                }
                                
                                Player player = (Player) sender;
                                
                                if (!player.hasPermission("cgenerators.use")) {
                                    player.sendMessage(languageManager.getPrefix() + 
                                        languageManager.getMessage("no-permission"));
                                    return true;
                                }
                                
                                // Menüyü aç
                                com.skyblock.cgenerators.gui.GeneratorMenu menu = 
                                    new com.skyblock.cgenerators.gui.GeneratorMenu(CGenerators.this, player);
                                menu.open();
                                
                                return true;
                            }
                        };
                        
                        generatorCmd.setDescription("Jeneratör menüsünü açar");
                        generatorCmd.setPermission("cgenerators.use");
                        
                        // Komutu kaydet
                        commandMap.register("cgenerators", generatorCmd);
                        
                        getLogger().info("Özel komut kaydedildi: /" + commandName);
                        
                        if (configManager.isDebug()) {
                            getLogger().info("DEBUG: Custom command '" + commandName + "' başarıyla register edildi");
                        }
                    }
                    
                } catch (Exception e) {
                    getLogger().warning("Özel komut kaydedilemedi: " + commandName);
                    if (configManager.isDebug()) {
                        getLogger().warning("Hata detayı: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * Reflection kullanarak CommandMap'i alır
     * Bukkit API'de getCommandMap() public değil, reflection gerekli
     */
    private CommandMap getCommandMap() {
        try {
            // Server nesnesinden commandMap field'ını al
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe("CommandMap alınamadı! Özel komut kaydedilemeyecek.");
            if (configManager.isDebug()) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    public void reloadPlugin() {
        reloadConfig();
        configManager = new ConfigManager(this);
        languageManager.reload();
        guiManager.reload();
        tierManager.loadTiers();
        
        // Custom command'i yeniden kaydet
        registerCustomCommand();
        
        getLogger().info(languageManager.getMessage("reload-success"));
    }
    
    // Getters
    public static CGenerators getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
    public GUIManager getGUIManager() {
        return guiManager;
    }
    
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
    
    public TierManager getTierManager() {
        return tierManager;
    }
}
