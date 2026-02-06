package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;

@Environment(EnvType.CLIENT)
public class DefaultMusicButton extends BooleanButton {
	public boolean disable;

	public DefaultMusicButton(MainSettingsScreen screen, boolean disable) {
		super(screen, getComponent(disable), 0, 2);
		this.disable = disable;
	}

	public void onPress() {
		super.onPress(getComponent(this.disable = !this.disable));
	}

	public static TranslatableComponent getComponent(boolean disable) {
		return new TranslatableComponent("vakiliner.musicpack.option.defaultMusic", new TranslatableComponent("vakiliner.musicpack." + (disable ? "en" : "dis") + "able"));
	}
}