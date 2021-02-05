package cn.shadow.OhTheWorld.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import io.papermc.lib.PaperLib;

public class WorldTeleportList implements GuiProvider {
	private final static String[] GUISETUP = {
			"abcdefghi", "jklmnopqr", "      123"
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private int offset;
	private List<ItemStack> icons;
	private List<String[]> lores;
	
	public WorldTeleportList() {
		icons = new ArrayList<>();
		lores = new ArrayList<>();
		gh = new GuiInventoryHolder(I18n.getInstance().TeleportWorldList, 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		offset = 0;
		for(World w : Bukkit.getWorlds()) {
			if(WorldConfigs.getInstance().worlds.containsKey(w.getName())) {
				SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(w.getName());
				ItemStack icon = swc.getWorldIcon();
				icons.add(icon);
				List<String> lores = icon.getItemMeta().getLore();
				if(lores == null) lores = new ArrayList<>();
				lores.add(0, I18n.getInstance().LeftClickToTeleport);
				lores.add(0, w.getName());

				String[] slores = new String[lores.size()];
				for(int i = 0; i < lores.size(); i++) {
					slores[i] = lores.get(i);
				}
				
				this.lores.add(slores);
			} else {
				icons.add(new ItemStack(MultiVersion.getInstance().getDirt()));
				lores.add(new String[] { w.getName(), I18n.getInstance().LeftClickToTeleport });
			}
		}
	}
	
	private void updateContent() {
		int start = offset * 18;
		char c = 'a';
		for(int i = start; i < start + 18; i++) {
			if(i >= icons.size()) break;
			final int index = i;
			
			gui.addElement(new StaticGuiElement(c,
			        new ItemStack(icons.get(i)),
			        1,
			        click -> {
			        	Player p = (Player) click.getEvent().getWhoClicked();
			        	World w = Bukkit.getWorld(lores.get(index)[0]);
			        	if(w != null) {
			        		gui.close();
			        		PaperLib.teleportAsync(p, w.getSpawnLocation());
			        	}
			        	return true;
			        },
			        lores.get(i)
			)); 
			c++;
		}
	}

	@Override
	public void openInv(Player p) {
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
		            if(offset * 18 >= icons.size()) offset -= 1;
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
		            return true;
		        },
		        I18n.getInstance().Back
		));
				
		gui.show(p);
	}
}
