/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import android.content.Context;

import com.ussol.employeetracker.models.CustomerGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;

/**
 * 
 * @author hoa-nx
 *  Sử dụng để tạo data init theo như công ty FJN
 */
public class InitDatabase {
	 DatabaseAdapter mDatabaseAdapter;
	 Context mContext;
	 public InitDatabase(Context context){
		 mContext = context;
		 mDatabaseAdapter = new DatabaseAdapter(mContext);
		
		 
	 }
	 public void InitAllData(){
		 /**Xóa hết data cũ ( xóa logic) */
		 mDatabaseAdapter.open();
		 mDatabaseAdapter.deleteDept();
		 mDatabaseAdapter.deleteTeam();
		 mDatabaseAdapter.deletePosition();
		 mDatabaseAdapter.deletePositionGroup();
		 mDatabaseAdapter.deleteCustomerGroup();
		 mDatabaseAdapter.close();
		 /** init data bộ phận */
		 initDept();
		 /** init data nhóm */
		 initTeam();
		 /** init data chức vụ */
		 initPosition();
		 /** init data nhóm chức danh*/
		 initPositionGroup();
		 /** init data nhóm các khách hàng*/
		 initCustomerGroup(); 
	 }
	 
	/**
	 * khởi tạo data cho phòng ban
	 */
	 private void initDept(){
		 try{
				Dept dept ;
				mDatabaseAdapter.open();
				
				/** Dept 1 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 1" ;
				dept.ryaku= "Ban 1";
				dept.yobi_text1= "Thân Minh Trung";
				dept.yobi_text2= "";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				/** Dept 2 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 2" ;
				dept.ryaku= "Ban 2";
				dept.yobi_text1= "Hoàng Đình Lệ Ngân";
				dept.yobi_text2= "";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				/** Dept 3 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 3" ;
				dept.ryaku= "Ban 3";
				dept.yobi_text1= "Lý Vĩnh Nhân Liêm";
				dept.yobi_text2= "";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				/** Dept 4 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 4" ;
				dept.ryaku= "Ban 4";
				dept.yobi_text1= "Mai Xuân Hiếu";
				dept.yobi_text2= "";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				/** Dept 5 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 5" ;
				dept.ryaku= "Ban 5";
				dept.yobi_text1= "Thân Minh Trung";
				dept.yobi_text2= "Nguyễn Xuân Hòa";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				
				/** Dept 6 */
				dept = new Dept();
				dept.mkbn=MasterConstants.MASTER_MKBN_DEPT;
				dept.name = "Ban 6" ;
				dept.ryaku= "Ban 6";
				dept.yobi_text1= "Phạm Nguyễn Mạnh";
				dept.yobi_text2= "";
				dept.note =	"";
				mDatabaseAdapter.insertToMasterTable(dept);
				
				/** đóng kết nối */
				mDatabaseAdapter.close();
		 }catch(Exception e ){
			 mDatabaseAdapter.close();
		 }	
	}
	 /**
	 * khởi tạo data cho nhóm
	 */
	 private void initTeam(){
		 try{
				Team  team;
				mDatabaseAdapter.open();
				/** Team 5.1 */
				team = new Team();
				team.mkbn=MasterConstants.MASTER_MKBN_TEAM;
				team.name = "Team 5.1" ;
				team.ryaku= "Team 5.1" ; 
				team.yobi_text1= "Ban 5";
				team.yobi_text2= "Nguyễn Minh Hải";
				team.note =	"";
				mDatabaseAdapter.insertToMasterTable(team);
				
				/** Team 5.2 */
				team = new Team();
				team.mkbn=MasterConstants.MASTER_MKBN_TEAM;
				team.name = "Team 5.2" ;
				team.ryaku= "Team 5.2" ; 
				team.yobi_text1= "Ban 5";
				team.yobi_text2= "Dương Thái Trung";
				team.note =	"";
				mDatabaseAdapter.insertToMasterTable(team);
				
				/** Team 5.3 */
				team = new Team();
				team.mkbn=MasterConstants.MASTER_MKBN_TEAM;
				team.name = "Team 5.3" ;
				team.ryaku= "Team 5.3" ; 
				team.yobi_text1= "Ban 5";
				team.yobi_text2= "Tưởng Nhật Quang";
				team.note =	"";
				mDatabaseAdapter.insertToMasterTable(team);
				
				/** đóng kết nối */
				mDatabaseAdapter.close();
		 }catch(Exception e ){
			 mDatabaseAdapter.close();
		 }	
	}
	 /**
	 * khởi tạo data cho chức vụ
	 */
	 private void initPosition(){
		 try{
				Position  pos;
				mDatabaseAdapter.open();
				
				/** System Administrator */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "System Administrator" ;
				pos.ryaku= "System Administrator" ; 
				pos.yobi_text1= "System Administrator";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Director */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Director" ;
				pos.ryaku= "Director" ; 
				pos.yobi_text1= "Director";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Deputy Director */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Deputy Director" ;
				pos.ryaku= "Deputy Director" ; 
				pos.yobi_text1= "Deputy Director";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Director Board Member */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Director Board Member" ;
				pos.ryaku= "Director Board Member" ; 
				pos.yobi_text1= "Director Board";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Manager 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Manager 2" ;
				pos.ryaku= "Manager 2" ; 
				pos.yobi_text1= "M2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Manager 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Manager 1" ;
				pos.ryaku= "Manager 1" ; 
				pos.yobi_text1= "M1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Manager 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Vice Manager" ;
				pos.ryaku= "Vice Manager" ; 
				pos.yobi_text1= "M0";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Leader 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Leader 2" ;
				pos.ryaku= "Leader 2" ; 
				pos.yobi_text1= "L2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Technical Leader 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Technical Leader 2" ;
				pos.ryaku= "Technical Leader 2" ; 
				pos.yobi_text1= "TL2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Leader 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Leader 1" ;
				pos.ryaku= "Leader 1" ; 
				pos.yobi_text1= "L1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Technical Leader 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Technical Leader 1" ;
				pos.ryaku= "Technical Leader 1" ; 
				pos.yobi_text1= "TL1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** SubLeader 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "SubLeader 2" ;
				pos.ryaku= "SubLeader 2" ; 
				pos.yobi_text1= "SL2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Technical SubLeader 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Technical SubLeader 2" ;
				pos.ryaku= "Technical SubLeader 2" ; 
				pos.yobi_text1= "TS2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** SubLeader 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "SubLeader 1" ;
				pos.ryaku= "SubLeader 1" ; 
				pos.yobi_text1= "SL1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Technical SubLeader 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Technical SubLeader 1" ;
				pos.ryaku= "Technical SubLeader 1" ; 
				pos.yobi_text1= "TS1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Senior Staff 4 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Senior Staff 4" ;
				pos.ryaku= "Senior Staff 4" ; 
				pos.yobi_text1= "SS4";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Senior Staff 3 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Senior Staff 3" ;
				pos.ryaku= "Senior Staff 3" ; 
				pos.yobi_text1= "SS3";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Senior Staff 2 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Senior Staff 2" ;
				pos.ryaku= "Senior Staff 2" ; 
				pos.yobi_text1= "SS2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Senior Staff 1 */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Senior Staff 1" ;
				pos.ryaku= "Senior Staff 1" ; 
				pos.yobi_text1= "SS1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				
				/** Junior Staff 2  */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Junior Staff 2 " ;
				pos.ryaku= "Junior Staff 2 " ; 
				pos.yobi_text1= "JS2";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Junior Staff 1  */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Junior Staff 1 " ;
				pos.ryaku= "Junior Staff 1 " ; 
				pos.yobi_text1= "JS1";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);

				/** Trial Staff  */
				pos = new Position();
				pos.mkbn=MasterConstants.MASTER_MKBN_POSITION;
				pos.name = "Trial Staff" ;
				pos.ryaku= "Trial Staff" ; 
				pos.yobi_text1= "Trial Staff";
				pos.yobi_code1= 0;
				pos.note =	"";
				mDatabaseAdapter.insertToMasterTable(pos);
				/** đóng kết nối */
				mDatabaseAdapter.close();
		 }catch(Exception e ){
			 mDatabaseAdapter.close();
		 }	
		 
	}
		
	 /**
	 * khởi tạo data cho nhóm
	 */
	 private void initPositionGroup(){
		 try{
				PositionGroup  positionGroup;
				mDatabaseAdapter.open();
				/** Tổng giám đốc */
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Tổng giám đốc" ;
				positionGroup.ryaku= "Tổng giám đốc" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phó Tổng giám đốc */
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phó Tổng giám đốc" ;
				positionGroup.ryaku= "Phó Tổng giám đốc" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Giám đốc */
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Giám đốc" ;
				positionGroup.ryaku= "Giám đốc" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Giám đốc kinh doanh*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Giám đốc kinh doanh" ;
				positionGroup.ryaku= "Giám đốc kinh doanh" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Giám đốc sản xuất*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Giám đốc sản xuất" ;
				positionGroup.ryaku= "Giám đốc sản xuất" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phó Giám đốc */
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phó Giám đốc" ;
				positionGroup.ryaku= "Phó Giám đốc" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phó Giám đốc kinh doanh*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phó Giám đốc kinh doanh" ;
				positionGroup.ryaku= "Phó Giám đốc kinh doanh" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phó Giám đốc sản xuất*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phó Giám đốc sản xuất" ;
				positionGroup.ryaku= "Phó Giám đốc sản xuất" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Trưởng phòng*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Trưởng phòng" ;
				positionGroup.ryaku= "Trưởng phòng" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phó phòng*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phó phòng" ;
				positionGroup.ryaku= "Phó phòng" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Trưởng nhóm*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Trưởng nhóm" ;
				positionGroup.ryaku= "Trưởng nhóm" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Lập trình*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Lập trình" ;
				positionGroup.ryaku= "Lập trình" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Phiên dịch*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Phiên dịch" ;
				positionGroup.ryaku= "Phiên dịch" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** Tổng vụ-Hành chánh*/
				positionGroup = new PositionGroup();
				positionGroup.mkbn=MasterConstants.MASTER_MKBN_POSITION_GROUP;
				positionGroup.name = "Tổng vụ-Hành chánh" ;
				positionGroup.ryaku= "Tổng vụ-Hành chánh" ; 
				positionGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(positionGroup);
				
				/** đóng kết nối */
				mDatabaseAdapter.close();
		 }catch(Exception e ){
			 mDatabaseAdapter.close();
		 }	
	}

	 /**
	 * khởi tạo data cho khách hàng
	 */
	 private void initCustomerGroup(){
		 try{
				CustomerGroup  customerGroup;
				mDatabaseAdapter.open();
				
				/** ITS*/
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "ITS" ;
				customerGroup.ryaku= "ITS" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** Uchida Esco */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "UCHIDA ESCO" ;
				customerGroup.ryaku= "UCHIDA ESCO" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** MDIS */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "MDIS" ;
				customerGroup.ryaku= "MDIS" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** UCHIDA YOKO */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "UCHIDA YOKO" ;
				customerGroup.ryaku= "UCHIDA" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				
				/** NTK */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "NTK" ;
				customerGroup.ryaku= "NTK" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** EX Corporation */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "EX(Factory-One-MF)" ;
				customerGroup.ryaku= "EX(Factory-One-MF)" ; 
				customerGroup.note =	"http://www.xeex.co.jp/";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** EX Corporation */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "EX(Factory-One-ST)" ;
				customerGroup.ryaku= "EX(Factory-One-ST)" ; 
				customerGroup.note =	"http://www.xeex.co.jp/";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** EX Corporation */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "EX(Factory-One-FS)" ;
				customerGroup.ryaku= "EX(Factory-One-FS)" ; 
				customerGroup.note =	"http://www.xeex.co.jp/";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** EX Corporation */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "EX(Factory-One-EX)" ;
				customerGroup.ryaku= "EX(Factory-One-EX)" ; 
				customerGroup.note =	"http://www.xeex.co.jp/";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** CHIYODA */
				customerGroup = new CustomerGroup();
				customerGroup.mkbn=MasterConstants.MASTER_MKBN_CUSTOMER_GROUP;
				customerGroup.name = "CHIYODA" ;
				customerGroup.ryaku= "CHIYODA" ; 
				customerGroup.note =	"";
				mDatabaseAdapter.insertToMasterTable(customerGroup);
				
				/** đóng kết nối */
				mDatabaseAdapter.close();
		 }catch(Exception e ){
			 mDatabaseAdapter.close();
		 }	
	 }
}
