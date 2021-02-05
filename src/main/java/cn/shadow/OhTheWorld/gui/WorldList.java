package cn.shadow.OhTheWorld.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import cn.shadow.OhTheWorld.world.WorldManager;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldList implements GuiProvider {
	private final static String[] GUISETUP = {
			"abcdefghi", "jklmnopqr", "      123"
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private int offset;
	private List<String> worlds;
	private List<ItemStack> icons;
	private GuiProvider parent;
	
	public WorldList(GuiProvider parent) {
		this.parent = parent;
		worlds = new ArrayList<>();
		icons = new ArrayList<>();
		gh = new GuiInventoryHolder(I18n.getInstance().WorldList[0], 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		offset = 0;
		for(Map.Entry<String, SingleWorldConfig> entry : WorldConfigs.getInstance().worlds.entrySet()) {
			if(Bukkit.getWorld(entry.getKey()) != null) {
				worlds.add(entry.getKey());
				icons.add(entry.getValue().getWorldIcon());
			} else if(WorldManager.unloaded.contains(entry.getKey())) {
				worlds.add(entry.getKey());
				icons.add(new ItemStack(MultiVersion.getInstance().getEmerald()));
			}
		}
		for(World w : Bukkit.getWorlds()) {
			if(!worlds.contains(w.getName())) {
				worlds.add(w.getName());
				icons.add(new ItemStack(MultiVersion.getInstance().getDirt()));
			}
		}
	}
	
	private void updateContent() {
		int start = offset * 18;
		char c = 'a';
		for(int i = start; i < start + 18; i++) {
			if(i >= worlds.size()) break;
			final int index = i;
			
			String title = worlds.get(i);
			if(icons.get(index).getType() == MultiVersion.getInstance().getEmerald()) {
				title += " " + I18n.getInstance().NotInMemory;
			}
			
			gui.addElement(new StaticGuiElement(c,
			        new ItemStack(icons.get(i)),
			        1,
			        click -> {
			        	Player p = (Player) click.getEvent().getWhoClicked();
			        	World w = Bukkit.getWorld(worlds.get(index));
			        	if(w != null || WorldConfigs.getInstance().worlds.containsKey(worlds.get(index))) {
			        		gui.close();
			        		
			        		if(!WorldConfigs.getInstance().worlds.containsKey(w.getName())) {
			        			WorldConfigs.getInstance().worlds.put(w.getName(), new SingleWorldConfig(w));
			        			WorldConfigs.safeSave();
			        		}
			        		
			        		WorldEditMenu ew = new WorldEditMenu(this, w.getName());
			        		ew.openInv(p);
			        	}
			        	return true;
			        },
			        WorldManager.getLore(title, title, I18n.getInstance().ClickToEdit)
			)); 
			c++;
		}
	}

	@Override
	public void openInv(Player p) {
		gui.setCloseAction(close -> {
		    return false;
		});
		updateContent();
		
		gui.addElement(new StaticGuiElement('1',
		        new ItemStack(MultiVersion.getInstance().getEndCrystal()),
		        1, // Display a number as the item count
		        click -> {
		            offset -= 1;
		            if(offset < 0) offset = 0;
		            gui.close();
		            openInv(p);
		            return true;
		        },
		        I18n.getInstance().PreviousPage
		)); 
		
		gui.addElement(new StaticGuiElement('2',
		        new ItemStack(MultiVersion.getInstance().getEndCrystal()),
		        1, // Display a number as the item count
		        click -> {
		            offset += 1;
		            if(offset * 18 >= worlds.size()) offset -= 1;
		            gui.close();
		            openInv(p);
		            return true;
		        },
		        I18n.getInstance().NextPage
		));
		
		gui.addElement(new StaticGuiElement('3',
		        new ItemStack(MultiVersion.getInstance().getLever()),
		        1,
		        click -> {
		        	gui.close();
		        	parent.openInv(p);
		            return true;
		        },
		        I18n.getInstance().Back
		));
				
		gui.show(p);
	}
}
