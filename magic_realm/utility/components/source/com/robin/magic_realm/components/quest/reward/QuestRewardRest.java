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
package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.swing.ChitRestManager;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardRest extends QuestReward {
	
	public static final String AMOUNT = "_amount";
	
	public QuestRewardRest(GameObject go) {
		super(go);
	}

	@Override
	public void processReward(JFrame frame, CharacterWrapper character) {
		ChitRestManager rester = new ChitRestManager(frame,character,getAmount());
		rester.setVisible(true);
	}
	
	@Override
	public RewardType getRewardType() {
		return RewardType.Rest;
	}
	@Override
	public String getDescription() {
		return "Character can rest " +getAmount() +" asterisks.";
	}
	private int getAmount() {
		return getInt(AMOUNT);
	}	
}