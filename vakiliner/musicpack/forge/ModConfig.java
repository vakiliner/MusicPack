package vakiliner.musicpack.forge;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private static final ForgeConfigSpec configSpec;
	private static final ModConfig config;
	public final ConfigValue<Boolean> enabled;
	public final ConfigValue<Boolean> hidersMusic;
	public final ConfigValue<Boolean> seekersMusic;
	public final ConfigValue<Boolean> disableDefaultMusic;

	public ModConfig(ForgeConfigSpec.Builder builder) {
		enabled = builder.translation("text.autoconfig.musicpack.option.enabled").define("enabled", true);
		hidersMusic = builder.translation("text.autoconfig.musicpack.option.hidersMusic").define("hiders_music", true);
		seekersMusic = builder.translation("text.autoconfig.musicpack.option.seekersMusic").define("seekers_music", true);
		disableDefaultMusic = builder.translation("text.autoconfig.musicpack.option.disableDefaultMusic").define("disable_default_music", true);
	}

	public static ModConfig get() {
		return config;
	}

	public boolean enabled() {
		return this.enabled.get();
	}

	public boolean hidersMusicEnabled() {
		return this.enabled() && this.hidersMusic.get();
	}

	public boolean seekersMusicEnabled() {
		return this.enabled() && this.seekersMusic.get();
	}

	public boolean disableDefaultMusic() {
		return this.enabled() && this.disableDefaultMusic.get();
	}

	public static ForgeConfigSpec getSpec() {
		return configSpec;
	}

	static {
		Pair<ModConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ModConfig::new);
		configSpec = pair.getRight();
		config = pair.getLeft();
	}
}