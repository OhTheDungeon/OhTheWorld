package cn.shadow.OhTheWorld.integration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import cn.shadow.OhTheWorld.utils.I18n;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class Vault {
	private static Economy econ = null;
	
    public static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static Economy getEconomy() {
    	return econ;
    }
    
    public static boolean enterFee(Player p, double price) {
    	if(econ == null) return true;
    	double balance = econ.getBalance(p);
    	if(balance < price) {
    		p.sendMessage(ChatColor.RED + I18n.getInstance().NotEnoughMoney);
    		return false;
    	}
    	econ.withdrawPlayer(p, price);
    	return true;
    }
}
