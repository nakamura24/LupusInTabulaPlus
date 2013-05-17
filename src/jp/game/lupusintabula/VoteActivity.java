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
import android.widget.AdapterView.OnItemClickListener;

public class VoteActivity extends Activity {
	private static final String Tag = "VoteActivity";
	private static final int ACTIVITY_VOTEACTION = 0x0501;
	private GameRuleClass GameData;
	private static boolean[] players;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_vote);

			GameData = GameRuleClass.getInstance();
			players = new boolean[GameData.getAlivePlayers().size()];
			ListView_update();
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(Tag, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_VOTEACTION:
				// リストビュー更新
				ListView_update();
				for (int i = 0; i < players.length; i++) {
					if (!players[i])
						return;
				}
				Intent intent = new Intent(this, JudgementActivity.class);
				startActivity(intent);
				break;
			}
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	// リストビュー更新
	private void ListView_update() {
		Log.i(Tag, "ListView_update");
		try {
			final ListView listView_players = (ListView) findViewById(R.id.vote_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice);
			for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
				adapter.add(GameData.getAlivePlayers().get(i));
			}
			listView_players.setAdapter(adapter);
			listView_players.setItemsCanFocus(false);
			listView_players.setClickable(true);
			listView_players.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView_players.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					try {
						if (!players[position])
							checkPlayerAlertDialog(position);
						listView_players.setItemChecked(position,
								players[position]);
					} catch (Exception e) {
						ErrorReportClass.LogException(VoteActivity.this, e);
					}
				}
			});
			for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
				listView_players.setItemChecked(i, players[i]);
			}
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
			String message = resource.getString(R.string.vote_text_message);
			message = String.format(message,
					GameData.getAlivePlayers().get(position));
			alertDialogBuilder.setMessage(message);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton(R.string.common_text_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(Tag, "onClick");
						}
					});
			alertDialogBuilder.setNegativeButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(Tag, "onClick");
							try {
								players[position] = true;
								Intent intent = new Intent(VoteActivity.this,
										VoteActionActivity.class);
								intent.putExtra("player", GameData
										.getAlivePlayers().get(position));
								startActivityForResult(intent,
										ACTIVITY_VOTEACTION);
							} catch (Exception e) {
								ErrorReportClass.LogException(
										VoteActivity.this, e);
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
