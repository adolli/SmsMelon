/**
 * 
 */
package adolli.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author 	adolli
 * @brief	��װ�˷������ݿ��һ�㷽��
 * @notice	��ʶ��������������벻Ҫʹ��'��Ӣ�ĵ����ţ�����ֵ����ʹ�ã���������ת��
 */
public class DatabaseHelper extends SQLiteOpenHelper 
{

	public DatabaseHelper(Context context, String name) 
	{
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) 
	{
		Log.i("DatabaseHelper", "database created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
	{
		Log.i("DatabaseHelper", "database upgrade");
	}

	
	
	/**
	 * @brief	����һ��Table����������򲻴���
	 * @param 	table_name ����
	 * @param 	col_names ���������԰����������ͺ��޶�����
	 */
	public void createTable(String table_name, String[] col_names) 
	{
		String sql = "CREATE TABLE IF NOT EXISTS " + table_name + " (";
		
		for (int i = 0; i < col_names.length - 1; ++i)
		{
			sql += col_names[i] + ", ";
		}
		sql += col_names[col_names.length - 1] + ");";
		
		Log.i("DatabaseHelper", "[SQL]" + sql);
		
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

	}
	
	
	
	/**
	 * @brief	ɾ�����ݱ���������������
	 * @param 	table_name ����
	 */
	public void dropTable(String table_name)
	{
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + table_name);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @brief	����в���һ��
	 * @param 	table_name ����
	 * @param 	col_names Ҫ���������
	 * @param 	vals Ҫ������������飬������Ҫ��Ӧ
	 * @notice	������������ַ�����ʾ����ΪSQL��䶼���ַ���
	 */
	public void insertInto(String table_name, String[] col_names, String[] vals)
	{
		// ����valֵ��ת��
		//makeEscape(vals);
		
		String sql = "INSERT INTO " + table_name + " (";
		int nCol = Math.min(col_names.length, vals.length);
		for (int i = 0; i < nCol - 1; ++i)
		{
			sql += col_names[i] + ", ";
		}
		sql += col_names[nCol - 1] + ")";
		sql += " VALUES (";
		for (int i = 0; i < nCol - 1; ++i)
		{
			sql += vals[i] + ", ";
		}
		sql += vals[nCol - 1] + "); ";
		
		Log.i("DatabaseHelper", "[SQL]" + sql);
		
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @brief	����в���һ�У� vals��Ҫת��
	 * @param 	table_name ����
	 * @param 	vals Ҫ�������������
	 * @notice	������������ַ�����ʾ����ΪSQL��䶼���ַ���
	 */
	public void insertInto(String table_name, String[] vals)
	{				
		String sql = "INSERT INTO " + table_name;
		sql += " VALUES (";
		for (int i = 0; i < vals.length - 1; ++i)
		{
			sql += vals[i] + ", ";
		}
		sql += vals[vals.length - 1] + "); ";
		
		Log.i("DatabaseHelper", "[SQL]" + sql);
		
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * @brief	�������ݱ�
	 * @param 	table ����
	 * @param 	set Ҫ�޸ĵ��еı��ʽ e.g. "phone  = '123456', addr = 'GZ'"
	 * @param 	where ѡ��Ҫ�޸ĵ��еı��ʽ e.g. "name = 'adolli'"
	 */
	public void update(String table, String set, String where)
	{
		String sql = "UPDATE " + table + " SET " + set + " WHERE " + where;
		Log.i("DatabaseHelper", "[SQL]" + sql);
	
		try
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * �������ݿ�ĳ���ĳ�У��Զ��Ὣֵת��
	 * @param table ����
	 * @param cols Ҫ���µ�����
	 * @param vals ���µ�ֵ
	 * @param where where���ʽ
	 */
	public void update(String table, String[] cols, String[] vals, String where)
	{
		// �ַ�ת��
		makeEscape(vals);
		
		int nCol = Math.min(cols.length, vals.length);
		ContentValues cv = new ContentValues();
		for (int i = 0; i < nCol; ++i)
		{
			cv.put(cols[i], vals[i]);
		}
		
		getWritableDatabase().update(table, cv, where, null);
	}
	
	
	public void debugPrintTable(String table)
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, new String[]{"*"}, null, null, null, null, null);
	    while (c.moveToNext())
	    {
	    	String row = "";
	    	int nCol = c.getColumnCount();
	    	for (int i = 0; i < nCol; ++i)
	    	{
	    		row += c.getString(i) + ", ";
	    	}
	    	Log.i("db " + table, row);
	    }
	    c.close();
	}
	
	
	
	/**
	 * @brief	��������������
	 * @param 	table ����
	 */
	public void clearTable(String table)
	{
		String sql = "DELETE FROM " + table;
		Log.i("DatabaseHelper", "[SQL]" + sql);
		try 
		{
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static String[] makeEscape(String[] vals)
	{
		for (int i = 0; i < vals.length; ++i)
		{
			vals[i] = vals[i].replace("/", "//");
			vals[i] = vals[i].replace("'", "''");
			vals[i] = vals[i].replace("[", "/[");
			vals[i] = vals[i].replace("]", "/]");
			vals[i] = vals[i].replace("%", "/%");
			vals[i] = vals[i].replace("&", "/&");
			vals[i] = vals[i].replace("_", "/_");
			vals[i] = vals[i].replace("(", "/(");
			vals[i] = vals[i].replace(")", "/)");
		}
		return vals;
	}
	
	public static String makeEscape(String val)
	{
		val = val.replace("/", "//");
		val = val.replace("'", "''");
		val = val.replace("[", "/[");
		val = val.replace("]", "/]");
		val = val.replace("%", "/%");
		val = val.replace("&", "/&");
		val = val.replace("_", "/_");
		val = val.replace("(", "/(");
		val = val.replace(")", "/)");
		return val;
	}
	
	
	public static String[] deEscape(String[] vals)
	{
		for (int i = 0; i < vals.length; ++i)
		{
			vals[i] = vals[i].replace("//", "/");
			vals[i] = vals[i].replace("''", "'");
			vals[i] = vals[i].replace("/[", "[");
			vals[i] = vals[i].replace("/]", "]");
			vals[i] = vals[i].replace("/%", "%");
			vals[i] = vals[i].replace("/&", "&");
			vals[i] = vals[i].replace("/_", "_");
			vals[i] = vals[i].replace("/(", "(");
			vals[i] = vals[i].replace("/)", ")");
		}
		return vals;
	}
	
	public static String deEscape(String val)
	{
		val = val.replace("//", "/");
		val = val.replace("''", "'");
		val = val.replace("/[", "[");
		val = val.replace("/]", "]");
		val = val.replace("/%", "%");
		val = val.replace("/&", "&");
		val = val.replace("/_", "_");
		val = val.replace("/(", "(");
		val = val.replace("/)", ")");
		return val;
	}
	
}


