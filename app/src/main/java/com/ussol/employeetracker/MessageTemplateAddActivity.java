package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.MessageItem;
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

public class MessageTemplateAddActivity extends Activity  implements OnClickListener {

	private Button addTodoBtn;
	private EditText subjectEditText;
	private EditText descEditText;
	/** kết nối Database */
	private DatabaseAdapter mDatabaseAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setTitle("Add Record");
        
        setContentView(R.layout.activity_add_message_template);
        
        mDatabaseAdapter = new DatabaseAdapter(this);
        
        subjectEditText = (EditText) findViewById(R.id.subject_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);
        
        addTodoBtn = (Button) findViewById(R.id.add_record);

        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.add_record:
        	
            final String name = subjectEditText.getText().toString();
            final String desc = descEditText.getText().toString();
            MessageTemplate item = new MessageTemplate();
            item.name = name ;
            item.content = desc;
            
            mDatabaseAdapter.open();
            mDatabaseAdapter.insertToMessageTemplateTable(item);
            mDatabaseAdapter.close();
            
            Intent main = new Intent(MessageTemplateAddActivity.this, MessageTemplateListActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            startActivity(main);
            break;
        }
    }

}