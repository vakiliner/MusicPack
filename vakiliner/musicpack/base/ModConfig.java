package vakiliner.musicpack.base;

public interface ModConfig {
	boolean enabled();
	boolean hidersMusicEnabled();
	boolean seekersMusicEnabled();
	boolean disableDefaultMusic();
	double hidersMusicVolume();
	double seekersMusicVolume();
	void enabled(boolean enabled);
	void hidersMusicEnabled(boolean hidersMusicEnabled);
	void seekersMusicEnabled(boolean seekersMusicEnabled);
	void disableDefaultMusic(boolean disableDefaultMusic);
	void hidersMusicVolume(double hidersMusicVolume);
	void seekersMusicVolume(double seekersMusicVolume);
}