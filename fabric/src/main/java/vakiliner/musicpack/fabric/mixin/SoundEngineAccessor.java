package vakiliner.musicpack.fabric.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
	@Accessor("instanceToChannel")
	Map<SoundInstance, ChannelAccess.ChannelHandle> getInstanceToChannel();

	@Invoker("calculateVolume")
	float calculateVolume(SoundInstance soundInstance);
}