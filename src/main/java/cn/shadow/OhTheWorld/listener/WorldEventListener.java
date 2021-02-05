package cn.shadow.OhTheWorld.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.integration.Forge112;
import cn.shadow.OhTheWorld.world.WorldManager;

public class WorldEventListener implements Listener {
	private Set<String> reservedWorld = new HashSet<>();
	
	public void createReservation(World world) {
		reservedWorld.add(world.getName());
	}
	
	public void createReservation(String world) {
		reservedWorld.add(world);
	}
	
	public void removeReservation(World world) {
		reservedWorld.remove(world.getName());
	}
	
	public void removeReservation(String world) {
		reservedWorld.remove(world);
	}
	
	@EventHandler
    public void onWorldInit(WorldInitEvent e){
		World world = e.getWorld();
		if(reservedWorld.contains(e.getWorld().getName())) {
			world.setKeepSpawnInMemory(false);
		}
		
		if(WorldConfigs.getInstance().worlds.containsKey(world.getName())) {
			SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(world.getName());
			world.setKeepSpawnInMemory(swc.getKeepSpawnInMemory());
			
			if(Forge112.isForge112()) {
				swc.setLockDimId(Forge112.getDimIdFromBukkitWorld(world));
				WorldConfigs.safeSave();
			}
		}
	}
	
	@EventHandler
    public void onWorldLoad(WorldLoadEvent e){
		World world = e.getWorld();
		if(reservedWorld.contains(e.getWorld().getName())) {
			world.setKeepSpawnInMemory(false);
		}
		
		if(WorldConfigs.getInstance().worlds.containsKey(world.getName())) {
			SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(world.getName());
			WorldManager.updateWorld(world, swc);
		}
	}
}
