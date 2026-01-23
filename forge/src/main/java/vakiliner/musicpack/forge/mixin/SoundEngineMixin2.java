package vakiliner.musicpack.forge.mixin;

import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ChannelManager.Entry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin2 implements SoundEngineMixin {
	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	void play(ISound soundInstance, CallbackInfo callbackInfo) {
		ModConfig config = MusicPack.getConfig();
		if (config.enabled() && soundInstance.getSource() == SoundCategory.MUSIC) {
			switch (soundInstance.getLocation().getNamespace()) {
				case "minecraft": {
					if (config.disableDefaultMusic()) callbackInfo.cancel();
					break;
				}
				case "music": {
					callbackInfo.cancel();
					String[] path = soundInstance.getLocation().getPath().split("\\.");
					SoundHandler soundManager = Minecraft.getInstance().getSoundManager();
					if (path.length > 0) switch (path[0]) {
						case "seek":
							if (path.length == 1) {
								if (config.seekersMusicEnabled()) soundManager.play(MusicPackSound.seek);
							}
							break;
						case "hide":
							if (path.length > 2) {
								short tick = Short.parseShort(path[2]);
								MusicPackSound sound = path[1].equals("level0") ? MusicPackSound.hideLvl0 : path[1].equals("level1") ? MusicPackSound.hideLvl1 : path[1].equals("level2") ? MusicPackSound.hideLvl2 : path[1].equals("level0glow") ? MusicPackSound.hideGlow : null;
								if (sound != null) {
									if (config.hidersMusicEnabled()) {
										if (tick == 1) {
											if (!soundManager.isActive(MusicPackSound.hideLvl0)) soundManager.play(MusicPackSound.hideLvl0.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideLvl1)) soundManager.play(MusicPackSound.hideLvl1.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideLvl2)) soundManager.play(MusicPackSound.hideLvl2.resetVolume());
											if (!soundManager.isActive(MusicPackSound.hideGlow)) soundManager.play(MusicPackSound.hideGlow.resetVolume());
										}
										Entry channelHandle = this.getInstanceToChannel().get(sound);
										if (channelHandle != null) channelHandle.execute((channel) -> channel.setVolume(this.calculateVolume(sound.setVolume(((LocatableSoundMixin) soundInstance).getVolume()).tick(tick))));
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

	@Inject(at = @At(value = "HEAD"), method = "stop(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/SoundCategory;)V", cancellable = true)
	void stop(@Nullable ResourceLocation resourceLocation, @Nullable SoundCategory soundSource, CallbackInfo callbackInfo) {
		ModConfig config = MusicPack.getConfig();
		if (config.enabled() && resourceLocation != null && soundSource == SoundCategory.MUSIC && resourceLocation.getNamespace().equals("music")) {
			callbackInfo.cancel();
			String[] path = resourceLocation.getPath().split("\\.");
			SoundHandler soundManager = Minecraft.getInstance().getSoundManager();
			if (path.length > 0) switch (path[0]) {
				case "seek":
					if (path.length == 1) soundManager.stop(MusicPackSound.seek);
					break;
				case "hide":
					if (path.length > 2) {
						short tick = Short.parseShort(path[2]);
						MusicPackSound sound = path[1].equals("level0") ? MusicPackSound.hideLvl0 : path[1].equals("level1") ? MusicPackSound.hideLvl1 : path[1].equals("level2") ? MusicPackSound.hideLvl2 : path[1].equals("level0glow") ? MusicPackSound.hideGlow : null;
						if (sound != null && soundManager.isActive(sound)) {
							if (config.hidersMusicEnabled()) {
								Entry channelHandle = this.getInstanceToChannel().get(sound);
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