package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public abstract class BooleanButton extends AbstractButton {
	protected final MainSettingsScreen screen;

	protected BooleanButton(MainSettingsScreen screen, Component component, int x, int y) {
		super(screen.width / 2 - 75 + 80 * x, 60 + 25 * y, 150, 20, component);
		this.screen = screen;
	}

	public static TranslatableComponent getComponent(boolean enabled, String key) {
		return new TranslatableComponent("options." + (enabled ? "on" : "off") + ".composed", new TranslatableComponent(key));
	}

	public void onPress(TranslatableComponent component) {
		this.setMessage(component);
	}
}