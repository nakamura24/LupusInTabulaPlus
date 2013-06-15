/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import static jp.game.lupusintabula.Constant.*;

public class PlayerActivity extends Activity {
	private static final String Tag = "PlayerActivity";
	private GameRuleClass GameData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_player);

			// プレイヤーの読み込み
			GameData = GameRuleClass.getInstance();
			GameData.loadPlayers(this);

			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.player_toggleButton_all);
			toggleButton_all.setChecked(true);

			// リストビュー更新
			ListView_update();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (requestCode) {
			case ACTIVITY_ADDPLAYER:
				String player = data.getStringExtra("player");
				boolean male = data.getBooleanExtra("gender", true);
				if (player != null && player.length() > 0) {
					GameData.addPlayer(player, male);
					// リストビュー更新
					ListView_update();
				}
				break;
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickAddButton(View view) {
		Log.i(Tag, "onClickAddButton");
		try {
			Intent intent = new Intent(this, AddPlayerActivity.class);
			startActivityForResult(intent, ACTIVITY_ADDPLAYER);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickAllButton(View view) {
		Log.i(Tag, "onClickAllButton");
		try {
			ListView listView_players = (ListView) findViewById(R.id.player_listView_players);
			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.player_toggleButton_all);
			if (toggleButton_all.isChecked()) {
				for (int i = 0; i < GameData.getAllPlayers().size(); i++) {
					listView_players.setItemChecked(i, true);
				}
			} else {
				for (int i = 0; i < GameData.getAllPlayers().size(); i++) {
					listView_players.setItemChecked(i, false);
				}
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickDelButton(View view) {
		Log.i(Tag, "onClickDelButton");
		try {
			ListView listView_players = (ListView) findViewById(R.id.player_listView_players);
			for (int i = GameData.getAllPlayers().size() - 1; i >= 0; i--) {
				if (listView_players.isItemChecked(i)) {
					GameData.removePlayer(GameData.getAllPlayers().get(i));
				}
			}
			// リストビュー更新
			ListView_update();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// リストビュー更新
	private void ListView_update() {
		Log.i(Tag, "ListView_update");
		try {
			ListView listView_players = (ListView) findViewById(R.id.player_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice);
			for (int i = 0; i < GameData.getAllPlayers().size(); i++) {
				adapter.add(GameData.getAllPlayers().get(i));
			}
			listView_players.setAdapter(adapter);
			ToggleButton toggleButton_all = (ToggleButton) findViewById(R.id.player_toggleButton_all);
			onClickAllButton(toggleButton_all);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			// プレイするプレイヤーを設定
			ArrayList<String> players = new ArrayList<String>();
			ListView listView_players = (ListView) findViewById(R.id.player_listView_players);
			for (int i = 0; i < GameData.getAllPlayers().size(); i++) {
				if (listView_players.isItemChecked(i)) {
					players.add(GameData.getAllPlayers().get(i));
				}
			}
			GameData.setPlayingPlayers(players);

			// プレイヤーの保存
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(PlayerActivity.this);
			for (int i = 0; i < GameData.getAllPlayers().size(); i++) {
				String player = GameData.getAllPlayers().get(i);
				Editor editer = sharedPreferences.edit();
				editer.putString("player_name" + String.valueOf(i), player);
				editer.putBoolean("player_gender" + String.valueOf(i),
						GameData.getGender(player));
				editer.putBoolean("player_playing" + String.valueOf(i),
						GameData.getPlaying(player));
				editer.commit();
			}
			Editor editer = sharedPreferences.edit();
			editer.putInt("number_of_player", GameData.getAllPlayers().size());
			editer.commit();

			// 画面の終了
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
