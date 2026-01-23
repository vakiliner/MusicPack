package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;

@OnlyIn(Dist.CLIENT)
public class HidersMusicButton extends BooleanButton {
	public HidersMusicButton(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), -1, 1, active);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		boolean enabled = !config.hidersMusicEnabled();
		config.hidersMusicEnabled(enabled);
		this.screen.hidersMusicSlider.active = enabled;
		super.onPress(getComponent());
	}

	public static TranslationTextComponent getComponent() {
		return getComponent(MusicPack.getConfig().hidersMusicEnabled(), "vakiliner.musicpack.option.hidersMusic");
	}
}