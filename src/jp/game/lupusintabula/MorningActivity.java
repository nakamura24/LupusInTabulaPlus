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
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MorningActivity extends Activity {
	private static final String Tag = "MorningActivity";
	private GameRuleClass GameData;
	private TextToSpeechClass TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_morning);

			GameData = GameRuleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			TextView textView_message = (TextView) findViewById(R.id.moning_textView_message);
			textView_message.setText(getMoningMassage());

			TTS.speechText(R.string.moning_speech_title);
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	// 朝のメッセージ
	public String getMoningMassage() {
		try {
			String message = "";
			Resources resource = getResources();
			String textView_died_message = resource
					.getString(R.string.moning_textView_died_message);
			String textView_no_died = resource
					.getString(R.string.moning_textView_no_died);
			if (GameData.getDiedPlayers().size() > 0) {
				String DiedPlayers = "";
				for (String player : GameData.getDiedPlayers()) {
					DiedPlayers += player + " ";
				}
				message = String.format(textView_died_message, DiedPlayers);
			} else {
				message = textView_no_died;
			}
			if (GameData.getDays() > 2) {
				String doutePlayer = "";
				String textView_doubt_message = resource
						.getString(R.string.moning_textView_doubt_message);
				int max = 0;
				for (String player : GameData.getAlivePlayers()) {
					if (GameData.getVote(player) > max) {
						max = GameData.getVote(player);
						doutePlayer = player;
					}
				}
				message += String.format(textView_doubt_message, doutePlayer);
			}
			GameData.clearDiedPlayers();
			return message;
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
		return null;
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			TTS.speechStop();
			if (GameData.checkGameOver() == GameRuleClass.GameOver_Continue) {
				Intent intent = new Intent(this, NoonActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, GameOverActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
