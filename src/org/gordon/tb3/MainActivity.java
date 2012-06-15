package org.gordon.tb3;

import java.util.UUID;

import org.gordon.tb3.manager.GordonManager;
import org.gordon.tb3.provider.table.GordonTable;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	private GordonManager gordonManager;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.gordonManager = new GordonManager();

		findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				add();
			}
		});

		findViewById(R.id.btnDelete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeLastItem();
			}
		});

		findViewById(R.id.btnUpdate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateLastItem();
			}
		});

		// Créer l'adpater
		final Cursor gordonCursor = gordonManager.getCursor(getContentResolver());

		final String[] columns = new String[] { GordonTable.NAME, GordonTable._ID };
		final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
				gordonCursor, columns, to);

		// Configurer la listeview
		listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
		listView.setStackFromBottom(true);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

	}

	private void add() {
		final Gordon gordon = new Gordon();
		final UUID uuid = UUID.randomUUID();
		gordon.name = uuid.toString().substring(0, 20);
		this.gordonManager.add(getContentResolver(), gordon);
	}

	private void removeLastItem() {
		final long id = listView.getItemIdAtPosition(listView.getCount() - 1);
		this.gordonManager.delete(getContentResolver(), id);
	}

	private void updateLastItem() {
		final long id = listView.getItemIdAtPosition(listView.getCount() - 1);

		final Gordon item = this.gordonManager.getById(getContentResolver(), id);
		item.name += "  [U]";

		this.gordonManager.update(getContentResolver(), item);
	}

}