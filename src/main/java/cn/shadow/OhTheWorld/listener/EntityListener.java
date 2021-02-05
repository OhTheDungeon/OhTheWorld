package cn.shadow.OhTheWorld.listener;

import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;

public class EntityListener implements Listener {
	@EventHandler
    public void creatureSpawn(CreatureSpawnEvent event) {
		if(event.isCancelled()) return;
		SpawnReason reason = event.getSpawnReason();
        if (reason == SpawnReason.CUSTOM || reason == SpawnReason.SPAWNER_EGG
                || reason == SpawnReason.BREEDING) {
            return;
        }
        
        SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getEntity().getWorld().getName());
        if(swc == null) return;
                
        LivingEntity entity = event.getEntity();
        
        if(entity instanceof WaterMob) {
        	if(!swc.getCreatures().waterAnimals) event.setCancelled(true);
        } else if(entity instanceof Monster) {
        	if(!swc.getCreatures().monsters) event.setCancelled(true);
        } else if(entity instanceof Animals) {
        	if(!swc.getCreatures().normalAnimals) event.setCancelled(true);
        }
	}
}
