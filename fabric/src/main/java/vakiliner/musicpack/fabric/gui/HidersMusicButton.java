package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;

@Environment(EnvType.CLIENT)
public class HidersMusicButton extends BooleanButton {
	public HidersMusicButton(MainSettingsScreen screen) {
		super(screen, getComponent(), -1, 1);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		boolean enabled = !config.hidersMusicEnabled();
		config.hidersMusicEnabled(enabled);
		this.screen.hidersMusicSlider.active = enabled;
		super.onPress(getComponent());
	}

	public static TranslatableComponent getComponent() {
		return getComponent(MusicPack.getConfig().hidersMusicEnabled(), "vakiliner.musicpack.option.hidersMusic");
	}
}