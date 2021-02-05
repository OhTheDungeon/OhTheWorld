package cn.shadow.OhTheWorld.utils;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {
    public static String itemStackToString(ItemStack is) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("item", is);
        return yml.saveToString();
    }
    
    public static ItemStack stringToItemStack(String str) {
        YamlConfiguration yml = new YamlConfiguration();
        ItemStack item;
        try {
            yml.loadFromString(str);
            item = yml.getItemStack("item");
        } catch (InvalidConfigurationException ex) {
            item = new ItemStack(Material.AIR, 1);
        }
        return item;
    }
}
