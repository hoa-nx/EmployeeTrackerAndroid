package com.ussol.employeetracker.helpers;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TeamDialogAdapter extends ArrayAdapter{
	private static final int RESOURCE = R.layout.dialog_master_list_row;
	private LayoutInflater inflater;
	private Context context;
    static class ViewHolder {
        TextView nameTxVw;
        TextView userInfo;
        CheckBox chkPositionGroup;
    }

	public TeamDialogAdapter(Context context, Team[] objects)
	{
		super(context, RESOURCE, objects);
		inflater = LayoutInflater.from(context);
		context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if ( convertView == null ) {
			// inflate a new view and setup the view holder for future use
			convertView = inflater.inflate( RESOURCE, null );

			holder = new ViewHolder();
			holder.nameTxVw =(TextView) convertView.findViewById(R.id.txtListUserFullName);
			holder.userInfo =(TextView) convertView.findViewById(R.id.txtListUserInfor);
			holder.chkPositionGroup =(CheckBox) convertView.findViewById(R.id.chkListItem);
			convertView.setTag( holder );
		}  else {
			// view already defined, retrieve view holder
			holder = (ViewHolder) convertView.getTag();
		}

		Team cat =(Team) getItem( position );
		if ( cat == null ) {
			
		}
		holder.chkPositionGroup.setVisibility(View.GONE);
		holder.nameTxVw.setText( cat.name );
		holder.userInfo.setText( "");
		//holder.nameTxVw.setCompoundDrawables( cat.getImg(), null, null, null );
		//holder.nameTxVw.setCompoundDrawables(getImg( R.drawable.user_info), null, null, null );
		return convertView;
	}
	private Drawable getImg( int res )
	{
		Drawable img =context.getResources().getDrawable( res );
		img.setBounds( 0, 0, 75, 85 );
		return img;
	}
}
