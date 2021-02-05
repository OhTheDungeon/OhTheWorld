package cn.shadow.OhTheWorld.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.PortalType;

import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.MultiVersion;

//for 1.12.2
@SuppressWarnings("deprecation")
public class PortalEventListener implements Listener {
	//为了1.12
	@EventHandler
    public void entityPortalCreate(EntityCreatePortalEvent event) {
        if (event.isCancelled() || event.getBlocks().size() == 0) {
            return;
        }
        
        SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getEntity().getWorld().getName());
        if(swc == null) return;
        if(event.getPortalType() == PortalType.ENDER) {
        	if(!swc.getAllowEndPortal()) event.setCancelled(true);
        }
        else if(event.getPortalType() == PortalType.NETHER) {
        	if(!swc.getAllowNetherPortal()) event.setCancelled(true);
        }
	}
	
	@EventHandler
    public void portalForm(PortalCreateEvent event) {
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getEntity().getWorld().getName());
        if(swc == null) return;
        if(!swc.getAllowNetherPortal()) event.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled = true)
    public void portalForm(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() != MultiVersion.getInstance().getEndPortalFrame()) {
            return;
        }
        if (event.getItem() == null || event.getItem().getType() != MultiVersion.getInstance().getEnderEye()) {
            return;
        }
        SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getPlayer().getWorld().getName());
        if(swc == null) return;
        
        if(!swc.getAllowEndPortal()) event.setCancelled(true);
    }
}
