package vakiliner.musicpack.forge;

import java.util.function.DoubleSupplier;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class MusicPackSound extends SimpleSound {
	public static final MusicPackSound seek = new MusicPackSound(MusicPack.SEEK, MusicPack.getConfig()::seekersMusicVolume, false);
	public static final MusicPackSound hideLvl0 = new MusicPackSound(MusicPack.HIDE_0, MusicPack.getConfig()::hidersMusicVolume);
	public static final MusicPackSound hideLvl1 = new MusicPackSound(MusicPack.HIDE_1, MusicPack.getConfig()::hidersMusicVolume);
	public static final MusicPackSound hideLvl2 = new MusicPackSound(MusicPack.HIDE_2, MusicPack.getConfig()::hidersMusicVolume);
	public static final MusicPackSound hideGlow = new MusicPackSound(MusicPack.HIDE_G, MusicPack.getConfig()::hidersMusicVolume);
	private final DoubleSupplier setting;
	private short tick = 0;

	private MusicPackSound(SoundEvent soundEvent, DoubleSupplier setting) {
		this(soundEvent, setting, true);
	}

	private MusicPackSound(SoundEvent soundEvent, DoubleSupplier setting, boolean looping) {
		super(soundEvent, SoundCategory.MUSIC, 1, 1, BlockPos.ZERO);
		this.setting = setting;
		this.looping = looping;
		this.attenuation = AttenuationType.NONE;
	}

	public float getVolume() {
		return super.getVolume() * (float) this.setting.getAsDouble();
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
		switch (id) {
			case "level0": return MusicPackSound.hideLvl0;
			case "level1": return MusicPackSound.hideLvl1;
			case "level2": return MusicPackSound.hideLvl2;
			case "level0glow": return MusicPackSound.hideGlow;
			default: return null;
		}
	}

	public boolean canStartSilent() {
		return true;
	}
}