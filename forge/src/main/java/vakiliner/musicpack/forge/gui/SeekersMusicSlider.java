package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineAccessor;
import vakiliner.musicpack.forge.mixin.SoundManagerAccessor;

@OnlyIn(Dist.CLIENT)
public class SeekersMusicSlider extends Slider {
	public SeekersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 1, 1, MusicPack.getConfig().seekersMusicVolume(), active);
	}

	public static TranslatableComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.seekersMusic", MusicPack.getConfig().seekersMusicVolume());
	}

	@Override
	protected void applyValue() {
		MusicPack.getConfig().seekersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		ChannelAccess.ChannelHandle handle = accessor.getInstanceToChannel().get(MusicPackSound.seek);
		if (handle != null) {
			handle.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.seek)));
		}
	}

	@Override
	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}