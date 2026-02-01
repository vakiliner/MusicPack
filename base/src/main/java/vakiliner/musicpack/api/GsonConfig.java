package vakiliner.musicpack.api;

import vakiliner.musicpack.base.ModConfig;

public class GsonConfig {
	public Boolean enabled = true;
	public Boolean hidersMusic;
	public Boolean seekersMusic;
	public Boolean disableDefaultMusic;
	public Double hidersMusicVolume;
	public Double seekersMusicVolume;

	public void parse(ModConfig config) {
		this.hidersMusic = config.hidersMusicEnabled();
		this.seekersMusic = config.seekersMusicEnabled();
		this.disableDefaultMusic = config.disableDefaultMusic();
		this.hidersMusicVolume = config.hidersMusicVolume();
		this.seekersMusicVolume = config.seekersMusicVolume();
	}

	public boolean equals(ModConfig config) {
		return a(this.hidersMusic, config.hidersMusicEnabled())
			&& a(this.seekersMusic, config.seekersMusicEnabled())
			&& a(this.disableDefaultMusic, config.disableDefaultMusic())
			&& a(this.hidersMusicVolume, config.hidersMusicVolume())
			&& a(this.seekersMusicVolume, config.seekersMusicVolume());
	}

	private <V> boolean a(V nullable, V check) {
		return nullable == null || nullable == check;
	}
}