package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@OnlyIn(Dist.CLIENT)
public abstract class BooleanButton extends AbstractButton {
	protected final MainSettingsScreen screen;

	protected BooleanButton(MainSettingsScreen screen, ITextComponent component, int x, int y) {
		super(screen.width / 2 - 75 + 80 * x, 60 + 25 * y, 150, 20, component);
		this.screen = screen;
	}

	public static TranslationTextComponent getComponent(boolean enabled, String key) {
		return new TranslationTextComponent("options." + (enabled ? "on" : "off") + ".composed", new TranslationTextComponent(key));
	}

	public void onPress(TranslationTextComponent component) {
		this.setMessage(component);
	}
}