package cn.shadow.OhTheWorld.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.ItemStackUtil;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.GuiStorageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;

public class SelectWorldIcon implements GuiProvider {
	
	private final static String[] GUISETUP = {
			"   "," a ","  b",
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private Inventory inv;
	private String[] item;
	private GuiProvider parent;
	
	// Ke@Y: 你不觉得直接传ItemStack[]更好吗
	// -- : String[]可以避免耦合，封箱成本可以忽略
	public SelectWorldIcon(GuiProvider parent, String[] item) {
		this.item = item;
		this.parent = parent;
		gh = new GuiInventoryHolder(I18n.getInstance().WorldIcon[0], 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		gui.setFiller(MultiVersion.getInstance().getGrayStainedGlass());
		inv = Bukkit.createInventory(null, InventoryType.CHEST);
		inv.setItem(0, ItemStackUtil.stringToItemStack(item[0]));
	}
	
	@Override
	public void openInv(Player p) {
		gui.addElement(new GuiStorageElement('a', inv));
		gui.setCloseAction(close -> {
		    return false;
		});
		
		gui.addElement(new StaticGuiElement('b',
		        new ItemStack(MultiVersion.getInstance().getLever()),
		        1,
		        click -> {
		        	gui.close();
		        	if(inv.getItem(0) != null && inv.getItem(0).getType() != Material.AIR) {
				    	item[0] = ItemStackUtil.itemStackToString(inv.getItem(0));
				    }
		        	parent.openInv(p);
		            return true;
		        },
		        I18n.getInstance().Back
		));
		
		gui.show(p);
	}
}
