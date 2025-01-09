package vakiliner.musicpack.fabric.mixin;

import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess.ChannelHandle;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin2 implements SoundEngineMixin {
	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	void play(SoundInstance soundInstance, CallbackInfo callbackInfo) {
		ModConfig config = MusicPack.getConfig();
		if (config.enabled() && soundInstance.getSource() == SoundSource.MUSIC) {
			switch (soundInstance.getLocation().getNamespace()) {
				case "minecraft": {
					if (config.disableDefaultMusic()) callbackInfo.cancel();
					break;
				}
				case "music": {
					callbackInfo.cancel();
					String[] path = soundInstance.getLocation().getPath().split("\\.");
					SoundManager soundManager = Minecraft.getInstance().getSoundManager();
					if (path.length > 0) switch (path[0]) {
						case "seek":
							if (path.length == 1) {
								if (config.seekersMusicEnabled()) soundManager.play(MusicPackSound.seek);
							}
							break;
						case "hide":
							if (path.length > 2) {
								short tick = Short.parseShort(path[2]);
								MusicPackSound sound = MusicPackSound.getSound(path[1]);
								if (sound != null) {
									if (config.hidersMusicEnabled()) {
										if (tick == 1) {
											if (!soundManager.isActive(MusicPackSound.hideLvl0)) soundManager.play(MusicPackSound.hideLvl0.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideLvl1)) soundManager.play(MusicPackSound.hideLvl1.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideLvl2)) soundManager.play(MusicPackSound.hideLvl2.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideGlow)) soundManager.play(MusicPackSound.hideGlow.resetVolume());
										}
										ChannelHandle channelHandle = this.getInstanceToChannel().get(sound);
										if (channelHandle != null) channelHandle.execute((channel) -> channel.setVolume(this.calculateVolume(sound.setVolume(((AbstractSoundInstanceMixin) soundInstance).getVolume()).tick(tick))));
									} else {
										if (soundManager.isActive(MusicPackSound.hideLvl0)) soundManager.stop(MusicPackSound.hideLvl0);
										if (soundManager.isActive(MusicPackSound.hideLvl1)) soundManager.stop(MusicPackSound.hideLvl1);
										if (soundManager.isActive(MusicPackSound.hideLvl2)) soundManager.stop(MusicPackSound.hideLvl2);
										if (soundManager.isActive(MusicPackSound.hideGlow)) soundManager.stop(MusicPackSound.hideGlow);
									}
								}
							}
							break;
					}
					break;
				}
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "stop(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/sounds/SoundSource;)V", cancellable = true)
	void stop(@Nullable ResourceLocation resourceLocation, @Nullable SoundSource soundSource, CallbackInfo callbackInfo) {
		ModConfig config = MusicPack.getConfig();
		if (config.enabled() && resourceLocation != null && soundSource == SoundSource.MUSIC && resourceLocation.getNamespace().equals("music")) {
			callbackInfo.cancel();
			String[] path = resourceLocation.getPath().split("\\.");
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			if (path.length > 0) switch (path[0]) {
				case "seek":
					if (path.length == 1) soundManager.stop(MusicPackSound.seek);
					break;
				case "hide":
					if (path.length > 2) {
						short tick = Short.parseShort(path[2]);
						MusicPackSound sound = MusicPackSound.getSound(path[1]);
						if (soundManager.isActive(sound)) {
							if (config.hidersMusicEnabled()) {
								ChannelHandle channelHandle = this.getInstanceToChannel().get(sound);
								if (channelHandle != null && sound.equalsTick(tick)) new Thread(() -> {
									try {
										Thread.sleep(5);
									} catch (InterruptedException err) {
										err.printStackTrace();
									}
									if (sound.equalsTick(tick)) channelHandle.execute((channel) -> channel.setVolume(this.calculateVolume(sound.resetVolume())));
								}).start();
							} else soundManager.stop(sound);
						}
					}
					break;
			}
		}
	}
}