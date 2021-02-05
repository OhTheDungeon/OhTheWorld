package cn.shadow.OhTheWorld.world;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.integration.BukkitGenerators;
import cn.shadow.OhTheWorld.integration.Forge112;
import cn.shadow.OhTheWorld.utils.FileUtil;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.LogUtil;
import cn.shadow.OhTheWorld.utils.SeedUtil;
import io.papermc.lib.PaperLib;
import net.md_5.bungee.api.ChatColor;

public class WorldManager {
	
	public static List<String> unloaded;
	
	public static boolean init() {
		unloaded = new ArrayList<>();
		return true;
	}
	
	public static String[] getLore(String worldName, String... head) {
		List<String> lores = new ArrayList<>();
		if(head != null) {
			for(String str : head) {
				lores.add(str);
			}
		}
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(worldName);
		World world = Bukkit.getWorld(worldName);
		{
			if(swc != null && swc.getAlias() != null) {
				String msg = String.format(I18n.getInstance().LoreWorldAlias, swc.getAlias());
				lores.add(msg);
			}
		}
		{
			if(world != null && Forge112.isForge112()) {
				Integer id = Forge112.getDimIdFromBukkitWorld(world);
				if(id != null) {
					String msg = String.format(I18n.getInstance().LoreWorldDimId, id.toString());
					lores.add(msg);
				}
			}
		}
		{
			if(world != null) {
				String msg = String.format(I18n.getInstance().LoreWorldEnv, world.getEnvironment().toString());
				lores.add(msg);
			}
		}
		
		String[] res = new String[lores.size()];
		for(int i = 0; i < lores.size(); i++) {
			res[i] = lores.get(i);
		}
		return res;
	}
	
	public static void updateWorld(CommandSender sender, String worldName, SingleWorldConfig swc) {
		World world = Bukkit.getWorld(worldName);
		if(world != null) {
			world.setPVP(swc.getPvp());
			world.setDifficulty(swc.getDifficulty());
			world.setKeepSpawnInMemory(swc.getKeepSpawnInMemory());
		}
	}
	
	public static World getDefaultWorld() {
		return Bukkit.getWorlds().get(0);
	}
	
	public static boolean importWorld(CommandSender sender, String worldName, SingleWorldConfig worldConfig) {
		WorldCreator wc = new WorldCreator(worldName);
		wc.seed(SeedUtil.getSeed(worldConfig.getSeed()));
		wc.environment(worldConfig.getEnvironment());
		wc.generateStructures(worldConfig.getGenerateStructures());
		wc.generator(worldConfig.getGenerator().generator);
		
		Main.getInstance().getWorldEventListener().createReservation(worldName);
		wc.createWorld();
		Main.getInstance().getWorldEventListener().removeReservation(worldName);
		
		sender.sendMessage(ChatColor.BLUE + I18n.getInstance().WorldImportSuccess);
		sender.sendMessage(ChatColor.YELLOW + I18n.getInstance().TeleportTip);
		
		return true;
	}
	
	public static boolean createWorld(CommandSender sender, String worldName, SingleWorldConfig worldConfig) {
		sender.sendMessage(ChatColor.BLUE + I18n.getInstance().BeforeCreatingWorld);
		WorldCreator wc = new WorldCreator(worldName);
		wc.seed(SeedUtil.getSeed(worldConfig.getSeed()));
		wc.environment(worldConfig.getEnvironment());
		wc.generateStructures(worldConfig.getGenerateStructures());
		wc.generator(worldConfig.getGenerator().generator);
		
		Main.getInstance().getWorldEventListener().createReservation(worldName);
		wc.createWorld();
		
		if(Bukkit.getWorld(worldName) == null) {
			String msg = String.format(I18n.getInstance().FailToCreateWorld, worldName);
			sender.sendMessage(ChatColor.RED + msg);
			return false;
		}
		
		if(worldConfig.getGenerator().forgeGenerator != null && Forge112.isForge112()) {
			//forge
			File worldFolder = Bukkit.getWorld(worldName).getWorldFolder();
			Bukkit.unloadWorld(worldName, true);
			File region = new File(worldFolder, "region");
			FileUtil.deleteDirectory(region);
			File levelDat = new File(worldFolder, "level.dat");
			Forge112.modifyLevelDat(levelDat, worldConfig.getGenerator().forgeGenerator);
			wc.createWorld();
		}
		
		ChunkGenWorker.initWorldSpawn(Bukkit.getWorld(worldName));
		sender.sendMessage(ChatColor.BLUE + I18n.getInstance().WorldCreateSuccess);
		sender.sendMessage(ChatColor.YELLOW + I18n.getInstance().TeleportTip);
		
		Main.getInstance().getWorldEventListener().removeReservation(worldName);
		
		return true;
	}
	
	public static void loadAllWorld(CommandSender sender) {
		List<String> toRemove = new ArrayList<>();
		Server server = Bukkit.getServer();
		for(Map.Entry<String, SingleWorldConfig> entry : WorldConfigs.getInstance().worlds.entrySet()) {
			File file = new File(server.getWorldContainer(), entry.getKey());
			if(!file.exists()) {
				toRemove.add(entry.getKey());
				String msg = String.format(I18n.getInstance().InexistWorld, entry.getKey());
				if(sender != null) sender.sendMessage(ChatColor.YELLOW + msg);
				else LogUtil.getLogger().log(Level.WARNING, msg);
			} else {
				loadWorld(sender, entry.getKey(), entry.getValue());
			}
			
			if(Forge112.isForge112()) {
				World w = Bukkit.getWorld(entry.getKey());
				if(w != null) {
					Integer dimid = Forge112.getDimIdFromBukkitWorld(w);
					entry.getValue().setLockDimId(dimid);
					WorldConfigs.safeSave();
				}
			}
		}
	}
	
	public static World loadWorld(CommandSender sender, String worldName, SingleWorldConfig worldConfig) {
		if(Bukkit.getWorld(worldName) != null) {
			//检查Dim Locker
			if(worldConfig.getLockDimIdStatus()) {
				String msg = String.format(I18n.getInstance().FailToFixDimIdDueToWorldAlreadyLoaded, worldName);
				
				if(sender != null) sender.sendMessage(ChatColor.RED + msg);
				else LogUtil.getLogger().log(Level.WARNING, msg);
			}
		}
		
		if(worldConfig.getLockDimIdStatus() && worldConfig.getLockDimId() != null) 
			Forge112.prepareDim(worldConfig.getLockDimId());
		WorldCreator wc = new WorldCreator(worldName);
		
		if(worldConfig.getGenerator().isBukkit()) {
			List<String> genarray = new ArrayList<>(Arrays.asList(worldConfig.getGenerator().generator.split(":")));
			if(genarray.size() < 2) genarray.add("");
			ChunkGenerator generator = BukkitGenerators.getChunkGenerator(genarray.get(0), genarray.get(1), worldName);
			wc.generator(generator);
		} else {
			wc.generator((String) null);
		}
		wc.environment(worldConfig.getEnvironment());
		wc.seed(SeedUtil.getSeed(worldConfig.getSeed()));
		
		World world = wc.createWorld();
		
		updateWorld(world, worldConfig);
		
		return world;
	}
	
	public static void updateWorld(World world, SingleWorldConfig worldConfig) {
		world.setPVP(worldConfig.getPvp());
		world.setDifficulty(worldConfig.getDifficulty());
		world.setKeepSpawnInMemory(worldConfig.getKeepSpawnInMemory());
	}
	
	public static void updateWorld(World world) {
		SingleWorldConfig worldConfig = WorldConfigs.getInstance().worlds.get(world.getName());
		if(worldConfig == null) return;
		world.setPVP(worldConfig.getPvp());
		world.setDifficulty(worldConfig.getDifficulty());
		world.setKeepSpawnInMemory(worldConfig.getKeepSpawnInMemory());
	}
	
	public static void unloadWorld(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		if(w == null) return;
		List<Player> players = w.getPlayers();
		
		World dw = Bukkit.getWorlds().get(0);
		Location spawn = dw.getSpawnLocation();
		
		for(Player p : players) {
			PaperLib.teleportAsync(p, spawn);
		}
		
		Bukkit.unloadWorld(worldName, true);
		if(Bukkit.getWorld(worldName) == null) {
			unloaded.add(worldName);
			String msg = String.format(I18n.getInstance().UnloadedWorld, worldName);
			if(sender == null) LogUtil.getLogger().info(msg);
			else sender.sendMessage(ChatColor.BLUE + msg);
		}
	}
	
	public static void regenerateWorld(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		if(w == null) return;
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(worldName);
		if(swc == null) return;
		
		File folder = w.getWorldFolder();
		unloadWorld(sender, worldName);
		if(Bukkit.getWorld(worldName) == null) {
			if(!FileUtil.deleteDirectory(folder)) {
				String msg = String.format(I18n.getInstance().FailToDeleteWorld, worldName);
				if(sender != null) sender.sendMessage(ChatColor.RED + msg);
				else LogUtil.getLogger().log(Level.WARNING, msg);
			}
			swc.setSeed(SeedUtil.generateSeed(8));
			createWorld(sender, worldName, swc);
			WorldConfigs.safeSave();
			String msg = String.format(I18n.getInstance().WorldRegenSuccess, worldName);
			sender.sendMessage(ChatColor.BLUE + msg);
		} else {
			String msg = String.format(I18n.getInstance().FailToUnloadWorld, worldName);
			if(sender != null) sender.sendMessage(ChatColor.RED + msg);
			else LogUtil.getLogger().log(Level.WARNING, msg);
		}
	}
	
	public static void resetWorld(CommandSender sender, String worldName) {
		World w = Bukkit.getWorld(worldName);
		if(w == null) return;
		SingleWorldConfig swc = null;
		if(WorldConfigs.getInstance().worlds.containsKey(worldName))
			swc = WorldConfigs.getInstance().worlds.get(worldName);
		if(swc == null) return;
		
		File folder = w.getWorldFolder();
		File region = new File(folder, "region");
		unloadWorld(sender, worldName);
		if(Bukkit.getWorld(worldName) == null) {
			region.delete();
			loadWorld(sender, worldName, swc);
			String msg = String.format(I18n.getInstance().WorldResetSuccess, worldName);
			sender.sendMessage(ChatColor.BLUE + msg);
		} else {
			String msg = String.format(I18n.getInstance().FailToUnloadWorld, worldName);
			if(sender != null) sender.sendMessage(ChatColor.RED + msg);
			else LogUtil.getLogger().log(Level.WARNING, msg);
		}
	}
}
