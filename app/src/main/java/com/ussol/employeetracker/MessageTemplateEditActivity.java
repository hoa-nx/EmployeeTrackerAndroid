package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.MessageTemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MessageTemplateEditActivity extends Activity implements OnClickListener {
	
	private EditText titleText;
	private Button updateBtn, deleteBtn;
	private EditText descText;
	
	private int _id;
	
	/** kết nối Database */
	private DatabaseAdapter mDatabaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setTitle("Modify Record");
		  
		setContentView(R.layout.activity_modify_message_template);
		mDatabaseAdapter = new DatabaseAdapter(this);

		titleText = (EditText) findViewById(R.id.subject_edittext);
		descText = (EditText) findViewById(R.id.description_edittext);
		
		updateBtn = (Button) findViewById(R.id.btn_update);
		deleteBtn = (Button) findViewById(R.id.btn_delete);

		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		String name = intent.getStringExtra("title");
		String desc = intent.getStringExtra("desc");

		_id = Integer.parseInt(id);

		titleText.setText(name);
		descText.setText(desc);
		
		updateBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update:
			String title = titleText.getText().toString();
			String desc = descText.getText().toString();

			MessageTemplate item = new MessageTemplate();
			item.code = _id;
            item.name = title ;
            item.content = desc;
            
            mDatabaseAdapter.open();
            mDatabaseAdapter.editMessageTemplateTable(item);
            mDatabaseAdapter.close();
            
			this.returnHome();
			break;

		case R.id.btn_delete:
			mDatabaseAdapter.open();
			mDatabaseAdapter.deleteMessageTemplateByCode(_id);
			mDatabaseAdapter.close();
			this.returnHome();
			break;
		}
	}

	public void returnHome() {
		Intent home_intent = new Intent(getApplicationContext(), MessageTemplateListActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home_intent);
	}
}