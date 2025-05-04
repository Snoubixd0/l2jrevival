package com.l2jrevival.gameserver.network.serverpackets;

import com.l2jrevival.Config;
import com.l2jrevival.events.CTF;
import com.l2jrevival.events.TvT;
import com.l2jrevival.gameserver.instancemanager.CastleManager;
import com.l2jrevival.gameserver.model.actor.Attackable;
import com.l2jrevival.gameserver.model.actor.Creature;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.entity.Siege;
import com.l2jrevival.gameserver.model.entity.Siege.SiegeSide;
import com.l2jrevival.gameserver.model.pledge.Clan;
import Base.AutoFarm.AutofarmPlayerRoutine;




public class Die extends L2GameServerPacket
{
	private final Creature _activeChar;
	private final int _charObjId;
	private final boolean _fake;
	
	private boolean _sweepable;
	private boolean _allowFixedRes;
	private Clan _clan;
	private boolean _canEvent;
	
	public Die(Creature cha)
	{
		_activeChar = cha;
		_charObjId = cha.getObjectId();
		_fake = !cha.isDead();
		
		if (cha instanceof Player)
		{
			Player player = (Player) cha;
						final AutofarmPlayerRoutine bot = player.getBot();
			_allowFixedRes = player.getAccessLevel().allowFixedRes();
			_clan = player.getClan();
			_canEvent = !(((TvT.is_started() || TvT.is_teleport()) && player._inEventTvT) || ((CTF.is_started() || CTF.is_teleport()) && player._inEventCTF) || cha.isInArenaEvent());
					
						
				    	if (player.isAutoFarm())
				    	{
				    		bot.stop();
				    		player.setAutoFarm(false);
				    	}
			
		}
		else if (cha instanceof Attackable)
			_sweepable = ((Attackable) cha).isSpoiled();
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_fake)
			return;
		
		writeC(0x06);
		writeD(_charObjId);
		writeD(_canEvent ? 0x01 : 0); // 6d 00 00 00 00 - to nearest village
		
		if (_canEvent && _clan != null)
		{
			SiegeSide side = null;
			
			final Siege siege = CastleManager.getInstance().getSiege(_activeChar);
			if (siege != null)
				side = siege.getSide(_clan);
			
			writeD((_clan.hasHideout()) ? 0x01 : 0x00); // to clanhall
			writeD((_clan.hasCastle() || side == SiegeSide.OWNER || side == SiegeSide.DEFENDER) ? 0x01 : 0x00); // to castle
			writeD((side == SiegeSide.ATTACKER && _clan.getFlag() != null) ? 0x01 : 0x00); // to siege HQ
		}
		else
		{
			writeD(0x00); // to clanhall
			writeD(0x00); // to castle
			writeD(0x00); // to siege HQ
		}
		
		writeD((_sweepable) ? 0x01 : 0x00); // sweepable (blue glow)
		if (Config.CUSTOM_TELEGIRAN_ON_DIE)
			writeD(_canEvent ? 0x01 : 0x00); // FIXED
		else
			writeD(_allowFixedRes ? 0x01 : 0x00); // FIXED
			
	}
}