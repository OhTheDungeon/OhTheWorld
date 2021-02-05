package cn.shadow.OhTheWorld.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class WorldCreature implements GuiProvider {
	
	private final static String[] GUISETUP = {
			"abc","   ","  z",
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private GuiProvider parent;
	
	private boolean[] normalAnimals;
	private boolean[] waterAnimals;
	private boolean[] monsters;
	
	public WorldCreature(GuiProvider parent,
			boolean[] normalAnimals,
			boolean[] waterAnimals,
			boolean[] monsters) {
		this.parent = parent;
		gh = new GuiInventoryHolder(I18n.getInstance().CreatureSpawn[0], 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		
		this.normalAnimals = normalAnimals;
		this.waterAnimals = waterAnimals;
		this.monsters = monsters;
	}
	
	@Override
	public void openInv(Player p) {
		gui.setCloseAction(close -> {
		    return false;
		});
		GuiMacro.checkBox('a', I18n.getInstance().NormalAnimals, normalAnimals, this, gui);
		GuiMacro.checkBox('b', I18n.getInstance().WaterAnimals, waterAnimals, this, gui);
		GuiMacro.checkBox('c', I18n.getInstance().Monsters, monsters, this, gui);
		
		gui.addElement(new StaticGuiElement('z',
		        new ItemStack(MultiVersion.getInstance().getLever()),
		        1,
		        click -> {
		        	gui.close();
		        	parent.openInv(p);
		            return true;
		        },
		        I18n.getInstance().CreateNewWorld
		));
		
		gui.show(p);
	}
}

