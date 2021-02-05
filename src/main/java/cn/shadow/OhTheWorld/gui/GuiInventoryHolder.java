package cn.shadow.OhTheWorld.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiInventoryHolder implements InventoryHolder {
	private Inventory inv;
	private String title;
	
    public GuiInventoryHolder(String title, int slot) {
        this.title = title;
        inv = Bukkit.createInventory(this, slot, this.title);
    }
    
    public GuiInventoryHolder(String title, InventoryType type) {
        this.title = title;
        inv = Bukkit.createInventory(this, type);
    }
    
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	public String getTitle() { return this.title; }
}
