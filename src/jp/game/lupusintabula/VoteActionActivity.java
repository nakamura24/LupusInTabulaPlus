/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VoteActionActivity extends Activity {
	private static final String Tag = "VoteActionActivity";
	private GameRuleClass GameData;
	private String Player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_vote);

			GameData = GameRuleClass.getInstance();
			Intent intent = getIntent();
			Player = intent.getStringExtra("player");

			TextView textView_player = (TextView) findViewById(R.id.vote_textView_player);
			textView_player.setText(Player);

			final ListView listView_players = (ListView) findViewById(R.id.vote_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked);
			for (int i = 0; i < GameData.getVoteablePlayers().size(); i++) {
				adapter.add(GameData.getVoteablePlayers().get(i));
			}
			listView_players.setAdapter(adapter);
			listView_players.setClickable(true);
			listView_players.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView_players.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					try {
						checkPlayerAlertDialog(position);
					} catch (Exception e) {
						ErrorReportClass.LogException(VoteActionActivity.this,
								e);
					}
				}
			});
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	// 投票するかどうかの確認
	private void checkPlayerAlertDialog(final int position) {
		Log.i(Tag, "checkPlayerAlertDialog");
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// アラートダイアログのタイトルを設定します
			alertDialogBuilder.setTitle(R.string.common_text_check);
			// アラートダイアログのメッセージを設定します
			Resources resource = getResources();
			String message = resource.getString(R.string.vote_text_check);
			message = String.format(message,
					GameData.getVoteablePlayers().get(position));
			alertDialogBuilder.setMessage(message);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(Tag, "onClick");
							try {
								GameData.votePlayer(Player, GameData
										.getVoteablePlayers().get(position));
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							} catch (Exception e) {
								ErrorReportClass.LogException(
										VoteActionActivity.this, e);
							}
						}
					});
			alertDialogBuilder.setNegativeButton(R.string.common_text_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								ListView listView_players = (ListView) findViewById(R.id.vote_listView_players);
								listView_players
										.setItemChecked(position, false);
							} catch (Exception e) {
								ErrorReportClass.LogException(
										VoteActionActivity.this, e);
							}
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			// アラートダイアログを表示します
			alertDialog.show();
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
