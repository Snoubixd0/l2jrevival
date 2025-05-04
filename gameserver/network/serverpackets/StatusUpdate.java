package com.l2jrevival.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import com.l2jrevival.gameserver.model.WorldObject;
import com.l2jrevival.gameserver.model.actor.instance.Player;

/**
 * by Sobek
 */
public class StatusUpdate extends L2GameServerPacket
{
	public static final int LEVEL = 0x01;
	public static final int EXP = 0x02;
	public static final int STR = 0x03;
	public static final int DEX = 0x04;
	public static final int CON = 0x05;
	public static final int INT = 0x06;
	public static final int WIT = 0x07;
	public static final int MEN = 0x08;
	
	public static final int CUR_HP = 0x09;
	public static final int MAX_HP = 0x0a;
	public static final int CUR_MP = 0x0b;
	public static final int MAX_MP = 0x0c;
	
	public static final int SP = 0x0d;
	public static final int CUR_LOAD = 0x0e;
	public static final int MAX_LOAD = 0x0f;
	
	public static final int P_ATK = 0x11;
	public static final int ATK_SPD = 0x12;
	public static final int P_DEF = 0x13;
	public static final int EVASION = 0x14;
	public static final int ACCURACY = 0x15;
	public static final int CRITICAL = 0x16;
	public static final int M_ATK = 0x17;
	public static final int CAST_SPD = 0x18;
	public static final int M_DEF = 0x19;
	public static final int PVP_FLAG = 0x1a;
	public static final int KARMA = 0x1b;
	
	public static final int CUR_CP = 0x21;
	public static final int MAX_CP = 0x22;
	
	private final int _objectId;
	private final List<Attribute> _attributes;
	
	private Player _actor;
	
	static class Attribute
	{
		/**
		 * id values 09 - current health 0a - max health 0b - current mana 0c - max mana
		 */
		public int id;
		public int value;
		
		Attribute(int pId, int pValue)
		{
			id = pId;
			value = pValue;
		}
	}
	
	public StatusUpdate(WorldObject object)
	{
		_attributes = new ArrayList<>();
		_objectId = object.getObjectId();
		if(object instanceof Player)
			_actor = (Player) object;
	}

	public void addAttribute(int id, int level)
	{
		_attributes.add(new Attribute(id, level));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0e);
		if (_actor != null)
		{

			writeD(_actor.getObjectId());
			writeD(28); // all the attributes
			
			writeD(LEVEL);
			writeD(_actor.getLevel());
			writeD(EXP);
			writeD((int) _actor.getExp());
			writeD(STR);
			writeD(_actor.getSTR());
			writeD(DEX);
			writeD(_actor.getDEX());
			writeD(CON);
			writeD(_actor.getCON());
			writeD(INT);
			writeD(_actor.getINT());
			writeD(WIT);
			writeD(_actor.getWIT());
			writeD(MEN);
			writeD(_actor.getMEN());
			
			writeD(CUR_HP);
			writeD((int) _actor.getCurrentHp());
			writeD(MAX_HP);
			writeD(_actor.getMaxHp());
			writeD(CUR_MP);
			writeD((int) _actor.getCurrentMp());
			writeD(MAX_MP);
			writeD(_actor.getMaxMp());
			writeD(SP);
			writeD(_actor.getSp());
			writeD(CUR_LOAD);
			writeD(_actor.getCurrentLoad());
			writeD(MAX_LOAD);
			writeD(_actor.getMaxLoad());
			
			writeD(P_ATK);
			writeD(_actor.getPAtk(null));
			writeD(ATK_SPD);
			writeD(_actor.getPAtkSpd());
			writeD(P_DEF);
			writeD(_actor.getPDef(null));
			writeD(EVASION);
			writeD(_actor.getEvasionRate(null));
			writeD(ACCURACY);
			writeD(_actor.getAccuracy());
			writeD(CRITICAL);
			writeD(_actor.getCriticalHit(null, null));
			writeD(M_ATK);
			writeD(_actor.getMAtk(null, null));
			
			writeD(CAST_SPD);
			writeD(_actor.getMAtkSpd());
			writeD(M_DEF);
			writeD(_actor.getMDef(null, null));
			writeD(PVP_FLAG);
			writeD(_actor.getPvpFlag());
			writeD(KARMA);
			writeD(_actor.getKarma());
			writeD(CUR_CP);
			writeD((int) _actor.getCurrentCp());
			writeD(MAX_CP);
			writeD(_actor.getMaxCp());
			
		}
		else
		{
			
			writeD(_objectId);
			writeD(_attributes.size());
			
			for (int i = 0; i < _attributes.size(); i++)
			{
				Attribute temp = _attributes.get(i);
				
				writeD(temp.id);
				writeD(temp.value);
			}
		}
	}
}