package vakiliner.musicpack.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.audio.LocatableSound;

@Mixin(LocatableSound.class)
public interface LocatableSoundMixin {
	@Accessor("volume")
	float getVolume();
}