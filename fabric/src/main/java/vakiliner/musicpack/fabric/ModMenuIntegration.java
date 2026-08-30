package vakiliner.musicpack.fabric;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vakiliner.musicpack.fabric.gui.MainSettingsScreen;

@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	public static boolean fail = false;

	@Override
	public String getModId() {
		return MusicPack.MOD_ID;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (!fail) try {
			MainSettingsScreen.test();
			return MainSettingsScreen::new;
		} catch (Throwable err) {
			err.printStackTrace();
			fail = true;
		}
		return (screen) -> null;
	}
}
