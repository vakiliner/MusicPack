package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public class HidersMusicButton extends BooleanButton {
	public HidersMusicButton(MainSettingsScreen screen, boolean enabled) {
		super(screen, getComponent(enabled), -1, 0);
	}

	@Override
	public void onPress() {
		HidersMusicSlider slider = this.screen.hidersMusicSlider;
		super.onPress(getComponent(slider.active = !slider.active));
	}

	public static TranslatableComponent getComponent(boolean enabled) {
		return getComponent(enabled, "vakiliner.musicpack.option.hidersMusic");
	}
}