/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package net.orbismc.ferocity.format;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.Template;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A basic formatter which allows for embedding generic player information.
 */
public class BasicPlayerTemplateProvider extends TemplateProvider {
	@Override
	public List<Template> getTemplates(@NotNull Player player) {
		final var server = player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "";

		return List.of(
				Template.of("player-name", player.getUsername()),
				Template.of("player-ping", Long.toString(player.getPing())),
				Template.of("player-server", server)
		);
	}
}
