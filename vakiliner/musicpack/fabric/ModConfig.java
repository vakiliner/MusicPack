package vakiliner.musicpack.fabric;

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private boolean enabled = true;
	private boolean hidersMusic = true;
	private boolean seekersMusic = true;
	private boolean disableDefaultMusic = true;

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
}