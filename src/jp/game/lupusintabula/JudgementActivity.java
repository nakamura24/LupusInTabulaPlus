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

public class JudgementActivity extends Activity {
	private static final String Tag = "JudgementActivity";
	private GameRuleClass GameData;
	private TextToSpeechClass TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_judgement);

			GameData = GameRuleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			TextView textView_player = (TextView) findViewById(R.id.judgement_textView_message);
			if (GameData.checkJudgement()) {
				TTS.speechText(R.string.judgemen_speech_conclusion);
			} else {
				TTS.speechText(R.string.judgemen_speech_revote);
			}
			textView_player.setText(getJudgementMassage());
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	// 審判のメッセージ
	public String getJudgementMassage() {
		try {
			Resources resource = getResources();
			String textView_revote_message = resource
					.getString(R.string.judgement_textView_revote_message);
			String textView_died_message = resource
					.getString(R.string.judgement_textView_died_message);
			String textView_no_died = resource
					.getString(R.string.judgemen_textView_no_died);
			String message = "";
			if (GameData.getVoteablePlayers().size() > 1) {
				for (String player : GameData.getVoteablePlayers()) {
					message += player + " ";
				}
				message = String.format(textView_revote_message, message);
			} else if (GameData.getVoteablePlayers().size() == 1) {
				String LynchedPlayer = GameData.getVoteablePlayers().get(0);
				message = LynchedPlayer;
				if (GameData.hasLover(LynchedPlayer)) {
					message = "";
					for (String player : GameData.getLovers()) {
						message += player + " ";
					}
				}
				message = String.format(textView_died_message, message);
			} else {
				message = textView_no_died;
			}
			return message;
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
		return null;
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			if (GameData.checkRevote()) {
				Intent intent = new Intent(this, VoteActivity.class);
				startActivity(intent);
			} else {
				if (GameData.checkGameOver() == GameRuleClass.GameOver_Continue) {
					Intent intent = new Intent(this, NightActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(this, GameOverActivity.class);
					startActivity(intent);
				}
			}
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
