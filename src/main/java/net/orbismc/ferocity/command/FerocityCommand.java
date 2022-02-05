/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package net.orbismc.ferocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.orbismc.ferocity.BuildConstants;
import net.orbismc.ferocity.Ferocity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Management commands
 */
public class FerocityCommand implements SimpleCommand {
	private final Ferocity plugin;

	public FerocityCommand(final @NotNull Ferocity plugin) {
		this.plugin = plugin;
	}

	@Override
	public void execute(final @NotNull Invocation invocation) {
		final var args = invocation.arguments();
		final var source = invocation.source();

		if (args.length != 1) {
			source.sendMessage(Component.text("Usage: /ferocity <reload|version>", NamedTextColor.RED));
			return;
		}

		if (Objects.equals(args[0], "version")) {
			source.sendMessage(Component.text("Ferocity ").append(Component.text("v" + BuildConstants.VERSION, NamedTextColor.BLUE)));
		} else if (Objects.equals(args[0], "reload")) {
			plugin.reload();
			source.sendMessage(Component.text("Reloaded.", NamedTextColor.GREEN));
		}
	}

	@Override
	public List<String> suggest(final Invocation invocation) {
		return List.of("version", "reload");
	}

	@Override
	public boolean hasPermission(final @NotNull Invocation invocation) {
		return invocation.source().hasPermission("ferocity.manage");
	}
}
