package vakiliner.musicpack.fabric.gui;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import vakiliner.musicpack.base.ModConfig;
import vakiliner.musicpack.fabric.MusicPack;
import vakiliner.musicpack.fabric.MusicPackSound;
import vakiliner.musicpack.fabric.mixin.SoundEngineMixin;
import vakiliner.musicpack.fabric.mixin.SoundManagerMixin;

@Environment(EnvType.CLIENT)
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
		super(new TranslatableComponent("vakiliner.musicpack.title"));
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
		SoundManager soundManager = this.minecraft.getSoundManager();
		ModConfig config = MusicPack.getConfig();
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
		drawCenteredString(poseStack, this.font, this.title, this.width / 2, 15, 0xffffff);
		super.render(poseStack, i, j, f);
	}
}