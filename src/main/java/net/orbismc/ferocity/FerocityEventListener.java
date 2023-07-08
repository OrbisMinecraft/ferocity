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
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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
		this.message = MiniMessage.builder().build();
	}

	@Subscribe(order = PostOrder.LAST)
	public void connect(final @NotNull ServerConnectedEvent event) {
		plugin.getServer().getScheduler().buildTask(plugin, this::updatePlayerList).delay(2, TimeUnit.SECONDS)
				.schedule();
	}

	@Subscribe(order = PostOrder.LAST)
	public void disconnect(final @NotNull DisconnectEvent event) {
		plugin.getServer().getScheduler().buildTask(plugin, this::updatePlayerList).delay(2, TimeUnit.SECONDS)
				.schedule();
	}

	public void updatePlayerList() {
		this.plugin.getServer().getAllPlayers().forEach(this::updatePlayerList);
	}

	public void updatePlayerList(final @NotNull Player forPlayer) {
		for (final Player other : this.plugin.getServer().getAllPlayers()) {
			final var resolver = TagResolver.resolver(TemplateProvider.getAllTemplates(other));
			other.getCurrentServer().ifPresent(server -> {

				if (forPlayer.getTabList().containsEntry(other.getUniqueId())) {
					forPlayer.getTabList().removeEntry(other.getUniqueId());
				}

				final var text = message.deserialize(
						plugin.getTabListEntryText(),
						resolver
				);

				forPlayer.getTabList().addEntry(
						TabListEntry.builder()
								.displayName(text)
								.profile(other.getGameProfile())
								.gameMode(0)
								.latency(30)
								.tabList(forPlayer.getTabList())
								.build()
				);
			});
		}

		final var resolver = TagResolver.resolver(TemplateProvider.getAllTemplates(forPlayer));
		final var header = message.deserialize(plugin.getTabListHeaderText(), resolver);
		final var footer = message.deserialize(plugin.getTabListFooterText(), resolver);
		forPlayer.sendPlayerListHeaderAndFooter(header, footer);
	}
}
