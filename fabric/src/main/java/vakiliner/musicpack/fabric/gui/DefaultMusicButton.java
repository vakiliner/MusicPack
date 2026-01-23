package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;

@Environment(EnvType.CLIENT)
public class DefaultMusicButton extends BooleanButton {
	public DefaultMusicButton(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 0, 3, active);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		config.disableDefaultMusic(!config.disableDefaultMusic());
		super.onPress(getComponent());
	}

	public static TranslatableComponent getComponent() {
		return new TranslatableComponent("vakiliner.musicpack.option.defaultMusic", new TranslatableComponent("vakiliner.musicpack." + (MusicPack.getConfig().disableDefaultMusic() ? "en" : "dis") + "able"));
	}
}