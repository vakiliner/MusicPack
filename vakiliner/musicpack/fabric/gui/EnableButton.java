package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;

@Environment(EnvType.CLIENT)
public class EnableButton extends BooleanButton {
	public EnableButton(MainSettingsScreen screen) {
		super(screen, getComponent(), 0, 0, true);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		boolean enabled = !config.enabled();
		config.enabled(enabled);
		super.onPress(getComponent());
		this.screen.hidersMusicButton.active = enabled;
		this.screen.seekersMusicButton.active = enabled;
		this.screen.defaultMusicButton.active = enabled;
		this.screen.hidersMusicSlider.active = enabled && config.hidersMusicEnabled();
		this.screen.seekersMusicSlider.active = enabled && config.seekersMusicEnabled();
	}

	public static TranslatableComponent getComponent() {
		return new TranslatableComponent("vakiliner.musicpack." + (MusicPack.getConfig().enabled() ? "en" : "dis") + "abled");
	}
}