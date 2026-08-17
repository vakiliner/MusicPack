package vakiliner.musicpack.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import vakiliner.musicpack.api.GsonConfig;

public interface ModConfig {
	void parse(GsonConfig config);

	GsonConfig toGson();

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

	@Deprecated
	default boolean disableDefaultMusic() {
		return this.disableMusicManager().bool();
	}

	double hidersMusicVolume();

	double seekersMusicVolume();

	DisableMusicManager disableMusicManager();

	@Deprecated
	default void enabled(boolean enabled) {
	}

	void hidersMusicEnabled(boolean hidersMusicEnabled);

	void seekersMusicEnabled(boolean seekersMusicEnabled);

	@Deprecated
	default void disableDefaultMusic(boolean disableDefaultMusic) {
		this.disableMusicManager(DisableMusicManager.getByBool(disableDefaultMusic));
	}

	void hidersMusicVolume(double hidersMusicVolume);

	void seekersMusicVolume(double seekersMusicVolume);

	void disableMusicManager(DisableMusicManager disableMusicManager);

	public static enum DisableMusicManager {
		NOWHERE(0),
		EVERYWHERE(1),
		ONLY_IN_GAME(2);

		private static final Map<Integer, DisableMusicManager> map = Maps.newHashMap();
		private final int value;

		private DisableMusicManager(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		@Deprecated
		public boolean bool() {
			return this != NOWHERE;
		}

		public static DisableMusicManager getByInt(int value) {
			return map.get(value);
		}

		public static DisableMusicManager getByInt(int value, DisableMusicManager def) {
			return map.getOrDefault(value, def);
		}

		@Deprecated
		public static DisableMusicManager getByBool(boolean value) {
			return value ? EVERYWHERE : NOWHERE;
		}

		static {
			for (DisableMusicManager type : values()) {
				map.put(type.value, type);
			}
		}
	}
}