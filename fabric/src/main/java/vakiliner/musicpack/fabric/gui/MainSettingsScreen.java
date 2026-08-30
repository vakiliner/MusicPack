package vakiliner.musicpack.fabric.gui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.api.GsonConfig;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.base.ModConfig.DisableMusicManager;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;

@Environment(EnvType.CLIENT)
public class MainSettingsScreen extends Screen {
	private static final TranslatableComponent TITLE = new TranslatableComponent("vakiliner.musicpack.title");
	private static final Method DRAW_CENTERED_STRING;
	private final GsonConfig gsonConfig = new GsonConfig();
	public final Screen parent;
	public final ModConfig config;
	public final ConfigPressOption<Boolean> hidersMusic;
	public final ConfigPressOption<Boolean> seekersMusic;
	public final ConfigProgressOption hidersMusicVolume;
	public final ConfigProgressOption seekersMusicVolume;
	public final ConfigPressOption<DisableMusicManager> disableMusicManager;
	public AbstractWidget hidersMusicSlider;
	public AbstractWidget seekersMusicSlider;

	static {
		Method method = null;
		Class<?>[] parameterTypes = { PoseStack.class, Font.class, Component.class, int.class, int.class, int.class };
		try {
			method = Screen.class.getMethod("method_27534", parameterTypes);
		} catch (NoSuchMethodException a) {
			parameterTypes[2] = FormattedText.class;
			try {
				method = Screen.class.getMethod("method_27534", parameterTypes);
			} catch (NoSuchMethodException err) {
				err.printStackTrace();
			}
		}
		DRAW_CENTERED_STRING = method;
	}

	public static void test() {
	}

	public MainSettingsScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
		this.config = MusicPack.getConfig();
		this.gsonConfig.parse(this.config);
		this.hidersMusic = new ConfigPressOption<>(MainSettingsScreen::getHidersMusicComponent, (enabled) -> this.hidersMusicSlider.active = !enabled, this.config.hidersMusicEnabled());
		this.seekersMusic = new ConfigPressOption<>(MainSettingsScreen::getSeekersMusicComponent, (enabled) -> this.seekersMusicSlider.active = !enabled, this.config.seekersMusicEnabled());
		this.hidersMusicVolume = new ConfigProgressOption(MainSettingsScreen::getHidersMusicVolumeComponent, this.config::hidersMusicVolume, this.config::hidersMusicVolume);
		this.seekersMusicVolume = new ConfigProgressOption(MainSettingsScreen::getSeekersMusicVolumeComponent, this.config::seekersMusicVolume, this.config::seekersMusicVolume);
		this.disableMusicManager = new ConfigPressOption<>(MainSettingsScreen::getDisableMusicManagerComponent, MainSettingsScreen::updateDisableMusicManager, this.config.disableMusicManager());
	}

	private static Component getHidersMusicComponent(boolean enabled) {
		return CommonComponents.optionStatus(new TranslatableComponent("vakiliner.musicpack.option.hidersMusic"), enabled);
	}

	private static Component getSeekersMusicComponent(boolean enabled) {
		return CommonComponents.optionStatus(new TranslatableComponent("vakiliner.musicpack.option.seekersMusic"), enabled);
	}

	private static Component getHidersMusicVolumeComponent(double value) {
		return new TranslatableComponent("options.percent_value", new TranslatableComponent("vakiliner.musicpack.option.hidersMusic"), (int) (value * 100));
	}

	private static Component getSeekersMusicVolumeComponent(double value) {
		return new TranslatableComponent("options.percent_value", new TranslatableComponent("vakiliner.musicpack.option.seekersMusic"), (int) (value * 100));
	}

	private static Component getDisableMusicManagerComponent(DisableMusicManager disableType) {
		return new TranslatableComponent("vakiliner.musicpack.option.disableMusicManager", new TranslatableComponent("vakiliner.musicpack.option.disableMusicManager." + disableType.name().toLowerCase()));
	}

	private static DisableMusicManager updateDisableMusicManager(DisableMusicManager disableType) {
		int ordinal = disableType.ordinal() + 1;
		DisableMusicManager[] values = DisableMusicManager.values();
		return values[ordinal >= values.length ? 0 : ordinal];
	}

	@Override
	protected void init() {
		this.addButton(this.hidersMusic.createButton(this, 150, -80, 60));
		this.addButton(this.seekersMusic.createButton(this, 150, 80, 60));
		this.hidersMusicSlider = this.addButton(this.hidersMusicVolume.createButton(this, 150, -80, 85));
		this.hidersMusicSlider.active = this.hidersMusic.value();
		this.seekersMusicSlider = this.addButton(this.seekersMusicVolume.createButton(this, 150, 80, 85));
		this.seekersMusicSlider.active = this.seekersMusic.value();
		this.addButton(this.disableMusicManager.createButton(this, 200, 0, 110));
		this.addButton(new Button((this.width - 200) / 2, 175, 200, 20, CommonComponents.GUI_DONE, (button) -> this.onClose()));
	}

	@Override
	public void onClose() {
		this.config.hidersMusicEnabled(this.hidersMusic.value());
		this.config.seekersMusicEnabled(this.seekersMusic.value());
		this.config.disableMusicManager(this.disableMusicManager.value());
		if (!this.gsonConfig.equals(this.config)) try {
			this.config.save();
		} catch (IOException err) {
			MusicPack.LOGGER.error("Failed to save config", err);
		}
		this.minecraft.setScreen(this.parent);
		MusicManager musicManager = this.minecraft.getMusicManager();
		switch (this.config.disableMusicManager()) {
			case ONLY_IN_GAME:
				if (MusicPack.isMusicMenuPlayed(this.minecraft)) break;
			case EVERYWHERE:
				musicManager.stopPlaying();
				break;
			default: break;
		}
		SoundManager soundManager = this.minecraft.getSoundManager();
		if (!this.config.seekersMusicEnabled()) {
			soundManager.stop(MusicPackSound.seek);
		}
		if (!this.config.hidersMusicEnabled()) {
			soundManager.stop(MusicPackSound.hideLvl0);
			soundManager.stop(MusicPackSound.hideLvl1);
			soundManager.stop(MusicPackSound.hideLvl2);
			soundManager.stop(MusicPackSound.hideGlow);
		}
	}

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.drawTitle0(poseStack);
		super.render(poseStack, i, j, f);
	}

	private void drawTitle0(PoseStack poseStack) {
		Object[] args = { poseStack, this.font, this.title, this.width / 2, 15, 0xFFFFFF };
		try {
			DRAW_CENTERED_STRING.invoke(this, args);
		} catch (IllegalAccessException | InvocationTargetException err) {
			throw new IllegalStateException(err);
		}
	}
}
