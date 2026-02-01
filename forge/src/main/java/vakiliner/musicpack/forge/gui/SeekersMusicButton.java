package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;

@OnlyIn(Dist.CLIENT)
public class SeekersMusicButton extends BooleanButton {
	public SeekersMusicButton(MainSettingsScreen screen) {
		super(screen, getComponent(), 1, 1);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		boolean enabled = !config.seekersMusicEnabled();
		config.seekersMusicEnabled(enabled);
		this.screen.seekersMusicSlider.active = enabled;
		super.onPress(getComponent());
	}

	public static TranslationTextComponent getComponent() {
		return getComponent(MusicPack.getConfig().seekersMusicEnabled(), "vakiliner.musicpack.option.seekersMusic");
	}
}