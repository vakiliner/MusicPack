package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineAccessor;
import vakiliner.musicpack.forge.mixin.SoundHandlerAccessor;

@OnlyIn(Dist.CLIENT)
public class HidersMusicSlider extends Slider {
	public HidersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), -1, 1, MusicPack.getConfig().hidersMusicVolume(), active);
	}

	public static TranslationTextComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.hidersMusic", MusicPack.getConfig().hidersMusicVolume());
	}

	@Override
	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundHandlerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<ISound, ChannelManager.Entry> instanceToChannel = accessor.getInstanceToChannel();
		MusicPackSound[] sounds = { MusicPackSound.hideLvl0, MusicPackSound.hideLvl1, MusicPackSound.hideLvl2, MusicPackSound.hideGlow };
		ChannelManager.Entry[] handles = new ChannelManager.Entry[sounds.length];
		for (int i = 0; i < sounds.length; i++) handles[i] = instanceToChannel.get(sounds[i]);
		accessor.getExecutor().execute(() -> {
			for (int i = 0; i < sounds.length; i++) {
				MusicPackSound sound = sounds[i];
				handles[i].execute((channel) -> {
					channel.setVolume(accessor.calculateVolume(sound));
				});
			}
		});
	}

	@Override
	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}