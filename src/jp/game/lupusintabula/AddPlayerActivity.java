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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddPlayerActivity extends Activity {
	private static final String Tag = "AddPlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_add_player);
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			EditText player = (EditText) findViewById(R.id.add_player_editText_player);
			RadioButton male = (RadioButton) findViewById(R.id.add_player_radio_male);
			// 画面の終了
			Intent intent = new Intent();
			intent.putExtra("player", player.getText().toString());
			intent.putExtra("gender", male.isChecked());
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			ErrorReportClass.LogException(this, e);
		}
	}
}
