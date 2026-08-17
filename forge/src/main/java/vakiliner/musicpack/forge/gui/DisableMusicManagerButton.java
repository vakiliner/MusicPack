package vakiliner.musicpack.forge.gui;

import java.util.Objects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.base.ModConfig.DisableMusicManager;

@OnlyIn(Dist.CLIENT)
public class DisableMusicManagerButton extends BooleanButton {
	private DisableMusicManager value;

	public DisableMusicManagerButton(MainSettingsScreen screen, DisableMusicManager value) {
		super(screen, getComponent(value), 0, 2);
		this.value(value);
	}

	public DisableMusicManager value() {
		return this.value;
	}

	public void value(DisableMusicManager value) {
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public void onPress() {
		int ordinal = this.value.ordinal() + 1;
		DisableMusicManager[] values = DisableMusicManager.values();
		super.onPress(getComponent(this.value = values[ordinal >= values.length ? 0 : ordinal]));
	}

	public static TranslatableComponent getComponent(DisableMusicManager value) {
		return new TranslatableComponent("vakiliner.musicpack.option.disableMusicManager", new TranslatableComponent("vakiliner.musicpack.option.disableMusicManager." + value.name().toLowerCase()));
	}
}