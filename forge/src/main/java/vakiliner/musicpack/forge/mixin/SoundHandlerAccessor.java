package vakiliner.musicpack.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;

@Mixin(SoundHandler.class)
public interface SoundHandlerAccessor {
	@Accessor("soundEngine")
	SoundEngine getSoundEngine();
}