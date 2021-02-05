package cn.shadow.OhTheWorld;

import org.bukkit.plugin.java.JavaPlugin;

import cn.shadow.OhTheWorld.command.CommandManager;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.integration.PlaceHolderAPI;
import cn.shadow.OhTheWorld.integration.ProtocolLib;
import cn.shadow.OhTheWorld.integration.Vault;
import cn.shadow.OhTheWorld.listener.EntityListener;
import cn.shadow.OhTheWorld.listener.PlayerEventListener;
import cn.shadow.OhTheWorld.listener.PortalEventListener;
import cn.shadow.OhTheWorld.listener.WeatherListener;
import cn.shadow.OhTheWorld.listener.WorldEventListener;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.world.WorldManager;

public class Main extends JavaPlugin {
	private static Main instance;
	private WorldEventListener worldEventListener;
	
	public static Main getInstance() { return instance; }
	
	public Main() {
		instance = this;
	}
	
	public WorldEventListener getWorldEventListener() {
		return this.worldEventListener;
	}
	
    @Override
    public void onEnable() {
    	if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
    	if(!I18n.init()) return;
    	if(!WorldManager.init()) return;
    	if(!WorldConfigs.init()) return;
    	worldEventListener = new WorldEventListener();
    	
    	getServer().getPluginManager().registerEvents(worldEventListener, this);
    	getServer().getPluginManager().registerEvents(new WeatherListener(), this);
    	getServer().getPluginManager().registerEvents(new EntityListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
    	getServer().getPluginManager().registerEvents(new PortalEventListener(), this);
    	
    	CommandManager.registerCommands(this);
    	
    	WorldManager.loadAllWorld(null);
    	
    	PlaceHolderAPI.init();
    	ProtocolLib.init();
    	Vault.setupEconomy();
    }
    
    @Override
    public void onDisable() {
    	ProtocolLib.disable();
    	this.getServer().getScheduler().cancelTasks(this);
    }
}
