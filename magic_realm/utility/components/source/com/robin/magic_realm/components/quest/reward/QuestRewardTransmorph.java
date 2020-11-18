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
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.effect.SpellEffectContext;
import com.robin.magic_realm.components.effect.TransmorphEffect;
import com.robin.magic_realm.components.quest.DieRollType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardTransmorph extends QuestReward {
	
	public static final String DIE_ROLL = "_dr";
	public static final String REVERT_TRANSFORMATION = "_revert";
	
	public QuestRewardTransmorph(GameObject go) {
		super(go);
	}

	@Override
	public void processReward(JFrame frame, CharacterWrapper character) {
		GameWrapper gameWrapper = GameWrapper.findGame(getGameData());
		RealmComponent target = RealmComponent.getRealmComponent(character.getGameObject());
		GamePool pool = new GamePool(getGameData().getGameObjects());
		GameObject sp = pool.findFirst("name=Transform");
		SpellWrapper spell = new SpellWrapper(sp);
		spell.setString(SpellWrapper.CASTER_ID, String.valueOf(character.getGameObject().getId()));
		SpellEffectContext spellEffectContext = new SpellEffectContext(frame, gameWrapper, target, spell, character.getGameObject());
		TransmorphEffect transmorph = new TransmorphEffect("roll");
		
		if (revert()) {
			transmorph.unapply(spellEffectContext);
			return;
		}
	
		String transformBlock = "roll"+getDieRoll();
		GameObject transformAnimal = transmorph.prepareTransformation(transformBlock, target, spell, frame);
		character.setTransmorph(transformAnimal);
	}
	
	@Override
	public RewardType getRewardType() {
		return RewardType.Transmorph;
	}
	@Override
	public String getDescription() {
		if (revert()) {
			return "Character is tranformed back.";
		}
		return "Transmorphs the character.";
	}
	private boolean revert() {
		return getBoolean(REVERT_TRANSFORMATION);
	}
	private int getDieRoll() {
		String dieRoll = getString(DIE_ROLL);
		return getDieRoll(DieRollType.valueOf(dieRoll));
	}
}