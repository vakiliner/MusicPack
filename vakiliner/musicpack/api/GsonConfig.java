package vakiliner.musicpack.api;

import com.google.gson.annotations.Since;
import vakiliner.musicpack.base.ModConfig;

public class GsonConfig {
	public Boolean enabled;
	public Boolean hidersMusic;
	public Boolean seekersMusic;
	public Boolean disableDefaultMusic;
	@Since(3)
	public Double hidersMusicVolume;
	@Since(3)
	public Double seekersMusicVolume;

	public void parse(ModConfig config) {
		this.enabled = config.enabled();
		this.hidersMusic = config.hidersMusicEnabled();
		this.seekersMusic = config.seekersMusicEnabled();
		this.disableDefaultMusic = config.disableDefaultMusic();
		this.hidersMusicVolume = config.hidersMusicVolume();
		this.seekersMusicVolume = config.seekersMusicVolume();
	}

	public boolean equals(ModConfig config) {
		return (this.enabled == null || this.enabled == config.enabled())
			&& (this.hidersMusic == null || this.hidersMusic == config.hidersMusicEnabled())
			&& (this.seekersMusic == null || this.seekersMusic == config.seekersMusicEnabled())
			&& (this.disableDefaultMusic == null || this.disableDefaultMusic == config.disableDefaultMusic())
			&& (this.hidersMusicVolume == null || this.hidersMusicVolume == config.hidersMusicVolume())
			&& (this.seekersMusicVolume == null || this.seekersMusicVolume == config.seekersMusicVolume());
	}
}