package vakiliner.musicpack.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;

@Mixin(AbstractSoundInstance.class)
public interface AbstractSoundInstanceAccessor {
	@Accessor("volume")
	float getVolume();
}