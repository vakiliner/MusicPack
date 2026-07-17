package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineAccessor;
import vakiliner.musicpack.forge.mixin.SoundHandlerAccessor;

@OnlyIn(Dist.CLIENT)
public class SeekersMusicSlider extends Slider {
	public SeekersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 1, 1, MusicPack.getConfig().seekersMusicVolume(), active);
	}

	public static TranslationTextComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.seekersMusic", MusicPack.getConfig().seekersMusicVolume());
	}

	@Override
	protected void applyValue() {
		MusicPack.getConfig().seekersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundHandlerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		ChannelManager.Entry handle = accessor.getInstanceToChannel().get(MusicPackSound.seek);
		if (handle != null) {
			handle.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.seek)));
		}
	}

	@Override
	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}