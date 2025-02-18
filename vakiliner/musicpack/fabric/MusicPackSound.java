package vakiliner.musicpack.fabric;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import vakiliner.musicpack.base.ModConfig;

public class MusicPackSound extends SimpleSoundInstance {
	public static final MusicPackSound seek = new MusicPackSound(MusicPack.SEEK, false);
	public static final MusicPackSound hideLvl0 = new MusicPackSound(MusicPack.HIDE_0);
	public static final MusicPackSound hideLvl1 = new MusicPackSound(MusicPack.HIDE_1);
	public static final MusicPackSound hideLvl2 = new MusicPackSound(MusicPack.HIDE_2);
	public static final MusicPackSound hideGlow = new MusicPackSound(MusicPack.HIDE_G);
	private short tick = 0;

	private MusicPackSound(SoundEvent soundEvent) {
		this(soundEvent, true);
	}

	private MusicPackSound(SoundEvent soundEvent, boolean looping) {
		super(soundEvent.getLocation(), SoundSource.MUSIC, 1, 1, looping, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, false);
	}

	public float getVolume() {
		ModConfig config = MusicPack.getConfig();
		return super.getVolume() * (float) (this == seek ? config.seekersMusicVolume() : config.hidersMusicVolume());
	}

	public MusicPackSound setVolume(float f) {
		this.volume = f;
		return this;
	}

	public MusicPackSound tick(short tick) {
		this.tick = tick;
		return this;
	}

	public boolean equalsTick(short tick) {
		return this.tick == tick;
	}

	public MusicPackSound resetVolume() {
		return this.setVolume(0.0001f);
	}

	public static MusicPackSound getSound(String id) {
		return id.equals("level0") ? MusicPackSound.hideLvl0 : id.equals("level1") ? MusicPackSound.hideLvl1 : id.equals("level2") ? MusicPackSound.hideLvl2 : id.equals("level0glow") ? MusicPackSound.hideGlow : null;
	}

	public boolean canStartSilent() {
		return true;
	}
}