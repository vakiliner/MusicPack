package vakiliner.musicpack.fabric.mixin;

import vakiliner.musicpack.fabric.MusicPack;
import java.util.Map;
import net.minecraft.class_1109;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import net.minecraft.class_1144;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_4235.class_4236;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1140.class)
public abstract class SoundEngineMixin {
	private static final MusicPackSound seek = new MusicPackSound(MusicPack.SEEK, false);
	private static final MusicPackSound hideLvl0 = new MusicPackSound(MusicPack.HIDE_0);
	private static final MusicPackSound hideLvl1 = new MusicPackSound(MusicPack.HIDE_1);
	private static final MusicPackSound hideLvl2 = new MusicPackSound(MusicPack.HIDE_2);
	private static final MusicPackSound hideGlow = new MusicPackSound(MusicPack.HIDE_G);

	@Inject(at = @At("HEAD"), method = "play", cancellable = true)
	private void play(class_1113 soundInstance, CallbackInfo callbackInfo) {
		if (soundInstance.method_4774() == class_3419.field_15253) {
			switch (soundInstance.method_4775().method_12836()) {
				case "minecraft": {
					if (MusicPack.getConfig().disableDefaultMusic()) callbackInfo.cancel();
					break;
				}
				case "music": {
					callbackInfo.cancel();
					String[] path = soundInstance.method_4775().method_12832().split("\\.");
					class_1144 soundManager = class_310.method_1551().method_1483();
					if (path.length > 0) switch (path[0]) {
						case "seek":
							if (path.length == 1) {
								if (MusicPack.getConfig().seekersMusicEnabled()) soundManager.method_4873(seek);
							}
							break;
						case "hide":
							if (path.length > 2) {
								short tick = Short.parseShort(path[2]);
								MusicPackSound sound = path[1].equals("level0") ? hideLvl0 : path[1].equals("level1") ? hideLvl1 : path[1].equals("level2") ? hideLvl2 : path[1].equals("level0glow") ? hideGlow : null;
								if (sound != null) {
									if (MusicPack.getConfig().hidersMusicEnabled()) {
										if (tick == 1) {
											if (!soundManager.method_4877(hideLvl0)) soundManager.method_4873(hideLvl0.resetVolume());
											if (!soundManager.method_4877(hideLvl1)) soundManager.method_4873(hideLvl1.resetVolume());
											if (!soundManager.method_4877(hideLvl2)) soundManager.method_4873(hideLvl2.resetVolume());
											if (!soundManager.method_4877(hideGlow)) soundManager.method_4873(hideGlow.resetVolume());
										}
										class_4236 channelHandle = this.getInstanceToChannel().get(sound);
										if (channelHandle != null) channelHandle.method_19735((channel) -> channel.method_19647(this.calculateVolume(sound.setVolume(((SoundMixin) soundInstance).getVolume()).tick(tick))));
									} else {
										if (soundManager.method_4877(hideLvl0)) soundManager.method_4870(hideLvl0);
										if (soundManager.method_4877(hideLvl1)) soundManager.method_4870(hideLvl1);
										if (soundManager.method_4877(hideLvl2)) soundManager.method_4870(hideLvl2);
										if (soundManager.method_4877(hideGlow)) soundManager.method_4870(hideGlow);
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
	private void stop(@Nullable class_2960 resourceLocation, @Nullable class_3419 soundSource, CallbackInfo callbackInfo) {
		if (resourceLocation != null && soundSource == class_3419.field_15253 && resourceLocation.method_12836().equals("music")) {
			callbackInfo.cancel();
			String[] path = resourceLocation.method_12832().split("\\.");
			class_1144 soundManager = class_310.method_1551().method_1483();
			if (path.length > 0) switch (path[0]) {
				case "seek":
					if (path.length == 1) soundManager.method_4870(seek);
					break;
				case "hide":
					if (path.length > 2) {
						short tick = Short.parseShort(path[2]);
						MusicPackSound sound = path[1].equals("level0") ? hideLvl0 : path[1].equals("level1") ? hideLvl1 : path[1].equals("level2") ? hideLvl2 : path[1].equals("level0glow") ? hideGlow : null;
						if (soundManager.method_4877(sound)) {
							if (MusicPack.getConfig().hidersMusicEnabled()) {
								class_4236 channelHandle = this.getInstanceToChannel().get(sound);
								if (channelHandle != null && sound.tick == tick) new Thread(() -> {
									try {
										Thread.sleep(5);
									} catch (InterruptedException err) {
										err.printStackTrace();
									}
									if (sound.tick == tick) channelHandle.method_19735((channel) -> channel.method_19647(this.calculateVolume(sound.resetVolume())));
								}).start();
							} else soundManager.method_4870(sound);
						}
					}
					break;
			}
		}
	}

	private static class MusicPackSound extends class_1109 {
		private short tick = 0;

		private MusicPackSound(class_3414 soundEvent) {
			this(soundEvent, true);
		}

		private MusicPackSound(class_3414 soundEvent, boolean looping) {
			super(soundEvent.method_14833(), class_3419.field_15253, 1, 1, looping, 0, class_1113.class_1114.field_5478, 0, 0, 0, false);
		}

		public MusicPackSound setVolume(float f) {
			this.field_5442 = f;
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
	abstract Map<class_1113, class_4236> getInstanceToChannel();

	@Invoker("calculateVolume")
	abstract float calculateVolume(class_1113 soundInstance);
}