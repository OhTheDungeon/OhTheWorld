package cn.shadow.OhTheWorld.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MultiVersion {
	
	private Boolean isLegacy = null;
	private Boolean hasNetherUpdate = null;
	
	private MultiVersion() {
		
	}
	
	private static MultiVersion instance = new MultiVersion();
	
	public static MultiVersion getInstance() { return instance; }
	
	public boolean isLegacy() {
		if(isLegacy != null) return isLegacy;
		try {
			Material mat = Material.valueOf("BOOK_AND_QUILL");
			isLegacy = mat != null;
		} catch (IllegalArgumentException ex) {
			isLegacy = false;
		}
		return isLegacy;
	}
	
	public boolean hasNetherUpdate() {
		if(hasNetherUpdate != null) return hasNetherUpdate;
		try {
			Material mat = Material.valueOf("BLACKSTONE");
			hasNetherUpdate = mat != null;
		} catch (IllegalArgumentException ex) {
			hasNetherUpdate = false;
		}
		return hasNetherUpdate;
	}
	
	public Material getEndPortalFrame() {
		if(isLegacy()) return Material.valueOf("ENDER_PORTAL_FRAME");
		else return Material.valueOf("END_PORTAL_FRAME");
	}
	
	public Material getEnderEye() {
		if(isLegacy()) return Material.valueOf("EYE_OF_ENDER");
		else return Material.valueOf("ENDER_EYE");
	}
	
	public Material getWritableBook() {
		if(isLegacy()) return Material.valueOf("BOOK_AND_QUILL");
		else return Material.valueOf("WRITABLE_BOOK");
	}
	
	public Material getDirt() {
		return Material.DIRT;
	}
	
	public Material getWaterBucket() {
		return Material.WATER_BUCKET;
	}
	
	public Material getAir() {
		return Material.AIR;
	}
	
	public Material getLavaBucket() {
		return Material.LAVA_BUCKET;
	}
	
	public Material getEmptyBucket() {
		return Material.BUCKET;
	}
	
	public Material getCompass() {
		return Material.COMPASS;
	}
	
	public Material getLever() {
		return Material.LEVER;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getGrayStainedGlass() {
		if(isLegacy()) return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 7);
		else return new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"), 1);
	}
	
	public ItemStack getGrassBlock() {
		if(isLegacy()) return new ItemStack(Material.valueOf("GRASS"), 1);
		else return new ItemStack(Material.valueOf("GRASS_BLOCK"), 1);
	}
	
	public ItemStack getNetherRack() {
		return new ItemStack(Material.NETHERRACK, 1);
	}
	
	public ItemStack getEndStone() {
		if(isLegacy()) return new ItemStack(Material.valueOf("ENDER_STONE"), 1);
		else return new ItemStack(Material.valueOf("END_STONE"), 1);
	}
	
	public Material getDiamondSword() {
		return Material.DIAMOND_SWORD;
	}
	
	public Material getDiamond() {
		return Material.DIAMOND;
	}
	
	public Material getDiamondPickaxe() {
		return Material.DIAMOND_PICKAXE;
	}
	
	public Material getBarrier() {
		return Material.BARRIER;
	}
	
	public Material getEndCrystal() {
		return Material.END_CRYSTAL;
	}
	
	public Material getFire() {
		return Material.FIRE;
	}
	
	public Material getSunflower() {
		if(isLegacy()) return Material.valueOf("DOUBLE_PLANT");
		else return Material.valueOf("SUNFLOWER");
	}
	
	public Material getEmerald() {
		return Material.EMERALD;
	}
	
	public Material getFeather() {
		return Material.FEATHER;
	}
}
