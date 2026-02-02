package com.skyblock.cgenerators.managers;

import com.skyblock.cgenerators.CGenerators;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    
    private final CGenerators plugin;
    private Economy economy;
    
    public EconomyManager(CGenerators plugin) {
        this.plugin = plugin;
    }
    
    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer()
                .getServicesManager().getRegistration(Economy.class);
        
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }
    
    public boolean hasEnoughMoney(Player player, double amount) {
        return economy.has(player, amount);
    }
    
    public boolean withdrawMoney(Player player, double amount) {
        if (!hasEnoughMoney(player, amount)) {
            return false;
        }
        
        economy.withdrawPlayer(player, amount);
        return true;
    }
    
    public double getBalance(Player player) {
        return economy.getBalance(player);
    }
    
    public String format(double amount) {
        return economy.format(amount);
    }
    
    public Economy getEconomy() {
        return economy;
    }
}
