package cn.shadow.OhTheWorld.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.LogUtil;

public class WorldConfigs {
	private static WorldConfigs instance;
	public Map<String, SingleWorldConfig> worlds = new HashMap<>();
	public Map<String, List<SingleWorldConfig>> inactives = new HashMap<>();
	
	public static WorldConfigs getInstance() { return instance; }
	
	public static File getConfigFile() {
		JavaPlugin plugin = Main.getInstance();
		// make sure file exists
        File configDir = plugin.getDataFolder();
		if(!configDir.exists()){
			configDir.mkdir();
		}
		File config_file = new File(configDir.toString() + File.separator + "config.json");
		
		return config_file;
	}
	
	private static void load() throws IOException {
		File config_file = getConfigFile();
		
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(config_file), "UTF8"));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        reader.close();
        instance = (new Gson()).fromJson(sb.toString(), WorldConfigs.class);
	}
	
	public static void safeSave() {
		try {
			save();
		} catch (IOException ex) {
			LogUtil.getLogger().log(Level.SEVERE, I18n.getInstance().FailToSave);
			ex.printStackTrace();
		}
	}
	
    public static void save() throws IOException {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	String json = gson.toJson(instance);
        File file = getConfigFile();
        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
        oStreamWriter.append(json);
        oStreamWriter.close();
    }

	public static boolean init() {
		File config_file = getConfigFile();
		try {
			if(!config_file.exists()) {
				config_file.createNewFile();
				instance = new WorldConfigs();
				save();
			}
			load();
			if(updateConfig()) save();
			return true;
		} catch (IOException ex) {
			LogUtil.getLogger().log(Level.SEVERE, I18n.getInstance().FailToInitConfig);
			ex.printStackTrace();
			return false;
		}
	}

	private static boolean updateConfig() {
		//check config version and update
		return false;
	}
}
