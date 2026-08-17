package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public abstract class Slider extends AbstractSliderButton {
	protected final MainSettingsScreen screen;

	protected Slider(MainSettingsScreen screen, Component component, int x, int y, double volume, boolean active) {
		super(screen.width / 2 - 75 + 80 * x, 60 + 25 * y, x == 0 ? 180 : 150, 20, component, volume);
		this.screen = screen;
		this.active = active;
	}

	public static TranslatableComponent getComponent(String key, double value) {
		return new TranslatableComponent("options.percent_value", new TranslatableComponent(key), (int) (value * 100));
	}
}