package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(EnvType.CLIENT)
public class HidersMusicButton extends BooleanButton {
	public HidersMusicButton(MainSettingsScreen screen, boolean enabled) {
		super(screen, getComponent(enabled), -1, 0);
	}

	public void onPress() {
		HidersMusicSlider slider = this.screen.hidersMusicSlider;
		super.onPress(getComponent(slider.active = !slider.active));
	}

	public static TranslatableComponent getComponent(boolean enabled) {
		return getComponent(enabled, "vakiliner.musicpack.option.hidersMusic");
	}
}