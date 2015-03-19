package app.bean;

import java.util.List;

public class App05DataTrans implements DataTransIF{
	private List<GroupAppBean> listGroupApp;
	
	private String message;
	
	private int numPage;
	
	private List<String> listPage;
	
	private GroupAppBean groupAppEdit;

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

	public GroupAppBean getGroupAppEdit() {
		return groupAppEdit;
	}

	public void setGroupAppEdit(GroupAppBean groupAppEdit) {
		this.groupAppEdit = groupAppEdit;
	}
}
