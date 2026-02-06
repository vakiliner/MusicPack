package vakiliner.musicpack.fabric.mixin;

import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
abstract class SoundEngineMixin {
	@Shadow
	public Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

	@Shadow
	public abstract float calculateVolume(SoundInstance soundInstance);

	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	void play(SoundInstance soundInstance, CallbackInfo callbackInfo) {
		if (soundInstance.getSource() != SoundSource.MUSIC) return;
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
				SoundManager soundManager = Minecraft.getInstance().getSoundManager();
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
						ChannelAccess.ChannelHandle channelHandle = this.instanceToChannel.get(sound);
						if (channelHandle == null) break;
						float f = this.calculateVolume(sound.setVolume(((AbstractSoundInstanceAccessor) soundInstance).getVolume()).tick(tick));
						channelHandle.execute((channel) -> channel.setVolume(f));
						break;
					}
				}
				break;
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "stop(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/sounds/SoundSource;)V", cancellable = true)
	void stop(@Nullable ResourceLocation resourceLocation, @Nullable SoundSource soundSource, CallbackInfo callbackInfo) {
		if (soundSource != SoundSource.MUSIC) return;
		if (resourceLocation == null || !resourceLocation.getNamespace().equals("music")) return;
		callbackInfo.cancel();
		String[] path = resourceLocation.getPath().split("\\.");
		Minecraft minecraft = Minecraft.getInstance();
		SoundManager soundManager = minecraft.getSoundManager();
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
				ChannelAccess.ChannelHandle channelHandle = this.instanceToChannel.get(sound);
				if (channelHandle == null) break;
				float f = this.calculateVolume(sound.resetVolume());
				channelHandle.execute((channel) -> channel.setVolume(f));
				break;
			}
		}
	}
}