package com.l2jrevival.gameserver.network.serverpackets;

import com.l2jrevival.gameserver.model.pledge.Clan;
import com.l2jrevival.gameserver.model.pledge.SubPledge;

/**
 * @author -Wooden-
 */
public class PledgeReceiveSubPledgeCreated extends L2GameServerPacket
{
	private final SubPledge _subPledge;
	private final Clan _clan;
	
	public PledgeReceiveSubPledgeCreated(SubPledge subPledge, Clan clan)
	{
		_subPledge = subPledge;
		_clan = clan;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x3f);
		
		writeD(0x01);
		writeD(_subPledge.getId());
		writeS(_subPledge.getName());
		writeS(_clan.getSubPledgeLeaderName(_subPledge.getId()));
	}
}