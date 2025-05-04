package com.l2jrevival.gameserver.model.actor.instance;

import com.l2jrevival.commons.concurrent.ThreadPool;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.template.NpcTemplate;

import com.l2jrevival.Dungeon.Dungeon;



/**
 * @author Anarchy
 */
public class DungeonMob extends Monster
{
	private Dungeon dungeon;
	
	public DungeonMob(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		if(dungeon != null) // It will be dungeon == null when mob is spawned from admin and not from dungeon
			ThreadPool.schedule(() -> dungeon.onMobKill(this), 1000*2);
		
		return true;
	}
	
	public void setDungeon(Dungeon dungeon)
	{
		this.dungeon = dungeon;
	}
}
