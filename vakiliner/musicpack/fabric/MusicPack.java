package vakiliner.musicpack.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import com.google.gson.Gson;
import com.google.common.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class MusicPack extends vakiliner.musicpack.base.MusicPack implements ClientModInitializer {
	private static final Type TYPE = new TypeToken<ModConfig>() { }.getType();
	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(MusicPack.MOD_ID + ".json").toFile();
	public static final SoundEvent SEEK = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "seek"));
	public static final SoundEvent HIDE_0 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.0"));
	public static final SoundEvent HIDE_1 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.1"));
	public static final SoundEvent HIDE_2 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.2"));
	public static final SoundEvent HIDE_G = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.g"));
	private static ModConfig config;

	public void onInitializeClient() {
		loadConfig();
		LOGGER.info("Музыкальный пакет активирован");
	}

	public static vakiliner.musicpack.base.ModConfig getConfig() {
		return config;
	}

	public static void saveConfig() {
		byte[] bytes = new Gson().toJson(config).getBytes();
		try {
			Files.newOutputStream(CONFIG_FILE.toPath()).write(bytes);
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	public static void loadConfig() {
		if (CONFIG_FILE.exists()) {
			try {
				config = new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE)), TYPE);
			} catch (FileNotFoundException err) {
				err.printStackTrace();
			}
		} else {
			config = new ModConfig();
			saveConfig();
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