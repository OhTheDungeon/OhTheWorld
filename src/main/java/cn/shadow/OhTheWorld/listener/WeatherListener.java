package cn.shadow.OhTheWorld.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import cn.shadow.OhTheWorld.config.WorldConfigs;
import cn.shadow.OhTheWorld.config.sub.SingleWorldConfig;

public class WeatherListener implements Listener {
	@EventHandler
    public void weatherChange(WeatherChangeEvent event) {
		if(event.isCancelled()) return;
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getWorld().getName());
		if(swc == null) return;
		
		if(event.toWeatherState() && !swc.getWeather().allowRain) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
    public void thunderChange(ThunderChangeEvent event) {
		if(event.isCancelled()) return;
		SingleWorldConfig swc = WorldConfigs.getInstance().worlds.get(event.getWorld().getName());
		if(swc == null) return;
		
		if(event.toThunderState() && !swc.getWeather().allowThunder) {
			event.setCancelled(true);
		}
	}
}
