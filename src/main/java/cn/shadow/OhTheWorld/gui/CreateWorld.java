package cn.shadow.OhTheWorld.gui;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.gui.impl.BasicWorldGui;
import cn.shadow.OhTheWorld.utils.I18n;
import de.themoep.inventorygui.InventoryGui;

public class CreateWorld extends BasicWorldGui {
	
	private final static String[] GUISETUP = {
			"abotdlpq ", "rcefghijv", "kmnsu    ", "       20"
	};
	
	private void init() {
		gh = new GuiInventoryHolder(I18n.getInstance().CreateNewWorld[0], 4 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
	}

	public CreateWorld(GuiProvider parent) {
		super(parent);
		init();
	}

}
