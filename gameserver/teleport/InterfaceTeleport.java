package com.l2jrevival.gameserver.teleport;

import com.l2jrevival.gameserver.model.location.Location;

public final class InterfaceTeleport
{
	private final int id;
	private final Location location;
	private final int priceId;
	private final int priceCount;
	
	public InterfaceTeleport(int id, Location location, int priceId, int priceCount)
    {
        this.id = id;
        this.location = location;
        this.priceId = priceId;
        this.priceCount = priceCount;
    }
	
	public int getId()
	{
		return id;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public int getPriceId()
	{
		return priceId;
	}
	
	public int getPriceCount()
	{
		return priceCount;
	}
}