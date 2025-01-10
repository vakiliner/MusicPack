package vakiliner.musicpack.fabric;

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private boolean enabled = true;
	private boolean hidersMusic = true;
	private boolean seekersMusic = true;
	private boolean disableDefaultMusic = true;
	private double hidersMusicVolume = 1;
	private double seekersMusicVolume = 1;

	public boolean enabled() {
		return this.enabled;
	}

	public boolean hidersMusicEnabled() {
		return this.hidersMusic;
	}

	public boolean seekersMusicEnabled() {
		return this.seekersMusic;
	}

	public boolean disableDefaultMusic() {
		return this.disableDefaultMusic;
	}

	public double hidersMusicVolume() {
		return this.hidersMusicVolume;
	}

	public double seekersMusicVolume() {
		return this.seekersMusicVolume;
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void hidersMusicEnabled(boolean hidersMusic) {
		this.hidersMusic = hidersMusic;
	}

	public void seekersMusicEnabled(boolean seekersMusic) {
		this.seekersMusic = seekersMusic;
	}

	public void disableDefaultMusic(boolean disableDefaultMusic) {
		this.disableDefaultMusic = disableDefaultMusic;
	}

	public void hidersMusicVolume(double hidersMusicVolume) {
		this.hidersMusicVolume = hidersMusicVolume;
	}

	public void seekersMusicVolume(double seekersMusicVolume) {
		this.seekersMusicVolume = seekersMusicVolume;
	}
}