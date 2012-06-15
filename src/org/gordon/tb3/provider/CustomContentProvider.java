package org.gordon.tb3.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public abstract class CustomContentProvider extends ContentProvider {

	protected static final String AUTHORITY = "gordon";

	protected static CoreDatabase mDB;

	@Override
	public boolean onCreate() {
		if (mDB == null) {
			mDB = new CoreDatabase(getContext());
		}
		return true;
	}

	@Override
	public abstract int delete(Uri uri, String selection, String[] selectionArgs);

	@Override
	public abstract String getType(Uri uri);

	@Override
	public abstract Uri insert(Uri uri, ContentValues values);

	@Override
	public abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder);

	/**
	 * Classe d'aide à l'accès à la base de données.
	 * 
	 * @author romain
	 * 
	 */
	protected static class CoreDatabase extends SQLiteOpenHelper {

		private static final int DB_VERSION = 1;
		private static final String DB_NAME = "gordon.db";

		public CoreDatabase(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE gordon (_id INTEGER PRIMARY KEY, name TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DELETE FROM gordon;");
		}

	}

}
