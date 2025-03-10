package vakiliner.musicpack.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MusicPack {
	public static final String MOD_ID = "musicpack";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	protected void ready() {
		LOGGER.info("Музыкальный пакет активирован");
	}
}