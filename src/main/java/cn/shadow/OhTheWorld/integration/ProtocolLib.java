package cn.shadow.OhTheWorld.integration;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.MultiVersion;

public class ProtocolLib {
	public static void init() {
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
			new Impl();
		}
	}
	
	public static void disable() {
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
			ProtocolLibrary.getProtocolManager().removePacketListeners(Main.getInstance());
		}
	}
	
	private static class Impl {
		public Impl() {
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			protocolManager.addPacketListener(
					new PacketAdapter(Main.getInstance(), PacketType.Play.Server.RESPAWN, PacketType.Play.Server.LOGIN ) {
					    @Override
					    public void onPacketSending(PacketEvent event) {
					    	Player player = event.getPlayer();
					    	if(player == null) return;
					    	World w = player.getWorld();
					    	Environment env = w.getEnvironment();
					    	
					    	if(env != Environment.NORMAL) return;
					    	SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(w.getName());
					    	
					    	if(swc == null || !swc.getBlueSky()) return;
					    	
					    	PacketType type = event.getPacketType();
					    	if(MultiVersion.getInstance().hasNetherUpdate()) {
					    		if(type == PacketType.Play.Server.RESPAWN) {
					    			event.getPacket().getBooleans().write(1, true);
					    		}
					    		if(type == PacketType.Play.Server.LOGIN) {
					    			event.getPacket().getBooleans().write(4, true);
					    		}
					    	} else {
					    		if(type == WrapperPlayServerRespawn.TYPE) {
					    			WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(event.getPacket());
					    			wrapperPlayServerRespawn.setLevelType(WorldType.FLAT);
					    		}
					    		if(type == WrapperPlayServerLogin.TYPE) {
					    			WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(event.getPacket());
					    			wrapperPlayServerLogin.setLevelType(WorldType.FLAT);
					    		}
					    	}
					    }
					});
		}
	}
}
