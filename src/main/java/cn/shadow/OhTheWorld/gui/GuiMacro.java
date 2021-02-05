package cn.shadow.OhTheWorld.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.gui.impl.BasicWorldGui;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.wesjd.anvilgui.AnvilGUI;

public class GuiMacro {
	
	public static void checkBox(char placeholder, String[] langs, boolean[] ref,
			GuiProvider guiholder, InventoryGui gui) {
		List<String> loresEnabled = new ArrayList<>();
		for(String sub : langs) loresEnabled.add(sub);
		List<String> loresDisabled = new ArrayList<>(loresEnabled);
		loresEnabled.add(1, I18n.getInstance().Status + I18n.getInstance().Enabled);
		loresDisabled.add(1, I18n.getInstance().Status + I18n.getInstance().Disabled);
		String[] enabled = new String[loresEnabled.size()];
		for(int i = 0; i < loresEnabled.size(); i++) enabled[i] = loresEnabled.get(i);
		String[] disabled = new String[loresEnabled.size()];
		for(int i = 0; i < loresDisabled.size(); i++) disabled[i] = loresDisabled.get(i);
		
		GuiStateElement element = new GuiStateElement(placeholder, 
		        new GuiStateElement.State(
		                change -> {
		                	ref[0] = true;
		                },
		                "Enabled",
		                new ItemStack(MultiVersion.getInstance().getWaterBucket()),
		                enabled
		        ),
		        new GuiStateElement.State(
		                change -> {
		                	ref[0] = false;
		                },
		                "Disabled",
		                new ItemStack(MultiVersion.getInstance().getEmptyBucket()),
		                disabled
		        )
		);
		 
		if (ref[0]) {
		    element.setState("Enabled");
		} else {
		    element.setState("Disabled");
		}
		 
		gui.addElement(element);
	}
	
	public static void InputBox(char placeholder, String[] langs, String[] ref, 
			BasicWorldGui guiholder, InventoryGui gui) {
		String[] lores = new String[langs.length];
		System.arraycopy(langs, 0, lores, 0, langs.length);
		
		for(int i = 0; i < lores.length; i++) {
			lores[i] = String.format(lores[i], ref[0]);
		}
		
		gui.addElement(new StaticGuiElement(placeholder,
		    new ItemStack(MultiVersion.getInstance().getWritableBook()),
		    1,
		    click -> {
		    	gui.close();
		    	Player player = (Player) click
		    			.getEvent()
		    			.getWhoClicked();
		    	
		    	new AnvilGUI.Builder()
		        .onClose(cp -> {
		        	guiholder.openInv(cp);
		        })
		        .onComplete((cp, text) -> {
		            if(!text.isEmpty()) {
		            	ref[0] = text;
		                return AnvilGUI.Response.close();
		            } else {
		                return AnvilGUI.Response.text(ref[0]);
		            }
		        })
		        .text(ref[0])
		        .itemLeft(new ItemStack(MultiVersion.getInstance().getDiamondSword()))
		        .title(lores[0])
		        .plugin(Main.getInstance())
		        .open(player);
		        return true;
		    },
		    lores
		)); 
	}
}
