/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package net.orbismc.ferocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.player.TabListEntry;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.orbismc.ferocity.format.TemplateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * The main velocity event listener class of <i>Ferocity</i>. Handles and implements all handlers necessary for
 * sending cross-server messages.
 */
public class FerocityEventListener {
	private final Ferocity plugin;
	private final MiniMessage message;

	public FerocityEventListener(final @NotNull Ferocity plugin) {
		this.plugin = plugin;
		this.message = MiniMessage.builder()
				.markdown()
				.markdownFlavor(DiscordFlavor.get())
				.build();
	}

	@Subscribe(order = PostOrder.LAST)
	public void connect(final @NotNull ServerConnectedEvent event) {
		plugin.getServer().getScheduler().buildTask(plugin, () ->
				updatePlayerList(event.getPlayer())).delay(2, TimeUnit.SECONDS).schedule();
	}

	@Subscribe(order = PostOrder.LAST)
	public void disconnect(final @NotNull DisconnectEvent event) {
		final var player = event.getPlayer();

		for (final Player other : this.plugin.getServer().getAllPlayers()) {
			other.getCurrentServer().ifPresent(server -> {
				if (other.getTabList().containsEntry(player.getUniqueId())) {
					other.getTabList().removeEntry(player.getUniqueId());
				}
			});
		}
	}


	public void updatePlayerList(final @NotNull Player forPlayer) {
		for (final Player other : this.plugin.getServer().getAllPlayers()) {
			other.getCurrentServer().ifPresent(server -> {
				if (other.getTabList().containsEntry(forPlayer.getUniqueId())) {
					other.getTabList().removeEntry(forPlayer.getUniqueId());
				}

				final var text = message.parse(
						plugin.getTabListEntryText(),
						TemplateProvider.getAllTemplates(forPlayer)
				);

				other.getTabList().addEntry(
						TabListEntry.builder()
								.displayName(text)
								.profile(forPlayer.getGameProfile())
								.gameMode(0)
								.latency(30)
								.tabList(other.getTabList())
								.build()
				);
			});
		}

		final var header = message.parse(plugin.getTabListHeaderText(), TemplateProvider.getAllTemplates(forPlayer));
		final var footer = message.parse(plugin.getTabListFooterText(), TemplateProvider.getAllTemplates(forPlayer));
		forPlayer.sendPlayerListHeaderAndFooter(header, footer);
	}
}
