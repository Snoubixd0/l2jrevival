package com.l2jrevival.gameserver.model.actor;

import java.util.Collection;
import com.l2jrevival.commons.concurrent.ThreadPool;
import com.l2jrevival.gameserver.model.World; 
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.network.serverpackets.StatusUpdate;


/**
 * @author Sobek
 *
 */
public class StatusRealTime
{
	public StatusRealTime()
	{
		ThreadPool.scheduleAtFixedRate(new LoadStatus(), 500, 500);
	}
	
	public static StatusRealTime getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final StatusRealTime INSTANCE = new StatusRealTime();
	}
	
}

	


class LoadStatus implements Runnable
 { @Override public void run () { try 
 { Collection<Player> allPlayers = World.getInstance().getPlayers(); for 
 ( Player player : allPlayers ) 
 { try { if ( player != null ) if ( player .getTarget () != null && player .getTarget () instanceof Player ) { 
		Player target = ( Player ) player .getTarget (); 
		updateStatus ( target , player ); } } catch ( Exception e ) { 
		e.printStackTrace (); } } } catch ( Exception e ) { 
	    e.printStackTrace (); } } 
		private static void updateStatus ( Player _target , Player player ) { StatusUpdate su = new StatusUpdate ( _target ); 
		su.addAttribute ( StatusUpdate.CUR_HP, ( int ) _target .getCurrentHp ()); 
		su.addAttribute ( StatusUpdate.CUR_CP, ( int ) _target .getCurrentCp ()); 
		su.addAttribute ( StatusUpdate.MAX_HP, _target .getMaxHp ()); 
		su.addAttribute ( StatusUpdate.MAX_CP, _target.getMaxCp ());  


		    _target . getMaxCp (); 
		player . sendPacket ( su ); } }
