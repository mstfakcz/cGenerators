package com.skyblock.cgenerators.models;

import org.bukkit.Material;
import java.util.Map;

public class Tier {
    
    private final int level;
    private final String name;
    private final String description;
    private final double cost;
    private final String permission;
    private final Map<Material, Integer> drops;
    
    public Tier(int level, String name, String description, double cost, 
                String permission, Map<Material, Integer> drops) {
        this.level = level;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.permission = permission;
        this.drops = drops;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getCost() {
        return cost;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public Map<Material, Integer> getDrops() {
        return drops;
    }
    
    public Material getRandomDrop() {
        int total = drops.values().stream().mapToInt(Integer::intValue).sum();
        int random = (int) (Math.random() * total);
        
        int current = 0;
        for (Map.Entry<Material, Integer> entry : drops.entrySet()) {
            current += entry.getValue();
            if (random < current) {
                return entry.getKey();
            }
        }
        
        return Material.COBBLESTONE;
    }
}
