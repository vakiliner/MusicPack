package vakiliner.musicpack.fabric.gui;

import java.util.Objects;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
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
	public Button createButton(int x, int y, int size) {
		return new Button(x, y, size, 20, this.getMessage(), this::onPress);
	}

	protected void onPress(Button button) {
		button.setMessage(this.getMessage(this.onPress()));
	}
}