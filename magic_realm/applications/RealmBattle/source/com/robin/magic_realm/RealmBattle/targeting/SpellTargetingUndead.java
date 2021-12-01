/* 
 * RealmSpeak is the Java application for playing the board game Magic Realm.
 * Copyright (c) 2005-2015 Robin Warren
 * E-mail: robin@dewkid.com
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 *
 * http://www.gnu.org/licenses/
 */
package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.*;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingUndead extends SpellTargetingMultiple {

	public SpellTargetingUndead(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		BattleGroup bg = battleModel.getParticipantsBattleGroup(activeParticipant);
		ArrayList<RealmComponent> otherOpponents = combatFrame.findCanBeSeen(battleModel.getAllOtherBattleParticipants(bg,true,combatFrame.allowsTreachery()),true);
		otherOpponents = CombatSheet.filterNativeFriendly(activeParticipant, otherOpponents);
		for (RealmComponent rc : otherOpponents) {
			if (rc.isMonster() && rc.getGameObject().hasThisAttribute(Constants.UNDEAD)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}