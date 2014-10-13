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
 * @brief	封装了访问数据库的一般方法
 * @notice	标识符（如表名列名请不要使用'（英文单引号）），值可以使用，该类会对其转义
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
	 * @brief	创建一个Table，如果存在则不创建
	 * @param 	table_name 表名
	 * @param 	col_names 列名，可以包含数据类型和限定符等
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
	 * @brief	删除数据表，如果不存在则忽略
	 * @param 	table_name 表名
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
	 * @brief	向表中插入一行
	 * @param 	table_name 表名
	 * @param 	col_names 要插入的列名
	 * @param 	vals 要插入的数据数组，与列名要对应
	 * @notice	插入的数据用字符串表示，因为SQL语句都是字符串
	 */
	public void insertInto(String table_name, String[] col_names, String[] vals)
	{
		// 处理val值的转意
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
	 * @brief	向表中插入一行， vals需要转义
	 * @param 	table_name 表名
	 * @param 	vals 要插入的数据数组
	 * @notice	插入的数据用字符串表示，因为SQL语句都是字符串
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
	 * @brief	更新数据表
	 * @param 	table 表名
	 * @param 	set 要修改的列的表达式 e.g. "phone  = '123456', addr = 'GZ'"
	 * @param 	where 选择要修改的行的表达式 e.g. "name = 'adolli'"
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
	 * 更新数据库某表的某行，自动会将值转义
	 * @param table 表名
	 * @param cols 要更新的列名
	 * @param vals 更新的值
	 * @param where where表达式
	 */
	public void update(String table, String[] cols, String[] vals, String where)
	{
		// 字符转义
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
	 * @brief	清空整个表的内容
	 * @param 	table 表名
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


