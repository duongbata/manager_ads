package app.bean;

import java.util.List;
import java.util.Set;

import manager.common.bean.UserBean;

public class App01DataTrans implements DataTransIF{
	private String message;
	
	private boolean existGroup;
	
	private List<UserBean> listDev;
	
	private List<OSConfigBean> listOsConfig;
	
	private Set<String> setGroupId;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getExistGroup() {
		return existGroup;
	}

	public void setExistGroup(boolean existGroup) {
		this.existGroup = existGroup;
	}

	public List<UserBean> getListDev() {
		return listDev;
	}

	public void setListDev(List<UserBean> listDev) {
		this.listDev = listDev;
	}

	public List<OSConfigBean> getListOsConfig() {
		return listOsConfig;
	}

	public void setListOsConfig(List<OSConfigBean> listOsConfig) {
		this.listOsConfig = listOsConfig;
	}

	public Set<String> getSetGroupId() {
		return setGroupId;
	}

	public void setSetGroupId(Set<String> setGroupId) {
		this.setGroupId = setGroupId;
	}
}
