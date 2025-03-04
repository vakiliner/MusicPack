package vakiliner.musicpack.forge;

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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

@Mod(MusicPack.MOD_ID)
public class MusicPack extends vakiliner.musicpack.base.MusicPack {
	private static final ModConfig config = new ModConfig();
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
			if (ModConfig.FILE.exists()) {
				config.load();
			} else {
				config.save();
			}
		} catch (Throwable err) {
			err.printStackTrace();
		}
		this.ready();
	}

	public static vakiliner.musicpack.base.ModConfig getConfig() {
		return config;
	}

	/**
	 * @deprecated Use {@link ModConfig#save()}
	 */
	@Deprecated
	public static void saveConfig() {
		byte[] bytes = new Gson().toJson(config).getBytes();
		try {
			if (!ModConfig.FILE.getParentFile().exists()) {
				Files.createDirectories(ModConfig.FILE.toPath().getParent());
			}
			Files.newOutputStream(ModConfig.FILE.toPath()).write(bytes);
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	/**
	 * @deprecated Use {@link ModConfig#load()}
	 */
	@Deprecated
	public static void loadConfig() {
		if (ModConfig.FILE.exists()) {
			try {
				config.parse((JsonObject) new Gson().fromJson(new JsonReader(new FileReader(ModConfig.FILE)), JsonObject.class));
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
	static final File FILE = new File(".").toPath().resolve("config").resolve(MusicPack.MOD_ID + ".json").toFile();
	boolean enabled = true;
	boolean hidersMusic = true;
	boolean seekersMusic = true;
	boolean disableDefaultMusic = true;
	double hidersMusicVolume = 1;
	double seekersMusicVolume = 1;

	void parse(JsonObject jsonObject) {
		if (jsonObject.get("enabled").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("enabled").isBoolean()) this.enabled = jsonObject.get("enabled").getAsBoolean();
		if (jsonObject.get("hidersMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("hidersMusic").isBoolean()) this.hidersMusic = jsonObject.get("hidersMusic").getAsBoolean();
		if (jsonObject.get("seekersMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("seekersMusic").isBoolean()) this.seekersMusic = jsonObject.get("seekersMusic").getAsBoolean();
		if (jsonObject.get("disableDefaultMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("disableDefaultMusic").isBoolean()) this.disableDefaultMusic = jsonObject.get("disableDefaultMusic").getAsBoolean();
		if (jsonObject.get("hidersMusicVolume").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("hidersMusicVolume").isNumber()) this.hidersMusicVolume = jsonObject.get("hidersMusicVolume").getAsDouble();
		if (jsonObject.get("seekersMusicVolume").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("seekersMusicVolume").isNumber()) this.seekersMusicVolume = jsonObject.get("seekersMusicVolume").getAsDouble();
	}

	public void parse(GsonConfig config) {
		if (config.enabled != null) this.enabled = config.enabled;
		if (config.hidersMusic != null) this.hidersMusic = config.hidersMusic;
		if (config.seekersMusic != null) this.seekersMusic = config.seekersMusic;
		if (config.disableDefaultMusic != null) this.disableDefaultMusic = config.disableDefaultMusic;
		if (config.hidersMusicVolume != null) this.hidersMusicVolume = config.hidersMusicVolume;
		if (config.seekersMusicVolume != null) this.seekersMusicVolume = config.seekersMusicVolume;
	}

	public void load() throws FileNotFoundException, JsonSyntaxException {
		this.parse(new Gson().fromJson(new FileReader(FILE), GsonConfig.class));
	}

	public void save() throws IOException {
		if (!FILE.getParentFile().exists()) {
			Files.createDirectories(FILE.toPath().getParent());
		}
		Files.write(FILE.toPath(), new Gson().toJson(this).getBytes());
	}

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
		return MathHelper.clamp(this.hidersMusicVolume, 0, 1);
	}

	public double seekersMusicVolume() {
		return MathHelper.clamp(this.seekersMusicVolume, 0, 1);
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