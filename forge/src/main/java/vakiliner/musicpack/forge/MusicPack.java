package vakiliner.musicpack.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import vakiliner.musicpack.api.GsonConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@Mod(MusicPack.MOD_ID)
public class MusicPack extends vakiliner.musicpack.base.MusicPack {
	private static final ModConfig CONFIG = new ModConfig();
	public static final SoundEvent SEEK = new SoundEvent(new ResourceLocation(MOD_ID, "seek"));
	public static final SoundEvent HIDE_0 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.0"));
	public static final SoundEvent HIDE_1 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.1"));
	public static final SoundEvent HIDE_2 = new SoundEvent(new ResourceLocation(MOD_ID, "hide.2"));
	public static final SoundEvent HIDE_G = new SoundEvent(new ResourceLocation(MOD_ID, "hide.g"));

	public MusicPack() {
		this(ModLoadingContext.get());
	}

	public MusicPack(ModLoadingContext context) {
		context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, new ModMenuIntegration());
		MinecraftForge.EVENT_BUS.register(this);
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

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(new SoundEvent[] { SEEK, HIDE_0, HIDE_1, HIDE_2, HIDE_G });
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

	@Override
	public Path getPath() {
		Path folderPath = new File(".").toPath().resolve("config");
		if (!folderPath.toFile().exists()) try {
			Files.createDirectories(folderPath);
		} catch (IOException err) {
			throw new IllegalStateException("Failed to create config directory", err);
		}
		return folderPath.resolve(MusicPack.MOD_ID + ".json");
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