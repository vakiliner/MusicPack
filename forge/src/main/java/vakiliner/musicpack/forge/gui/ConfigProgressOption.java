package vakiliner.musicpack.forge.gui;

import java.util.Objects;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

@OnlyIn(Dist.CLIENT)
public class ConfigProgressOption extends ConfigOption<Double> {
	private final DoubleSupplier getValue;
	private final DoubleConsumer applyValue;

	public ConfigProgressOption(Function<Double, Component> getMessage, DoubleSupplier getValue, DoubleConsumer applyValue) {
		super(getMessage);
		this.getValue = Objects.requireNonNull(getValue);
		this.applyValue = Objects.requireNonNull(applyValue);
	}

	public double getValue() {
		return this.getValue.getAsDouble();
	}

	protected void applyValue(double value) {
		this.applyValue.accept(value);
	}

	public Component getMessage() {
		return this.getMessage(this.getValue());
	}

	@Override
	public AbstractSliderButton createButton(int x, int y, int size) {
		return new AbstractSliderButton(x, y, size, 20, this.getMessage(), this.getValue()) {
			@Override
			protected void applyValue() {
				ConfigProgressOption.this.applyValue(this.value);
			}

			@Override
			protected void updateMessage() {
				this.setMessage(ConfigProgressOption.this.getMessage(this.value));
			}
		};
	}
}