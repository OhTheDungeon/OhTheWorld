package cn.shadow.OhTheWorld.integration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolderAPI {
	
	public static void init() {
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PapiImpl().register();
		}
	}
	
	private static class PapiImpl extends PlaceholderExpansion {
	    @Override
	    public boolean canRegister(){
	        return true;
	    }
	
		@Override
		public String getAuthor() {
			return "shadow";
		}
	
		@Override
		public String getIdentifier() {
			return "otw";
		}
	
		@Override
		public String getVersion() {
			return "1.0";
		}
		
	    @Override
	    public String onPlaceholderRequest(Player p, String identifier){
	        if(p == null){
	            return "";
	        }
	        
	        SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(p.getWorld().getName());
	
	        if(identifier.equals("worldname")){
	            return p.getWorld().getName();
	        }
	
	        if(identifier.equals("worldalias")){
	            if(swc == null) return "";
	            return swc.getAlias();
	        }
	
	        return null;
	    }
	}
}
