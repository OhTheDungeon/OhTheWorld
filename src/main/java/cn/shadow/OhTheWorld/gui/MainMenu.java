package cn.shadow.OhTheWorld.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class MainMenu implements GuiProvider {
	
	private final static String[] GUISETUP = {
			"abc","   ","   ",
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	public MainMenu() {
		gh = new GuiInventoryHolder(I18n.getInstance().MainMenu, 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
	}
	
	@Override
	public void openInv(Player p) {
		gui.addElement(new StaticGuiElement('a',
		        new ItemStack(MultiVersion.getInstance().getWritableBook()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	CreateWorld cw = new CreateWorld(this);
		        	cw.openInv(player);
		            return true;
		        },
		        I18n.getInstance().CreateNewWorld
		));
		gui.addElement(new StaticGuiElement('b',
		        new ItemStack(MultiVersion.getInstance().getDiamond()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	WorldList wl = new WorldList(this);
		        	wl.openInv(player);
		            return true;
		        },
		        I18n.getInstance().WorldList
		));
		gui.addElement(new StaticGuiElement('c',
		        new ItemStack(MultiVersion.getInstance().getDiamondPickaxe()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	ImportWorld wl = new ImportWorld(this);
		        	wl.openInv(player);
		            return true;
		        },
		        I18n.getInstance().ImportWorld
		));
		
		gui.show(p);
	}
}
