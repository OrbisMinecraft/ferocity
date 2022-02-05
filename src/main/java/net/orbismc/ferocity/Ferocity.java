package net.orbismc.ferocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.orbismc.ferocity.format.TemplateProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
		id = "ferocity",
		name = "Ferocity",
		version = BuildConstants.VERSION,
		description = "A Velocity plugin for managing a cross-server player list.",
		authors = {"lmichaelis"}
)
public class Ferocity {
	@Inject
	private Logger logger;

	@Inject
	private ProxyServer server;

	@Inject
	@DataDirectory
	private Path configurationDirectory;

	private String tabListHeader;
	private String tabListFooter;
	private String tabListEntry;

	public ProxyServer getServer() {
		return this.server;
	}

	@Subscribe
	public void onProxyInitialization(final @NotNull ProxyInitializeEvent event) {
		final var config = FerocityConfig.load(configurationDirectory.resolve("config.yml").toFile());
		this.tabListHeader = config.getNode("header").getString("");
		this.tabListFooter = config.getNode("footer").getString("");
		this.tabListEntry = config.getNode("entry").getString("%player_name%");

		final var pluginManager = server.getPluginManager();
		TemplateProvider.addAvailableProviders(pluginManager);

		final var eventListener = new FerocityEventListener(this);
		server.getEventManager().register(this, eventListener);
	}

	public String getTabListHeaderText() {
		return tabListHeader;
	}

	public String getTabListFooterText() {
		return tabListFooter;
	}

	public String getTabListEntryText() {
		return tabListEntry;
	}
}
