package vakiliner.musicpack.forge.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.ChannelManager.Entry;

@Mixin(SoundEngine.class)
public interface SoundEngineMixin {
	@Accessor("instanceToChannel")
	Map<ISound, Entry> getInstanceToChannel();

	@Invoker("calculateVolume")
	float calculateVolume(ISound soundInstance);
}