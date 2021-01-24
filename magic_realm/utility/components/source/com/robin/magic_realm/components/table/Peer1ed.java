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

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class Peer1ed extends Search {
	public Peer1ed(JFrame frame) {
		this(frame,null);
	}
	public Peer1ed(JFrame frame,ClearingDetail clearing) {
		super(frame,clearing);
	}
	public String getTableName(boolean longDescription) {
		return "Peer"+(longDescription?"\n(Hidden Paths, Hidden Enemies)":"");
	}
	public String getTableKey() {
		return "Peer";
	}
	
	public String applyOne(CharacterWrapper character) {
		// Find chits
		return doDiscoverChits(character);
	}

	public String applyTwo(CharacterWrapper character) {
		// Glimpse
		return doGlimpse(character);
	}

	public String applyThree(CharacterWrapper character) {
		// Hidden Enemies
		return doHiddenEnemies(character);
	}

	public String applyFour(CharacterWrapper character) {
		// Hidden Enemies
		return doHiddenEnemies(character);
	}

	public String applyFive(CharacterWrapper character) {
		// Glimpse
		return doGlimpse(character);
	}

	public String applySix(CharacterWrapper character) {
		// Nothing
		return "Nothing";
	}

	@Override
	protected ArrayList<ImageIcon> getHintIcons(CharacterWrapper character) {
		ArrayList<ImageIcon> list = new ArrayList<>();
		for(RealmComponent rc:getAllDiscoverableChits(character,true)) {
			list.add(getIconForSearch(rc));
		}
		return list;
	}
}