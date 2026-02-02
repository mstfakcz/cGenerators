package com.skyblock.cgenerators.commands;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.gui.GeneratorMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeneratorsCommand implements CommandExecutor {
    
    private final CGenerators plugin;
    
    public GeneratorsCommand(CGenerators plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLanguageManager().getMessage("player-only"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("cgenerators.use")) {
            player.sendMessage(plugin.getLanguageManager().getPrefix() + 
                plugin.getLanguageManager().getMessage("no-permission"));
            return true;
        }
        
        // Menüyü aç
        GeneratorMenu menu = new GeneratorMenu(plugin, player);
        menu.open();
        
        return true;
    }
}
