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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardTeleport extends QuestReward {
	private static Logger logger = Logger.getLogger(QuestStep.class.getName());
	public static final String LOCATION = "_l";
	
	public QuestRewardTeleport(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		QuestLocation loc = getQuestLocation();
		ArrayList<String> addresses = new ArrayList<String>();
		String lockAddress = loc.getLockAddress();
		if (lockAddress!=null) {
			addresses.add(lockAddress);
		}
		else {
			ArrayList<String> choiceAddresses = loc.getChoiceAddresses();
			if (choiceAddresses != null) {
				addresses.addAll(loc.getChoiceAddresses());
			}
		}
		if (addresses.size()==0) {
			logger.fine("QuestLocation "+loc.getName()+" doesn't have any valid addresses!");
			return;
		}
		int r = RandomNumber.getRandom(addresses.size());
		String address = addresses.get(r);
		TileLocation tileLocation = QuestLocation.fetchTileLocationWithClearing(getGameData(),address);
		if(tileLocation == null) {
			logger.fine("QuestLocation "+loc.getName()+" doesn't have any valid locations!");
			return;
		}
		character.moveToLocation(frame, tileLocation);
	}
	
	public boolean usesLocationTag(String tag) {
		QuestLocation loc = getQuestLocation();
		return loc!=null && tag.equals(loc.getName());
	}
	
	public String getDescription() {
		return "Teleport to "+getQuestLocation().getName()+".";
	}

	public RewardType getRewardType() {
		return RewardType.Teleport;
	}

	public void setQuestLocation(QuestLocation location) {
		setString(LOCATION,location.getGameObject().getStringId());
	}
	
	public QuestLocation getQuestLocation() {
		String id = getString(LOCATION);
		if (id!=null) {
			GameObject go = getGameData().getGameObject(Long.valueOf(id));
			if (go!=null) {
				return new QuestLocation(go);
			}
		}
		return null;
	}

	public void updateIds(Hashtable<Long, GameObject> lookup) {
		updateIdsForKey(lookup,LOCATION);
	}
}