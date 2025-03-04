package vakiliner.musicpack.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import vakiliner.musicpack.api.GsonConfig;

public interface ModConfig {
	void parse(GsonConfig config);
	void load() throws FileNotFoundException, JsonSyntaxException;
	void save() throws IOException;
	// Getters
	boolean enabled();
	boolean hidersMusicEnabled();
	boolean seekersMusicEnabled();
	boolean disableDefaultMusic();
	double hidersMusicVolume();
	double seekersMusicVolume();
	// Setters
	void enabled(boolean enabled);
	void hidersMusicEnabled(boolean hidersMusicEnabled);
	void seekersMusicEnabled(boolean seekersMusicEnabled);
	void disableDefaultMusic(boolean disableDefaultMusic);
	void hidersMusicVolume(double hidersMusicVolume);
	void seekersMusicVolume(double seekersMusicVolume);
}