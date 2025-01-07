package vakiliner.musicpack.fabric;

import java.util.function.Function;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_437;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	public ModMenuIntegration() { }

	public String getModId() {
		return MusicPack.MOD_ID;
	}

	public Function<class_437, ? extends class_437> getConfigScreenFactory() {
		return (parent) -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
	}
}