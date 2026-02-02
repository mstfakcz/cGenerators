package com.skyblock.cgenerators.listeners;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.models.Tier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class CobbleGenerateListener implements Listener {
    
    private final CGenerators plugin;
    
    public CobbleGenerateListener(CGenerators plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCobbleGenerate(BlockFormEvent event) {
        Block block = event.getBlock();
        
        // Sadece cobblestone oluşumunu kontrol et
        if (event.getNewState().getType() != Material.COBBLESTONE) {
            return;
        }
        
        // En yakın oyuncuyu bul
        Player nearestPlayer = getNearestPlayer(block.getLocation(), 10);
        
        if (nearestPlayer == null) {
            return;
        }
        
        // Oyuncunun tier'ını al
        Tier playerTier = plugin.getTierManager().getPlayerCurrentTier(nearestPlayer);
        
        if (playerTier == null) {
            return;
        }
        
        // Rastgele bir maden seç
        Material newMaterial = playerTier.getRandomDrop();
        
        // Bloğu değiştir
        event.setCancelled(true);
        
        // Bir tick sonra bloğu değiştir (async sorunlarını önlemek için)
        Bukkit.getScheduler().runTask(plugin, () -> {
            block.setType(newMaterial);
        });
        
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Oyuncu: " + nearestPlayer.getName() + 
                " | Tier: " + playerTier.getLevel() + 
                " | Üretilen: " + newMaterial.name());
        }
    }
    
    private Player getNearestPlayer(Location location, double radius) {
        Player nearest = null;
        double nearestDistance = radius;
        
        for (Player player : location.getWorld().getPlayers()) {
            double distance = player.getLocation().distance(location);
            
            if (distance < nearestDistance) {
                nearest = player;
                nearestDistance = distance;
            }
        }
        
        return nearest;
    }
    
    private boolean isNearLava(Block block) {
        BlockFace[] faces = {
            BlockFace.NORTH, BlockFace.SOUTH, 
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.UP, BlockFace.DOWN
        };
        
        for (BlockFace face : faces) {
            Block relative = block.getRelative(face);
            if (relative.getType() == Material.LAVA) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isNearWater(Block block) {
        BlockFace[] faces = {
            BlockFace.NORTH, BlockFace.SOUTH, 
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.UP, BlockFace.DOWN
        };
        
        for (BlockFace face : faces) {
            Block relative = block.getRelative(face);
            if (relative.getType() == Material.WATER) {
                return true;
            }
        }
        
        return false;
    }
}
