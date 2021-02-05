package cn.shadow.OhTheWorld.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldWeather implements GuiProvider {
	
	private final static String[] GUISETUP = {
			"ab ","   ","  z",
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private GuiProvider parent;
	
	private boolean[] allowRain;
	private boolean[] allowThunder;
	
	public WorldWeather(GuiProvider parent,
			boolean[] allowRain,
			boolean[] allowThunder) {
		this.parent = parent;
		gh = new GuiInventoryHolder(I18n.getInstance().CreatureSpawn[0], 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		
		this.allowRain = allowRain;
		this.allowThunder = allowThunder;
	}
	
	@Override
	public void openInv(Player p) {
		gui.setCloseAction(close -> {
		    return false;
		});
		GuiMacro.checkBox('a', I18n.getInstance().Rain, allowRain, this, gui);
		GuiMacro.checkBox('b', I18n.getInstance().Thunder, allowThunder, this, gui);
		
		gui.addElement(new StaticGuiElement('z',
		        new ItemStack(MultiVersion.getInstance().getLever()),
		        1,
		        click -> {
		        	gui.close();
		        	parent.openInv(p);
		            return true;
		        },
		        I18n.getInstance().Weather
		));
		
		gui.show(p);
	}
}

