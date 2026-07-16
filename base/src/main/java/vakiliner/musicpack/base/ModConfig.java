package vakiliner.musicpack.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import vakiliner.musicpack.api.GsonConfig;

public interface ModConfig {
	void parse(GsonConfig config);

	void load() throws FileNotFoundException, JsonSyntaxException, JsonIOException;

	void save() throws IOException;

	default void loadOrSave() throws IOException {
		boolean exists = this.getFile().exists();
		if (exists) try {
			this.load();
		} catch (FileNotFoundException err) {
			exists = false;
		} catch (JsonIOException err) {
			throw new IOException(err);
		}
		if (!exists) {
			this.save();
		}
	}

	default Path getPath() {
		final Method method;
		try {
			method = this.getClass().getMethod("getFile");
		} catch (NoSuchMethodException err) {
			throw new IllegalStateException(err);
		}
		if (method.getDeclaringClass() == ModConfig.class) {
			throw new UnsupportedOperationException("Unimplemented methods getFile & getPath");
		}
		return this.getFile().toPath();
	};

	default File getFile() {
		final Method method;
		try {
			method = this.getClass().getMethod("getPath");
		} catch (NoSuchMethodException err) {
			throw new IllegalStateException(err);
		}
		if (method.getDeclaringClass() == ModConfig.class) {
			throw new UnsupportedOperationException("Unimplemented methods getFile & getPath");
		}
		return this.getPath().toFile();
	};

	@Deprecated
	default boolean enabled() {
		return true;
	}

	boolean hidersMusicEnabled();

	boolean seekersMusicEnabled();

	boolean disableDefaultMusic();

	double hidersMusicVolume();

	double seekersMusicVolume();

	@Deprecated
	default void enabled(boolean enabled) {
	}

	void hidersMusicEnabled(boolean hidersMusicEnabled);

	void seekersMusicEnabled(boolean seekersMusicEnabled);

	void disableDefaultMusic(boolean disableDefaultMusic);

	void hidersMusicVolume(double hidersMusicVolume);

	void seekersMusicVolume(double seekersMusicVolume);
}