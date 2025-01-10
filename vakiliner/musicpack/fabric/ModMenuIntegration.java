package vakiliner.musicpack.fabric;

import java.util.function.Function;
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
		return (parent) -> new MainSettingsScreen(parent);
	}
}