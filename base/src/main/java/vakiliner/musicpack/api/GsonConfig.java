package vakiliner.musicpack.api;

import vakiliner.musicpack.base.ModConfig;

public class GsonConfig {
	@Deprecated
	public Boolean enabled = true;
	public Boolean hidersMusic;
	public Boolean seekersMusic;
	@Deprecated
	public Boolean disableDefaultMusic;
	public Double hidersMusicVolume;
	public Double seekersMusicVolume;
	public Integer disableMusicManager;

	@SuppressWarnings("deprecation")
	public void parse(ModConfig config) {
		this.hidersMusic = config.hidersMusicEnabled();
		this.seekersMusic = config.seekersMusicEnabled();
		this.hidersMusicVolume = config.hidersMusicVolume();
		this.seekersMusicVolume = config.seekersMusicVolume();
		this.disableMusicManager = config.disableMusicManager().value();
		this.disableDefaultMusic = config.disableMusicManager().bool();
	}

	public boolean equals(ModConfig config) {
		return a(this.hidersMusic, config.hidersMusicEnabled())
			&& a(this.seekersMusic, config.seekersMusicEnabled())
			&& a(this.hidersMusicVolume, config.hidersMusicVolume())
			&& a(this.seekersMusicVolume, config.seekersMusicVolume())
			&& a(this.disableMusicManager, config.disableMusicManager().value());
	}

	private <V> boolean a(V nullable, V check) {
		return nullable == null || nullable.equals(check);
	}
}