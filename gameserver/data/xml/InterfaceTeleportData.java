package com.l2jrevival.gameserver.data.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.l2jrevival.commons.data.xml.XMLDocument;

import com.l2jrevival.gameserver.model.location.Location;
import com.l2jrevival.gameserver.teleport.InterfaceTeleport;
import com.l2jrevival.gameserver.templates.StatsSet;

import org.w3c.dom.Document;

public class InterfaceTeleportData extends XMLDocument
{
	private final Map<Integer, InterfaceTeleport> interfaceTeleportMap = new HashMap<>();
	
	private InterfaceTeleportData()
	{
		load();
	}
	
	@Override
	protected void load()
	{
		loadDocument("./data/xml/interfaceTeleports.xml");
		LOGGER.info("Loaded {} interface teleports.", interfaceTeleportMap.size());
	}
	
	@Override
	protected void parseDocument(Document doc, File file)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "teleport", teleportNode ->
			{
				final StatsSet teleportSet = parseAttributes(teleportNode);
				final int id = teleportSet.getInteger("id");
				final int x = teleportSet.getInteger("x");
				final int y = teleportSet.getInteger("y");
				final int z = teleportSet.getInteger("z");
				final Location location = new Location(x, y, z);
				final int priceId = teleportSet.getInteger("priceId");
				final int priceCount = teleportSet.getInteger("priceCount");
				final InterfaceTeleport interfaceTeleport = new InterfaceTeleport(id, location, priceId, priceCount);
				interfaceTeleportMap.put(id, interfaceTeleport);
			});
		});
	}
	
	public final InterfaceTeleport getInterfaceTeleport(int id)
	{
		return interfaceTeleportMap.get(id);
	}
	
	public static final InterfaceTeleportData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		private static final InterfaceTeleportData INSTANCE = new InterfaceTeleportData();
	}
}