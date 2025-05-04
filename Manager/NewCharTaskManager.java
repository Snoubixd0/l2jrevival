package com.l2jrevival.Manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.l2jrevival.commons.concurrent.ThreadPool;

import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.instance.Player;

/**
 * @author Baggos
 */
public final class NewCharTaskManager implements Runnable
{
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();

	protected NewCharTaskManager()
	{
		// Run task each 10 second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}

	public final void add(Player player)
	{
		_players.put(player, System.currentTimeMillis());
	}

	public final void remove(Creature player)
	{
		_players.remove(player);
	}

	@Override
	public final void run()
	{
		if (_players.isEmpty())
			return;

		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			final Player player = entry.getKey();

			if (player.getMemos().getLong("newEndTime") < System.currentTimeMillis())
			{
				Player.removeNewChar(player);
				remove(player);
			}
		}
	}

	public static final NewCharTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final NewCharTaskManager _instance = new NewCharTaskManager();
	}
}
