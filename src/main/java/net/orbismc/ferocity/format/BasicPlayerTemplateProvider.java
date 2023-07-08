/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package net.orbismc.ferocity.format;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A basic formatter which allows for embedding generic player information.
 */
public class BasicPlayerTemplateProvider extends TemplateProvider {
	@Override
	public List<TagResolver> getTemplates(@NotNull Player player) {
		final var server = player.getCurrentServer().isPresent() ?
				player.getCurrentServer().get().getServerInfo().getName() :
				"";

		return List.of(
				TagResolver.resolver("player-name", Tag.inserting(Component.text(player.getUsername()))),
				TagResolver.resolver("player-ping", Tag.inserting(Component.text(player.getPing()))),
				TagResolver.resolver("player-server", Tag.inserting(Component.text(server)))
		);
	}
}
