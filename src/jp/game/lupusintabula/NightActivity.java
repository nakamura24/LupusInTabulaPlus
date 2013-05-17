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
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class NightActivity extends Activity {
	private static final String Tag = "NightActivity";
	public static final int ACTIVITY_ROLEVIEW = 0x0201;
	private static final int ACTIVITY_ROLEACTION = 0x0202;
	private GameRuleClass GameData;
	private TextToSpeechClass TTS;
	private boolean[] players;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_night);

			GameData = GameRuleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			players = new boolean[GameData.getAlivePlayers().size()];
			GameData.initVotes();

			Resources resource = getResources();
			String title = resource.getString(R.string.night_textView_title);
			TextView textView_title = (TextView) findViewById(R.id.night_textView_title);
			textView_title.setText(String.format(title, GameData.getDays()));

			// リストビュー更新
			ListView_update();

			Button button_ok = (Button) findViewById(R.id.button_ok);
			button_ok.setVisibility(View.GONE);

			TTS.speechText(R.string.night_speech_title);
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(Tag, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_ROLEVIEW:
			case ACTIVITY_ROLEACTION:
				// リストビュー更新
				ListView_update();
				for (int i = 0; i < players.length; i++) {
					if (!players[i])
						return;
				}
				Button button_ok = (Button) findViewById(R.id.button_ok);
				button_ok.setVisibility(View.VISIBLE);
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
			final ListView listView_players = (ListView) findViewById(R.id.night_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_multiple_choice);
			for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
				adapter.add(GameData.getAlivePlayers().get(i));
			}
			listView_players.setAdapter(adapter);
			listView_players.setItemsCanFocus(false);
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
						ErrorReportClass.LogException(NightActivity.this, e);
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

	// 本人かどうかの確認
	private void checkPlayerAlertDialog(final int position) {
		Log.i(Tag, "checkPlayerAlertDialog");
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// アラートダイアログのタイトルを設定します
			alertDialogBuilder.setTitle(R.string.common_text_check);
			// アラートダイアログのメッセージを設定します
			Resources resource = getResources();
			String message = resource.getString(R.string.night_text_check);
			message = String.format(message,
					GameData.getAlivePlayers().get(position));
			alertDialogBuilder.setMessage(message);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setNegativeButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(Tag, "onClick");
							try {
								players[position] = true;
								if (GameData.getDays() > 1) {
									Intent intent = new Intent(
											NightActivity.this,
											RoleActionActivity.class);
									intent.putExtra("player", GameData
											.getAlivePlayers().get(position));
									startActivityForResult(intent,
											ACTIVITY_ROLEACTION);
								} else {
									Intent intent = new Intent(
											NightActivity.this,
											RoleViewActivity.class);
									intent.putExtra("player", GameData
											.getAlivePlayers().get(position));
									startActivityForResult(intent,
											ACTIVITY_ROLEVIEW);
								}
							} catch (ActivityNotFoundException e) {
								ErrorReportClass.LogException(
										NightActivity.this, e);
							}
						}
					});
			alertDialogBuilder.setPositiveButton(R.string.common_text_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								// リストビュー更新
								ListView_update();
							} catch (ActivityNotFoundException e) {
								ErrorReportClass.LogException(
										NightActivity.this, e);
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

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			// 夜が終わった時の処理
			if (GameData.getDays() > 1) {
				GameData.nightAction();
			} else {
				GameData.inclementDays();
			}
			Intent intent = new Intent(this, MorningActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
