package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;

@OnlyIn(Dist.CLIENT)
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

	public static TranslationTextComponent getComponent() {
		return new TranslationTextComponent("vakiliner.musicpack." + (MusicPack.getConfig().enabled() ? "en" : "dis") + "abled");
	}
}