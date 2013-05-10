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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RoleActivity extends Activity {
	private static final String Tag = "RoleActivity";
	private RoleClass Roles;
	private GameRuleClass GameData;
	private int number_of_players = 0;
	private int[] Values = new int[ROLE.values().length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_role);

			// プレイヤーの読み込み
			GameData = GameRuleClass.getInstance();
			GameData.loadPlayers(this);

			// 役職の読み込み
			Roles = RoleClass.getInstance();
			Roles.loadRoles(this);

			Resources resource = getResources();
			String textView_number = resource
					.getString(R.string.role_textView_number);
			String textView_max = resource.getString(R.string.role_button_max);
			number_of_players = GameData.getPlayers().size();
			playersAlertDialog(number_of_players);
			TextView role_textView_number = (TextView) findViewById(R.id.role_textView_number);
			role_textView_number.setText(String.format(textView_number,
					number_of_players));
			Button role_button_max = (Button) findViewById(R.id.role_button_max);
			role_button_max.setText(String.format(textView_max, Roles.MaxPlayers));
			RadioButton radio_none = (RadioButton) findViewById(R.id.role_radio_none);
			radio_none.setChecked(!Roles.Random);
			RadioButton radio_without_werewolf = (RadioButton) findViewById(R.id.role_radio_without_werewolf);
			radio_without_werewolf.setChecked(Roles.Random && !Roles.IncludeWerewolf);
			RadioButton radio_include_werewolf = (RadioButton) findViewById(R.id.role_radio_include_werewolf);
			radio_include_werewolf.setChecked(Roles.IncludeWerewolf);

			// 役職の人数を表示
			addTableRows();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// 役職の人数を表示
	private void addTableRows() {
		try {
			TableLayout tablelayout_roles = (TableLayout) findViewById(R.id.role_tablelayout_roles);
			Resources resource = getResources();
			String[] role_names = resource.getStringArray(R.array.role_names);
			for (ROLE role : ROLE.values()) {
				if (Roles.get(role, Roles.MinPlayers) < 0)
					continue;
				TableRow table_row = new TableRow(this);
				TextView TextView_role_name = new TextView(this);
				TextView_role_name.setText(role_names[role.ordinal()]);
				TextView TextView_role_number = new TextView(this);
				TextView_role_number.setId(role.ordinal());
				Values[role.ordinal()] = Roles.get(role, number_of_players);
				TextView_role_number.setText(
						String.valueOf(Values[role.ordinal()]));
				Button inclement = new Button(this);
				inclement.setText(" ＋ ");
				inclement.setId(0x0100 | role.ordinal());
				inclement.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						view.getId();
						TextView TextView_role_number = 
								(TextView) findViewById(view.getId() & 0x00ff);
						Values[view.getId() & 0x00ff]++;
						switch (view.getId() & 0x00ff) {
						case 7:
							if (Values[view.getId() & 0x00ff] == 1)
								Values[view.getId() & 0x00ff]++;
							break;
						case 13:
							Values[view.getId() & 0x00ff] = 1;
							break;
						}
						TextView_role_number.setText(
								String.valueOf(Values[view.getId() & 0x00ff]));
					}
				});
				Button declement = new Button(this);
				declement.setText(" － ");
				declement.setId(0x0200 | role.ordinal());
				declement.setHeight(12);
				declement.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						view.getId();
						TextView TextView_role_number = 
								(TextView) findViewById(view.getId() & 0x00ff);
						if (Values[view.getId() & 0x00ff] > 0)
							Values[view.getId() & 0x00ff]--;
						switch (view.getId() & 0x00ff) {
						case 0:
							if (Values[view.getId() & 0x00ff] == 0)
								Values[view.getId() & 0x00ff] = 1;
							break;
						case 7:
							if (Values[view.getId() & 0x00ff] == 1)
								Values[view.getId() & 0x00ff]--;
							break;
						case 13:
							Values[view.getId() & 0x00ff] = 0;
							break;
						}
						TextView_role_number.setText(
								String.valueOf(Values[view.getId() & 0x00ff]));
					}
				});
				table_row.addView(TextView_role_name);
				table_row.addView(TextView_role_number);
				table_row.addView(inclement);
				table_row.addView(declement);
				tablelayout_roles.addView(table_row);
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// 人数の警告ダイアログ
	private void playersAlertDialog(final int number_of_players) {
		Log.i(Tag, "onClickOkButton");
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// アラートダイアログのタイトルを設定します
			alertDialogBuilder.setTitle(R.string.common_text_warning);
			// アラートダイアログのメッセージを設定します
			Resources resource = getResources();
			String warning_players = "";
			if (number_of_players < Roles.MinPlayers) {
				warning_players = resource
						.getString(R.string.role_text_warning_min);
			}
			if (number_of_players > Roles.MaxPlayers) {
				warning_players = resource
						.getString(R.string.role_text_warning_max);
			}
			warning_players = String.format(warning_players, Roles.MinPlayers);
			int count = 0;
			for (int i = 0; i < ROLE.values().length; i++) {
				count += Values[i];
			}
			if (number_of_players < count) {
				warning_players = resource
						.getString(R.string.role_text_warning_role);
			}
			alertDialogBuilder.setMessage(warning_players);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (number_of_players < Roles.MinPlayers) {
								// 画面の終了
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							}
						}
					});
			if (number_of_players < Roles.MinPlayers
					|| number_of_players > Roles.MaxPlayers
					|| number_of_players < count) {
				AlertDialog alertDialog = alertDialogBuilder.create();
				// アラートダイアログを表示します
				alertDialog.show();
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickMaxButton(View view) {
		Log.i(Tag, "onClickMaxButton");
		try {
			// 最大人数入力ダイアログ
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// アラートダイアログのタイトルを設定します
			Resources resource = getResources();
			final String textView_max = resource
					.getString(R.string.role_button_max);
			alertDialogBuilder.setTitle(String.format(textView_max,
					Roles.MaxPlayers));
			// アラートダイアログのメッセージを設定します
			final EditText editText = new EditText(this);
			editText.setText(String.valueOf(Roles.MaxPlayers));
			alertDialogBuilder.setView(editText);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setNegativeButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int max = Integer.parseInt(editText.getText()
									.toString());
							if (max > Roles.MaxPlayers) {
								Roles.changeMax(max);
								Button role_button_max = (Button) findViewById(R.id.role_button_max);
								role_button_max.setText(String.format(textView_max,
										Roles.MaxPlayers));
							}
						}
					});
			// アラートダイアログのキャンセルが可能かどうかを設定します
			alertDialogBuilder.setCancelable(true);
			AlertDialog alertDialog = alertDialogBuilder.create();
			// アラートダイアログを表示します
			alertDialog.show();
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	public void onClickOkButton(View view) {
		Log.i(Tag, "onClickOkButton");
		try {
			playersAlertDialog(number_of_players);
			int count = 0;
			for (int i = 0; i < ROLE.values().length; i++) {
				count += Values[i];
			}
			if (number_of_players < count)
				return;
			// 役職設定保存
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(RoleActivity.this);
			for (ROLE role : ROLE.values()) {
				TextView TextView_role_number = (TextView) findViewById(role
						.ordinal());
				if (TextView_role_number == null)
					continue;
				Roles.set(role, number_of_players, Values[role.ordinal()]);
				Editor editer = sharedPreferences.edit();
				for (int i = Roles.MinPlayers; i <= Roles.MaxPlayers; i++) {
					editer.putInt(role.toString() + String.valueOf(i),
							Roles.get(role, i));
				}
				editer.commit();
			}
			RadioButton radio_none = (RadioButton) findViewById(R.id.role_radio_none);
			RadioButton radio_include_werewolf = (RadioButton) findViewById(R.id.role_radio_include_werewolf);
			Editor editer = sharedPreferences.edit();
			editer.putInt("max_of_player", Roles.MaxPlayers);
			editer.putBoolean("randam", !radio_none.isChecked());
			editer.putBoolean("include_werewolf", radio_include_werewolf.isChecked());
			editer.commit();

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
