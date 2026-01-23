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
import vakiliner.musicpack.forge.mixin.SoundEngineMixin;
import vakiliner.musicpack.forge.mixin.SoundHandlerMixin;

@OnlyIn(Dist.CLIENT)
public class MainSettingsScreen extends Screen {
	private static final TranslationTextComponent TITLE = new TranslationTextComponent("vakiliner.musicpack.title");
	private static final Method drawCenteredString;
	private final GsonConfig gsonConfig = new GsonConfig();
	public final Screen parent;
	public EnableButton enableButton;
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
		boolean enabled = config.enabled();
		this.enableButton = this.addButton(new EnableButton(this));
		this.hidersMusicButton = this.addButton(new HidersMusicButton(this, enabled));
		this.seekersMusicButton = this.addButton(new SeekersMusicButton(this, enabled));
		this.defaultMusicButton = this.addButton(new DefaultMusicButton(this, enabled));
		this.hidersMusicSlider = this.addButton(new HidersMusicSlider(this, enabled && config.hidersMusicEnabled()));
		this.seekersMusicSlider = this.addButton(new SeekersMusicSlider(this, enabled && config.seekersMusicEnabled()));
		this.doneButton = this.addButton(new DoneButton(this));
	}

	@SuppressWarnings("null")
	public void onClose() {
		ModConfig config = MusicPack.getConfig();
		if (!this.gsonConfig.equals(config)) try {
			config.save();
		} catch (Throwable err) {
			err.printStackTrace();
		}
		this.minecraft.setScreen(this.parent);
		boolean enabled = config.enabled();
		((SoundEngineMixin) ((SoundHandlerMixin) this.minecraft.getSoundManager()).getSoundEngine()).getInstanceToChannel().forEach((soundInstance, channelHandle) -> {
			if (soundInstance.getSource() == SoundCategory.MUSIC) switch (soundInstance.getLocation().getNamespace()) {
				case MusicPack.MOD_ID:
					if (!enabled
						|| soundInstance == MusicPackSound.seek && !config.seekersMusicEnabled()
						|| (
							soundInstance == MusicPackSound.hideLvl0 ||
							soundInstance == MusicPackSound.hideLvl1 ||
							soundInstance == MusicPackSound.hideLvl2 ||
							soundInstance == MusicPackSound.hideGlow
						) && !config.hidersMusicEnabled()
					) channelHandle.execute(SoundSource::stop);
					break;
				case "minecraft":
					if (enabled && config.disableDefaultMusic()) channelHandle.execute(SoundSource::stop);
					break;
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