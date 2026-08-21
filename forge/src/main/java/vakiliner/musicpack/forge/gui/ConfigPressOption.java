package vakiliner.musicpack.forge.gui;

import java.util.Objects;
import java.util.function.Function;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@OnlyIn(Dist.CLIENT)
public class ConfigPressOption<T> extends ConfigOption<T> {
	private final Function<T, T> onPress;
	private T value;

	public ConfigPressOption(Function<T, Component> getMessage, Function<T, T> onPress, T value) {
		super(getMessage);
		this.onPress = Objects.requireNonNull(onPress);
		this.value = value;
	}

	public T value() {
		return this.value;
	}

	protected T onPress() {
		return this.onPress(this.value);
	}

	protected T onPress(T value) {
		return this.value = this.onPress.apply(value);
	}

	public Component getMessage() {
		return this.getMessage(this.value);
	}

	@Override
	public Button createButton(MainSettingsScreen screen, int size, int x, int y) {
		return new Button((screen.width + size) / 2 + x, y, size, 20, this.getMessage(), this::onPress);
	}

	protected void onPress(Button button) {
		button.setMessage(this.getMessage(this.onPress()));
	}
}