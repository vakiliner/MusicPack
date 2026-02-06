package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.audio.SoundSource;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.SoundCategory;
import vakiliner.musicpack.api.GsonConfig;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineAccessor;
import vakiliner.musicpack.forge.mixin.SoundHandlerAccessor;

@OnlyIn(Dist.CLIENT)
public class MainSettingsScreen extends Screen {
	private static final TranslationTextComponent TITLE = new TranslationTextComponent("vakiliner.musicpack.title");
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
		try {
			Class<?>[] parameterTypes = { MatrixStack.class, net.minecraft.client.gui.FontRenderer.class, net.minecraft.util.text.ITextComponent.class, int.class, int.class, int.class };
			try {
				method = Screen.class.getMethod("func_238472_a_", parameterTypes);
			} catch (NoSuchMethodException e) {
				try {
					parameterTypes[2] = net.minecraft.util.text.ITextProperties.class;
					method = Screen.class.getMethod("func_238472_a_", parameterTypes);
				} catch (NoSuchMethodException err) {
					err.printStackTrace();
				}
			}
		} catch (SecurityException err) {
			err.printStackTrace();
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

	public void onClose() {
		ModConfig config = MusicPack.getConfig();
		config.hidersMusicEnabled(this.hidersMusicSlider.active);
		config.seekersMusicEnabled(this.seekersMusicSlider.active);
		config.disableDefaultMusic(this.defaultMusicButton.disable);
		if (!this.gsonConfig.equals(config)) try {
			config.save();
		} catch (Throwable err) {
			err.printStackTrace();
		}
		this.minecraft.setScreen(this.parent);
		((SoundEngineAccessor) ((SoundHandlerAccessor) this.minecraft.getSoundManager()).getSoundEngine()).getInstanceToChannel().forEach((soundInstance, channelHandle) -> {
			if (soundInstance.getSource() != SoundCategory.MUSIC) return;
			switch (soundInstance.getLocation().getNamespace()) {
				case MusicPack.MOD_ID: {
					if (soundInstance == MusicPackSound.seek && !config.seekersMusicEnabled()
						|| (
							soundInstance == MusicPackSound.hideLvl0 ||
							soundInstance == MusicPackSound.hideLvl1 ||
							soundInstance == MusicPackSound.hideLvl2 ||
							soundInstance == MusicPackSound.hideGlow
						) && !config.hidersMusicEnabled()
					) channelHandle.execute(SoundSource::stop);
					break;
				}
				case "minecraft": {
					if (config.disableDefaultMusic()) channelHandle.execute(SoundSource::stop);
					break;
				}
			}
		});
	}

	public void render(@javax.annotation.Nonnull MatrixStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.drawTitle(poseStack);
		super.render(poseStack, i, j, f);
	}

	private void drawTitle(MatrixStack poseStack) {
		try {
			drawCenteredString.invoke(this, poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		} catch (IllegalAccessException | InvocationTargetException err) {
			throw new RuntimeException(err);
		}
	}
}