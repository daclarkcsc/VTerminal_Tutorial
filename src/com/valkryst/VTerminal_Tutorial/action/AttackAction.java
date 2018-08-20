package com.valkryst.VTerminal_Tutorial.action;

import com.valkryst.VDice.DiceRoller;
import com.valkryst.VTerminal_Tutorial.Message;
import com.valkryst.VTerminal_Tutorial.entity.Entity;
import com.valkryst.VTerminal_Tutorial.gui.controller.GameController;
import com.valkryst.VTerminal_Tutorial.item.Equipment;
import com.valkryst.VTerminal_Tutorial.item.EquipmentSlot;
import com.valkryst.VTerminal_Tutorial.item.Inventory;
import com.valkryst.VTerminal_Tutorial.statistic.BoundStat;

public class AttackAction extends Action {
    /** The target. */
    private final Entity target;

    /** The result of the roll which determines the type of attack to perform. */
    private final int actionRoll;

    /**
     * Constructs a new AttackAction.
     *
     * @param target
     *          The target.
     */
    public AttackAction(final Entity target) {
        this.target = target;

        final DiceRoller diceRoller = new DiceRoller();
        diceRoller.addDice(20, 1);
        actionRoll = diceRoller.roll();
    }

    @Override
    public void perform(final GameController controller, final Entity self) {
        if (controller == null || self == null || target == null) {
            return;
        }

        int damage = 0;
        Message message = new Message();

        // Critical Miss
        if (actionRoll == 1) {
            damage = calculateDamage(self, self);

            final BoundStat health = (BoundStat) self.getStat("Health");
            health.setValue(health.getValue() - damage);

            controller.displayMessage(getCriticalMissMessage(self, damage));
            super.perform(controller, self);

            if (health.getValue() == health.getMinValue()) {
                new DeathAction().perform(controller, self);
            }

            return;
        }

        // Miss
        if (actionRoll > 1 && actionRoll < 5) {
            message = getAttackMissMessage(self);
        }

        // Normal Attack
        if (actionRoll >= 5 && actionRoll <= 16) {
            damage = calculateDamage(self, target);
            message = getAttackMessage(self, damage);
        }

        // Double Attack
        if (actionRoll > 16 && actionRoll < 20) {
            damage = calculateDamage(self, target) * 2;
            message = getDoubleAttackMessage(self, damage);
        }

        // Critical Attack
        if (actionRoll == 20) {
            damage = calculateDamage(self, target) * 3;
            message = getCriticalAttackMessage(self, damage);
        }

        final BoundStat health = (BoundStat) target.getStat("Health");

        if (damage > 0) {
            health.setValue(health.getValue() - damage);
            controller.displayMessage(message);
        } else {
            controller.displayMessage(getDodgeMessage(self));
        }

        super.perform(controller, self);

        if (health.getValue() == health.getMinValue()) {
            new DeathAction().perform(controller, target);
        }
    }

    /**
     * Calculates the damage to deal.
     *
     * @param self
     *        The attacking creature.
     *
     * @param target
     *        The creature being attacked.
     *
     * @return
     *        The damage dealt.
     */
    private int calculateDamage(final Entity self, final Entity target) {
        // Calculate Target Armor
        final Inventory targetInventory = target.getInventory();

        int armor = 0;

        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            final Equipment equipment = targetInventory.getEquipment(slot);
            armor += (equipment == null ? 0 : equipment.getArmor());
        }

        // Calculate Damage
        final Inventory selfInventory = self.getInventory();
        final Equipment mainHand = selfInventory.getEquipment(EquipmentSlot.MAIN_HAND);
        final Equipment offHand = selfInventory.getEquipment(EquipmentSlot.OFF_HAND);

        int damage = 0;
        damage+= (mainHand == null ? 0 : mainHand.rollDamage());
        damage+= (offHand == null ? 0 : offHand.rollDamage());

        // Calculate Result
        final int result = damage - armor;
        return result > 0 ? result : 0;
    }

    /**
     * Constructs a critical miss message.
     *
     * @param self
     *          The creature that missed.
     *
     * @param damage
     *          The damage dealt.
     *
     * @return
     *          The message.
     */
    private Message getCriticalMissMessage(final Entity self, final int damage) {
        return new Message().appendEntityName(self)
                               .append(" missed and attacked itself for " + damage + " damage.");
    }

    /**
     * Constructs an attack missed message.
     *
     * @param self
     *          The attacking creature.
     *
     * @return
     *          The message.
     */
    private Message getAttackMissMessage(final Entity self) {
        return new Message().appendEntityName(self).append(" missed it's target.");
    }

    /**
     * Constructs a no-damage message.
     *
     * @param self
     *          The attacking creature.
     *
     * @return
     *          The message.
     */
    private Message getDodgeMessage(final Entity self) {
        return new Message().appendEntityName(target)
                .append(" dodged ")
                .appendEntityName(self)
                .append("'s attack.");
    }

    /**
     * Constructs an attack message.
     *
     * @param self
     *          The attacking creature.
     *
     * @param damage
     *          The damage dealt.
     *
     * @return
     *          The message.
     */
    private Message getAttackMessage(final Entity self, final int damage) {
        return new Message().appendEntityName(self)
                               .append(" attacked ")
                               .appendEntityName(target)
                               .append(" for " + damage + " damage.");
    }

    /**
     * Constructs a double-attack message.
     *
     * @param self
     *          The attacking creature.
     *
     * @param damage
     *          The damage dealt.
     *
     * @return
     *          The message.
     */
    private Message getDoubleAttackMessage(final Entity self, final int damage) {
        return new Message().appendEntityName(self)
                               .append(" landed a heavy attack against ")
                               .appendEntityName(target)
                               .append(" for " + damage + " damage.");
    }

    /**
     * Constructs a critical attack message.
     *
     * @param self
     *          The attacking creature.
     *
     * @param damage
     *          The damage dealt.
     *
     * @return
     *          The message.
     */
    private Message getCriticalAttackMessage(final Entity self, final int damage) {
        return new Message().appendEntityName(self)
                               .append(" landed a critical attack against ")
                               .appendEntityName(target)
                               .append(" for " + damage + " damage.");
    }
}