package org.gordon.tb3.manager;

import java.util.ArrayList;
import java.util.List;

import org.gordon.tb3.Gordon;
import org.gordon.tb3.provider.impl.GordonProvider;
import org.gordon.tb3.provider.table.GordonTable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

public class GordonManager {

	/**
	 * Récupérer un cursor contenant tous les éléments.
	 * 
	 * @param contentResolver
	 * @return un cursor
	 */
	public Cursor getCursor(ContentResolver contentResolver) {
		return contentResolver.query(GordonProvider.CONTENT_URI, null, null, null, null);
	}

	/**
	 * Récupérer tous les éléments, sans filtre possible.
	 * 
	 * @param contentResolver
	 * @return la liste de tous les éléments de la table
	 */
	public List<Gordon> getAll(ContentResolver contentResolver) {
		final Cursor c = contentResolver.query(GordonProvider.CONTENT_URI, null, null, null, null);
		return getFromCursor(c);
	}

	/**
	 * Récupérer un Gordon par son id.
	 * 
	 * @param contentResolver
	 * @param id
	 *            L'id du Gordon désiré.
	 * @return Le Gordon désiré.
	 */
	public Gordon getById(ContentResolver contentResolver, long id) {
		final Cursor c = contentResolver.query(GordonProvider.CONTENT_URI, null, GordonTable._ID + "=?",
				new String[] { String.valueOf(id) }, null);
		return getFirstFromCursor(c);
	}

	/**
	 * Ajouter un Gordon.
	 * 
	 * @param contentResolver
	 * @param item
	 *            Le Gordon à ajouter.
	 */
	public void add(final ContentResolver contentResolver, final Gordon item) {
		final ContentValues values = getContentValues(item);
		contentResolver.insert(GordonProvider.CONTENT_URI, values);
	}

	/**
	 * Supprimer un élément.
	 * 
	 * @param contentResolver
	 * @param id
	 *            L'id du Gordon à éradiquer.
	 */
	public void delete(final ContentResolver contentResolver, long id) {
		contentResolver.delete(GordonProvider.CONTENT_URI, GordonTable._ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	/**
	 * Mettre à jour un élément.
	 * 
	 * @param contentResolver
	 * @param item
	 *            Le Gordon à modifier.
	 */
	public void update(final ContentResolver contentResolver, final Gordon item) {
		final ContentValues values = getContentValues(item);
		contentResolver.update(GordonProvider.CONTENT_URI, values, GordonTable._ID + " = ?",
				new String[] { String.valueOf(item._id) });
	}

	/**
	 * Préparer Gordon pour son entrée en base.
	 * 
	 * @param item
	 *            Le Gordon.
	 * @return a ContentValue filled with ArretItem values, for a FavoriTable
	 *         structure
	 */
	private ContentValues getContentValues(Gordon item) {
		final ContentValues values = new ContentValues();
		values.put(GordonTable.NAME, item.name);
		return values;
	}

	/**
	 * Transformer un curseur en liste d'éléments
	 * 
	 * @param c
	 *            un Curseur
	 * @return une liste d'éléments
	 */
	private List<Gordon> getFromCursor(Cursor c) {
		List<Gordon> items = new ArrayList<Gordon>();
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				items.add(getSingleFromCursor(c));
				c.moveToNext();
			}
		}
		c.close();
		return items;
	}

	/**
	 * Retourner le premier élément d'un curseur
	 * 
	 * @param c
	 * @return le premier élément du curseur
	 */
	private Gordon getFirstFromCursor(Cursor c) {
		Gordon result = null;
		if (c.getCount() > 0) {
			c.moveToFirst();
			result = getSingleFromCursor(c);
		}
		c.close();
		return result;
	}

	/**
	 * Transformer la position courante d'un curseur en élément
	 * 
	 * @param c
	 *            le curseur
	 * @return un élément
	 */
	private Gordon getSingleFromCursor(Cursor c) {
		final Gordon item = new Gordon();

		item._id = c.getInt(c.getColumnIndex(GordonTable._ID));
		item.name = c.getString(c.getColumnIndex(GordonTable.NAME));

		return item;
	}

}
