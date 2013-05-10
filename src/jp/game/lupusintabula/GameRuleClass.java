/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import java.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GameRuleClass {
	private static GameRuleClass instance = new GameRuleClass();
	
	public class PlayerClass {
		public String Player;
		public boolean Gender;
		public boolean Playing;
		public ROLE Role;
		public STATUS Status;
		public int Vote;
		public boolean Voteable;

		public String SelectedPlayer;
		public boolean PlayerDied;
		public boolean Lover;
		public boolean Hilling;
		public boolean Poison;

		public PlayerClass(String player, boolean gender) {
			this.Player = player;
			this.Gender = gender;
			this.Playing = false;
			this.Role = ROLE.Villager;
			this.Status = STATUS.Alive;
			this.Vote = 0;
			this.Voteable = false;

			this.SelectedPlayer = null;
			this.PlayerDied = false;
			this.Lover = false;
			this.Hilling = true;
			this.Poison = true;
		}
	}
	private HashMap<String, PlayerClass> Players = new HashMap<String, PlayerClass>();
	private ArrayList<String> Keys = new ArrayList<String>();
	private int Days = 1;
	private ArrayList<String> DiedPlayers = new ArrayList<String>();
	private String LynchedPlayer = "";
	private Random randam = new Random();
	public static final int GameOver_Continue = 0;
	public static final int GameOver_Werewolves_win = -1;
	public static final int GameOver_Villagers_win = -2;
	public static final int GameOver_Werehamster_win = -3;
	public static final int GameOver_Devil_win = -4;
	public static final int GameOver_Fool_win = -5;
	public static final int GameOver_Lovers_win = -6;
	public static final int GameOver_Cultists_win = -7;
	
	private GameRuleClass() {}
	
	public static GameRuleClass getInstance() {  
        return instance;  
    }

	// 全プレイヤーを取得
	public ArrayList<String> getAllPlayers() {
		return this.Keys;
	}
	
	// プレイヤー追加
	public void addPlayer(String player, boolean gender) {
		this.Keys.add(player);
		this.Players.put(player, new PlayerClass(player, gender));
	}

	// プレイヤー削除
	public void removePlayer(String player) {
		this.Keys.remove(player);
		this.Players.remove(player);
	}

	// プレイヤークリア
	public void clearPlayers() {
		this.Keys.clear();
		this.Players.clear();
	}

	// プレイヤーの読み込み
	public void loadPlayers(Context context) {
		clearPlayers();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int number_of_player = sharedPreferences.getInt("number_of_player", 0);
		for (int i = 0; i < number_of_player; i++) {
			String player_name = sharedPreferences.getString("player_name"
					+ String.valueOf(i), null);
			boolean player_gender = sharedPreferences.getBoolean(
					"player_gender" + String.valueOf(i), true);
			boolean player_playing = sharedPreferences.getBoolean(
					"player_playing" + String.valueOf(i), false);
			addPlayer(player_name, player_gender);
			setPlaying(player_name, player_playing);
		}
	}

	// プレイするプレイヤーを設定
	public void setPlayingPlayers(ArrayList<String> players) {
		for (String player : this.Keys) {
			PlayerClass object = this.Players.get(player);
			object.Playing = false;
			this.Players.put(player, object);
		}
		for (String player : players) {
			PlayerClass object = this.Players.get(player);
			object.Playing = true;
			this.Players.put(player, object);
		}
	}

	public void initialize(Context context) {
		Days = 1;
		// プレイヤーの読み込み
		loadPlayers(context);

		// 役職の読み込み
		RoleClass Roles = RoleClass.getInstance();
		Roles.loadRoles(context);

		// プレイヤーに役職を割り振る
		distributePlayers();
	}

	// プレイヤーに役職を割り振る
	private void distributePlayers() {
		RoleClass Roles = RoleClass.getInstance();
		for (ROLE role : ROLE.values()) {
			if (Roles.get(role, Roles.MinPlayers) < 0)
				continue;
			int number = Roles.get(role, getPlayers().size());
			if (Roles.Random) {
				switch (role) {
				case Werewolf:
					if (Roles.IncludeWerewolf)
						number = randam.nextInt(number) + 1;
					break;
				case Freemason:
					if (number > 2)
						number = 2 + randam.nextInt(number - 1);
					break;
				default:
					number = randam.nextInt(number + 1);
					break;
				}
			}
			for (int i = 0; i < number;) {
				int select = randam.nextInt(getPlayers().size());
				String player = getPlayers().get(select);
				if (getRole(player) == ROLE.Villager) {
					setRole(player, role);
					i++;
				}
			}
		}
	}


	// プレイするプレイヤーを取得
	public ArrayList<String> getPlayers() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : this.Keys) {
			if (this.Players.get(player).Playing)
				players.add(this.Players.get(player).Player);
		}
		return players;
	}

	// 生存しているプレイヤーを取得
	public ArrayList<String> getAlivePlayers() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : this.Keys) {
			if (this.Players.get(player).Playing
					&& this.Players.get(player).Status == STATUS.Alive)
				players.add(this.Players.get(player).Player);
		}
		return players;
	}

	// 投票でソート
	@SuppressWarnings("unchecked")
	public ArrayList<String> sortVotePlayers() {
		ArrayList<String> players = new ArrayList<String>();
		Object[] objects = this.Players.values().toArray();
		Arrays.sort(objects, new VoteComparator());
		for (int i = 0; i < this.Players.size(); i++) {
			players.add(((PlayerClass) objects[i]).Player);
		}
		return players;
	}

	public boolean getGender(String player) {
		return this.Players.get(player).Gender;
	}

	public boolean getPlaying(String player) {
		return this.Players.get(player).Playing;
	}

	public void setPlaying(String player, boolean Playing) {
		PlayerClass object = this.Players.get(player);
		object.Playing = Playing;
		this.Players.put(player, object);
	}

	public ROLE getRole(String player) {
		return this.Players.get(player).Role;
	}

	public void setRole(String player, ROLE Role) {
		PlayerClass object = this.Players.get(player);
		object.Role = Role;
		this.Players.put(player, object);
	}

	public STATUS getStatus(String player) {
		return this.Players.get(player).Status;
	}

	public void setStatus(String player, STATUS Status) {
		PlayerClass object = this.Players.get(player);
		object.Status = Status;
		this.Players.put(player, object);
	}

	public int getVote(String player) {
		return this.Players.get(player).Vote;
	}

	public void inclementVote(String player) {
		PlayerClass object = this.Players.get(player);
		object.Vote++;
		this.Players.put(player, object);
	}

	public void clearVotes() {
		for (String player : this.Keys) {
			PlayerClass object = this.Players.get(player);
			object.Vote = 0;
			this.Players.put(player, object);
		}
	}

	public boolean getVoteable(String player) {
		return this.Players.get(player).Voteable;
	}

	public void setVoteable(String player, boolean value) {
		PlayerClass object = this.Players.get(player);
		object.Voteable = value;
		this.Players.put(player, object);
	}

	public void clearVoteables() {
		for (String player : this.Keys) {
			setVoteable(player, false);
		}
	}

	public void setVoteable(String player) {
		setVoteable(player, true);
	}

	public boolean hasLover(String player) {
		return this.Players.get(player).Lover;
	}

	public void setLover(String player) {
		PlayerClass object = this.Players.get(player);
		object.Lover = true;
		this.Players.put(player, object);
	}

	public String getSelectedPlayer(String player) {
		return this.Players.get(player).SelectedPlayer;
	}

	public void setSelectedPlayer(String player, String SelectedPlayer) {
		PlayerClass object = this.Players.get(player);
		object.SelectedPlayer = SelectedPlayer;
		this.Players.put(player, object);
	}

	public boolean getDiedPlayer(String player) {
		return this.Players.get(player).PlayerDied;
	}

	public void setDiedPlayer(String player, boolean PlayerDied) {
		PlayerClass object = this.Players.get(player);
		object.PlayerDied = PlayerDied;
		this.Players.put(player, object);
	}

	public boolean hasHilling(String player) {
		return this.Players.get(player).Hilling;
	}

	public void useHilling(String player) {
		PlayerClass object = this.Players.get(player);
		object.Hilling = false;
		this.Players.put(player, object);
	}

	public boolean hasPoison(String player) {
		return this.Players.get(player).Poison;
	}

	public void usePoison(String player) {
		PlayerClass object = this.Players.get(player);
		object.Poison = false;
		this.Players.put(player, object);
	}

	public ArrayList<String> getWerewolves() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : getAlivePlayers()) {
			if (this.Players.get(player).Role == ROLE.Werewolf)
				players.add(player);
		}
		return players;
	}

	public ArrayList<String> getMasons() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : getAlivePlayers()) {
			if (this.Players.get(player).Role == ROLE.Freemason)
				players.add(player);
		}
		return players;
	}

	public ArrayList<String> getLovers() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : getAlivePlayers()) {
			if (this.Players.get(player).Lover)
				players.add(player);
		}
		return players;
	}

	public ArrayList<String> getCultists() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : getAlivePlayers()) {
			if (this.Players.get(player).Role == ROLE.CultLeader ||
					this.Players.get(player).Role == ROLE.Cultist)
				players.add(player);
		}
		return players;
	}

	public int getDays() {
		return this.Days;
	}

	public void inclementDays() {
		this.Days++;
	}

	public String getLynchedPlayer() {
		return this.LynchedPlayer;
	}

	public ArrayList<String> getDiedPlayers() {
		return this.DiedPlayers;
	}

	public void addDiedPlayers(String DiedPlayers) {
		this.DiedPlayers.add(DiedPlayers);
	}

	public void clearDiedPlayers() {
		this.DiedPlayers.clear();
	}

	public void setLynchedPlayer(String LynchedPlayer) {
		this.LynchedPlayer = LynchedPlayer;
	}

	// 夜が終わった時の処理
	public void nightAction() {
		werewolvesAction();
		oracleAction();
		etcAction();
		if (getLovers().size() == 2) {
			String lover_male = getLovers().get(0);
			String lover_female = getLovers().get(1);
			if (getStatus(lover_male) != STATUS.Alive
					&& getStatus(lover_female) == STATUS.Alive) {
				setStatus(lover_female, STATUS.Died);
				DiedPlayers.add(lover_female);
			}
			if (getStatus(lover_male) == STATUS.Alive
					&& getStatus(lover_female) != STATUS.Alive) {
				setStatus(lover_male, STATUS.Died);
				DiedPlayers.add(lover_male);
			}
		}
		Days++;
	}

	// 夜が終わった時の人狼の処理
	private void werewolvesAction() {
		int rnd = randam.nextInt(getWerewolves().size());
		String selectWerewolf = getWerewolves().get(rnd);
		String killed = getSelectedPlayer(selectWerewolf);
		for (String werewolf : getWerewolves()) {
			setSelectedPlayer(werewolf, null);
		}

		for (String player : getPlayers()) {
			switch (getRole(player)) {
			case Bodyguard:
				if (!player.equals(killed) && getSelectedPlayer(player).equals(killed)) {
					killed = null;
				}
			default:
				break;
			}
		}
		if (killed != null) {
			switch (getRole(killed)) {
			case Werehamster:
				break;
			case Devil:
				setDiedPlayer(killed, true);
				break;
			case Hunter:
				setStatus(killed, STATUS.Killed);
				String revenge = getSelectedPlayer(killed);
				setStatus(revenge, STATUS.Died);
				DiedPlayers.add(killed);
				DiedPlayers.add(revenge);
				break;
			case Apothecary:
				setDiedPlayer(killed, true);
				setStatus(killed, STATUS.Killed);
				DiedPlayers.add(killed);
				break;
			case Gravedigger:
				setRole(killed, ROLE.Werewolf);
				break;
			case Toughgay:
				if(!getDiedPlayer(killed)){
					setDiedPlayer(killed, true);
				} else {
					setStatus(killed, STATUS.Killed);
					DiedPlayers.add(killed);
				}
				break;
			default:
				setStatus(killed, STATUS.Killed);
				DiedPlayers.add(killed);
				break;
			}
		}
	}

	// 夜が終わった時の祈祷師処理
	private void oracleAction() {
		for (String player : getAlivePlayers()) {
			if (getRole(player) == ROLE.Oracle) {
				String oracle = getSelectedPlayer(player);
				for (String owl : getAlivePlayers()) {
					if (getRole(owl) == ROLE.Owlman
							&& getSelectedPlayer(owl).equals(oracle)) {
						setSelectedPlayer(owl, null);
					}
				}
			}
		}
	}

	// 夜が終わった時のその他の処理
	private void etcAction() {
		for (String player : getAlivePlayers()) {
			String selected = getSelectedPlayer(player);
			switch (getRole(player)) {
			case Owlman:
				if (getAlivePlayers().size() > 20) {
					setStatus(selected, STATUS.Died);
					DiedPlayers.add(player);
				}
				break;
			case Werehamster:
				if (getDiedPlayer(player)) {
					setDiedPlayer(player, false);
					setStatus(player, STATUS.Died);
					DiedPlayers.add(player);
				}
				break;
			case Mythomaniac:
				switch (getRole(selected)) {
				case Werewolf:
					setRole(player, ROLE.Werewolf);
					break;
				case Seer:
					setRole(player, ROLE.Seer);
					break;
				default:
					setRole(player, ROLE.Villager);
					break;
				}
				break;
			case CultLeader:
				switch (getRole(selected)) {
				case Werewolf:
				case Mythomaniac:
				case Mystic:
				case Thief:
					break;
				default:
					setRole(selected, ROLE.Cultist);
					break;
				}
				break;
			case Witch:
				if (getDiedPlayer(player)
						&& (!hasHilling(player) || !
								hasPoison(player))) {
					if (selected == null) {
						setStatus(LynchedPlayer, STATUS.Alive);
					} else {
						setStatus(selected, STATUS.Died);
						DiedPlayers.add(selected);
					}
					setDiedPlayer(player, false);
				}
				break;
			case Mystic:
				setRole(player, getRole(selected));
				break;
			case Crone:
				if (getWerewolves().size() * 2 < Days + 1) {
					setStatus(player, STATUS.Died);
					DiedPlayers.add(player);
				}
				break;
			case Thief:
				setRole(player, getRole(selected));
				setRole(selected, ROLE.Villager);
				break;
			default:
				break;
			}
		}
		for (String player : getPlayers()) {
			switch (getRole(player)) {
			case Seer:
				if (getStatus(player) != STATUS.Alive 
						&& !getDiedPlayer(player)) {
					for (String scryer : getAlivePlayers()) {
						if (getRole(scryer) == ROLE.Scryer) {
							setRole(scryer, ROLE.Seer);
							setDiedPlayer(player, true);
							break;
						}
					}
				}
			default:
				break;
			}
		}
	}

	// 投票の初期化
	public void initVotes() {
		clearVotes();
		for (String player : getAlivePlayers()) {
			setVoteable(player);
		}
	}

	// 投票可能なプレイヤーを取得
	public ArrayList<String> getVoteablePlayers() {
		ArrayList<String> players = new ArrayList<String>();
		for (String player : getAlivePlayers()) {
			if (getVoteable(player))
				players.add(player);
		}
		return players;
	}

	// 投票
	public void votePlayer(String player, String voted) {
		if (getRole(player) == ROLE.Magistrate) {
			inclementVote(voted);
		}
		inclementVote(voted);
	}

	// 投票結果をチェック
	public boolean checkJudgement() {
		clearVoteables();
		ArrayList<String> sortedPlayer = sortVotePlayers();
		// 過半数を超えている
		if ((getVote(sortedPlayer.get(0)) * 2) > getAlivePlayers().size()) {
			setVoteable(sortedPlayer.get(0));
		} else {
			setVoteable(sortedPlayer.get(0));
			for (String player : getAlivePlayers()) {
				if (getVote(sortedPlayer.get(1)) == getVote(player)) {
					setVoteable(player);
				}
			}
		}
		for (String player : getAlivePlayers()) {
			if (getRole(player) == ROLE.Owlman) {
				if (getSelectedPlayer(player) != null
						&& getSelectedPlayer(player).length() > 0) {
					setVoteable(getSelectedPlayer(player));
					setSelectedPlayer(player, null);
				}
			}
		}
		if (getVoteablePlayers().size() == 1) {
			String player = getVoteablePlayers().get(0);
			if (getRole(player) == ROLE.Hierophant
					&& !getDiedPlayer(player)) {
				setVoteable(player, false);
				setDiedPlayer(player, true);
			}
		}
		clearVotes();
		if (getVoteablePlayers().size() > 1) {
			return false;
		} else{
			return true;
		}
	}

	// 再投票
	public boolean checkRevote() {
		if (getVoteablePlayers().size() > 1) {
			return true;
		} else if (getVoteablePlayers().size() == 1) {
			LynchedPlayer = getVoteablePlayers().get(0);
			setStatus(LynchedPlayer, STATUS.Lynched);
			if (hasLover(LynchedPlayer)) {
				for (String player : getLovers()) {
					setStatus(player, STATUS.Died);
				}
			}
		}
		return false;
	}

	// ゲームオーバー判定
	public int checkGameOver() {
		int werewofves = 0;
		int villagers = 0;
		for (String player : getPlayers()) {
			switch (getRole(player)) {
			case Devil:
				if (getStatus(player) == STATUS.Lynched
						&& getDiedPlayer(player)) {
					return GameOver_Devil_win;
				}
				break;
			case Fool:
				if (getStatus(player) == STATUS.Lynched)
					return GameOver_Fool_win;
				break;
			default:
			}
		}
		if (getAlivePlayers().size() == 2) {
			if (hasLover(getAlivePlayers().get(0))
					&& hasLover(getAlivePlayers().get(1))) {
				if ((getRole(getAlivePlayers().get(0)) == ROLE.Werewolf && 
						getRole(getAlivePlayers().get(1)) != ROLE.Werewolf)
						|| (getRole(getAlivePlayers().get(1)) == ROLE.Werewolf && 
						getRole(getAlivePlayers().get(0)) != ROLE.Werewolf)) {
					return GameOver_Lovers_win;
				}
			}

		}
		if (getAlivePlayers().size() == getCultists().size()) {
			return GameOver_Cultists_win;
		}
		for (String player : getAlivePlayers()) {
			if (getRole(player) == ROLE.Werewolf) {
				werewofves++;
			} else {
				villagers++;
			}
		}
		if (werewofves != 0 && villagers > werewofves)
			return GameOver_Continue;
		for (String player : getAlivePlayers()) {
			if (getRole(player) == ROLE.Werehamster) {
				return GameOver_Werehamster_win;
			}
		}
		if (werewofves == 0)
			return GameOver_Villagers_win;
		return GameOver_Werewolves_win;
	}
}
