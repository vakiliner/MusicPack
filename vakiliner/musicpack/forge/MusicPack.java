package vakiliner.musicpack.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import vakiliner.musicpack.forge.gui.MainSettingsScreen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@Mod(MusicPack.MOD_ID)
public class MusicPack extends vakiliner.musicpack.base.MusicPack {
	private static final File CONFIG_FILE = new File(".").toPath().resolve("config").resolve(MusicPack.MOD_ID + ".json").toFile();
	public static final SoundEvent SEEK = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "seek"));
	public static final SoundEvent HIDE_0 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.0"));
	public static final SoundEvent HIDE_1 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.1"));
	public static final SoundEvent HIDE_2 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.2"));
	public static final SoundEvent HIDE_G = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.g"));
	private static ModConfig config = null;

	public MusicPack() {
		this(ModLoadingContext.get());
	}

	public MusicPack(ModLoadingContext context) {
		context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (minecraft, parent) -> new MainSettingsScreen(parent));
		MinecraftForge.EVENT_BUS.register(this);
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
				config = new ModConfig(new Gson().fromJson(new JsonReader(new FileReader(CONFIG_FILE)), JsonObject.class));
			} catch (FileNotFoundException err) {
				err.printStackTrace();
			}
		}
		if (config == null) {
			config = new ModConfig();
			saveConfig();
		}
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(new SoundEvent[] { SEEK, HIDE_0, HIDE_1, HIDE_2, HIDE_G });
	}
}