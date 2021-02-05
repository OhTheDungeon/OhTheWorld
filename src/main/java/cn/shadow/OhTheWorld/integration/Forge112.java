package cn.shadow.OhTheWorld.integration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;

import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.LogUtil;
import cn.shadow.OhTheWorld.utils.nbt.NBTHelper;
import cn.shadow.OhTheWorld.utils.nbt.tags.CompoundTag;
import cn.shadow.OhTheWorld.utils.nbt.tags.StringTag;
import cn.shadow.OhTheWorld.utils.nbt.tags.Tag;

public class Forge112 {
	
	private final static Set<String> DEFAULTS;
	static {
		DEFAULTS = new HashSet<>();
		DEFAULTS.add("default");
		DEFAULTS.add("flat");
		DEFAULTS.add("largeBiomes");
		DEFAULTS.add("amplified");
		DEFAULTS.add("customized");
		DEFAULTS.add("debug_all_block_states");
		DEFAULTS.add("default_1_1");
	}
	
	/*
	 * https://github.com/Luohuayu/CatServer/blob/1.12.2/src/main/java/net/minecraftforge/common/DimensionManager.java
	 * Dim id重启改变的原因：
	 * Forge关闭时，会记录当前所有注册的维度的id列表，并在下一次服务器开启时恢复该列表
	 * 该列表中将包含Bukkit世界的虚拟维度id
	 * 在使用Bukkit的WorldCreator加载世界时，列表中已包含上次注册的虚拟维度的id（尽管已经不存在）
	 * 故forge会重新分配新的虚拟维度id
	 * 
	 * 解决方案：
	 * 清除usedIds中的虚拟维度id，并将lastUsedId设定为该虚拟维度id
	 * 
	 * 安全性：
	 * 安全 - 该修改仅会影响getNextFreeDimId()的结果。getNextFreeDimId()中总是会调用checkAvailable()
	 * 检查维度是否可用，因此不会与其他mod/插件冲突
	 */
	public static void prepareDim(Integer dimid) {
		if(dimid == null) return;
	    try {
	        Class<?> clazz = Class.forName("net.minecraftforge.common.DimensionManager");
	        Field field = clazz.getDeclaredField("worlds");
	        field.setAccessible(true);
	        Map<?, ?> map = (Map<?, ?>)field.get((Object)null);
	        map.remove(Integer.valueOf(dimid));
	        field = clazz.getDeclaredField("dimensions");
	        field.setAccessible(true);
	        map = (Map<?, ?>)field.get((Object)null);
	        map.remove(Integer.valueOf(dimid));
	        field = clazz.getDeclaredField("usedIds");
	        field.setAccessible(true);
	        Set<?> obj = (Set<?>)field.get((Object)null);
	        obj.remove(Integer.valueOf(dimid));
	        field = clazz.getDeclaredField("lastUsedId");
	        field.setAccessible(true);
	        field.set((Object)null, Integer.valueOf(dimid));
	    } catch (ClassNotFoundException|IllegalAccessException|IllegalArgumentException|NoSuchFieldException|SecurityException ex) {
	    	String msg = String.format(I18n.getInstance().FailToFixDimId, dimid);
	    	LogUtil.getLogger().log(Level.SEVERE, msg);
	    } 
	}
	
	public static List<String> getWorldTypes() {
		List<String> worldTypes = new ArrayList<>();
		try {
	        Class<?> clazz = Class.forName("net.minecraft.world.WorldType");
	        Class<?> clazzReflectionHelper = 
	        		Class.forName("net.minecraftforge.fml.relauncher.ReflectionHelper");
	        Method findField = 
	        		clazzReflectionHelper.getDeclaredMethod("findField", Class.class, String.class, String.class);
	        Field field = (Field) findField.invoke(null, clazz, "WORLD_TYPES", "field_77139_a");
	        
	        Method findMethod =
	        		clazzReflectionHelper.getDeclaredMethod("findMethod", Class.class, String.class, String.class, Class[].class);
	        Method getName = (Method) findMethod.invoke(null, clazz, "getName", "func_77127_a", new Class[]{});
	        
	        if(getName == null) Bukkit.getLogger().info("!!!");
	        
	        Object[] types = (Object[]) field.get((Object) null);
	        for(Object type : types) {
	        	if(type == null) continue;
	        	worldTypes.add((String) getName.invoke(type));
	        }
		} catch (ClassNotFoundException |
				IllegalAccessException |
				IllegalArgumentException |
				SecurityException | 
				InvocationTargetException | 
				NoSuchMethodException ex) {
	    	LogUtil.getLogger().log(Level.SEVERE, I18n.getInstance().FailToGetWorldType);
	    	ex.printStackTrace();
	    }
		
		List<String> result = new ArrayList<>();
		for(String type : worldTypes) {
			if(!DEFAULTS.contains(type)) result.add(type);
		}
		
		return result;
	}
	
	public static boolean isForge112() {
		try {
			Class.forName("org.bukkit.craftbukkit.v1_12_R1.CraftWorld");
			Class.forName("net.minecraft.world.DimensionType");
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		}
	}
	
	public static Integer getDimIdFromBukkitWorld(World world) {
		Object craftWorld = (Object) world;
		try {
			Class<?> clazz = Class.forName("org.bukkit.craftbukkit.v1_12_R1.CraftWorld");
			Method handle = clazz.getMethod("getHandle");
			Object forgeWorld = handle.invoke(craftWorld);
			
			Class<?> dimensionManager = Class.forName("net.minecraftforge.common.DimensionManager");
			Field worldsField = dimensionManager.getDeclaredField("worlds");
			worldsField.setAccessible(true);
			Map<?, ?> worlds = (Map<?, ?>) worldsField.get(null);
			for(Map.Entry<?, ?> entry : worlds.entrySet()) {
				if(entry.getValue().equals(forgeWorld)) {
					return (Integer) entry.getKey();
				}
			}
			
			return null;
		} catch (ClassNotFoundException |
				IllegalAccessException |
				IllegalArgumentException |
				SecurityException | 
				InvocationTargetException | 
				NoSuchFieldException | 
				NoSuchMethodException ex) {
			String msg = String.format(I18n.getInstance().FailToGetWorldDimensionId, world.getName());
	    	LogUtil.getLogger().log(Level.SEVERE, msg);
	    	return null;
	    }
	}
	
	private static CompoundTag setWorldType(CompoundTag tags, String worldType) {
		Map<String, Tag> value = tags.getValue();
		CompoundTag data = (CompoundTag) value.get("Data");
		Map<String, Tag> data_value = data.getValue();
		StringTag generatorName = (StringTag) data_value.get("generatorName");
		generatorName.setValue(worldType);
		data_value.put("generatorName", generatorName);
		data.setValue(data_value);
		value.put("Data", data);
		tags.setValue(value);
		return tags;
	}
	
	public static void modifyLevelDat(File file, String worldType) {
		CompoundTag tags = NBTHelper.readFile(file, true);
		if(tags == null) {
			LogUtil.getLogger().log(Level.SEVERE, I18n.getInstance().FailToSetWorldType);
			return;
		}
		
		try {
			tags = setWorldType(tags, worldType);
			NBTHelper.writeFile(tags, file, true);
		} catch(Exception ex) {
			LogUtil.getLogger().log(Level.SEVERE, I18n.getInstance().FailToSetWorldType);
			ex.printStackTrace();
			return;
		}
	}
}
