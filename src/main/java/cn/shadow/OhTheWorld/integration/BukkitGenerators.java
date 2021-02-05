package cn.shadow.OhTheWorld.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.LogUtil;

public class BukkitGenerators {
    public static ChunkGenerator getChunkGenerator(String generator, final String generatorID, final String worldName) {
        if (generator == null) {
            return null;
        }

        final Plugin plugin = Main.getInstance().getServer().getPluginManager().getPlugin(generator);
        if (plugin == null) {
            return null;
        } else {
        	try {
        		return plugin.getDefaultWorldGenerator(worldName, generatorID);
        	} catch (Exception ex) {
        		String msg = String.format(I18n.getInstance().FailToGetGenerator, plugin.getName());
        		LogUtil.getLogger().log(Level.SEVERE, msg);
        		ex.printStackTrace();
        		return null;
        	}
        }
    }
    
    public static List<String> getAllGenerators() {
    	List<String> generators = new ArrayList<>();
    	Plugin[] plugins = Main.getInstance().getServer().getPluginManager().getPlugins();
		for (Plugin p : plugins) {
            if (p.isEnabled() && p.getDefaultWorldGenerator("world", "") != null) {
                generators.add(p.getDescription().getName());
            }
        }
		return generators;
    }
}
