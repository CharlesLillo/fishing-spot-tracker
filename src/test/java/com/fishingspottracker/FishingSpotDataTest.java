package com.fishingspottracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class FishingSpotDataTest
{
	@Test
	public void npcIdsAreUnique()
	{
		Set<Integer> seen = new HashSet<>();
		for (FishingSpotData spot : FishingSpotData.values())
		{
			for (int id : spot.getNpcIds())
			{
				assertTrue("Duplicate NPC ID " + id + " in " + spot, seen.add(id));
			}
		}
	}

	@Test
	public void wyrmscraigSpotsMapToExistingTypes()
	{
		assertEquals(FishingSpotData.SHARK, FishingSpotData.findSpot(16335));
		assertEquals(FishingSpotData.LOBSTER, FishingSpotData.findSpot(16336));
		assertEquals(FishingSpotData.LAVA_EEL, FishingSpotData.findSpot(16337));
	}

	@Test
	public void boatSpotsAreStatic()
	{
		for (int id = 16338; id <= 16343; id++)
		{
			FishingSpotData spot = FishingSpotData.findSpot(id);
			assertTrue("Boat spot " + id + " should be static", spot != null && spot.isStatic());
		}
	}

	@Test
	public void frenziedAerialSpotUsesItsOwnTimer()
	{
		FishingSpotData spot = FishingSpotData.findSpot(16346);
		assertEquals(FishingSpotData.FRENZIED_AERIAL, spot);
		assertEquals(28, spot.getMaxTicks());
		assertFalse(spot.isStatic());
	}
}
