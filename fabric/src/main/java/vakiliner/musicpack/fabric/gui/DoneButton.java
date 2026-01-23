package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(EnvType.CLIENT)
public class DoneButton extends AbstractButton {
	private final MainSettingsScreen screen;

	protected DoneButton(MainSettingsScreen screen) {
		super(screen.width / 2 - 100, 175, 200, 20, new TranslatableComponent("gui.done"));
		this.screen = screen;
	}

	public void onPress() {
		this.screen.onClose();
	}
}