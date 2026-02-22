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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class FishingSpotTrackerOverlay extends Overlay
{
	private final FishingSpotTrackerPlugin plugin;
	private final FishingSpotTrackerConfig config;
	private final ItemManager itemManager;

	@Inject
	FishingSpotTrackerOverlay(
		FishingSpotTrackerPlugin plugin,
		FishingSpotTrackerConfig config,
		ItemManager itemManager)
	{
		this.plugin = plugin;
		this.config = config;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Figure out which NPC the local player is interacting with (if any)
		NPC activeSpot = null;
		Player localPlayer = plugin.getClient().getLocalPlayer();
		if (localPlayer != null)
		{
			Actor interacting = localPlayer.getInteracting();
			if (interacting instanceof NPC)
			{
				NPC target = (NPC) interacting;
				if (FishingSpotData.findSpot(target.getId()) != null)
				{
					activeSpot = target;
				}
			}
		}

		NPC newestSpot = config.showNewestBadge() ? plugin.getNewestSpot() : null;

		// Parse ignored fish list once per frame
		Set<String> ignoredFish = parseIgnoredFish(config.ignoredFish());

		// Deduplication: skip rendering multiple NPCs on the same tile
		Set<WorldPoint> renderedTiles = new HashSet<>();

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

			// Filter: static spots
			if (spotData.isStatic() && !config.showStaticSpots())
			{
				continue;
			}

			// Deduplication
			WorldPoint wp = npc.getWorldLocation();
			if (!renderedTiles.add(wp))
			{
				continue;
			}

			// Filter: skip spot entirely if all its fish are ignored
			if (!ignoredFish.isEmpty() && spotData.getDisplayFish(ignoredFish) == null)
			{
				continue;
			}

			boolean isActive = npc.equals(activeSpot);
			boolean isNewest = npc.equals(newestSpot);
			renderSpotOverlay(graphics, npc, spotData, isActive, isNewest, ignoredFish);
		}

		return null;
	}

	private void renderSpotOverlay(Graphics2D graphics, NPC npc, FishingSpotData spotData,
		boolean isActive, boolean isNewest, Set<String> ignoredFish)
	{
		double progress = plugin.getSpotProgress(npc);
		Color baseColor;
		if (isActive)
		{
			baseColor = config.activeColor();
		}
		else
		{
			baseColor = blendColors(config.freshColor(), config.expiredColor(), progress);
		}

		int alpha = Math.max(0, Math.min(255, config.circleOpacity()));
		Color strokeColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);

		// Render pie-timer circle
		if (config.showCircle())
		{
			renderCircleHighlight(graphics, npc, strokeColor, alpha, baseColor, progress);
		}

		// Position for text elements — use canvas text location
		Point canvasPoint = npc.getCanvasTextLocation(graphics, "", npc.getLogicalHeight() + 40);
		if (canvasPoint == null)
		{
			return;
		}

		int radius = config.circleRadius();

		// Timer text (with optional star prefix for newest spot)
		if (config.showTimer())
		{
			Integer spawnTick = plugin.getSpawnTick(npc);
			if (spawnTick != null)
			{
				int elapsedTicks = plugin.getClient().getTickCount() - spawnTick;
				int elapsedSeconds = elapsedTicks * 600 / 1000;
				int minutes = elapsedSeconds / 60;
				int seconds = elapsedSeconds % 60;

				String timerText;
				if (spotData.isUnpredictable())
				{
					timerText = String.format("%d:%02d (?)", minutes, seconds);
				}
				else
				{
					timerText = String.format("%d:%02d", minutes, seconds);
				}

				// Prepend star for newest spot
				if (isNewest)
				{
					timerText = "\u2605 " + timerText;
				}

				Point textPoint = new Point(canvasPoint.getX(),
					canvasPoint.getY() - radius - 8);

				if (isNewest)
				{
					Color badgeColor = new Color(255, 215, 0, alpha);
					OverlayUtil.renderTextLocation(graphics, textPoint, timerText, badgeColor);
				}
				else
				{
					OverlayUtil.renderTextLocation(graphics, textPoint, timerText, strokeColor);
				}
			}
		}
		else if (isNewest)
		{
			// Timer is off but badge is on — show just the star
			String badge = "\u2605";
			Color badgeColor = new Color(255, 215, 0, alpha);
			Point badgePoint = new Point(canvasPoint.getX(), canvasPoint.getY() - radius - 8);
			OverlayUtil.renderTextLocation(graphics, badgePoint, badge, badgeColor);
		}

		// Resolve which fish to display based on ignore filter
		FishingSpotData.Fish displayFish = spotData.getDisplayFish(ignoredFish);

		// Spot name (shows highest-level unfiltered fish name)
		if (config.showSpotName())
		{
			String displayName = (displayFish != null) ? displayFish.getName() : spotData.getName();
			Point namePoint = new Point(canvasPoint.getX(), canvasPoint.getY() + radius + 16);
			OverlayUtil.renderTextLocation(graphics, namePoint, displayName, strokeColor);
		}

		// Fish icon (hidden if all fish at this spot are ignored)
		if (config.showFishIcon() && displayFish != null)
		{
			BufferedImage icon = itemManager.getImage(displayFish.getItemId());
			if (icon != null)
			{
				Point iconPoint = npc.getCanvasImageLocation(icon, npc.getLogicalHeight() / 2);
				if (iconPoint != null)
				{
					OverlayUtil.renderImageLocation(graphics, iconPoint, icon);
				}
			}
		}
	}

	private void renderCircleHighlight(Graphics2D graphics, NPC npc, Color strokeColor,
		int alpha, Color baseColor, double progress)
	{
		Point canvasPoint = npc.getCanvasTextLocation(graphics, "", npc.getLogicalHeight() + 40);
		if (canvasPoint == null)
		{
			return;
		}

		int radius = config.circleRadius();
		double x = canvasPoint.getX() - radius;
		double y = canvasPoint.getY() - radius;
		double diameter = radius * 2;

		// Remaining sweep: full circle at 0% progress, empty at 100%
		double sweepAngle = 360.0 * (1.0 - progress);

		// Draw filled pie slice showing remaining time
		if (config.fillCircle() && sweepAngle > 0)
		{
			int fillAlpha = Math.max(0, alpha / 3);
			Color fillColor = new Color(baseColor.getRed(), baseColor.getGreen(),
				baseColor.getBlue(), fillAlpha);
			Arc2D.Double pieSlice = new Arc2D.Double(x, y, diameter, diameter,
				90, sweepAngle, Arc2D.PIE);
			graphics.setColor(fillColor);
			graphics.fill(pieSlice);
		}

		// Draw the full circle outline as a reference ring
		Ellipse2D.Double outline = new Ellipse2D.Double(x, y, diameter, diameter);
		graphics.setColor(strokeColor);
		graphics.setStroke(new BasicStroke(config.strokeWidth()));
		graphics.draw(outline);

		// Draw the arc edge on top of the outline for emphasis
		if (sweepAngle > 0 && sweepAngle < 360)
		{
			Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter,
				90, sweepAngle, Arc2D.PIE);
			graphics.setColor(strokeColor);
			graphics.draw(arc);
		}
	}

	private static Set<String> parseIgnoredFish(String configValue)
	{
		if (configValue == null || configValue.trim().isEmpty())
		{
			return Collections.emptySet();
		}

		return Arrays.stream(configValue.split(","))
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.map(String::toLowerCase)
			.collect(Collectors.toSet());
	}

	static Color blendColors(Color from, Color to, double progress)
	{
		float t = (float) Math.max(0.0, Math.min(1.0, progress));
		int r = Math.round(from.getRed() + t * (to.getRed() - from.getRed()));
		int g = Math.round(from.getGreen() + t * (to.getGreen() - from.getGreen()));
		int b = Math.round(from.getBlue() + t * (to.getBlue() - from.getBlue()));
		return new Color(r, g, b);
	}
}
