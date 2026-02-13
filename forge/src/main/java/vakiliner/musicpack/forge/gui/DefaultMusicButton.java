package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.TranslationTextComponent;

@OnlyIn(Dist.CLIENT)
public class DefaultMusicButton extends BooleanButton {
	public boolean disable;

	public DefaultMusicButton(MainSettingsScreen screen, boolean disable) {
		super(screen, getComponent(disable), 0, 2);
		this.disable = disable;
	}

	public void onPress() {
		super.onPress(getComponent(this.disable = !this.disable));
	}

	public static TranslationTextComponent getComponent(boolean disable) {
		return new TranslationTextComponent("vakiliner.musicpack.option.defaultMusic", new TranslationTextComponent("vakiliner.musicpack." + (disable ? "en" : "dis") + "able"));
	}
}