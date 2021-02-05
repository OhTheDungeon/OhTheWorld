package cn.shadow.OhTheWorld.config.sub;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.utils.ItemStackUtil;
import cn.shadow.OhTheWorld.utils.MultiVersion;

public class SingleWorldConfig {
	private String alias = "";
	private boolean pvp = true;
	private boolean bedRespawn = true;
	private String gamemode = GameMode.SURVIVAL.toString();
	private boolean keepSpawnInMemory = false;
	private boolean allowNetherPortal = true;
	private boolean allowEndPortal = true;
	private boolean hunger = true;
	private boolean autoHeal = true;
	private boolean lockDimId = false;
	private Integer dimid = null;
	private String respawnWorldName = "";
	private String seed = "";
	private long regenOnEveryXHours = 0;
	private long currentLiveHours = 0;
	private double enterFee = 0;
	private String worldIcon = ItemStackUtil.itemStackToString(new ItemStack(MultiVersion.getInstance().getDirt()));
	private String env = Environment.NORMAL.toString();
	private String difficulty = Difficulty.NORMAL.toString();
	private boolean generateStructures = true;
	private boolean blueSky = false;
	
	private WorldCreatureConfig creatures = new WorldCreatureConfig();
	private WorldGeneratorConfig generator = new WorldGeneratorConfig();
	private WorldWeatherConfig weather = new WorldWeatherConfig();
	
	public SingleWorldConfig() {
		//TODO
	}
	
	public SingleWorldConfig(World world) {
		alias = world.getName();
		pvp = true;
		bedRespawn = true;
		gamemode = GameMode.SURVIVAL.toString();
		keepSpawnInMemory = world.getKeepSpawnInMemory();
		env = world.getEnvironment().toString();
		difficulty = world.getDifficulty().toString();
	}
	
	public boolean getBlueSky() { return blueSky; }
	public SingleWorldConfig setBlueSky(boolean blueSky) { this.blueSky = blueSky; return this; }
	public Difficulty getDifficulty() { return Difficulty.valueOf(difficulty); }
	public SingleWorldConfig setDifficulty(Difficulty difficulty) { this.difficulty = difficulty.toString(); return this; }
	public String getAlias() { return alias; }
	public SingleWorldConfig setAlias(String alias) { this.alias = alias; return this; }
	public boolean getPvp() { return pvp; }
	public SingleWorldConfig setPvp(boolean pvp) { this.pvp = pvp; return this; }
	public boolean getBedRespawn() { return bedRespawn; }
	public SingleWorldConfig setBedRespawn(boolean bedRespawn) { this.bedRespawn = bedRespawn; return this; }
	public GameMode getGameMode() { return GameMode.valueOf(gamemode); }
	public SingleWorldConfig setGameMode(GameMode gamemode) { this.gamemode = gamemode.toString(); return this; }
	public boolean getKeepSpawnInMemory() { return keepSpawnInMemory; }
	public SingleWorldConfig setKeepSpawnInMemory(boolean keepSpawnInMemory) { this.keepSpawnInMemory = keepSpawnInMemory; return this; }
	public boolean getAllowNetherPortal() { return allowNetherPortal; }
	public SingleWorldConfig setAllowNetherPortal(boolean allowNetherPortal) { this.allowNetherPortal = allowNetherPortal; return this; }
	public boolean getAllowEndPortal() { return allowEndPortal; }
	public SingleWorldConfig setAllowEndPortal(boolean allowEndPortal) { this.allowEndPortal = allowEndPortal; return this; }
	public boolean getHunger() { return hunger; }
	public SingleWorldConfig setHunger(boolean hunger) { this.hunger = hunger; return this; }
	public boolean getAutoHeal() { return autoHeal; }
	public SingleWorldConfig setAutoHeal(boolean autoHeal) { this.autoHeal = autoHeal; return this; }
	public boolean getLockDimIdStatus() { return lockDimId; }
	public SingleWorldConfig setLockDimIdStatus(boolean lockDimId) { this.lockDimId = lockDimId; return this; }
	public Integer getLockDimId() { return dimid; }
	public SingleWorldConfig setLockDimId(Integer dimid) { this.dimid = dimid; return this; }
	public String getRespawnWorldName() { return respawnWorldName; }
	public SingleWorldConfig setRespawnWorldName(String respawnWorldName) { this.respawnWorldName = respawnWorldName; return this; }
	public String getSeed() { return seed; }
	public SingleWorldConfig setSeed(String seed) { this.seed = seed; return this; }
	public long getRegenOnEveryXHours() { return regenOnEveryXHours; }
	public SingleWorldConfig setRegenOnEveryXHours(long regenOnEveryXHours) { this.regenOnEveryXHours = regenOnEveryXHours; return this; }
	public long getCurrentLiveHours() { return currentLiveHours; }
	public SingleWorldConfig setCurrentLiveHours(long currentLiveHours) { this.currentLiveHours = currentLiveHours; return this; }
	public WorldCreatureConfig getCreatures() { return creatures; }
	public SingleWorldConfig setCreatures(WorldCreatureConfig creatures) {
		this.creatures.monsters = creatures.monsters;
		this.creatures.normalAnimals = creatures.normalAnimals;
		this.creatures.waterAnimals = creatures.waterAnimals;
		return this;
	}
	public double getEnterFee() { return enterFee; }
	public SingleWorldConfig setEnterFee(double enterFee) { this.enterFee = enterFee; return this; }
	public WorldGeneratorConfig getGenerator() { return generator; }
	public SingleWorldConfig setGenerator(WorldGeneratorConfig generator) {
		this.generator.generator = generator.generator;
		this.generator.forgeGenerator = generator.forgeGenerator;
		return this;
	}
	public WorldWeatherConfig getWeather() { return weather; }
	public SingleWorldConfig setWeather(WorldWeatherConfig weather) {
		this.weather.allowRain = weather.allowRain;
		this.weather.allowThunder = weather.allowThunder;
		return this;
	}
	public Environment getEnvironment() { return Environment.valueOf(env); }
	public SingleWorldConfig setEnvironment(Environment env) { this.env = env.toString(); return this; }
	public boolean getGenerateStructures() { return generateStructures; }
	public SingleWorldConfig setgetGenerateStructures(boolean generateStructures) { this.generateStructures = generateStructures; return this; }
	public ItemStack getWorldIcon() { return ItemStackUtil.stringToItemStack(this.worldIcon); }
	public String getWorldIconStr() { return this.worldIcon; }
	public SingleWorldConfig setWorldIcon(String item) { this.worldIcon = item; return this; }
	public SingleWorldConfig setWorldIcon(ItemStack is) { this.worldIcon = ItemStackUtil.itemStackToString(is); return this; }
}
