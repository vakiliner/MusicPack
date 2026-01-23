package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.ChannelAccess.ChannelHandle;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import vakiliner.musicpack.fabric.mixin.SoundEngineMixin;
import vakiliner.musicpack.fabric.mixin.SoundManagerMixin;

@Environment(EnvType.CLIENT)
public class SeekersMusicSlider extends Slider {
	public SeekersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), 1, 2, MusicPack.getConfig().seekersMusicVolume(), active);
	}

	public static TranslatableComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.seekersMusic", MusicPack.getConfig().seekersMusicVolume());
	}

	protected void applyValue() {
		MusicPack.getConfig().seekersMusicVolume(this.value);
		SoundEngineMixin soundEngineMixin = (SoundEngineMixin) ((SoundManagerMixin) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		ChannelHandle channelHandle = soundEngineMixin.getInstanceToChannel().get(MusicPackSound.seek);
		if (channelHandle != null) channelHandle.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.seek)));
	}

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}