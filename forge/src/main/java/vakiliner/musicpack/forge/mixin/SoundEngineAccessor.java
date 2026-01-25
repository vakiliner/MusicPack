package vakiliner.musicpack.forge.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
	@Accessor("instanceToChannel")
	Map<ISound, ChannelManager.Entry> getInstanceToChannel();

	@Invoker("calculateVolume")
	float calculateVolume(ISound soundInstance);
}