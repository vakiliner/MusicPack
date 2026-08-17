package vakiliner.musicpack.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import vakiliner.musicpack.api.GsonConfig;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class MusicPack extends vakiliner.musicpack.base.MusicPack implements ClientModInitializer {
	private static final ModConfig CONFIG = new ModConfig();
	public static final SoundEvent SEEK = new SoundEvent(new ResourceLocation(MOD_ID, "seek"));
	public static final SoundEvent HIDE_0 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.0"));
	public static final SoundEvent HIDE_1 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.1"));
	public static final SoundEvent HIDE_2 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.2"));
	public static final SoundEvent HIDE_G = new SoundEvent(new ResourceLocation(MOD_ID, "hide.g"));
	private static final Method SITUATIONAL_MUSIC;
	private static final Object MUSIC_MENU;

	static {
		try {
			SITUATIONAL_MUSIC = Minecraft.class.getMethod("method_1544");
		} catch (NoSuchMethodException err) {
			throw new IllegalStateException(err);
		}
		Class<?> musicsClass;
		try {
			musicsClass = Class.forName("net.minecraft.class_1143");
		} catch (ClassNotFoundException a) {
			try {
				musicsClass = Class.forName("net.minecraft.class_1142$class_1143");
			} catch (ClassNotFoundException err) {
				throw new IllegalStateException(err);
			}
		}
		Object musicMenu;
		try {
			musicMenu = musicsClass.getField("field_5585").get(null);
		} catch (NoSuchFieldException | IllegalAccessException err) {
			throw new IllegalStateException(err);
		}
		MUSIC_MENU = musicMenu;
	}

	public static boolean isMusicMenuPlayed(Minecraft minecraft) {
		try {
			return SITUATIONAL_MUSIC.invoke(minecraft) == MUSIC_MENU;
		} catch (IllegalAccessException | InvocationTargetException err) {
			throw new IllegalStateException(err);
		}
	}

	@Override
	public void onInitializeClient() {
		try {
			CONFIG.loadOrSave();
		} catch (IOException err) {
			throw new IllegalStateException("Failed to load config", err);
		}
		this.ready();
	}

	public static vakiliner.musicpack.base.ModConfig getConfig() {
		return CONFIG;
	}

	/**
	 * @deprecated Use {@link ModConfig#save()}
	 */
	@Deprecated
	public static void saveConfig() {
		try {
			getConfig().save();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	/**
	 * @deprecated Use {@link ModConfig#load()}
	 */
	@Deprecated
	public static void loadConfig() {
		vakiliner.musicpack.base.ModConfig config = getConfig();
		if (config.getFile().exists()) {
			try {
				config.load();
			} catch (FileNotFoundException err) {
				err.printStackTrace();
			}
		}
	}

	private static void registerSound(SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, soundEvent.getLocation(), soundEvent);
	}

	static {
		registerSound(SEEK);
		registerSound(HIDE_0);
		registerSound(HIDE_1);
		registerSound(HIDE_2);
		registerSound(HIDE_G);
	}
}

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private boolean hidersMusic = true;
	private boolean seekersMusic = true;
	private double hidersMusicVolume = 1;
	private double seekersMusicVolume = 1;
	private DisableMusicManager disableMusicManager = DisableMusicManager.NOWHERE;

	@SuppressWarnings("deprecation")
	@Override
	public void parse(GsonConfig config) {
		if (config.hidersMusic != null) this.hidersMusic = config.hidersMusic;
		if (config.seekersMusic != null) this.seekersMusic = config.seekersMusic;
		if (config.hidersMusicVolume != null) this.hidersMusicVolume = config.hidersMusicVolume;
		if (config.seekersMusicVolume != null) this.seekersMusicVolume = config.seekersMusicVolume;
		if (config.disableMusicManager != null) this.disableMusicManager = DisableMusicManager.getByInt(config.disableMusicManager, this.disableMusicManager);
		else if (config.disableDefaultMusic != null) this.disableMusicManager = DisableMusicManager.getByBool(config.disableDefaultMusic);
	}

	@Override
	public GsonConfig toGson() {
		GsonConfig config = new GsonConfig();
		config.parse(this);
		return config;
	}

	@Override
	public void load() throws FileNotFoundException, JsonSyntaxException, JsonIOException {
		this.parse(new Gson().fromJson(new FileReader(this.getFile()), GsonConfig.class));
	}

	@Override
	public void save() throws IOException {
		Files.write(this.getPath(), new Gson().toJson(this.toGson()).getBytes());
	}

	@SuppressWarnings("deprecation")
	@Override
	public Path getPath() {
		return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(MusicPack.MOD_ID + ".json");
	}

	@Override
	public boolean hidersMusicEnabled() {
		return this.hidersMusic;
	}

	@Override
	public boolean seekersMusicEnabled() {
		return this.seekersMusic;
	}

	@Override
	public double hidersMusicVolume() {
		return Mth.clamp(this.hidersMusicVolume, 0, 1);
	}

	@Override
	public double seekersMusicVolume() {
		return Mth.clamp(this.seekersMusicVolume, 0, 1);
	}

	@Override
	public DisableMusicManager disableMusicManager() {
		return this.disableMusicManager;
	}

	@Override
	public void hidersMusicEnabled(boolean hidersMusic) {
		this.hidersMusic = hidersMusic;
	}

	@Override
	public void seekersMusicEnabled(boolean seekersMusic) {
		this.seekersMusic = seekersMusic;
	}

	@Override
	public void hidersMusicVolume(double hidersMusicVolume) {
		this.hidersMusicVolume = hidersMusicVolume;
	}

	@Override
	public void seekersMusicVolume(double seekersMusicVolume) {
		this.seekersMusicVolume = seekersMusicVolume;
	}

	@Override
	public void disableMusicManager(DisableMusicManager disableMusicManager) {
		this.disableMusicManager = Objects.requireNonNull(disableMusicManager);
	}
}