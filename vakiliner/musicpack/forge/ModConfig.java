package vakiliner.musicpack.forge;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private static final Pair<ModConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ModConfig::new);
	public final ConfigValue<Boolean> enabled;
	public final ConfigValue<Boolean> hidersMusicEnabled;
	public final ConfigValue<Boolean> seekersMusicEnabled;
	public final ConfigValue<Boolean> disableDefaultMusic;
	public final DoubleValue hidersMusicVolume;
	public final DoubleValue seekersMusicVolume;

	public ModConfig(ForgeConfigSpec.Builder builder) {
		enabled = builder.translation("vakiliner.musicpack.enabled").define("enabled", true);
		hidersMusicEnabled = builder.translation("vakiliner.musicpack.option.hidersMusic").define("hiders_music", true);
		seekersMusicEnabled = builder.translation("vakiliner.musicpack.option.seekersMusic").define("seekers_music", true);
		disableDefaultMusic = builder.translation("vakiliner.musicpack.option.disableDefaultMusic").define("disable_default_music", true);
		hidersMusicVolume = builder.translation("vakiliner.musicpack.option.hidersMusic").defineInRange("hiders_music_volume", 1d, 0, 1);
		seekersMusicVolume = builder.translation("vakiliner.musicpack.option.seekersMusic").defineInRange("seekers_music_volume", 1d, 0, 1);
	}

	public static ModConfig get() {
		return pair.getLeft();
	}

	public static ForgeConfigSpec getSpec() {
		return pair.getRight();
	}

	public boolean enabled() {
		return this.enabled.get();
	}

	public boolean hidersMusicEnabled() {
		return this.hidersMusicEnabled.get();
	}

	public boolean seekersMusicEnabled() {
		return this.seekersMusicEnabled.get();
	}

	public boolean disableDefaultMusic() {
		return this.disableDefaultMusic.get();
	}

	public double hidersMusicVolume() {
		return this.hidersMusicVolume.get();
	}

	public double seekersMusicVolume() {
		return this.seekersMusicVolume.get();
	}

	public void enabled(boolean enabled) {
		this.enabled.set(enabled);
	}

	public void hidersMusicEnabled(boolean hidersMusicEnabled) {
		this.hidersMusicEnabled.set(hidersMusicEnabled);
	}

	public void seekersMusicEnabled(boolean seekersMusicEnabled) {
		this.seekersMusicEnabled.set(seekersMusicEnabled);
	}

	public void disableDefaultMusic(boolean disableDefaultMusic) {
		this.disableDefaultMusic.set(disableDefaultMusic);
	}

	public void hidersMusicVolume(double hidersMusicVolume) {
		this.hidersMusicVolume.set(hidersMusicVolume);
	}

	public void seekersMusicVolume(double seekersMusicVolume) {
		this.seekersMusicVolume.set(seekersMusicVolume);
	}
}