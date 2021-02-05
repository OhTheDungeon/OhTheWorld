package cn.shadow.OhTheWorld.gui;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.gui.impl.BasicWorldGui;
import cn.shadow.OhTheWorld.utils.I18n;
import de.themoep.inventorygui.InventoryGui;

public class ImportWorld extends BasicWorldGui {
	
	private final static String[] GUISETUP = {
			"abodpq   ", "cefghijv ", "kmnsu    ", "       21"
	};

	private void init() {
		gh = new GuiInventoryHolder(I18n.getInstance().ImportWorld[0], 4 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
	}
	
	public ImportWorld(GuiProvider parent) {
		super(parent);
		init();
	}
}
