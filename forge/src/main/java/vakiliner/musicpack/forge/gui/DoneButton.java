package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public class DoneButton extends AbstractButton {
	private final MainSettingsScreen screen;

	protected DoneButton(MainSettingsScreen screen) {
		super(screen.width / 2 - 100, 175, 200, 20, new TranslatableComponent("gui.done"));
		this.screen = screen;
	}

	@Override
	public void onPress() {
		this.screen.onClose();
	}
}