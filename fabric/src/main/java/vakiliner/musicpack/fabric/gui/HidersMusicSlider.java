package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess.ChannelHandle;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import vakiliner.musicpack.fabric.mixin.SoundEngineMixin;
import vakiliner.musicpack.fabric.mixin.SoundManagerMixin;

@Environment(EnvType.CLIENT)
public class HidersMusicSlider extends Slider {
	public HidersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), -1, 2, MusicPack.getConfig().hidersMusicVolume(), active);
	}

	public static TranslatableComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.hidersMusic", MusicPack.getConfig().hidersMusicVolume());
	}

	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineMixin soundEngineMixin = (SoundEngineMixin) ((SoundManagerMixin) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<SoundInstance, ChannelHandle> instanceToChannel = soundEngineMixin.getInstanceToChannel();
		ChannelHandle channelHandle0 = instanceToChannel.get(MusicPackSound.hideLvl0);
		ChannelHandle channelHandle1 = instanceToChannel.get(MusicPackSound.hideLvl1);
		ChannelHandle channelHandle2 = instanceToChannel.get(MusicPackSound.hideLvl2);
		ChannelHandle channelHandleG = instanceToChannel.get(MusicPackSound.hideGlow);
		if (channelHandle0 != null) channelHandle0.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl0)));
		if (channelHandle1 != null) channelHandle1.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl1)));
		if (channelHandle2 != null) channelHandle2.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl2)));
		if (channelHandleG != null) channelHandleG.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideGlow)));
	}

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}