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
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("fishingspottracker")
public interface FishingSpotTrackerConfig extends Config
{
	// ── Colors ─────────────────────────────────────────────────────────

	@ConfigSection(
		name = "Colors",
		description = "Color settings for the spot overlay",
		position = 0
	)
	String colorSection = "colors";

	@Alpha
	@ConfigItem(
		keyName = "freshColor",
		name = "Fresh Color",
		description = "Circle color when a spot has just appeared",
		position = 0,
		section = colorSection
	)
	default Color freshColor()
	{
		return new Color(0, 255, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "expiredColor",
		name = "Expiring Color",
		description = "Circle color when a spot is about to move",
		position = 1,
		section = colorSection
	)
	default Color expiredColor()
	{
		return new Color(255, 0, 0);
	}

	@Alpha
	@ConfigItem(
		keyName = "activeColor",
		name = "Active Fishing Color",
		description = "Circle color when you are actively fishing at a spot",
		position = 2,
		section = colorSection
	)
	default Color activeColor()
	{
		return new Color(0, 200, 0);
	}

	@ConfigItem(
		keyName = "circleOpacity",
		name = "Circle Opacity",
		description = "How opaque the circle overlay is (0 = transparent, 255 = solid)",
		position = 3,
		section = colorSection
	)
	default int circleOpacity()
	{
		return 128;
	}

	// ── Display ────────────────────────────────────────────────────────

	@ConfigSection(
		name = "Display",
		description = "Display settings",
		position = 1
	)
	String displaySection = "display";

	@ConfigItem(
		keyName = "spotRenderStyle",
		name = "Render Style",
		description = "How to highlight fishing spots",
		position = 0,
		section = displaySection
	)
	default RenderStyle renderStyle()
	{
		return RenderStyle.CIRCLE;
	}

	@ConfigItem(
		keyName = "showCircleOverlay",
		name = "Show Circle",
		description = "Draw the circle overlay around fishing spots",
		position = 1,
		section = displaySection
	)
	default boolean showCircle()
	{
		return true;
	}

	@ConfigItem(
		keyName = "circleRadius",
		name = "Circle Radius",
		description = "Radius of the circle (only used in Circle render style)",
		position = 2,
		section = displaySection
	)
	default int circleRadius()
	{
		return 18;
	}

	@ConfigItem(
		keyName = "strokeWidth",
		name = "Stroke Width",
		description = "Thickness of the highlight outline",
		position = 3,
		section = displaySection
	)
	default int strokeWidth()
	{
		return 2;
	}

	@ConfigItem(
		keyName = "fillCircle",
		name = "Fill Circle",
		description = "Whether to fill the circle with a translucent version of the color",
		position = 4,
		section = displaySection
	)
	default boolean fillCircle()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showTimer",
		name = "Show Timer Text",
		description = "Display elapsed time text above the fishing spot",
		position = 5,
		section = displaySection
	)
	default boolean showTimer()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showSpotName",
		name = "Show Spot Name",
		description = "Display the fishing spot type name",
		position = 6,
		section = displaySection
	)
	default boolean showSpotName()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showFishIcon",
		name = "Show Fish Icon",
		description = "Display the fish item sprite on fishing spots",
		position = 7,
		section = displaySection
	)
	default boolean showFishIcon()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showNewestBadge",
		name = "Show Newest Spot Badge",
		description = "Mark the most recently spawned spot (least likely to move)",
		position = 8,
		section = displaySection
	)
	default boolean showNewestBadge()
	{
		return true;
	}

	// ── Filtering ──────────────────────────────────────────────────────

	@ConfigSection(
		name = "Filtering",
		description = "Control which spots are shown",
		position = 2
	)
	String filterSection = "filtering";

	@ConfigItem(
		keyName = "onlyCurrentType",
		name = "Only Current Fish Type",
		description = "Only show overlays for the type of spot you are currently fishing at",
		position = 0,
		section = filterSection
	)
	default boolean onlyCurrentType()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showStaticSpots",
		name = "Show Static Spots",
		description = "Show overlays for spots that never move (Karambwan, Tempoross, etc.)",
		position = 1,
		section = filterSection
	)
	default boolean showStaticSpots()
	{
		return false;
	}

	@ConfigItem(
		keyName = "ignoredFish",
		name = "Ignored Fish",
		description = "Comma-separated list of fish names to hide (e.g. Shrimp, Tuna). If all fish at a spot are ignored, the icon is hidden.",
		position = 2,
		section = filterSection
	)
	default String ignoredFish()
	{
		return "";
	}

	// ── Notifications ──────────────────────────────────────────────────

	@ConfigSection(
		name = "Notifications",
		description = "Notification settings",
		position = 3
	)
	String notificationSection = "notifications";

	@ConfigItem(
		keyName = "idleNotification",
		name = "Idle Notification",
		description = "Notify when you stop fishing (animation stops)",
		position = 0,
		section = notificationSection
	)
	default boolean idleNotification()
	{
		return false;
	}

	// ── Minimap ────────────────────────────────────────────────────────

	@ConfigSection(
		name = "Minimap",
		description = "Minimap overlay settings",
		position = 4
	)
	String minimapSection = "minimap";

	@ConfigItem(
		keyName = "showMinimapDots",
		name = "Show Minimap Dots",
		description = "Show colored dots on the minimap for tracked fishing spots",
		position = 0,
		section = minimapSection
	)
	default boolean showMinimapDots()
	{
		return false;
	}
}
