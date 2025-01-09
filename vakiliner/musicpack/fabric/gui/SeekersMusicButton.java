package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;

@Environment(EnvType.CLIENT)
public class SeekersMusicButton extends BooleanButton {
	public SeekersMusicButton(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 1, 1, active);
	}

	public void onPress() {
		ModConfig config = MusicPack.getConfig();
		config.seekersMusicEnabled(!config.seekersMusicEnabled());
		super.onPress(getComponent());
	}

	public static TranslatableComponent getComponent() {
		return getComponent(MusicPack.getConfig().seekersMusicEnabled(), "vakiliner.musicpack.option.seekersMusic");
	}
}