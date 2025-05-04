package com.l2jrevival.gameserver.skills.effects;

import com.l2jrevival.gameserver.model.L2Effect;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.pledge.Clan;
import com.l2jrevival.gameserver.network.SystemMessageId;
import com.l2jrevival.gameserver.network.serverpackets.SystemMessage;
import com.l2jrevival.gameserver.skills.AbnormalEffect;
import com.l2jrevival.gameserver.skills.Env;
import com.l2jrevival.gameserver.templates.skills.L2EffectType;

/**
 * @author ZaKaX (Ghost @ L2D)
 */
public class EffectClanGate extends L2Effect
{
	public EffectClanGate(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
		if (getEffected() instanceof Player)
		{
			Clan clan = ((Player) getEffected()).getClan();
			if (clan != null)
			{
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.COURT_MAGICIAN_CREATED_PORTAL);
				clan.broadcastToOtherOnlineMembers(msg, ((Player) getEffected()));
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.CLAN_GATE;
	}
}