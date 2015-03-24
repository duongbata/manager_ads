package app.bean;

import java.util.List;
import java.util.Map;

import manager.common.bean.UserBean;

public class App05DataTrans implements DataTransIF{
	private List<GroupAppBean> listGroupApp;
	
	private String message;
	
	private int numPage;
	
	private List<String> listPage;
	
//	private GroupAppBean groupAppEdit;
	
	private List<UserBean> listDev;
	
	private Map<String, String> mapOs;

	public List<GroupAppBean> getListGroupApp() {
		return listGroupApp;
	}

	public void setListGroupApp(List<GroupAppBean> listGroupApp) {
		this.listGroupApp = listGroupApp;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumPage() {
		return numPage;
	}

	public void setNumPage(int numPage) {
		this.numPage = numPage;
	}

	public List<String> getListPage() {
		return listPage;
	}

	public void setListPage(List<String> listPage) {
		this.listPage = listPage;
	}

	/*public GroupAppBean getGroupAppEdit() {
		return groupAppEdit;
	}

	public void setGroupAppEdit(GroupAppBean groupAppEdit) {
		this.groupAppEdit = groupAppEdit;
	}*/

	public List<UserBean> getListDev() {
		return listDev;
	}

	public void setListDev(List<UserBean> listDev) {
		this.listDev = listDev;
	}

	public Map<String, String> getMapOs() {
		return mapOs;
	}

	public void setMapOs(Map<String, String> mapOs) {
		this.mapOs = mapOs;
	}
}
