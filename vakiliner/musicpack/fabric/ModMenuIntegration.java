package vakiliner.musicpack.fabric;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vakiliner.musicpack.fabric.gui.MainSettingsScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	public String getModId() {
		return MusicPack.MOD_ID;
	}

	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (MainSettingsScreen.a()) return (screen) -> null;
		return (parent) -> new MainSettingsScreen(parent);
	}
}