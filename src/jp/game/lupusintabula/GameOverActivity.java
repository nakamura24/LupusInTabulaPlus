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

public class GameOverActivity extends Activity {
	private static final String Tag = "GameOverActivity";
	private GameRuleClass GameData;
	private TextToSpeechClass TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_gameover);

			GameData = GameRuleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			String message = "";
			Resources resource = getResources();
			String[] role_names = resource.getStringArray(R.array.role_names);
			TextView textView_win = (TextView) findViewById(R.id.gameover_textView_win);
			TextView textView_winners = (TextView) findViewById(R.id.gameover_textView_winners);
			switch (GameData.checkGameOver()) {
			case GameRuleClass.GameOver_Werewolves_win:
				for (String player : GameData.getPlayers()) {
					switch (GameData.getRole(player)) {
					case Werewolf:
					case Possessed:
					case Sorceress:
						message += player
								+ " - ("
								+ role_names[GameData.getRole(player).ordinal()]
								+ ")\n";
						break;
					default:
					}
				}
				textView_win.setText(R.string.gameover_textView_werewofves_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_werewofves_win);
				break;
			case GameRuleClass.GameOver_Villagers_win:
				for (String player : GameData.getPlayers()) {
					switch (GameData.getRole(player)) {
					case Werewolf:
					case Possessed:
					case Werehamster:
					case Devil:
					case Sorceress:
					case Fool:
					case CultLeader:
					case Cultist:
						break;
					default:
						message += player
								+ " - ("
								+ role_names[GameData.getRole(player).ordinal()]
								+ ")\n";
					}
				}
				textView_win.setText(R.string.gameover_textView_villagers_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_villagers_win);
				break;
			case GameRuleClass.GameOver_Werehamster_win:
				for (String player : GameData.getPlayers()) {
					if (GameData.getRole(player) == ROLE.Werehamster)
						message += player + "\n";
				}
				textView_win
						.setText(R.string.gameover_textView_werehamster_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_werehamster_win);
				break;
			case GameRuleClass.GameOver_Devil_win:
				for (String player : GameData.getPlayers()) {
					if (GameData.getRole(player) == ROLE.Devil)
						message += player + "\n";
				}
				textView_win.setText(R.string.gameover_textView_devil_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_devil_win);
				break;
			case GameRuleClass.GameOver_Fool_win:
				for (String player : GameData.getPlayers()) {
					if (GameData.getRole(player) == ROLE.Fool)
						message += player + "\n";
				}
				textView_win.setText(R.string.gameover_textView_fool_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_fool_win);
				break;
			case GameRuleClass.GameOver_Lovers_win:
				for (String player : GameData.getPlayers()) {
					if (GameData.hasLover(player)) {
						message += player
								+ " - ("
								+ role_names[GameData.getRole(player).ordinal()]
								+ ")\n";
					}
				}
				textView_win.setText(R.string.gameover_textView_lovers_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_lovers_win);
				break;
			case GameRuleClass.GameOver_Cultists_win:
				for (String player : GameData.getPlayers()) {
					switch (GameData.getRole(player)) {
					case CultLeader:
					case Cultist:
						message += player
								+ " - ("
								+ role_names[GameData.getRole(player).ordinal()]
								+ ")\n";
						break;
					default:
					}
				}
				textView_win.setText(R.string.gameover_textView_cultists_win);
				textView_winners.setText(message);

				TTS.speechText(R.string.gameover_speech_cultists_win);
				break;
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
