package cn.shadow.OhTheWorld.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import io.papermc.lib.PaperLib;

public class ChunkGenWorker {
	
    public static abstract class RepeatingTask implements Runnable {

        private int taskId;

        public RepeatingTask(JavaPlugin plugin, int arg1, int arg2) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
        }

        public void cancel() {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }
	
	public static RepeatingTask initWorldSpawn(World world) {
		if(world == null) return null;

		return new RepeatingTask(Main.getInstance(), 1, 1) {
			int j = -196;
			int k = -196;
            @Override
            public void run() {
				if(k > 196) {
					k = -196;
					j += 16;
				}
				if(j > 196) {
					Location spawnLocation = world.getSpawnLocation();
					
					while (spawnLocation.getBlock().getType() != MultiVersion.getInstance().getAir()
							|| spawnLocation.getBlock().getRelative(BlockFace.UP).getType() != MultiVersion.getInstance().getAir()) {
		                spawnLocation.add(0, 1, 0);
		            }
					
					world.setSpawnLocation(spawnLocation);
					
					WorldManager.updateWorld(world);
					cancel();
				}
				
				Location spawn = world.getSpawnLocation();
				PaperLib.getChunkAtAsync(world, spawn.getBlockX() >> 4 + j >> 4, spawn.getBlockZ() >> 4 + k >> 4);
				
				k += 16;
            }
        };
	}
}
