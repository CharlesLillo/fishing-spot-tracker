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

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;

/**
 * Fishing spot types with their NPC IDs, movement timers, and catchable fish.
 *
 * NPC IDs sourced from RuneLite's FishingSpot.java.
 * Timer data sourced from the OSRS Wiki:
 *   - Most spots: 250-530 ticks (2.5-5 min)
 *   - Aerial fishing: 10-19 ticks (~6-11 sec)
 *   - Minnow: 12-25 ticks (~7-15 sec)
 *   - Sacred/Infernal eels: 100-300 ticks (1-3 min)
 *   - Anglerfish: 8-830 ticks (unpredictable)
 *   - Static spots (Karambwan, Tempoross, etc.): effectively never move
 */
@Getter
public enum FishingSpotData
{
	// ── Standard spots: 250-530 ticks ──────────────────────────────────
	SHRIMP("Shrimp / Anchovies", 250, 530,
		new Fish[] {
			new Fish("Anchovies", 15, 321),
			new Fish("Shrimp", 1, 317)
		},
		1514, 1517, 1518, 1521, 1523, 1524, 1525, 1528, 1530, 1544,
		3913, 7155, 7459, 7462, 7467, 7469, 7947,
		10513, 14038, 14040, 14041, 14524, 15066
	),
	LOBSTER("Lobster / Swordfish / Tuna", 250, 530,
		new Fish[] {
			new Fish("Swordfish", 50, 373),
			new Fish("Lobster", 40, 377),
			new Fish("Tuna", 35, 359)
		},
		1510, 1519, 1522, 2146, 3914, 5820,
		7199, 7460, 7465, 7470, 7946,
		9173, 9174, 10515, 10635, 12777, 14039,
		15070, 15071, 15075, 15076, 15079, 15084, 15086
	),
	SHARK("Shark / Bass", 250, 530,
		new Fish[] {
			new Fish("Shark", 76, 383),
			new Fish("Bass", 46, 363)
		},
		1511, 1520, 3419, 3915, 4476, 4477, 5233, 5234, 5821,
		7200, 7461, 7466, 8525, 8526, 8527,
		9171, 9172, 10514, 12775, 12776, 14037, 14523,
		15067, 15068, 15069, 15077, 15080, 15082, 15083, 15087
	),
	MONKFISH("Monkfish", 250, 530,
		new Fish[] {
			new Fish("Monkfish", 62, 7944)
		},
		4316
	),
	SALMON("Salmon / Trout", 250, 530,
		new Fish[] {
			new Fish("Salmon", 30, 331),
			new Fish("Trout", 20, 335)
		},
		394, 1506, 1507, 1508, 1509, 1512, 1513, 1515, 1516,
		1526, 1527, 1529,
		3417, 3418, 7463, 7464, 7468, 8524,
		12774, 14036, 14521, 14522, 14525, 14526, 14527, 14528,
		15072, 15073
	),
	LAVA_EEL("Lava Eel", 250, 530,
		new Fish[] {
			new Fish("Lava Eel", 53, 2148)
		},
		4928, 6784, 15384
	),
	BARB_FISH("Barbarian Fishing", 250, 530,
		new Fish[] {
			new Fish("Leaping Sturgeon", 70, 11330),
			new Fish("Leaping Salmon", 58, 11329),
			new Fish("Leaping Trout", 48, 11328)
		},
		1542, 7323
	),
	CAVE_EEL("Cave Eel", 250, 530,
		new Fish[] {
			new Fish("Cave Eel", 38, 5001)
		},
		1497, 1498, 1499, 1500
	),
	SLIMY_EEL("Slimy Eel", 250, 530,
		new Fish[] {
			new Fish("Slimy Eel", 28, 3379)
		},
		2653, 2654, 2655
	),
	DARK_CRAB("Dark Crab", 250, 530,
		new Fish[] {
			new Fish("Dark Crab", 85, 11934)
		},
		1535, 1536
	),
	SQUID("Squid", 250, 530,
		new Fish[] {
			new Fish("Squid", 44, 31561)
		},
		15074, 15078, 15081, 15085
	),
	CAMDOZAAL("Camdozaal Fishing", 250, 530,
		new Fish[] {
			new Fish("Guppy", 33, 25652),
			new Fish("Cavefish", 20, 25654),
			new Fish("Tetra", 5, 25656)
		},
		10686
	),
	CAMDOZAAL_CAVE_EEL("Camdozaal Cave Eel", 250, 530,
		new Fish[] {
			new Fish("Cave Eel", 38, 5001)
		},
		10653
	),
	CIVITAS("Civitas illa Fortis Park", 250, 530,
		new Fish[] {
			new Fish("Barblore", 15, 29325)
		},
		13329
	),

	// ── Fast-rotation spots ────────────────────────────────────────────
	COMMON_TENCH("Aerial Fishing", 10, 19,
		new Fish[] {
			new Fish("Greater Siren", 91, 22827),
			new Fish("Mottled Eel", 68, 22828),
			new Fish("Common Tench", 56, 22829),
			new Fish("Bluegill", 43, 22826)
		},
		8523
	),
	MINNOW("Minnow", 12, 25,
		new Fish[] {
			new Fish("Minnow", 82, 21356)
		},
		7730, 7731, 7732, 7733
	),

	// ── Shorter-timer spots ────────────────────────────────────────────
	SACRED_EEL("Sacred Eel", 100, 300,
		new Fish[] {
			new Fish("Sacred Eel", 87, 13339)
		},
		6488
	),
	INFERNAL_EEL("Infernal Eel", 100, 300,
		new Fish[] {
			new Fish("Infernal Eel", 80, 21293)
		},
		7676
	),
	ANGLERFISH("Anglerfish", 8, 830,
		new Fish[] {
			new Fish("Anglerfish", 82, 13439)
		},
		6825
	),

	// ── Static spots (never/rarely move) ───────────────────────────────
	KARAMBWAN("Karambwan", 10000, 10000,
		new Fish[] {
			new Fish("Karambwan", 65, 3142)
		},
		4712, 4713
	),
	KARAMBWANJI("Karambwanji / Shrimp", 10000, 10000,
		new Fish[] {
			new Fish("Karambwanji", 5, 3150)
		},
		4710
	),
	HARPOONFISH("Harpoonfish (Tempoross)", 10000, 10000,
		new Fish[] {
			new Fish("Harpoonfish", 35, 25564)
		},
		10565, 10568, 10569
	);

	static final int DEFAULT_MAX_TICKS = 530;

	private static final Map<Integer, FishingSpotData> SPOT_MAP;

	private final String name;
	private final int minTicks;
	private final int maxTicks;
	private final Fish[] fish;
	private final int[] npcIds;

	static
	{
		ImmutableMap.Builder<Integer, FishingSpotData> builder = new ImmutableMap.Builder<>();
		for (FishingSpotData spot : values())
		{
			for (int id : spot.npcIds)
			{
				builder.put(id, spot);
			}
		}
		SPOT_MAP = builder.build();
	}

	FishingSpotData(String name, int minTicks, int maxTicks, Fish[] fish, int... npcIds)
	{
		this.name = name;
		this.minTicks = minTicks;
		this.maxTicks = maxTicks;
		this.fish = fish;
		this.npcIds = npcIds;
	}

	/**
	 * Look up a fishing spot by NPC ID.
	 */
	public static FishingSpotData findSpot(int npcId)
	{
		return SPOT_MAP.get(npcId);
	}

	/**
	 * Returns the default item ID (highest level fish at this spot).
	 */
	public int getItemId()
	{
		return fish[0].itemId;
	}

	/**
	 * Returns the highest-level fish at this spot that is not in the ignore set.
	 * Fish names are compared case-insensitively.
	 * Returns null if all fish at this spot are ignored.
	 */
	public Fish getDisplayFish(Set<String> ignoredFish)
	{
		for (Fish f : fish)
		{
			if (!ignoredFish.contains(f.name.toLowerCase()))
			{
				return f;
			}
		}
		return null;
	}

	/**
	 * Whether this spot type is effectively static (never moves).
	 */
	public boolean isStatic()
	{
		return minTicks >= 10000;
	}

	/**
	 * Whether this spot type has an unpredictable timer (huge range).
	 */
	public boolean isUnpredictable()
	{
		return this == ANGLERFISH;
	}

	/**
	 * A single catchable fish at a fishing spot, ordered by fishing level (highest first).
	 */
	@Getter
	public static class Fish
	{
		private final String name;
		private final int level;
		private final int itemId;

		Fish(String name, int level, int itemId)
		{
			this.name = name;
			this.level = level;
			this.itemId = itemId;
		}
	}
}
