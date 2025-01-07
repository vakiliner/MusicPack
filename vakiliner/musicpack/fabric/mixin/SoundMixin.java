package vakiliner.musicpack.fabric.mixin;

import net.minecraft.class_1102;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_1102.class)
public interface SoundMixin {
	@Accessor("volume")
	float getVolume();
}