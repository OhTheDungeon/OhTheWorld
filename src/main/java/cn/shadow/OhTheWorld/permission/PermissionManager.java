package cn.shadow.OhTheWorld.permission;

import org.bukkit.World;
import org.bukkit.entity.Player;

import cn.shadow.OhTheWorld.world.WorldManager;

public class PermissionManager {
	public final static String PLAYER_WORLD_PERMISSION_PREFIX = "ohtheworld.world";
	
	public static String getPermission(World w) {
		return PLAYER_WORLD_PERMISSION_PREFIX + "." + w.getName();
	}
	
	public static boolean hasWorldPermission(Player p, World w) {
		return hasWorldPermission(p, w, true);
	}
	
	public static boolean hasWorldPermission(Player p, World w, boolean ignoreManage) {
		if(ignoreManage && hasManagePermission(p)) return true; 
		if(w.getName().equals(WorldManager.getDefaultWorld().getName())) return true;
		String all = PLAYER_WORLD_PERMISSION_PREFIX + ".*";
		String world = getPermission(w);
		return p.hasPermission(all) || p.hasPermission(world);
	}
	
	public static boolean hasManagePermission(Player p) {
		return p.hasPermission("ohtheworld.manage");
	}
	
	public static boolean hasTeleportPermission(Player p) {
		return p.hasPermission("ohtheworld.teleport");
	}
}
