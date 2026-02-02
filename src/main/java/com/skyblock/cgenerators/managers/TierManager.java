package com.skyblock.cgenerators.managers;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.models.Tier;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class TierManager {
    
    private final CGenerators plugin;
    private final Map<Integer, Tier> tiers;
    
    public TierManager(CGenerators plugin) {
        this.plugin = plugin;
        this.tiers = new HashMap<>();
    }
    
    public void loadTiers() {
        tiers.clear();
        
        ConfigurationSection tiersSection = plugin.getConfig().getConfigurationSection("tiers");
        if (tiersSection == null) {
            plugin.getLogger().severe("Config'de 'tiers' bölümü bulunamadı!");
            return;
        }
        
        for (String key : tiersSection.getKeys(false)) {
            try {
                int level = Integer.parseInt(key);
                ConfigurationSection tierSection = tiersSection.getConfigurationSection(key);
                
                if (tierSection == null) continue;
                
                String name = tierSection.getString("name", "Tier " + level);
                String description = tierSection.getString("description", "");
                double cost = tierSection.getDouble("cost", 0);
                String permission = tierSection.getString("permission", "cgenerators.tier." + level);
                
                Map<Material, Integer> drops = new HashMap<>();
                ConfigurationSection dropsSection = tierSection.getConfigurationSection("drops");
                
                if (dropsSection != null) {
                    for (String materialName : dropsSection.getKeys(false)) {
                        try {
                            Material material = Material.valueOf(materialName);
                            int chance = dropsSection.getInt(materialName);
                            drops.put(material, chance);
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("Geçersiz materyal: " + materialName);
                        }
                    }
                }
                
                Tier tier = new Tier(level, name, description, cost, permission, drops);
                tiers.put(level, tier);
                
                if (plugin.getConfigManager().isDebug()) {
                    plugin.getLogger().info("Tier yüklendi: " + level + " - " + name);
                }
                
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Geçersiz tier seviyesi: " + key);
            }
        }
        
        plugin.getLogger().info(tiers.size() + " tier yüklendi!");
    }
    
    public Tier getTier(int level) {
        return tiers.get(level);
    }
    
    public int getMaxTier() {
        return tiers.keySet().stream().max(Integer::compareTo).orElse(5);
    }
    
    public int getPlayerTier(Player player) {
        for (int i = getMaxTier(); i >= 1; i--) {
            Tier tier = getTier(i);
            if (tier != null && player.hasPermission(tier.getPermission())) {
                return i;
            }
        }
        return 1;
    }
    
    public Tier getPlayerCurrentTier(Player player) {
        return getTier(getPlayerTier(player));
    }
    
    public Tier getNextTier(Player player) {
        int currentTier = getPlayerTier(player);
        int nextTierLevel = currentTier + 1;
        
        if (nextTierLevel > getMaxTier()) {
            return null;
        }
        
        return getTier(nextTierLevel);
    }
    
    public boolean hasNextTier(Player player) {
        return getNextTier(player) != null;
    }
    
    public boolean upgradeTier(Player player) {
        Tier nextTier = getNextTier(player);
        if (nextTier == null) {
            return false;
        }
        
        // Permission'ı ver
        String permission = nextTier.getPermission();
        
        // Eğer LuckPerms varsa
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            plugin.getServer().dispatchCommand(
                plugin.getServer().getConsoleSender(),
                "lp user " + player.getName() + " permission set " + permission
            );
        } else {
            // Vault permissions kullan
            player.addAttachment(plugin, permission, true);
        }
        
        // Önceki tier'ın permission'ını kaldır (opsiyonel)
        int currentTier = getPlayerTier(player);
        if (currentTier > 0) {
            Tier oldTier = getTier(currentTier);
            if (oldTier != null && plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
                plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    "lp user " + player.getName() + " permission unset " + oldTier.getPermission()
                );
            }
        }
        
        return true;
    }
    
    public Map<Integer, Tier> getAllTiers() {
        return new HashMap<>(tiers);
    }
}
