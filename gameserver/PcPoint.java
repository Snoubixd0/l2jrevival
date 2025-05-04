package com.l2jrevival.gameserver;

import java.util.logging.Logger;

import com.l2jrevival.commons.random.Rnd;

import com.l2jrevival.Config;
import com.l2jrevival.gameserver.model.World;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;

/**
 * @author ProGramMoS
 */

public class PcPoint implements Runnable
{
	Logger _log = Logger.getLogger(PcPoint.class.getName());
	private static PcPoint _instance;
	
	public static PcPoint getInstance()
	{
		if (_instance == null)
		{
			_instance = new PcPoint();
		}
		
		return _instance;
	}
	
	private PcPoint()
	{
		_log.info("PcBang point event started.");
	}
	
	@Override
	public void run()
	{
		
		int score = 0;
		for (Player activeChar : World.getInstance().getPlayers())
		{			
			if (activeChar.isOnline() && !Config.REWARD_PCPOINT &&activeChar.getLevel() > Config.PCB_MIN_LEVEL)
			{
				score = Rnd.get(Config.PCB_POINT_MIN, Config.PCB_POINT_MAX);
				
				if (Rnd.get(100) <= Config.PCB_CHANCE_DUAL_POINT)
				{
					score *= 2;
					
					activeChar.addPcBangScore(score);
					
					SystemMessage sm = new SystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT_DOUBLE);
					sm.addNumber(score);
					activeChar.sendPacket(sm);
					sm = null;
					
					activeChar.updatePcBangWnd(score, true, true);
				}
				else
				{
					activeChar.addPcBangScore(score);
					
					SystemMessage sm = new SystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT);
					sm.addNumber(score);
					activeChar.sendPacket(sm);
					sm = null;
					
					activeChar.updatePcBangWnd(score, true, false);
				}
			}
			
			activeChar = null;
		}
	}
}
