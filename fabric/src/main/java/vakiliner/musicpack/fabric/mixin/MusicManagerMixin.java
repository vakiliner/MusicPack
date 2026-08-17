package vakiliner.musicpack.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Musics;
import vakiliner.musicpack.fabric.MusicPack;

@Mixin(MusicManager.class)
abstract class MusicManagerMixin {
	@Shadow
	private Minecraft minecraft;

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	void tick(CallbackInfo callbackInfo) {
		switch (MusicPack.getConfig().disableMusicManager()) {
			case ONLY_IN_GAME:
				if (this.minecraft.getSituationalMusic() == Musics.MENU) break;
			case EVERYWHERE:
				callbackInfo.cancel();
				break;
			default: break;
		}
	}
}