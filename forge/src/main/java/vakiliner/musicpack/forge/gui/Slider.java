package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@OnlyIn(Dist.CLIENT)
public abstract class Slider extends AbstractSlider {
	protected final MainSettingsScreen screen;

	protected Slider(MainSettingsScreen screen, ITextComponent component, int x, int y, double volume, boolean active) {
		super(screen.width / 2 - 75 + 80 * x, 60 + 25 * y, 150, 20, component, volume);
		this.screen = screen;
		this.active = active;
	}

	public static TranslationTextComponent getComponent(String key, double value) {
		return new TranslationTextComponent("options.percent_value", new TranslationTextComponent(key), (int) (value * 100));
	}
}