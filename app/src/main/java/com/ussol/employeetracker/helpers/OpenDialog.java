package com.ussol.employeetracker.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class OpenDialog {

	  public OpenDialog(Context context) {
	    _OpenDialogLayout = new OpenDialogLayout(context);

	    _Dialog = new AlertDialog.Builder(context);
	    _Dialog.setTitle("Open Dialog");
	    _Dialog.setView(_OpenDialogLayout);
	    _Dialog.setPositiveButton("Ok", _OnPositiveClick);
	    _Dialog.setNegativeButton("Cancel", _OnNegativeClick);
	  }

	  private Builder _Dialog = null;
	  private OpenDialogLayout _OpenDialogLayout = null;

	  // Event
	  private OnFileSelectedListener _OnFileSelected = null;
	  private OnNotifyEventListener _OnCanceled = null;

	  public void Show() {
	    _Dialog.show();
	  }

	  public void setOnFileSelected(OnFileSelectedListener value) {
	    _OnFileSelected = value;
	  }

	  public OnFileSelectedListener getOnFileSelected() {
	    return _OnFileSelected;
	  }

	  public void setOnCanceled(OnNotifyEventListener value) {
	    _OnCanceled = value;
	  }

	  public OnNotifyEventListener getOnCanceled() {
	    return _OnCanceled;
	  }

	  private DialogInterface.OnClickListener _OnPositiveClick = new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	      if (_OnFileSelected != null) {
	        _OnFileSelected.onSelected(_OpenDialogLayout.getPath(),
	            _OpenDialogLayout.getFileName());
	      }
	    }
	  };

	  private DialogInterface.OnClickListener _OnNegativeClick = new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	      if (_OnCanceled != null) {
	        _OnCanceled.onNotify(OpenDialog.this);
	      }
	    }
	  };

	}

	interface OnFileSelectedListener {

	  public void onSelected(String path, String fileName);

	}

	interface OnNotifyEventListener {

	  public void onNotify(Object sender);

	}

	interface OnPathChangedListener {

	  public void onChanged(String path);

	}