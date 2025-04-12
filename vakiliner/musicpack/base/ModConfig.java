package vakiliner.musicpack.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import com.google.gson.JsonSyntaxException;
import vakiliner.musicpack.api.GsonConfig;

public interface ModConfig {
	void parse(GsonConfig config);
	void load() throws FileNotFoundException, JsonSyntaxException;
	void save() throws IOException;
	default Path getPath() {
		try {
			Method method = this.getClass().getMethod("getFile");
			if (method.getDeclaringClass() == ModConfig.class) {
				throw new RuntimeException("Unrealized method getFile or getPath");
			}
			return this.getFile().toPath();
		} catch (NoSuchMethodException | SecurityException err) {
			throw new RuntimeException(err);
		}
	};
	default File getFile() {
		try {
			Method method = this.getClass().getMethod("getPath");
			if (method.getDeclaringClass() == ModConfig.class) {
				throw new RuntimeException("Unrealized method getFile or getPath");
			}
			return this.getPath().toFile();
		} catch (NoSuchMethodException | SecurityException err) {
			throw new RuntimeException(err);
		}
	};
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