package vakiliner.musicpack.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class MusicPack extends vakiliner.musicpack.base.MusicPack implements ClientModInitializer {
	public static final class_3414 SEEK;
	public static final class_3414 HIDE_0;
	public static final class_3414 HIDE_1;
	public static final class_3414 HIDE_2;
	public static final class_3414 HIDE_G;
	private static final ModConfig config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();

	public void onInitializeClient() {
		LOGGER.info("Музыкальный пакет активирован");
	}

	public static vakiliner.musicpack.base.ModConfig getConfig() {
		return config;
	}

	static {
		class_2960 resourceLocationSeek = new class_2960(MusicPack.MOD_ID, "seek");
		class_2960 resourceLocationHide0 = new class_2960(MusicPack.MOD_ID, "hide.0");
		class_2960 resourceLocationHide1 = new class_2960(MusicPack.MOD_ID, "hide.1");
		class_2960 resourceLocationHide2 = new class_2960(MusicPack.MOD_ID, "hide.2");
		class_2960 resourceLocationHideG = new class_2960(MusicPack.MOD_ID, "hide.g");
		SEEK = class_2378.method_10230(class_2378.field_11156, resourceLocationSeek, new class_3414(resourceLocationSeek));
		HIDE_0 = class_2378.method_10230(class_2378.field_11156, resourceLocationHide0, new class_3414(resourceLocationHide0));
		HIDE_1 = class_2378.method_10230(class_2378.field_11156, resourceLocationHide1, new class_3414(resourceLocationHide1));
		HIDE_2 = class_2378.method_10230(class_2378.field_11156, resourceLocationHide2, new class_3414(resourceLocationHide2));
		HIDE_G = class_2378.method_10230(class_2378.field_11156, resourceLocationHideG, new class_3414(resourceLocationHideG));
	}
}