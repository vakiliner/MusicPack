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

	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineAccessor accessor = (SoundEngineAccessor) ((SoundHandlerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<ISound, ChannelManager.Entry> instanceToChannel = accessor.getInstanceToChannel();
		ChannelManager.Entry channelHandle0 = instanceToChannel.get(MusicPackSound.hideLvl0);
		ChannelManager.Entry channelHandle1 = instanceToChannel.get(MusicPackSound.hideLvl1);
		ChannelManager.Entry channelHandle2 = instanceToChannel.get(MusicPackSound.hideLvl2);
		ChannelManager.Entry channelHandleG = instanceToChannel.get(MusicPackSound.hideGlow);
		if (channelHandle0 != null) channelHandle0.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl0)));
		if (channelHandle1 != null) channelHandle1.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl1)));
		if (channelHandle2 != null) channelHandle2.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideLvl2)));
		if (channelHandleG != null) channelHandleG.execute((channel) -> channel.setVolume(accessor.calculateVolume(MusicPackSound.hideGlow)));
	}

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}