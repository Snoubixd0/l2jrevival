package com.l2jrevival.gameserver.handler.itemhandlers;
 
import com.l2jrevival.gameserver.data.xml.AugmentationData;
import com.l2jrevival.gameserver.network.clientpackets.Say2;
import com.l2jrevival.gameserver.handler.IItemHandler;
import com.l2jrevival.gameserver.model.L2Augmentation;
import com.l2jrevival.gameserver.model.actor.Playable;
import com.l2jrevival.gameserver.model.actor.instance.Player;
import com.l2jrevival.gameserver.model.item.instance.ItemInstance;
import com.l2jrevival.gameserver.model.itemcontainer.Inventory;
import com.l2jrevival.gameserver.network.serverpackets.CreatureSay;
import com.l2jrevival.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jrevival.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jrevival.gameserver.network.serverpackets.SocialAction;
 
/**
 * @author RKolibri
 */
public class FastAugmentation implements IItemHandler {
    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
        Player player = (Player) playable;
        ItemInstance weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
        L2Augmentation topLs = AugmentationData.getInstance().generateRandomAugmentation(76, 3);
        L2Augmentation highLs = AugmentationData.getInstance().generateRandomAugmentation(76, 2);
        L2Augmentation midLs = AugmentationData.getInstance().generateRandomAugmentation(76, 1);
        L2Augmentation noGradeLs = AugmentationData.getInstance().generateRandomAugmentation(76, 0);
        InventoryUpdate iu = new InventoryUpdate();
        int ls = item.getItemId();
        if (weapon == null) {
            player.sendMessage("You have to equip a weapon first.");
            return;
        }
        if (weapon.getItem().getCrystalType().getId() <= 3) {
            player.sendMessage("Fast Augmentation available only for A and S grade  Weapons!");
            return;
        }
        if (weapon.isHeroItem()) {
            player.sendMessage("Hero weapons can't be augmented!");
            return;
        }
        if (weapon.isAugmented()) {
            removeAug(playable);
        } else {
            player.destroyItem("Consume", item.getObjectId(), 1, null, false);
            L2Augmentation augmentation;
            if (ls == 8762) {
                augmentation = topLs;
            } else if (ls == 8762) {
                augmentation = highLs;
            } else if (ls == 8752) {
                augmentation = midLs;
            } else if (ls == 8742) {
                augmentation = noGradeLs;
            } else {
                return;
            }
            weapon.setAugmentation(augmentation);
            iu.addModifiedItem(weapon);
            player.sendPacket(iu);
            player.broadcastUserInfo();
            if (weapon.getAugmentation().getSkill() == null) {
                player.sendMessage("No luck try again!");
            } else {
                checkAugmentResult(playable);
            }
        }
    }
 
    public static boolean removeAug(Playable playable) {
        Player player = (Player) playable;
        ItemInstance weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
        InventoryUpdate iu = new InventoryUpdate();
        weapon.getAugmentation().removeBonus(player);
        weapon.removeAugmentation();
        iu.addModifiedItem(weapon);
        player.sendPacket(iu);
        player.broadcastUserInfo();
        return true;
    }
 
    private static void checkAugmentResult(Playable playable) {
        Player player = (Player) playable;
        ItemInstance weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
        String name = weapon.getAugmentation().getSkill().getName();
        boolean isChance = weapon.getAugmentation().getSkill().isChance();
        boolean isActive = weapon.getAugmentation().getSkill().isActive();
        boolean isPassive = weapon.getAugmentation().getSkill().isPassive() && !isChance;
        InventoryUpdate iu = new InventoryUpdate();
 
        String type;
        if (isChance) {
            type = "CHANCE";
        } else if (isActive) {
            type = "ACTIVE";
        } else if (isPassive) {
            type = "PASSIVE";
        } else {
            return;
        } 
        
        player.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "[" + type + "]", "You got " + name));
        sendMessage(player, type + " : You got " + name);
        player.broadcastPacket(new SocialAction(player, 3));
        player.disarmWeapons();
        iu.addModifiedItem(weapon);
        player.sendPacket(iu);
        player.broadcastUserInfo();
    }
 
    public static void sendMessage(final Player player, final String message) {
        player.sendPacket(new ExShowScreenMessage(message, 3000, ExShowScreenMessage.SMPOS.TOP_CENTER, true));
        player.sendMessage(message);
    }
 
}