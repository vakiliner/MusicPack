package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;

@OnlyIn(Dist.CLIENT)
public class DefaultMusicButton extends BooleanButton {
	public DefaultMusicButton(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 0, 3, active);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		config.disableDefaultMusic(!config.disableDefaultMusic());
		super.onPress(getComponent());
	}

	public static TranslationTextComponent getComponent() {
		return new TranslationTextComponent("vakiliner.musicpack.option.defaultMusic", new TranslationTextComponent("vakiliner.musicpack." + (MusicPack.getConfig().disableDefaultMusic() ? "en" : "dis") + "able"));
	}
}