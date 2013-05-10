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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupActivity extends Activity {
	private static final String Tag = "GroupActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_group);

			Intent intent = getIntent();
			String title = intent.getStringExtra("title");
			String[] list = intent.getStringArrayExtra("list");

			TextView textView_player = (TextView) findViewById(R.id.group_textView_title);
			textView_player.setText(title);

			ListView listView_players = (ListView) findViewById(R.id.group_listView_players);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);
			for (int i = 0; i < list.length; i++) {
				adapter.add(list[i]);
			}
			listView_players.setAdapter(adapter);
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
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
