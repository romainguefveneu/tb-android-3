package org.gordon.tb3.provider.impl;

import org.gordon.tb3.provider.CustomContentProvider;
import org.gordon.tb3.provider.table.GordonTable;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GordonProvider extends CustomContentProvider {

	/**
	 * Chemin d'accès.
	 */
	private static final String GORDON_PATH = "gordon";

	/**
	 * Récupérer tous les éléments.
	 */
	public static final int GORDON_ALL = 100;
	/**
	 * Récupérer un élément selon son ID.
	 */
	public static final int GORDON_BY_ID = 110;

	private static final String AUTHORITY = "org.gordon.tb3.GordonProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + GORDON_PATH);

	/**
	 * Mapper les chemins et les clés de récupération.
	 */
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, GORDON_PATH, GORDON_ALL);
		URI_MATCHER.addURI(AUTHORITY, GORDON_PATH + "/#", GORDON_BY_ID);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(GordonTable.TABLE_NAME);

		final int uriType = URI_MATCHER.match(uri);

		switch (uriType) {
		case GORDON_BY_ID:
			queryBuilder.appendWhere(GordonTable._ID + "=" + uri.getLastPathSegment());
			break;
		case GORDON_ALL:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI (" + uri + ")");
		}

		final Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(), projection, selection, selectionArgs, null,
				null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;

		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (URI_MATCHER.match(uri) != GORDON_ALL) {
			throw new IllegalArgumentException("Unknown URI " + uri + " (" + URI_MATCHER.match(uri) + ")");
		}

		SQLiteDatabase db = mDB.getWritableDatabase();

		final long rowId = db.insert(GordonTable.TABLE_NAME, null, values);

		if (rowId > 0) {
			Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(uri, null);
			return insertUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count;
		final SQLiteDatabase db = mDB.getWritableDatabase();

		switch (URI_MATCHER.match(uri)) {
		case GORDON_ALL:
			count = db.delete(GordonTable.TABLE_NAME, selection, selectionArgs);
			break;
		case GORDON_BY_ID:
			final String segment = uri.getPathSegments().get(1);
			count = db.delete(GordonTable.TABLE_NAME, GordonTable._ID + "=" + segment
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI (" + URI_MATCHER.match(uri) + ") " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mDB.getWritableDatabase();
		final int rowCount = db.update(GordonTable.TABLE_NAME, values, selection, selectionArgs);
		if (rowCount > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowCount;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

}
