package cn.shadow.OhTheWorld.command;

import org.bukkit.plugin.java.JavaPlugin;

import cn.shadow.OhTheWorld.command.impl.CustomGeneratorText;
import cn.shadow.OhTheWorld.command.impl.MainMenuCommand;
import cn.shadow.OhTheWorld.command.impl.TeleportCommand;

public class CommandManager {
	public static void registerCommands(JavaPlugin plugin) {
		plugin.getCommand("otw").setExecutor(new MainMenuCommand());
		plugin.getCommand("otwg").setExecutor(new CustomGeneratorText());
		plugin.getCommand("wtp").setExecutor(new TeleportCommand());
	}
}
