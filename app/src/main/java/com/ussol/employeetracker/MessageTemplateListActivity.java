package com.ussol.employeetracker;

import java.io.File;
import java.util.Locale;

import com.ussol.employeetracker.helpers.DatabaseAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;	
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MessageTemplateListActivity  extends Activity  {
	private ListView listView;
	/** kết nối Database */
	private DatabaseAdapter mDatabaseAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message_template_list);
		mDatabaseAdapter = new DatabaseAdapter(getApplicationContext());
		
		listView = (ListView) findViewById(R.id.list);
		listView.setEmptyView(findViewById(R.id.empty));
		// Attach The Data From DataBase Into ListView Using Crusor Adapter
		mDatabaseAdapter.open();
		String[] from = new String[] { DatabaseAdapter.KEY_CODE, DatabaseAdapter.KEY_NAME,
				DatabaseAdapter.KEY_CONTENT};
		Cursor cursor = mDatabaseAdapter.getMessageTemplateList("",from, true);
		mDatabaseAdapter.close();

		int[] to = new int[] { R.id.id, R.id.title, R.id.desc };

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.activity_view_message_template, cursor, from, to);

		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);

		// OnCLickListiner For List Items
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long viewId) {
				TextView id_tv = (TextView) view.findViewById(R.id.id);
				TextView title_tv = (TextView) view.findViewById(R.id.title);
				TextView desc_tv = (TextView) view.findViewById(R.id.desc);

				String id = id_tv.getText().toString();
				String title = title_tv.getText().toString();
				String desc = desc_tv.getText().toString();

				Intent modify_intent = new Intent(getApplicationContext(),
						MessageTemplateEditActivity.class);
				modify_intent.putExtra("title", title);
				modify_intent.putExtra("desc", desc);
				modify_intent.putExtra("id", id);
				startActivity(modify_intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.message_template_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add_record) {
			Intent add_mem = new Intent(this, MessageTemplateAddActivity.class);
			startActivity(add_mem);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Exports the cursor value to an excel sheet.
	 * Recommended to call this method in a separate thread,
	 * especially if you have more number of threads.
	 *  
	 * @param cursor
	 */
	private void exportToExcel(Cursor cursor) {
		
		final String fileName = "MessageTemplateList.xls";
		
		/*//Saving file in external storage
		File sdCard = Environment.getExternalStorageDirectory();
		
		File directory = new File(sdCard.getAbsolutePath() + "/javatechig.todo");
		
		//create directory if not exist
		if(!directory.isDirectory()){
			directory.mkdirs();	
		}
		
		//file path
		File file = new File(directory, fileName);
		
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("vi", "VI"));
		
		WritableWorkbook workbook;
		
		try {
			workbook = Workbook.createWorkbook(file, wbSettings);
			
			//Excel sheet name. 0 represents first sheet
			WritableSheet sheet = workbook.createSheet("MyShoppingList", 0);

			try {
				sheet.addCell(new Label(0, 0, "Subject")); // column and row
				sheet.addCell(new Label(1, 0, "Description"));
				
				if (cursor.moveToFirst()) {
					do {
						String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODO_SUBJECT));
						String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODO_DESC));

						int i = cursor.getPosition() + 1;
						
						sheet.addCell(new Label(0, i, title));
						sheet.addCell(new Label(1, i, desc));
						
					} while (cursor.moveToNext());
				}
				
				//closing cursor
				cursor.close();
					
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			
			workbook.write();
			
			try {
				workbook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
}