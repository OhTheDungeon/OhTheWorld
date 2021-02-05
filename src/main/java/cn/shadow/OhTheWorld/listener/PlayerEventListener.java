package cn.shadow.OhTheWorld.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.integration.Vault;
import cn.shadow.OhTheWorld.permission.PermissionManager;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.world.WorldManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerEventListener implements Listener {
	
	private static void sendPlayerToDefaultWorld(Player p) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				p.teleport(WorldManager.getDefaultWorld().getSpawnLocation());
			}
		}, 1L);
	}
	
	private static void setGameMode(Player p, World w) {
		if(PermissionManager.hasManagePermission(p)) return;
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(w.getName());
		if(swc == null) return;
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				p.setGameMode(swc.getGameMode());
			}
		}, 1L);
	}
	
	
	@EventHandler
    public void playerRespawn(PlayerRespawnEvent event) {
		World world = event.getPlayer().getWorld();
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(world.getName());
		if(swc == null) return;
		
		String rw = swc.getRespawnWorldName();
		if(rw.isEmpty()) return;
		World respawn = Bukkit.getWorld(rw);
		if(respawn == null) return;
		
		event.setRespawnLocation(respawn.getSpawnLocation());
	}
	
	@EventHandler
    public void playerJoin(PlayerJoinEvent event) {
		World w = event.getPlayer().getWorld();
		Player p = event.getPlayer();
		if(!PermissionManager.hasWorldPermission(p, w)) {
			String msg = String.format(I18n.getInstance().NoPermissionInWorld, w.getName());
			p.sendMessage(ChatColor.RED + msg);
			sendPlayerToDefaultWorld(p);
		}
		
		setGameMode(p, w);
	}
	
    @EventHandler
    public void playerChangedWorld(PlayerChangedWorldEvent event) {
		World w = event.getPlayer().getWorld();
		Player p = event.getPlayer();
    	setGameMode(p, w);
    }
    
    private static boolean playerEnterFee(Player p, World w) {
    	SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(w.getName());
    	if(swc == null) return true;
    	
    	return Vault.enterFee(p, swc.getEnterFee());
    }
    
    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
    	if(event.isCancelled()) return;
    	Player p = event.getPlayer();
    	World w = event.getTo().getWorld();
    	
    	if(!playerEnterFee(p, w)) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	if(!PermissionManager.hasWorldPermission(p, w)) {
    		String msg = String.format(I18n.getInstance().NoPermissionInWorld, w.getName());
			p.sendMessage(ChatColor.RED + msg);
			event.setCancelled(true);
			return;
    	}
    	
    	setGameMode(p, w);
    }
    
    @EventHandler
    public void foodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(p.getWorld().getName());
            if(swc == null) return;
            
            if (event.getFoodLevel() < p.getFoodLevel()) {
            	if(!swc.getHunger()) event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void entityRegainHealth(EntityRegainHealthEvent event) {
        if (event.isCancelled()) {
            return;
        }
        RegainReason reason = event.getRegainReason();
        if (event.getEntity() instanceof Player) {
        	Player p = (Player) event.getEntity();
        	SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(p.getWorld().getName());
        	if(swc == null) return;
        	
        	if(!swc.getAutoHeal() && reason == RegainReason.REGEN) {
        		event.setCancelled(true);
        	}
        }
    }
}
