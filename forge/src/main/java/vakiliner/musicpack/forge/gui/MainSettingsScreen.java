package vakiliner.musicpack.forge.gui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import vakiliner.musicpack.api.GsonConfig;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;

@OnlyIn(Dist.CLIENT)
public class MainSettingsScreen extends Screen {
	private static final TranslatableComponent TITLE = new TranslatableComponent("vakiliner.musicpack.title");
	private static final Method drawCenteredString;
	private final GsonConfig gsonConfig = new GsonConfig();
	public final Screen parent;
	public HidersMusicButton hidersMusicButton;
	public SeekersMusicButton seekersMusicButton;
	public DefaultMusicButton defaultMusicButton;
	public HidersMusicSlider hidersMusicSlider;
	public SeekersMusicSlider seekersMusicSlider;
	public DoneButton doneButton;

	static {
		Method method = null;
		Class<?>[] parameterTypes = { PoseStack.class, Font.class, Component.class, int.class, int.class, int.class };
		try {
			method = Screen.class.getMethod("func_238472_a_", parameterTypes);
		} catch (NoSuchMethodException a) {
			parameterTypes[2] = FormattedText.class;
			try {
				method = Screen.class.getMethod("func_238472_a_", parameterTypes);
			} catch (NoSuchMethodException err) {
				err.printStackTrace();
			}
		}
		drawCenteredString = method;
	}

	public MainSettingsScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
	}

	public static boolean a() {
		return drawCenteredString == null;
	}

	@Override
	protected void init() {
		ModConfig config = MusicPack.getConfig();
		this.gsonConfig.parse(config);
		this.hidersMusicButton = this.addButton(new HidersMusicButton(this, config.hidersMusicEnabled()));
		this.seekersMusicButton = this.addButton(new SeekersMusicButton(this, config.seekersMusicEnabled()));
		this.defaultMusicButton = this.addButton(new DefaultMusicButton(this, config.disableDefaultMusic()));
		this.hidersMusicSlider = this.addButton(new HidersMusicSlider(this, config.hidersMusicEnabled()));
		this.seekersMusicSlider = this.addButton(new SeekersMusicSlider(this, config.seekersMusicEnabled()));
		this.doneButton = this.addButton(new DoneButton(this));
	}

	@Override
	public void onClose() {
		ModConfig config = MusicPack.getConfig();
		config.hidersMusicEnabled(this.hidersMusicSlider.active);
		config.seekersMusicEnabled(this.seekersMusicSlider.active);
		config.disableDefaultMusic(this.defaultMusicButton.disable);
		if (!this.gsonConfig.equals(config)) try {
			config.save();
		} catch (IOException err) {
			MusicPack.LOGGER.error("Failed to save config", err);
		}
		this.minecraft.setScreen(this.parent);
		if (config.disableDefaultMusic()) {
			this.minecraft.getMusicManager().stopPlaying();
		}
		SoundManager soundManager = this.minecraft.getSoundManager();
		if (!config.seekersMusicEnabled()) {
			soundManager.stop(MusicPackSound.seek);
		}
		if (!config.hidersMusicEnabled()) {
			soundManager.stop(MusicPackSound.hideLvl0);
			soundManager.stop(MusicPackSound.hideLvl1);
			soundManager.stop(MusicPackSound.hideLvl2);
			soundManager.stop(MusicPackSound.hideGlow);
		}
	}

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.drawTitle(poseStack);
		super.render(poseStack, i, j, f);
	}

	private void drawTitle(PoseStack poseStack) {
		try {
			drawCenteredString.invoke(this, poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		} catch (IllegalAccessException | InvocationTargetException err) {
			throw new IllegalStateException(err);
		}
	}
}