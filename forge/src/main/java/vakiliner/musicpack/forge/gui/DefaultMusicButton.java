package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.TranslatableComponent;

@OnlyIn(Dist.CLIENT)
public class DefaultMusicButton extends BooleanButton {
	public boolean disable;

	public DefaultMusicButton(MainSettingsScreen screen, boolean disable) {
		super(screen, getComponent(disable), 0, 2);
		this.disable = disable;
	}

	@Override
	public void onPress() {
		super.onPress(getComponent(this.disable = !this.disable));
	}

	public static TranslatableComponent getComponent(boolean disable) {
		return new TranslatableComponent("vakiliner.musicpack.option.defaultMusic", new TranslatableComponent("vakiliner.musicpack." + (disable ? "en" : "dis") + "able"));
	}
}