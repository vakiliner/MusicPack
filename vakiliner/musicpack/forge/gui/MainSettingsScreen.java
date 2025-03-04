package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.audio.SoundSource;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.audio.SoundHandler;
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
	private final GsonConfig gsonConfig = new GsonConfig();
	public final Screen parent;
	public EnableButton enableButton;
	public HidersMusicButton hidersMusicButton;
	public SeekersMusicButton seekersMusicButton;
	public DefaultMusicButton defaultMusicButton;
	public HidersMusicSlider hidersMusicSlider;
	public SeekersMusicSlider seekersMusicSlider;
	public DoneButton doneButton;

	public MainSettingsScreen(Screen parent) {
		super(new TranslationTextComponent("vakiliner.musicpack.title"));
		this.parent = parent;
		this.gsonConfig.parse(MusicPack.getConfig());
	}

	protected void init() {
		ModConfig config = MusicPack.getConfig();
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
		SoundHandler soundManager = this.minecraft.getSoundManager();
		if (config.enabled()) ((SoundEngineMixin) ((SoundHandlerMixin) soundManager).getSoundEngine()).getInstanceToChannel().forEach((soundInstance, channelHandle) -> {
			if (soundInstance.getSource() == SoundCategory.MUSIC) switch (soundInstance.getLocation().getNamespace()) {
				case MusicPack.MOD_ID:
					if (!config.seekersMusicEnabled() && soundInstance == MusicPackSound.seek ||
						!config.hidersMusicEnabled() && (soundInstance == MusicPackSound.hideLvl0
						|| soundInstance == MusicPackSound.hideLvl1
						|| soundInstance == MusicPackSound.hideLvl2
						|| soundInstance == MusicPackSound.hideGlow)
					) channelHandle.execute(SoundSource::stop);
					break;
				case "minecraft":
					if (config.disableDefaultMusic()) channelHandle.execute(SoundSource::stop);
					break;
			}
		});
	}

	public void render(@javax.annotation.Nonnull MatrixStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		super.render(poseStack, i, j, f);
	}
}