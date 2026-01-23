package vakiliner.musicpack.forge;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import vakiliner.musicpack.forge.gui.MainSettingsScreen;

@OnlyIn(Dist.CLIENT)
public class ModMenuIntegration implements Supplier<BiFunction<Minecraft, Screen, Screen>> {
	public BiFunction<Minecraft, Screen, Screen> get() {
		if (MainSettingsScreen.a()) return (minecraft, parent) -> null;
		return (minecraft, parent) -> new MainSettingsScreen(parent);
	}
}