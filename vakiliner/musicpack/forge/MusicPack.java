package vakiliner.musicpack.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@Mod(MusicPack.MOD_ID)
@EventBusSubscriber(modid = MusicPack.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class MusicPack extends vakiliner.musicpack.base.MusicPack {
	public static final SoundEvent SEEK = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "seek"));
	public static final SoundEvent HIDE_0 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.0"));
	public static final SoundEvent HIDE_1 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.1"));
	public static final SoundEvent HIDE_2 = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.2"));
	public static final SoundEvent HIDE_G = new SoundEvent(new ResourceLocation(MusicPack.MOD_ID, "hide.g"));

	public MusicPack() {
		ModLoadingContext.get().registerConfig(Type.CLIENT, ModConfig.getSpec(), MusicPack.MOD_ID + ".toml");
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	public static vakiliner.musicpack.base.ModConfig getConfig() {
		return ModConfig.get();
	}

	public static void saveConfig() {
		ModConfig.getSpec().save();
	}

	private void setup(FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		LOGGER.info("Музыкальный пакет активирован");
	}

	@SubscribeEvent
	public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(new SoundEvent[] { SEEK, HIDE_0, HIDE_1, HIDE_2, HIDE_G });
	}
}