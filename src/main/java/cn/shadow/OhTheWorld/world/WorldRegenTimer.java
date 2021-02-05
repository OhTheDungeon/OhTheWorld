package cn.shadow.OhTheWorld.world;

import org.bukkit.Bukkit;
import org.bukkit.World;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;

public class WorldRegenTimer {
	public static void startWorldRegenTimer() {
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()) {
					SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(world.getName());
					if(swc == null) continue;
					long live = swc.getCurrentLiveHours() + 1;
					if(live >= swc.getRegenOnEveryXHours()) {
						live = 0;
						WorldManager.regenerateWorld(null, world.getName());
					}
					swc.setCurrentLiveHours(live);
					WorldConfigs.safeSave();
				}
			}
		}, 1L, 20 * 3600);
	}
}
