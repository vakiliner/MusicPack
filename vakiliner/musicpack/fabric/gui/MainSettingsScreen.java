package vakiliner.musicpack.fabric.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import vakiliner.musicpack.api.GsonConfig;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import vakiliner.musicpack.fabric.mixin.SoundEngineMixin;
import vakiliner.musicpack.fabric.mixin.SoundManagerMixin;

@Environment(EnvType.CLIENT)
public class MainSettingsScreen extends Screen {
	private static final TranslatableComponent TITLE = new TranslatableComponent("vakiliner.musicpack.title");
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
			Class<?>[] parameterTypes = { PoseStack.class, net.minecraft.client.gui.Font.class, net.minecraft.network.chat.Component.class, int.class, int.class, int.class };
			try {
				method = Screen.class.getMethod("method_27534", parameterTypes);
			} catch (NoSuchMethodException e) {
				try {
					parameterTypes[2] = net.minecraft.network.chat.FormattedText.class;
					method = Screen.class.getMethod("method_27534", parameterTypes);
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

	public void onClose() {
		ModConfig config = MusicPack.getConfig();
		if (!this.gsonConfig.equals(config)) try {
			config.save();
		} catch (Throwable err) {
			err.printStackTrace();
		}
		this.minecraft.setScreen(this.parent);
		SoundManager soundManager = this.minecraft.getSoundManager();
		if (config.enabled()) ((SoundEngineMixin) ((SoundManagerMixin) soundManager).getSoundEngine()).getInstanceToChannel().forEach((soundInstance, channelHandle) -> {
			if (soundInstance.getSource() == SoundSource.MUSIC) switch (soundInstance.getLocation().getNamespace()) {
				case MusicPack.MOD_ID:
					if (!config.seekersMusicEnabled() && soundInstance == MusicPackSound.seek ||
						!config.hidersMusicEnabled() && (soundInstance == MusicPackSound.hideLvl0
						|| soundInstance == MusicPackSound.hideLvl1
						|| soundInstance == MusicPackSound.hideLvl2
						|| soundInstance == MusicPackSound.hideGlow)
					) channelHandle.execute(Channel::stop);
					break;
				case "minecraft":
					if (config.disableDefaultMusic()) channelHandle.execute(Channel::stop);
					break;
			}
		});
	}

	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.drawTitle(poseStack);
		super.render(poseStack, i, j, f);
	}

	private void drawTitle(PoseStack poseStack) {
		try {
			drawCenteredString.invoke(this, poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		} catch (IllegalAccessException | InvocationTargetException err) {
			throw new RuntimeException(err);
		}
	}
}