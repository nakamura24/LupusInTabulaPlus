/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import jp.game.lupusintabula.GameRuleClass.PlayerClass;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class VoteComparator implements Comparator {
	public int compare(Object obj1, Object obj2) {
		return ((PlayerClass) obj2).Vote - ((PlayerClass) obj1).Vote;
	}
}
