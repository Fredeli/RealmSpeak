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
package com.robin.magic_realm.components.quest;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellCreator {
	public static SpellWrapper CreateSpellWrapper(String spell, CharacterWrapper character) {
		GameObject spellGo = character.getGameData().getGameObjectByNameIgnoreCase(spell);
		if (spellGo == null) return null;
		SpellWrapper spellWrapper = new SpellWrapper(spellGo);
		spellWrapper.setString(SpellWrapper.CASTER_ID, String.valueOf(character.getGameObject().getId()));
		spellWrapper.setString(SpellWrapper.SPELL_ALIVE,"");
		return spellWrapper;
	}
}