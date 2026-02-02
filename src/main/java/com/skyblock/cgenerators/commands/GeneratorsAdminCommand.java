package com.skyblock.cgenerators.commands;

import com.skyblock.cgenerators.CGenerators;
import com.skyblock.cgenerators.managers.LanguageManager;
import com.skyblock.cgenerators.models.Tier;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeneratorsAdminCommand implements CommandExecutor {
    
    private final CGenerators plugin;
    private final LanguageManager lang;
    
    public GeneratorsAdminCommand(CGenerators plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cgenerators.admin")) {
            sender.sendMessage(lang.getPrefix() + lang.getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
                
            case "settier":
                if (args.length < 3) {
                    sender.sendMessage(lang.getPrefix() + "&cKullanım: /cgenadmin settier <oyuncu> <tier>");
                    return true;
                }
                handleSetTier(sender, args[1], args[2]);
                break;
                
            case "info":
                if (args.length < 2) {
                    sender.sendMessage(lang.getPrefix() + "&cKullanım: /cgenadmin info <oyuncu>");
                    return true;
                }
                handleInfo(sender, args[1]);
                break;
                
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void handleReload(CommandSender sender) {
        plugin.reloadPlugin();
        sender.sendMessage(lang.getPrefix() + lang.getMessage("reload-success"));
    }
    
    private void handleSetTier(CommandSender sender, String playerName, String tierStr) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(lang.getPrefix() + lang.getMessage("admin.player-not-found"));
            return;
        }
        
        try {
            int tier = Integer.parseInt(tierStr);
            Tier targetTier = plugin.getTierManager().getTier(tier);
            
            if (targetTier == null) {
                sender.sendMessage(lang.getPrefix() + lang.getMessage("admin.invalid-tier"));
                return;
            }
            
            // Tüm tier permission'larını kaldır
            for (int i = 1; i <= plugin.getTierManager().getMaxTier(); i++) {
                Tier t = plugin.getTierManager().getTier(i);
                if (t != null) {
                    if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
                        plugin.getServer().dispatchCommand(
                            plugin.getServer().getConsoleSender(),
                            "lp user " + target.getName() + " permission unset " + t.getPermission()
                        );
                    }
                }
            }
            
            // Yeni tier'ı ver
            if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
                plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    "lp user " + target.getName() + " permission set " + targetTier.getPermission()
                );
            } else {
                target.addAttachment(plugin, targetTier.getPermission(), true);
            }
            
            sender.sendMessage(lang.getPrefix() + 
                lang.getMessage("admin.tier-set", "{player}", target.getName(), "{tier}", String.valueOf(tier)));
            target.sendMessage(lang.getPrefix() + 
                lang.getMessage("admin.tier-set-notify", "{tier}", String.valueOf(tier)));
            
        } catch (NumberFormatException e) {
            sender.sendMessage(lang.getPrefix() + lang.getMessage("admin.invalid-number"));
        }
    }
    
    private void handleInfo(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(lang.getPrefix() + lang.getMessage("admin.player-not-found"));
            return;
        }
        
        int currentTier = plugin.getTierManager().getPlayerTier(target);
        Tier currentTierObj = plugin.getTierManager().getPlayerCurrentTier(target);
        String balance = plugin.getEconomyManager().format(plugin.getEconomyManager().getBalance(target));
        
        sender.sendMessage(lang.getMessage("admin.info.header"));
        sender.sendMessage(lang.getMessage("admin.info.player", "{player}", target.getName()));
        sender.sendMessage(lang.getMessage("admin.info.tier", "{tier}", String.valueOf(currentTier)));
        if (currentTierObj != null) {
            sender.sendMessage(lang.getMessage("admin.info.tier-name", "{tier_name}", currentTierObj.getName()));
        }
        sender.sendMessage(lang.getMessage("admin.info.balance", "{balance}", balance));
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(lang.getMessage("admin.help.header"));
        sender.sendMessage(lang.getMessage("admin.help.reload"));
        sender.sendMessage(lang.getMessage("admin.help.settier"));
        sender.sendMessage(lang.getMessage("admin.help.info"));
    }
}
