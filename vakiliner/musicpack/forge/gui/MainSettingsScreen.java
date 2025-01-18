package vakiliner.musicpack.forge.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.audio.SoundSource;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.SoundCategory;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.forge.MusicPack;
import vakiliner.musicpack.forge.MusicPackSound;
import vakiliner.musicpack.forge.mixin.SoundEngineMixin;
import vakiliner.musicpack.forge.mixin.SoundHandlerMixin;

@OnlyIn(Dist.CLIENT)
public class MainSettingsScreen extends Screen {
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
	}

	protected void init() {
		ModConfig config = MusicPack.getConfig();
		boolean enabled = config.enabled();
		enableButton = this.addButton(new EnableButton(this));
		hidersMusicButton = this.addButton(new HidersMusicButton(this, enabled));
		seekersMusicButton = this.addButton(new SeekersMusicButton(this, enabled));
		defaultMusicButton = this.addButton(new DefaultMusicButton(this, enabled));
		hidersMusicSlider = this.addButton(new HidersMusicSlider(this, enabled && config.hidersMusicEnabled()));
		seekersMusicSlider = this.addButton(new SeekersMusicSlider(this, enabled && config.seekersMusicEnabled()));
		doneButton = this.addButton(new DoneButton(this));
	}

	public void onClose() {
		MusicPack.saveConfig();
		this.minecraft.setScreen(this.parent);
		SoundHandler soundManager = this.minecraft.getSoundManager();
		ModConfig config = MusicPack.getConfig();
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

	public void render(MatrixStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		super.render(poseStack, i, j, f);
	}
}