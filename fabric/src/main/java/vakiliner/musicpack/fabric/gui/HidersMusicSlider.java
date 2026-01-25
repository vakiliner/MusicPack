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
		super(screen, getComponent(), -1, 2, MusicPack.getConfig().hidersMusicVolume(), active);
	}

	public static TranslatableComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.hidersMusic", MusicPack.getConfig().hidersMusicVolume());
	}

	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = accessor.getInstanceToChannel();
		ChannelAccess.ChannelHandle channelHandle0 = instanceToChannel.get(MusicPackSound.hideLvl0);
		ChannelAccess.ChannelHandle channelHandle1 = instanceToChannel.get(MusicPackSound.hideLvl1);
		ChannelAccess.ChannelHandle channelHandle2 = instanceToChannel.get(MusicPackSound.hideLvl2);
		ChannelAccess.ChannelHandle channelHandleG = instanceToChannel.get(MusicPackSound.hideGlow);
		if (channelHandle0 != null) channelHandle0.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl0)));
		if (channelHandle1 != null) channelHandle1.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl1)));
		if (channelHandle2 != null) channelHandle2.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl2)));
		if (channelHandleG != null) channelHandleG.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideGlow)));
	}

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}