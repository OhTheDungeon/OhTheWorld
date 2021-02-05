package cn.shadow.OhTheWorld.utils;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class LogUtil {
	private static Logger logger = null;
	private static void init() {
		if(logger == null) logger = Bukkit.getLogger();
	}
	
	public static Logger getLogger() {
		init();
		return logger;
	}
}
