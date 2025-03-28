package vakiliner.musicpack.fabric;

import java.util.function.Function;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import vakiliner.musicpack.fabric.gui.MainSettingsScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	public String getModId() {
		return MusicPack.MOD_ID;
	}

	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		if (MainSettingsScreen.a()) return null;
		return (parent) -> new MainSettingsScreen(parent);
	}

	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		Function<Screen, ? extends Screen> function = this.getConfigScreenFactory();
		return (screen) -> function == null ? null : function.apply(screen);
	}
}