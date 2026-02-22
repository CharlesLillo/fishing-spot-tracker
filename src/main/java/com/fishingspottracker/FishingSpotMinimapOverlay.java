/*
 * Copyright (c) 2026, SpockNinja
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.fishingspottracker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.inject.Inject;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class FishingSpotMinimapOverlay extends Overlay
{
	private static final int DOT_SIZE = 4;

	private final FishingSpotTrackerPlugin plugin;
	private final FishingSpotTrackerConfig config;

	@Inject
	FishingSpotMinimapOverlay(FishingSpotTrackerPlugin plugin, FishingSpotTrackerConfig config)
	{
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showMinimapDots())
		{
			return null;
		}

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (NPC npc : plugin.getTrackedSpots().keySet())
		{
			if (npc.getId() == -1)
			{
				continue;
			}

			FishingSpotData spotData = FishingSpotData.findSpot(npc.getId());
			if (spotData == null)
			{
				continue;
			}

			if (spotData.isStatic() && !config.showStaticSpots())
			{
				continue;
			}

			Point minimapLocation = npc.getMinimapLocation();
			if (minimapLocation == null)
			{
				continue;
			}

			double progress = plugin.getSpotProgress(npc);
			Color dotColor = FishingSpotTrackerOverlay.blendColors(
				config.freshColor(), config.expiredColor(), progress);

			graphics.setColor(dotColor);
			graphics.fillOval(
				minimapLocation.getX() - DOT_SIZE / 2,
				minimapLocation.getY() - DOT_SIZE / 2,
				DOT_SIZE,
				DOT_SIZE
			);
		}

		return null;
	}
}
