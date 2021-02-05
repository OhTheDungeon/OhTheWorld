package cn.shadow.OhTheWorld.config.sub;

import java.util.ArrayList;
import java.util.List;

import cn.shadow.OhTheWorld.utils.I18n;
import net.md_5.bungee.api.ChatColor;

public class WorldCreatureConfig {
	public boolean normalAnimals = true;
	public boolean waterAnimals = true;
	public boolean monsters = true;
	
	public String[] lores() {
		List<String> res = new ArrayList<>();
		for(String str : I18n.getInstance().CreatureSpawn) {
			res.add(str);
		}
		res.add(contentBuilder(I18n.getInstance().NormalAnimals[0], normalAnimals));
		res.add(contentBuilder(I18n.getInstance().WaterAnimals[0], waterAnimals));
		res.add(contentBuilder(I18n.getInstance().Monsters[0], monsters));
		
		String[] lores = new String[res.size()];
		for(int i = 0; i < res.size(); i++) lores[i] = res.get(i);
		return lores;
	}
	
	private static String contentBuilder(String lang, boolean status) {
		StringBuilder str = new StringBuilder(ChatColor.BLUE + lang);
		str.append(" : ");
		if(status) str.append(ChatColor.YELLOW + I18n.getInstance().Enabled);
		else str.append(ChatColor.RED + I18n.getInstance().Disabled);
		return str.toString();
	}
}
