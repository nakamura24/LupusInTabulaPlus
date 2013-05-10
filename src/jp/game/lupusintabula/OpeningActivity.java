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

public class OpeningActivity extends Activity {
	private static final String Tag = "OpeningActivity";
	private GameRuleClass GameData;
	private RoleClass Roles;
	private TextToSpeechClass TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_opening);

			GameData = GameRuleClass.getInstance();
			Roles = RoleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			// 初期化
			GameData.initialize(this);

			// プレイヤー人数の警告
			playersAlertDialog();

			TTS.speechText(R.string.opening_speech_message);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// プレイヤー人数の警告
	private void playersAlertDialog() {
		Log.i(Tag, "AlertWarningDialog");
		try {
			if (GameData.getAlivePlayers().size() < Roles.MinPlayers
					|| GameData.getAlivePlayers().size() > Roles.MaxPlayers) {
				Log.d(Tag, "AlertWarningDialog");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// アラートダイアログのタイトルを設定します
				alertDialogBuilder.setTitle(R.string.common_text_warning);
				// アラートダイアログのメッセージを設定します
				Resources resource = getResources();
				String warning_players = "";
				if (GameData.getAlivePlayers().size() < Roles.MinPlayers) {
					warning_players = resource
							.getString(R.string.role_text_warning_min);
					warning_players = String.format(warning_players,
							Roles.MinPlayers);
				}
				if (GameData.getAlivePlayers().size() > Roles.MaxPlayers) {
					warning_players = resource
							.getString(R.string.role_text_warning_max);
				}
				alertDialogBuilder.setMessage(warning_players);
				// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
				alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 画面の終了
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				// アラートダイアログを表示します
				alertDialog.show();
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			TTS.speechStop();
			Intent intent = new Intent(this, NightActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
