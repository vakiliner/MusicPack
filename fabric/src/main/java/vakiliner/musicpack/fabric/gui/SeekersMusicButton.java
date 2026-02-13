package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(EnvType.CLIENT)
public class SeekersMusicButton extends BooleanButton {
	public SeekersMusicButton(MainSettingsScreen screen, boolean enabled) {
		super(screen, getComponent(enabled), 1, 0);
	}

	public void onPress() {
		SeekersMusicSlider slider = this.screen.seekersMusicSlider;
		super.onPress(getComponent(slider.active = !slider.active));
	}

	public static TranslatableComponent getComponent(boolean enabled) {
		return getComponent(enabled, "vakiliner.musicpack.option.seekersMusic");
	}
}