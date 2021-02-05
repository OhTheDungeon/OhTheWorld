package cn.shadow.OhTheWorld.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.command.impl.CustomGeneratorText;
import cn.shadow.OhTheWorld.config.sub.WorldGeneratorConfig;
import cn.shadow.OhTheWorld.integration.BukkitGenerators;
import cn.shadow.OhTheWorld.integration.Forge112;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.md_5.bungee.api.ChatColor;

public class WorldGeneratorList implements GuiProvider {
	private final static String[] GUISETUP = {
			"abcdefghi", "jklmnopqr", "0     123"
	};
	
	private WorldGeneratorConfig[] generator;
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private int offset;
	private List<WorldGeneratorConfig> generators;
	private GuiProvider parent;
	
	public WorldGeneratorList(GuiProvider parent, WorldGeneratorConfig[] generator) {
		this(parent, generator, false);
	}
	
	public WorldGeneratorList(GuiProvider parent, WorldGeneratorConfig[] generator, boolean isImport) {
		this.parent = parent;
		this.generator = generator;
		gh = new GuiInventoryHolder(I18n.getInstance().CreateNewWorld[0], 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		this.offset = 0;
		this.generators = new ArrayList<>();
		
		generators.add(new WorldGeneratorConfig());
		
		List<String> bukkit = BukkitGenerators.getAllGenerators();
		for(String str : bukkit) {
			WorldGeneratorConfig sub = new WorldGeneratorConfig();
			sub.generator = str;
			sub.forgeGenerator = null;
			generators.add(sub);
		}
		
		if(!isImport) {
			if(Forge112.isForge112()) {
				List<String> forge = Forge112.getWorldTypes();
				for(String str : forge) {
					WorldGeneratorConfig sub = new WorldGeneratorConfig();
					sub.generator = null;
					sub.forgeGenerator = str;
					generators.add(sub);
				}
			}
		}
	}
	
	private void updateContent() {
		int start = offset * 18;
		char c = 'a';
		for(int i = start; i < start + 18; i++) {
			if(i >= generators.size()) break;
			final int index = i;
			Material type = MultiVersion.getInstance().getBarrier();
			if(this.generator[0].equals(generators.get(index))) 
				type = MultiVersion.getInstance().getDiamond();
			final ItemStack is = new ItemStack(type, 1);
			
			final char d = c;
			gui.addElement(new DynamicGuiElement(c, (viewer) -> {
			    return new StaticGuiElement(d, is, 
			        click -> {
			        	this.generator[0] = generators.get(index);
			        	updateContent();
			            click.getGui().draw(); // Update the GUI
			            return true;
			        }, 
			        generators.get(index).toLores()
			        );
			}));
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
		            if(offset * 18 >= generators.size()) offset -= 1;
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
		
		gui.addElement(new StaticGuiElement('0',
		        new ItemStack(MultiVersion.getInstance().getWritableBook()),
		        1,
		        click -> {
		        	if(CustomGeneratorText.getString(p) == null) {
		        		p.sendMessage(ChatColor.RED + I18n.getInstance().EmptyCustomGenerator);
		        		return true;
		        	}
		        	this.generator[0].forgeGenerator = null;
		        	this.generator[0].generator = CustomGeneratorText.getString(p);
		        	gui.close();
		        	parent.openInv(p);
		            return true;
		        },
		        I18n.getInstance().CustomGenerator
		));
		
		gui.show(p);
	}
}
