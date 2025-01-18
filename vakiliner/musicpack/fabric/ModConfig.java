package vakiliner.musicpack.fabric;

import com.google.gson.JsonObject;
import net.minecraft.util.Mth;

class ModConfig implements vakiliner.musicpack.base.ModConfig {
	private boolean enabled = true;
	private boolean hidersMusic = true;
	private boolean seekersMusic = true;
	private boolean disableDefaultMusic = true;
	private double hidersMusicVolume = 1;
	private double seekersMusicVolume = 1;

	protected ModConfig() { }

	protected ModConfig(JsonObject jsonObject) {
		if (jsonObject.get("enabled").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("enabled").isBoolean()) this.enabled = jsonObject.get("enabled").getAsBoolean();
		if (jsonObject.get("hidersMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("hidersMusic").isBoolean()) this.hidersMusic = jsonObject.get("hidersMusic").getAsBoolean();
		if (jsonObject.get("seekersMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("seekersMusic").isBoolean()) this.seekersMusic = jsonObject.get("seekersMusic").getAsBoolean();
		if (jsonObject.get("disableDefaultMusic").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("disableDefaultMusic").isBoolean()) this.disableDefaultMusic = jsonObject.get("disableDefaultMusic").getAsBoolean();
		if (jsonObject.get("hidersMusicVolume").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("hidersMusicVolume").isNumber()) this.hidersMusicVolume = jsonObject.get("hidersMusicVolume").getAsDouble();
		if (jsonObject.get("seekersMusicVolume").isJsonPrimitive() && jsonObject.getAsJsonPrimitive("seekersMusicVolume").isNumber()) this.seekersMusicVolume = jsonObject.get("seekersMusicVolume").getAsDouble();
	}

	public boolean enabled() {
		return this.enabled;
	}

	public boolean hidersMusicEnabled() {
		return this.hidersMusic;
	}

	public boolean seekersMusicEnabled() {
		return this.seekersMusic;
	}

	public boolean disableDefaultMusic() {
		return this.disableDefaultMusic;
	}

	public double hidersMusicVolume() {
		return Mth.clamp(this.hidersMusicVolume, 0, 1);
	}

	public double seekersMusicVolume() {
		return Mth.clamp(this.seekersMusicVolume, 0, 1);
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void hidersMusicEnabled(boolean hidersMusic) {
		this.hidersMusic = hidersMusic;
	}

	public void seekersMusicEnabled(boolean seekersMusic) {
		this.seekersMusic = seekersMusic;
	}

	public void disableDefaultMusic(boolean disableDefaultMusic) {
		this.disableDefaultMusic = disableDefaultMusic;
	}

	public void hidersMusicVolume(double hidersMusicVolume) {
		this.hidersMusicVolume = hidersMusicVolume;
	}

	public void seekersMusicVolume(double seekersMusicVolume) {
		this.seekersMusicVolume = seekersMusicVolume;
	}
}