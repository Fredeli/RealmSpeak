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
package com.robin.magic_realm.components.table;

import java.util.Collection;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TradeInfo;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class MeetingNeutral extends Meeting {
	public MeetingNeutral(JFrame frame,TradeInfo tradeInfo,GameObject merchandise,Collection<RealmComponent> hireGroup) {
		super(frame,tradeInfo,merchandise,hireGroup);
	}
	public String getMeetingTableName() {
		return "Neutral";
	}
	public String applyOne(CharacterWrapper character) {
		MeetingFriendly table = new MeetingFriendly(getParentFrame(),tradeInfo,merchandise,hireGroup);
		return doOpportunity(character,table);
	}

	public String applyTwo(CharacterWrapper character) {
		processPrice(character,3);
		return "Price x 3";
	}

	public String applyThree(CharacterWrapper character) {
		processPrice(character,4);
		return "Price x 4";
	}

	public String applyFour(CharacterWrapper character) {
		return "No Deal";
	}

	public String applyFive(CharacterWrapper character) {
		return "No Deal";
	}

	public String applySix(CharacterWrapper character) {
		MeetingUnfriendly table = new MeetingUnfriendly(getParentFrame(),tradeInfo,merchandise,hireGroup);
		setNewTable(table);
		return "Trouble!";
	}
}