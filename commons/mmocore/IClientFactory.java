package com.l2jrevival.commons.mmocore;

/**
 * @author KenM
 * @param <T>
 */
public interface IClientFactory<T extends MMOClient<?>>
{
	public T create(final MMOConnection<T> con);
}