/*
 * Copyright © 2022 Luis Michaelis
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package net.orbismc.ferocity.format;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.Template;
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
	public List<Template> getTemplates(@NotNull Player player) {
		final var metadata = this.adapter.getMetaData(player);

		final var templates = new ArrayList<Template>(2 + metadata.getMeta().size());
		templates.add(Template.of("luckperms-prefix", Objects.requireNonNullElse(metadata.getPrefix(), "")));
		templates.add(Template.of("luckperms-suffix", Objects.requireNonNullElse(metadata.getSuffix(), "")));

		for (final var m : metadata.getMeta().keySet()) {
			templates.add(Template.of("luckperms-meta-" + m, Objects.requireNonNull(metadata.getMetaValue(m))));
		}

		return templates;
	}
}
