package cn.shadow.OhTheWorld.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cn.shadow.OhTheWorld.Main;
import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;
import cn.shadow.OhTheWorld.utils.I18n;
import cn.shadow.OhTheWorld.utils.MultiVersion;
import cn.shadow.OhTheWorld.world.WorldManager;
import de.themoep.inventorygui.GuiStateElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import net.md_5.bungee.api.ChatColor;

public class WorldEditMenu implements GuiProvider {
	
	private final static String[] GUISETUP = {
			"a  ","bcd","e f",
	};
	
	private GuiInventoryHolder gh;
	private InventoryGui gui;
	private String worldName;
	private GuiProvider parent;
	public WorldEditMenu(GuiProvider parent, String worldName) {
		gh = new GuiInventoryHolder(I18n.getInstance().MainMenu, 3 * 9);
		gui = new InventoryGui(Main.getInstance(), gh, gh.getTitle(), GUISETUP);
		this.worldName = worldName;
		this.parent = parent;
	}
	
	@Override
	public void openInv(Player p) {
		gui.setCloseAction(close -> {
		    return false;
		});
		gui.addElement(new StaticGuiElement('a',
		        new ItemStack(MultiVersion.getInstance().getWritableBook()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	EditWorld ew = new EditWorld(this, worldName);
		        	ew.openInv(player);
		            return true;
		        },
		        I18n.getInstance().EditWorld
		));
		
		GuiStateElement element = new GuiStateElement('b', 
		        new GuiStateElement.State(
		                change -> {
		                	Player player = (Player) change.getEvent().getWhoClicked();
		                    WorldManager.unloadWorld(player, worldName);
		                },
		                "Unload",
		                new ItemStack(MultiVersion.getInstance().getFeather(), 1),
		                I18n.getInstance().UnloadWorld
		        ),
		        new GuiStateElement.State(
		                change -> {
		                    SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(worldName);
		                    if(swc != null) {
		                    	Player player = (Player) change.getEvent().getWhoClicked();
		                    	WorldManager.loadWorld(player, worldName, swc);
		                    }
		                },
		                "Load",
		                new ItemStack(MultiVersion.getInstance().getFeather(), 1),
		                I18n.getInstance().LoadWorld
		        )
		);
		
		if(Bukkit.getWorld(worldName) == null) {
			element.setState("Load");
		} else {
			element.setState("Unload");
		}
		
		gui.addElement(element);
		
		gui.addElement(new StaticGuiElement('c',
		        new ItemStack(MultiVersion.getInstance().getLavaBucket()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	WorldManager.unloadWorld(player, worldName);
		        	if(Bukkit.getWorld(worldName) == null) {
		        		WorldConfigs.getInstance().worlds.remove(worldName);
		        		String msg = String.format(I18n.getInstance().RemovedWorld, worldName);
		        		p.sendMessage(ChatColor.BLUE + msg);
		        	}
		            return true;
		        },
		        I18n.getInstance().RemoveWorldConfig
		));
		
		gui.addElement(new StaticGuiElement('d',
		        new ItemStack(MultiVersion.getInstance().getEmerald()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	WorldManager.regenerateWorld(player, worldName);
		            return true;
		        },
		        I18n.getInstance().RegenWorld
		));
		
		gui.addElement(new StaticGuiElement('e',
		        new ItemStack(MultiVersion.getInstance().getDiamond()),
		        1,
		        click -> {
		        	gui.close();
		        	Player player = (Player) click.getEvent().getWhoClicked();
		        	WorldManager.unloadWorld(player, worldName);
		        	if(Bukkit.getWorld(worldName) == null) {
		        		WorldManager.resetWorld(p, worldName);
		        	}
		            return true;
		        },
		        I18n.getInstance().ResetWorld
		));
		
		gui.addElement(new StaticGuiElement('f',
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
