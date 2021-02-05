package cn.shadow.OhTheWorld.command.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.shadow.OhTheWorld.permission.PermissionManager;
import cn.shadow.OhTheWorld.utils.I18n;
import net.md_5.bungee.api.ChatColor;

public class CustomGeneratorText implements CommandExecutor {
	private static Map<UUID, String> dict = new ConcurrentHashMap<>();
	
	public static String getString(Player p) {
		return dict.get(p.getUniqueId());
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
        	sender.sendMessage(ChatColor.YELLOW + I18n.getInstance().PlayerOnlyCommand);
        	return true;
        }
    	Player p = (Player) sender;
    	if(!PermissionManager.hasManagePermission(p)) {
    		p.sendMessage(ChatColor.RED + I18n.getInstance().NoPermission);
    		return true;
    	}
    	dict.put(p.getUniqueId(), args[0]);
    	return true;
	}
}
