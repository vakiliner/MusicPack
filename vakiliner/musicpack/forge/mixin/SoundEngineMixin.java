package vakiliner.musicpack.forge.mixin;

import vakiliner.musicpack.forge.MusicPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ChannelManager.Entry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import java.util.Map;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
	private static final MusicPackSound seek = new MusicPackSound(MusicPack.SEEK, false);
	private static final MusicPackSound hideLvl0 = new MusicPackSound(MusicPack.HIDE_0);
	private static final MusicPackSound hideLvl1 = new MusicPackSound(MusicPack.HIDE_1);
	private static final MusicPackSound hideLvl2 = new MusicPackSound(MusicPack.HIDE_2);
	private static final MusicPackSound hideGlow = new MusicPackSound(MusicPack.HIDE_G);

	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	private void play(ISound soundInstance, CallbackInfo callbackInfo) {
		if (soundInstance.getSource() == SoundCategory.MUSIC) {
			switch (soundInstance.getLocation().getNamespace()) {
				case "minecraft": {
					if (MusicPack.getConfig().disableDefaultMusic()) callbackInfo.cancel();
					break;
				}
				case "music": {
					callbackInfo.cancel();
					String[] path = soundInstance.getLocation().getPath().split("\\.");
					SoundHandler soundManager = Minecraft.getInstance().getSoundManager();
					if (path.length > 0) switch (path[0]) {
						case "seek":
							if (path.length == 1) {
								if (MusicPack.getConfig().seekersMusicEnabled()) soundManager.play(seek);
							}
							break;
						case "hide":
							if (path.length > 2) {
								short tick = Short.parseShort(path[2]);
								MusicPackSound sound = path[1].equals("level0") ? hideLvl0 : path[1].equals("level1") ? hideLvl1 : path[1].equals("level2") ? hideLvl2 : path[1].equals("level0glow") ? hideGlow : null;
								if (sound != null) {
									if (MusicPack.getConfig().hidersMusicEnabled()) {
										if (tick == 1) {
											if (!soundManager.isActive(hideLvl0)) soundManager.play(hideLvl0.resetVolume());
											if (!soundManager.isActive(hideLvl1)) soundManager.play(hideLvl1.resetVolume());
											if (!soundManager.isActive(hideLvl2)) soundManager.play(hideLvl2.resetVolume());
											if (!soundManager.isActive(hideGlow)) soundManager.play(hideGlow.resetVolume());
										}
										Entry channelHandle = this.getInstanceToChannel().get(sound);
										if (channelHandle != null) channelHandle.execute((channel) -> channel.setVolume(this.calculateVolume(sound.setVolume(((LocatableSoundMixin) soundInstance).getVolume()).tick(tick))));
									} else {
										if (soundManager.isActive(hideLvl0)) soundManager.stop(hideLvl0);
										if (soundManager.isActive(hideLvl1)) soundManager.stop(hideLvl1);
										if (soundManager.isActive(hideLvl2)) soundManager.stop(hideLvl2);
										if (soundManager.isActive(hideGlow)) soundManager.stop(hideGlow);
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
	private void stop(@Nullable ResourceLocation resourceLocation, @Nullable SoundCategory soundSource, CallbackInfo callbackInfo) {
		if (resourceLocation != null && soundSource == SoundCategory.MUSIC && resourceLocation.getNamespace().equals("music")) {
			callbackInfo.cancel();
			String[] path = resourceLocation.getPath().split("\\.");
			SoundHandler soundManager = Minecraft.getInstance().getSoundManager();
			if (path.length > 0) switch (path[0]) {
				case "seek":
					if (path.length == 1) soundManager.stop(seek);
					break;
				case "hide":
					if (path.length > 2) {
						short tick = Short.parseShort(path[2]);
						MusicPackSound sound = path[1].equals("level0") ? hideLvl0 : path[1].equals("level1") ? hideLvl1 : path[1].equals("level2") ? hideLvl2 : path[1].equals("level0glow") ? hideGlow : null;
						if (sound != null && soundManager.isActive(sound)) {
							if (MusicPack.getConfig().hidersMusicEnabled()) {
								Entry channelHandle = this.getInstanceToChannel().get(sound);
								if (channelHandle != null && sound.tick == tick) new Thread(() -> {
									try {
										Thread.sleep(5);
									} catch (InterruptedException err) {
										err.printStackTrace();
									}
									if (sound.tick == tick) channelHandle.execute((channel) -> channel.setVolume(this.calculateVolume(sound.resetVolume())));
								}).start();
							} else soundManager.stop(sound);
						}
					}
					break;
			}
		}
	}

	private static class MusicPackSound extends SimpleSound {
		private short tick = 0;

		private MusicPackSound(SoundEvent soundEvent) {
			this(soundEvent, true);
		}

		private MusicPackSound(SoundEvent soundEvent, boolean looping) {
			super(soundEvent.getLocation(), SoundCategory.MUSIC, 1, 1, looping, 0, ISound.AttenuationType.NONE, 0, 0, 0, false);
		}

		public MusicPackSound setVolume(float f) {
			this.volume = f;
			return this;
		}

		public MusicPackSound tick(short tick) {
			this.tick = tick;
			return this;
		}

		public MusicPackSound resetVolume() {
			return this.setVolume(0.0001f);
		}
	}

	@Accessor("instanceToChannel")
	abstract Map<ISound, Entry> getInstanceToChannel();

	@Invoker("calculateVolume")
	abstract float calculateVolume(ISound soundInstance);
}