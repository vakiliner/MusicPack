package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import vakiliner.musicpack.fabric.mixin.SoundEngineAccessor;
import vakiliner.musicpack.fabric.mixin.SoundManagerAccessor;

@Environment(EnvType.CLIENT)
public class HidersMusicSlider extends Slider {
	public HidersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), -1, 1, MusicPack.getConfig().hidersMusicVolume(), active);
	}

	public static TranslatableComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.hidersMusic", MusicPack.getConfig().hidersMusicVolume());
	}

	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = accessor.getInstanceToChannel();
		MusicPackSound[] sounds = { MusicPackSound.hideLvl0, MusicPackSound.hideLvl1, MusicPackSound.hideLvl2, MusicPackSound.hideGlow };
		ChannelAccess.ChannelHandle[] handles = new ChannelAccess.ChannelHandle[sounds.length];
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

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}