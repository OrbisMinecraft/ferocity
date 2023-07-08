/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package net.orbismc.ferocity.format;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.platform.PlayerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Formats a message with LuckPerms placeholders.
 */
public class LuckPermsTemplateProvider extends TemplateProvider {
	private final PlayerAdapter<Player> adapter;

	public LuckPermsTemplateProvider(final @NotNull LuckPerms api) {
		this.adapter = api.getPlayerAdapter(Player.class);
	}

	@Override
	public List<TagResolver> getTemplates(@NotNull Player player) {
		final var metadata = this.adapter.getMetaData(player);

		final var templates = new ArrayList<TagResolver>(2 + metadata.getMeta().size());

		final var prefix = Tag.inserting(Component.text(Objects.requireNonNullElse(metadata.getPrefix(), "")));
		final var suffix = Tag.inserting(Component.text(Objects.requireNonNullElse(metadata.getSuffix(), "")));

		templates.add(TagResolver.resolver("luckperms-prefix", prefix));
		templates.add(TagResolver.resolver("luckperms-suffix", suffix));

		for (final var m : metadata.getMeta().keySet()) {
			final var t = Tag.inserting(Component.text(Objects.requireNonNull(metadata.getMetaValue(m))));
			templates.add(TagResolver.resolver("luckperms-meta-" + m, t));
		}

		return templates;
	}
}
