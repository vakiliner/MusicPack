package vakiliner.musicpack.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import vakiliner.musicpack.fabric.MusicPack;

@Mixin(MusicManager.class)
abstract class MusicManagerMixin {
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	void tick(CallbackInfo callbackInfo, Music music) {
		switch (MusicPack.getConfig().disableMusicManager()) {
			case ONLY_IN_GAME:
				if (music == Musics.MENU) break;
			case EVERYWHERE:
				callbackInfo.cancel();
				break;
			default: break;
		}
	}
}