/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RoleClass {
	private static RoleClass instance = new RoleClass();

			//   1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
	private static int[] Werewolf = new int[] 
			{ 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, };
	private static int[] Seer = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Medium = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Possessed = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Bodyguard = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Owlman = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Freemason = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, };
	private static int[] Werehamster = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Mythomaniac = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	private static int[] Devil = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Hunter = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Apothecary = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Matchmaker = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Witch = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Scryer = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Oracle = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Hierophant = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Magistrate = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Gravedigger = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Crone = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] CultLeader = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Warlock = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Sorceress = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Mystic = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Fool = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Thief = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	private static int[] Toughgay = new int[] 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

	private HashMap<ROLE, int[]> Roles = new HashMap<ROLE, int[]>();
	public int MaxPlayers = 24;
	public final int MinPlayers = 5;
	public boolean Random = false;
	public boolean IncludeWerewolf = false;

	private RoleClass() {}
	
	public static RoleClass getInstance() {  
        return instance;  
    }

	private void initRole() {
		Roles.clear();
		Roles.put(ROLE.Werewolf, Werewolf);
		Roles.put(ROLE.Seer, Seer);
		Roles.put(ROLE.Medium, Medium);
		Roles.put(ROLE.Possessed, Possessed);
		Roles.put(ROLE.Bodyguard, Bodyguard);
		Roles.put(ROLE.Owlman, Owlman);
		Roles.put(ROLE.Freemason, Freemason);
		Roles.put(ROLE.Werehamster, Werehamster);
		Roles.put(ROLE.Mythomaniac, Mythomaniac);
		Roles.put(ROLE.Devil, Devil);
		Roles.put(ROLE.Hunter, Hunter);
		Roles.put(ROLE.Apothecary, Apothecary);
		Roles.put(ROLE.Matchmaker, Matchmaker);
		Roles.put(ROLE.Witch, Witch);
		Roles.put(ROLE.Scryer, Scryer);
		Roles.put(ROLE.Oracle, Oracle);
		Roles.put(ROLE.Hierophant, Hierophant);
		Roles.put(ROLE.Magistrate, Magistrate);
		Roles.put(ROLE.Gravedigger, Gravedigger);
		Roles.put(ROLE.Crone, Crone);
		Roles.put(ROLE.CultLeader, CultLeader);
		Roles.put(ROLE.Warlock, Warlock);
		Roles.put(ROLE.Sorceress, Sorceress);
		Roles.put(ROLE.Mystic, Mystic);
		Roles.put(ROLE.Fool, Fool);
		Roles.put(ROLE.Thief, Thief);
		Roles.put(ROLE.Toughgay, Toughgay);
	}

	// 役職の人数取得
	public int get(ROLE role, int number) {
		if (!Roles.containsKey(role))
			return -1;
		if (Roles.get(role).length <= number)
			return -1;
		return Roles.get(role)[number];
	}

	// 役職の人数設定
	public void set(ROLE role, int number, int value) {
		if (!Roles.containsKey(role))
			return;
		if (Roles.get(role).length <= number)
			return;
		int[] values = Roles.get(role);
		values[number] = value;
		Roles.put(role, values);
	}

	// 役職の読み込み
	public void loadRoles(Context context) {
		initRole();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		MaxPlayers = sharedPreferences.getInt("max_of_player", MaxPlayers);
		Random = sharedPreferences.getBoolean("randam", false);
		IncludeWerewolf = sharedPreferences.getBoolean("include_werewolf", false);
		for (ROLE role : ROLE.values()) {
			if (!Roles.containsKey(role))
				continue;
			int[] values = new int[MaxPlayers + 1];
			for (int i = MinPlayers; i <= MaxPlayers; i++) {
				values[i] = sharedPreferences.getInt(
						role.toString() + String.valueOf(i), get(role, i));
			}
			Roles.put(role, values);
		}
	}

	// 最大プレイ人数を変更
	public void changeMax(int max_of_player) {
		for (ROLE role : ROLE.values()) {
			if (!Roles.containsKey(role))
				continue;
			int[] values = new int[max_of_player + 1];
			for (int i = MinPlayers; i < Roles.get(role).length; i++)
				values[i] = Roles.get(role)[i];
			for (int i = Roles.get(role).length; i <= max_of_player; i++)
				values[i] = Roles.get(role)[Roles.get(role).length - 1];
			Roles.put(role, values);
		}
		MaxPlayers = max_of_player;
	}
}
