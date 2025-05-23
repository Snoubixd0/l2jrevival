package com.l2jrevival.gameserver.VoteSystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VoteSiteInfo {
    VoteSite voteSite();
    String apiKey();
}