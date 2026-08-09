package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public class SeekersMusicButton extends BooleanButton {
	public SeekersMusicButton(MainSettingsScreen screen, boolean enabled) {
		super(screen, getComponent(enabled), 1, 0);
	}

	@Override
	public void onPress() {
		SeekersMusicSlider slider = this.screen.seekersMusicSlider;
		super.onPress(getComponent(slider.active = !slider.active));
	}

	public static TranslatableComponent getComponent(boolean enabled) {
		return getComponent(enabled, "vakiliner.musicpack.option.seekersMusic");
	}
}