package vakiliner.musicpack.forge.gui;

import java.util.Objects;
import java.util.function.Function;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

@OnlyIn(Dist.CLIENT)
public abstract class ConfigOption<T> {
	private final Function<T, Component> getMessage;

	public ConfigOption(Function<T, Component> getMessage) {
		this.getMessage = Objects.requireNonNull(getMessage);
	}

	public Component getMessage(T value) {
		return this.getMessage.apply(value);
	}

	public AbstractWidget createButton(MainSettingsScreen screen, int size, int x, int y) {
		return this.createButton((screen.width - size) / 2 + x, y, size);
	}

	public abstract AbstractWidget createButton(int x, int y, int size);
}