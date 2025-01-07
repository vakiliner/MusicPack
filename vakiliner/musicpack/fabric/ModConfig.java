package vakiliner.musicpack.fabric;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = MusicPack.MOD_ID)
class ModConfig implements vakiliner.musicpack.base.ModConfig, ConfigData {
	private boolean enabled = true;
	private boolean hidersMusic = true;
	private boolean seekersMusic = true;
	private boolean disableDefaultMusic = true;

	public boolean enabled() {
		return this.enabled;
	}

	public boolean hidersMusicEnabled() {
		return this.enabled() && this.hidersMusic;
	}

	public boolean seekersMusicEnabled() {
		return this.enabled() && this.seekersMusic;
	}

	public boolean disableDefaultMusic() {
		return this.enabled() && this.disableDefaultMusic;
	}
}