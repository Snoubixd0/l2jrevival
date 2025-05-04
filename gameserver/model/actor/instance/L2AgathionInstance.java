/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jrevival.gameserver.model.actor.instance;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jrevival.commons.concurrent.ThreadPool;
import com.l2jrevival.commons.random.Rnd;
import com.l2jrevival.gameserver.model.actor.Npc;
import com.l2jrevival.gameserver.model.actor.ai.CtrlIntention;
import com.l2jrevival.gameserver.model.World;
import com.l2jrevival.gameserver.model.actor.template.NpcTemplate;
import com.l2jrevival.gameserver.network.clientpackets.Say2;
import com.l2jrevival.gameserver.network.serverpackets.ActionFailed;
import com.l2jrevival.gameserver.network.serverpackets.NpcSay;
import com.l2jrevival.gameserver.network.serverpackets.SocialAction;

public class L2AgathionInstance extends Npc
{
	protected static final Logger _log = Logger.getLogger(L2AgathionInstance.class.getName());
	
	protected static final Logger log = Logger.getLogger(L2AgathionInstance.class.getName());
	private Player _owner;
	private Boolean _follow = true;
	protected rAnimationTask _randomAniTask = null;
	//protected rAnimationTask _lostCheckTask = null;
	private final int LOST_CHECK_TIME = 60; // in seconds
	private final static int DISTANCE = 2500; // if this distance is exceeded, agathion will be teleported back
	private Future<?> _lostCheckTask;
	private Boolean _firstSpawn = true;
	private String [] agationSay = {"Hello :","Hi my friend :","Welcome again :","Hi","What a nice day :","I am back :"};
	private String [] agationLost = {"Hey, wait for me :","I am back :","Aha, there you are :","Take more care on me :","I've lost :"};
	
	/**
	 * @param objectId
	 * @param template
	 * @param owner 
	 */
	public L2AgathionInstance(int objectId, NpcTemplate template, Player owner)
	{
		super(objectId, template);
		_owner = owner;
		setRunning();
		//_lastSocialBroadcast = System.currentTimeMillis();
		startRandomAnimationTimer();

		_lostCheckTask = ThreadPool.scheduleAtFixedRate(new checkDistanceTask(owner, this), LOST_CHECK_TIME* 1000, LOST_CHECK_TIME*1000);
		getAI().startFollow(_owner, 35);
		//this.broadcastPacket(new MagicSkillUse(this, this,8435, 1, 2000, 0));
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		this.broadcastPacket(new SocialAction(this, 1));
		
		// when agathion spawn, he will say random text
		if(_firstSpawn)
		{
			broadcastPacket(new NpcSay(getObjectId(), Say2.ALL, getNpcId(), agationSay[Rnd.get(agationSay.length)]));
			_firstSpawn = false;
		}

	}
	public void setFollowStatus(boolean state)
	{
		_follow = state;
		if (_follow)
			getAI().setIntention(CtrlIntention.FOLLOW, _owner);
		else
			getAI().setIntention(CtrlIntention.IDLE, null);
	}

	@Override
	public void onAction(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	class checkDistanceTask implements Runnable
	{
		private final Player _owner;
		private final L2AgathionInstance _agathion;
		
		checkDistanceTask(Player activeChar, L2AgathionInstance pet)
		{
			_owner = activeChar;
			_agathion = pet;
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		public void run()
		{
			try
			{	if (_owner!=null && _agathion!= null)
			
				{
					if (!isInsideRadius(_owner.getX(), _owner.getY(), _owner.getZ(), DISTANCE, true, false))
					//if (_owner.getDistanceSq(_agathion)>DISTANCE)
					{
						teleportAgathionToOwner();
						broadcastPacket(new NpcSay(getObjectId(), Say2.ALL, getNpcId(), agationLost[Rnd.get(agationLost.length)]));
					}
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "Error on player [" + _owner.getName() + "] check distance task.", e);
			}
		}
	}
	
	private void teleportAgathionToOwner()
	{
		// teleport agathion to owner
		this.setFollowStatus(false);
		this.teleToLocation(_owner.getX(), _owner.getY(), _owner.getZ(), 0);
		this.setFollowStatus(true);
	}
	@Override
	public void onActionShift(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	/** Task launching the function onRandomAnimation() */
	protected class rAnimationTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if (this != _randomAniTask)
					return; // Shouldn't happen, but who knows... just to make sure every active npc has only one timer.
				
				if (!isInActiveRegion()) // NPCs in inactive region don't run this task
						return;
				
				if (!(isDead() || isStunned() || isSleeping() || isParalyzed()))
					onRandomAnimation(Rnd.get(2, 3));
				
				startRandomAnimationTimer();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}
	
	@Override
	public void startRandomAnimationTimer()
	{
		
		// Calculate the delay before the next animation
		int interval = 1000 * (Rnd.get(10, 11)); // next animation in 10-11 seconds
		
		// Create a RandomAnimation Task that will be launched after the calculated delay
		_randomAniTask = new rAnimationTask();
		ThreadPool.schedule(_randomAniTask, interval);
	}
	
	@Override
	public void onRandomAnimation(int animationId)
	{
		if (!this.isMoving())
		{
			broadcastPacket(new SocialAction(this, animationId));
		}
	}
	
	public synchronized void unSummon(Player owner)
	{
	
		if (_randomAniTask != null)
		{
			//ThreadPoolManager.getInstance().removeGeneral((RunnableScheduledFuture<?>) _randomAniTask);
			_randomAniTask = null;
		}
		
		if (_lostCheckTask != null)
		{
			_lostCheckTask.cancel(false);
			_lostCheckTask = null;
		}
		
		deleteMe();
		
		owner.setAgathion(null);

		// Drop pet from world's pet list.
		World.getInstance().removeAgathion(owner.getObjectId());
	}
	
	protected static final Logger _logPet = Logger.getLogger(L2AgathionInstance.class.getName());
}