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
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static jp.game.lupusintabula.Constant.*;

public class NoonActivity extends Activity {
	private static final String Tag = "NoonActivity";
	private GameRuleClass GameData;
	private TextToSpeechClass TTS;
	private int time = Debate_Time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_noon);

			GameData = GameRuleClass.getInstance();
			TTS = TextToSpeechClass.getInstance(this);
			time = 3;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_textView_timer);
			textView_timer.setText(timer_text);

			TTS.speechText(R.string.noon_speech_title);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickInclementButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			time++;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_textView_timer);
			textView_timer.setText(timer_text);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickDeclementkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			if (time > 0)
				time--;
			String timer_format = "%d:%02d";
			String timer_text = String.format(timer_format, time, 0);
			TextView textView_timer = (TextView) findViewById(R.id.noon_textView_timer);
			textView_timer.setText(timer_text);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickStartButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			// カウントダウンする
			new CountDownTimer(time * 60000, 1000) {
				TextView textView_timer = (TextView) findViewById(R.id.noon_textView_timer);
				String timer_format = "%d:%02d";

				// カウントダウン処理
				public void onTick(long millisUntilFinished) {
					String timer_text = String.format(timer_format,
							millisUntilFinished / 60000,
							(millisUntilFinished / 1000) % 60);
					textView_timer.setText(timer_text);
				}

				// カウントが0になった時の処理
				public void onFinish() {
					String timer_format = "%d:%02d";
					String timer_text = String.format(timer_format, 0, 0);
					TextView textView_timer = (TextView) findViewById(R.id.noon_textView_timer);
					textView_timer.setText(timer_text);
					
					if(TTS.getStatus() == TextToSpeech.SUCCESS){
						TTS.speechText(R.string.noon_speech_end);
					} else{
						ToneGenerator toneGenerator = new ToneGenerator(
								AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
						toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 3000);
					}
				}
			}.start();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			TTS.speechStop();
			// 投票の初期化(再投票になった時、初期化しないように、ここでする)
			GameData.initVotes();

			Intent intent = new Intent(this, VoteActivity.class);
			startActivity(intent);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
}
