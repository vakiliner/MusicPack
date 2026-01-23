package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ChannelManager.Entry;
import net.minecraft.util.text.TranslationTextComponent;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineMixin;
import vakiliner.musicpack.forge.mixin.SoundHandlerMixin;

@OnlyIn(Dist.CLIENT)
public class HidersMusicSlider extends Slider {
	public HidersMusicSlider(MainSettingsScreen screen, boolean active) {
		super(screen, getComponent(), -1, 2, MusicPack.getConfig().hidersMusicVolume(), active);
	}

	public static TranslationTextComponent getComponent() {
		return getComponent("vakiliner.musicpack.option.hidersMusic", MusicPack.getConfig().hidersMusicVolume());
	}

	protected void applyValue() {
		MusicPack.getConfig().hidersMusicVolume(this.value);
		SoundEngineMixin soundEngineMixin = (SoundEngineMixin) ((SoundHandlerMixin) Minecraft.getInstance().getSoundManager()).getSoundEngine();
		Map<ISound, Entry> instanceToChannel = soundEngineMixin.getInstanceToChannel();
		Entry channelHandle0 = instanceToChannel.get(MusicPackSound.hideLvl0);
		Entry channelHandle1 = instanceToChannel.get(MusicPackSound.hideLvl1);
		Entry channelHandle2 = instanceToChannel.get(MusicPackSound.hideLvl2);
		Entry channelHandleG = instanceToChannel.get(MusicPackSound.hideGlow);
		if (channelHandle0 != null) channelHandle0.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl0)));
		if (channelHandle1 != null) channelHandle1.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl1)));
		if (channelHandle2 != null) channelHandle2.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideLvl2)));
		if (channelHandleG != null) channelHandleG.execute((channel) -> channel.setVolume(soundEngineMixin.calculateVolume(MusicPackSound.hideGlow)));
	}

	protected void updateMessage() {
		this.setMessage(getComponent());
	}
}