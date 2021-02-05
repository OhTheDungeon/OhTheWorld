package cn.shadow.OhTheWorld.config.sub;

import cn.shadow.OhTheWorld.utils.I18n;

public class WorldGeneratorConfig {
	public String generator = null;
	public String forgeGenerator = null;
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if(!(o instanceof WorldGeneratorConfig)) return false;
		WorldGeneratorConfig wc = (WorldGeneratorConfig) o;
		return wc.generator == this.generator && wc.forgeGenerator == this.forgeGenerator;
	}
	
	public String[] toLores() {
		if(generator == null && forgeGenerator == null) {
			return new String[] {
					I18n.getInstance().DefaultGenerator,
					I18n.getInstance().ProvidedByVanilla
			};
		}
		if(generator != null) {
			return new String[] {
					generator, I18n.getInstance().ProvidedByBukkit
			};
		} else {
			return new String[] {
					forgeGenerator, I18n.getInstance().ProvidedByForge
			};
		}
	}
	
	public boolean isBukkit() {
		return generator != null && forgeGenerator == null;
	}
	
	public boolean isForge() {
		return generator == null && forgeGenerator != null;
	}
}
