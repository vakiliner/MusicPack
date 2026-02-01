package vakiliner.musicpack.forge.mixin;

import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import java.util.Map;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
abstract class SoundEngineMixin {
	public Map<ISound, ChannelManager.Entry> instanceToChannel;

	public abstract float calculateVolume(ISound soundInstance);

	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	void play(ISound soundInstance, CallbackInfo callbackInfo) {
		if (soundInstance.getSource() != SoundCategory.MUSIC) return;
		ResourceLocation resourceLocation = soundInstance.getLocation();
		ModConfig config = MusicPack.getConfig();
		switch (resourceLocation.getNamespace()) {
			case "minecraft": {
				if (config.disableDefaultMusic()) callbackInfo.cancel();
				break;
			}
			case "music": {
				callbackInfo.cancel();
				String[] path = resourceLocation.getPath().split("\\.");
				SoundHandler soundManager = Minecraft.getInstance().getSoundManager();
				switch (path[0]) {
					case "seek": {
						if (path.length != 1) break;
						if (config.seekersMusicEnabled()) soundManager.play(MusicPackSound.seek);
						break;
					}
					case "hide": {
						if (path.length != 3) break;
						if (!config.hidersMusicEnabled()) break;
						final short tick;
						try {
							tick = Short.parseShort(path[2]);
						} catch (NumberFormatException err) {
							break;
						}
						MusicPackSound sound = MusicPackSound.getSound(path[1]);
						if (sound == null) break;
						if (tick == 1) {
							if (!soundManager.isActive(MusicPackSound.hideLvl0)) soundManager.play(MusicPackSound.hideLvl0.resetVolume());
							if (!soundManager.isActive(MusicPackSound.hideLvl1)) soundManager.play(MusicPackSound.hideLvl1.resetVolume());
							if (!soundManager.isActive(MusicPackSound.hideLvl2)) soundManager.play(MusicPackSound.hideLvl2.resetVolume());
							if (!soundManager.isActive(MusicPackSound.hideGlow)) soundManager.play(MusicPackSound.hideGlow.resetVolume());
						}
						ChannelManager.Entry channelHandle = this.instanceToChannel.get(sound);
						if (channelHandle == null) break;
						float f = this.calculateVolume(sound.setVolume(((LocatableSoundAccessor) soundInstance).getVolume()).tick(tick));
						channelHandle.execute((channel) -> channel.setVolume(f));
						break;
					}
				}
				break;
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "stop(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/SoundCategory;)V", cancellable = true)
	void stop(@Nullable ResourceLocation resourceLocation, @Nullable SoundCategory soundSource, CallbackInfo callbackInfo) {
		if (soundSource != SoundCategory.MUSIC) return;
		if (resourceLocation == null || !resourceLocation.getNamespace().equals("music")) return;
		callbackInfo.cancel();
		String[] path = resourceLocation.getPath().split("\\.");
		Minecraft minecraft = Minecraft.getInstance();
		SoundHandler soundManager = minecraft.getSoundManager();
		switch (path[0]) {
			case "seek": {
				if (path.length != 1) break;
				soundManager.stop(MusicPackSound.seek);
				break;
			}
			case "hide": {
				if (path.length != 3) break;
				if (!MusicPack.getConfig().hidersMusicEnabled()) break;
				final short tick;
				try {
					tick = Short.parseShort(path[2]);
				} catch (NumberFormatException err) {
					break;
				}
				MusicPackSound sound = MusicPackSound.getSound(path[1]);
				if (sound == null || !sound.equalsTick(tick) || !soundManager.isActive(sound)) break;
				ChannelManager.Entry channelHandle = this.instanceToChannel.get(sound);
				if (channelHandle == null) break;
				float f = this.calculateVolume(sound.resetVolume());
				channelHandle.execute((channel) -> channel.setVolume(f));
				break;
			}
		}
	}
}