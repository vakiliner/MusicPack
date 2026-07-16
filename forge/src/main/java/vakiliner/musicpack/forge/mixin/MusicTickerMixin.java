package vakiliner.musicpack.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.audio.MusicTicker;
import vakiliner.musicpack.forge.MusicPack;

@Mixin(MusicTicker.class)
public class MusicTickerMixin {
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	void tick(CallbackInfo callbackInfo) {
		if (MusicPack.getConfig().disableDefaultMusic()) {
			callbackInfo.cancel();
		}
	}
}