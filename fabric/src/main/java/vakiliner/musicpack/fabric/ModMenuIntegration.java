package vakiliner.musicpack.fabric;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vakiliner.musicpack.fabric.gui.MainSettingsScreen;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public String getModId() {
		return MusicPack.MOD_ID;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (MainSettingsScreen.a()) return (screen) -> null;
		return (parent) -> new MainSettingsScreen(parent);
	}
}