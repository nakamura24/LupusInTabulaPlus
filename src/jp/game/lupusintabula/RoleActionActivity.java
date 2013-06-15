/* Copyright (C) 2013 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.game.lupusintabula;

import java.util.Random;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import static jp.game.lupusintabula.Constant.*;

public class RoleActionActivity extends Activity {
	private static final String Tag = "RoleActionActivity";
	private static final int ACTIVITY_ROLEVIEW = 0x0201;
	private GameRuleClass GameData;
	private String Player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Tag, "onCreate");
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_roleaction);

			GameData = GameRuleClass.getInstance();
			Intent intent = getIntent();
			Player = intent.getStringExtra("player");
			Resources resource = getResources();
			String[] role_names = resource.getStringArray(R.array.role_names);
			String[] role_messages = resource
					.getStringArray(R.array.role_messages);

			TextView textView_name = (TextView) findViewById(R.id.roleaction_textView_name);
			textView_name.setText(Player + " - ("
					+ role_names[GameData.getRole(Player).ordinal()] + ")");
			TextView textView_message = (TextView) findViewById(R.id.roleaction_textView_message);
			textView_message.setText(role_messages[GameData.getRole(Player)
					.ordinal()]);

			final ListView listView_players = (ListView) findViewById(R.id.roleaction_listView_players);
			if (GameData.getRole(Player) != ROLE.Matchmaker
					|| GameData.getDiedPlayer(Player)) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1);
				for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
					String player = GameData.getAlivePlayers().get(i);
					if (!Player.equals(player)) {
						adapter.add(player);
					}
				}
				listView_players.setAdapter(adapter);
				listView_players
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								checkPlayerAlertDialog((String) parent
										.getItemAtPosition(position));
							}
						});
				Button button_ok = (Button) findViewById(R.id.button_ok);
				button_ok.setVisibility(View.GONE);
			} else {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_multiple_choice);
				for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
					String player = GameData.getAlivePlayers().get(i);
					adapter.add(player);
				}
				listView_players.setAdapter(adapter);
				listView_players.setClickable(true);
				listView_players.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.i(Tag, "onActivityResult");
		try {
			switch (requestCode) {
			case ACTIVITY_ROLEVIEW:
			case ACTIVITY_GROUP:
				break;
			case ACTIVITY_POTION:
				boolean hilling = intent.getBooleanExtra("hilling", false);
				boolean poison = intent.getBooleanExtra("poison", false);
				if (GameData.getRole(Player) == ROLE.Witch) {
					if (hilling) {
						GameData.setDiedPlayer(Player, true);
						GameData.useHilling(Player);
						GameData.setSelectedPlayer(Player, null);
						Intent intentfinish = new Intent();
						setResult(RESULT_OK, intentfinish);
						finish();
					}
					if (poison) {
						GameData.setDiedPlayer(Player, true);
						GameData.usePoison(Player);
					}
				}
				break;
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}
	
	// 役職別の表示処理
	public void onClickConfirmButton(View view) {
		Log.d(Tag, "roleActionView");
		try {
			Intent intent = null;
			Resources resource = getResources();
			String title = "";
			String[] list = null;
			switch (GameData.getRole(Player)) {
			case Werewolf:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				if (GameData.getWerewolves().size() > 1) {
					title = resource
							.getString(R.string.group_textView_werewolves);
					list = new String[GameData.getWerewolves().size()];
					for (int i = 0; i < GameData.getWerewolves().size(); i++) {
						String werewolf = GameData.getWerewolves().get(i);
						String target = GameData.getSelectedPlayer(werewolf);
						if (target == null)
							target = "";
						list[i] = werewolf + " → " + target;
					}
					intent = new Intent(this, GroupActivity.class);
					intent.putExtra("title", title);
					intent.putExtra("list", list);
					startActivityForResult(intent, ACTIVITY_GROUP);
				} else {
					intent = new Intent(this, RoleViewActivity.class);
					intent.putExtra("player", Player);
					startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				}
				break;
			case Medium:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", GameData.getLynchedPlayer());
				intent.putExtra("message", false);
				switch (GameData.getRole(GameData.getLynchedPlayer())) {
				case Werewolf:
				case Devil:
					intent.putExtra("role", ROLE.Werewolf.ordinal());
					break;
				default:
					intent.putExtra("role", ROLE.Villager.ordinal());
				}
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			case Freemason:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				if (GameData.getMasons().size() > 1) {
					title = resource
							.getString(R.string.group_textView_freemasons);
					list = new String[GameData.getMasons().size()];
					for (int i = 0; i < GameData.getMasons().size(); i++) {
						list[i] = GameData.getMasons().get(i);
					}
					intent = new Intent(this, GroupActivity.class);
					intent.putExtra("title", title);
					intent.putExtra("list", list);
					startActivityForResult(intent, ACTIVITY_GROUP);
				} else {
					intent = new Intent(this, RoleViewActivity.class);
					intent.putExtra("player", Player);
					startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				}
				break;
			case Witch:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				if (GameData.hasHilling(Player) || GameData.hasPoison(Player)) {
					intent = new Intent(this, PotionActivity.class);
					intent.putExtra("player", Player);
					startActivityForResult(intent, ACTIVITY_POTION);
				}
				break;
			case Mystic:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				Random randam = new Random();
				int chengedPosition = randam.nextInt(GameData.getAlivePlayers()
						.size());
				String chengedPlayer = GameData.getAlivePlayers().get(
						chengedPosition);
				GameData.setSelectedPlayer(Player, chengedPlayer);
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", Player);
				intent.putExtra("role", GameData.getRole(chengedPlayer)
						.ordinal());
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			case CultLeader:
			case Cultist:
				Log.d(Tag, "roleActionView - "
						+ GameData.getRole(Player).toString());
				if (GameData.getCultists().size() > 1) {
					title = resource
							.getString(R.string.group_textView_cultists);
					list = new String[GameData.getCultists().size()];
					for (int i = 0; i < GameData.getCultists().size(); i++) {
						list[i] = GameData.getCultists().get(i);
					}
					intent = new Intent(this, GroupActivity.class);
					intent.putExtra("title", title);
					intent.putExtra("list", list);
					startActivityForResult(intent, ACTIVITY_GROUP);
				} else {
					intent = new Intent(this, RoleViewActivity.class);
					intent.putExtra("player", Player);
					startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				}
				break;
			default:
				Log.d(Tag, "roleActionView - default");
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", Player);
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
			}
			if (GameData.hasLover(Player)) {
				Log.d(Tag, "roleActionView - Lover");
				String[] role_names = resource
						.getStringArray(R.array.role_names);
				title = resource.getString(R.string.group_textView_lovers);
				list = new String[GameData.getLovers().size()];
				for (int i = 0; i < GameData.getLovers().size(); i++) {
					String lover = GameData.getLovers().get(i);
					list[i] = lover + " - ("
							+ role_names[GameData.getRole(lover).ordinal()]
							+ ")";
				}
				intent = new Intent(this, GroupActivity.class);
				intent.putExtra("title", title);
				intent.putExtra("list", list);
				startActivityForResult(intent, ACTIVITY_GROUP);
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// 役職別の処理
	private void roleAction(String selecedPlayer) {
		Log.d(Tag, "roleAction");
		try {
			Intent intent = null;
			switch (GameData.getRole(Player)) {
			case Werewolf:
			case Bodyguard:
			case Owlman:
			case Hunter:
			case Witch:
			case Oracle:
			case CultLeader:
			case Thief:
				Log.d(Tag, "roleAction - "
						+ GameData.getRole(Player).toString());
				GameData.setSelectedPlayer(Player, selecedPlayer);
				break;
			case Seer:
				Log.d(Tag, "roleAction - "
						+ GameData.getRole(Player).toString());
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", selecedPlayer);
				intent.putExtra("message", false);
				switch (GameData.getRole(selecedPlayer)) {
				case Werewolf:
					intent.putExtra("role", ROLE.Werewolf.ordinal());
					break;
				case Werehamster:
					intent.putExtra("role", ROLE.Werehamster.ordinal());
					GameData.setDiedPlayer(selecedPlayer, true);
					break;
				default:
					intent.putExtra("role", ROLE.Villager.ordinal());
				}
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			case Mythomaniac:
				Log.d(Tag, "roleAction - "
						+ GameData.getRole(Player).toString());
				GameData.setSelectedPlayer(Player, selecedPlayer);
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", selecedPlayer);
				switch (GameData.getRole(selecedPlayer)) {
				case Werewolf:
					intent.putExtra("role", ROLE.Werewolf.ordinal());
					break;
				case Seer:
					intent.putExtra("role", ROLE.Seer.ordinal());
					break;
				default:
					intent.putExtra("role", ROLE.Villager.ordinal());
				}
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			case Warlock:
				Log.d(Tag, "roleAction - "
						+ GameData.getRole(Player).toString());
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", selecedPlayer);
				intent.putExtra("message", false);
				switch (GameData.getRole(selecedPlayer)) {
				case Werehamster:
				case Devil:
				case Fool:
				case CultLeader:
				case Cultist:
					intent.putExtra("role", GameData.getRole(selecedPlayer)
							.ordinal());
					GameData.setDiedPlayer(selecedPlayer, true);
					break;
				default:
					intent.putExtra("role", ROLE.Villager.ordinal());
				}
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			case Sorceress:
				Log.d(Tag, "roleAction - "
						+ GameData.getRole(Player).toString());
				intent = new Intent(this, RoleViewActivity.class);
				intent.putExtra("player", selecedPlayer);
				intent.putExtra("message", false);
				switch (GameData.getRole(selecedPlayer)) {
				case Werewolf:
					intent.putExtra("role", ROLE.Werewolf.ordinal());
					break;
				case Seer:
					intent.putExtra("role", ROLE.Seer.ordinal());
					GameData.setDiedPlayer(selecedPlayer, true);
					break;
				default:
					intent.putExtra("role", ROLE.Villager.ordinal());
				}
				startActivityForResult(intent, ACTIVITY_ROLEVIEW);
				break;
			default:
				Log.d(Tag, "roleAction - default");
				GameData.votePlayer(Player, selecedPlayer);
				break;
			}
		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
			ErrorReportClass.LogException(this, e);
		}
	}

	// 対象のプレイヤーの確認
	private void checkPlayerAlertDialog(final String player) {
		Log.i(Tag, "checkPlayerAlertDialog");
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			// アラートダイアログのタイトルを設定します
			alertDialogBuilder.setTitle(R.string.common_text_check);
			// アラートダイアログのメッセージを設定します
			Resources resource = getResources();
			String[] role_actions = resource
					.getStringArray(R.array.role_actions);
			String message = String.format(role_actions[GameData
					.getRole(Player).ordinal()], player);
			alertDialogBuilder.setMessage(message);
			// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
			alertDialogBuilder.setPositiveButton(R.string.common_text_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.i(Tag, "onClick");
							// 役職別の処理
							roleAction(player);
							// 画面の終了
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							finish();
						}
					});
			alertDialogBuilder.setNegativeButton(R.string.common_text_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
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
			if (GameData.getRole(Player) == ROLE.Matchmaker
					&& !GameData.getDiedPlayer(Player)) {
				ListView listView_players = (ListView) findViewById(R.id.roleaction_listView_players);
				int selected = 0;
				for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
					if (listView_players.isItemChecked(i))
						selected++;
				}
				if (selected != 2)
					return;
				for (int i = 0; i < GameData.getAlivePlayers().size(); i++) {
					if (listView_players.isItemChecked(i)) {
						GameData.setLover(GameData.getAlivePlayers().get(i));
					}
				}
				GameData.setDiedPlayer(Player, true);
			}
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
