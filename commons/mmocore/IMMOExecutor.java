package com.l2jrevival.commons.mmocore;

/**
 * @author KenM
 * @param <T>
 */
public interface IMMOExecutor<T extends MMOClient<?>>
{
	public void execute(ReceivablePacket<T> packet);
}