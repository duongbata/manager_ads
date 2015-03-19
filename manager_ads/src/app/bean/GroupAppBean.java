package app.bean;

import java.util.List;

public class GroupAppBean {
	private String groupId;
	
	private String groupName;
	
	private String groupIcon;
	
	private String groupTitle;
	
	private String groupDescription;
	
	private String imgBanner;
	
	private String imgVertical;
	
	private String imgHorizontal;
	
	private List<OSConfigBean> listOsConfig;
	
	//APP05
	private List<AppBean> listAppBean;  
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupIcon() {
		return groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getImgBanner() {
		return imgBanner;
	}

	public void setImgBanner(String imgBanner) {
		this.imgBanner = imgBanner;
	}

	public String getImgVertical() {
		return imgVertical;
	}

	public void setImgVertical(String imgVertical) {
		this.imgVertical = imgVertical;
	}

	public String getImgHorizontal() {
		return imgHorizontal;
	}

	public void setImgHorizontal(String imgHorizontal) {
		this.imgHorizontal = imgHorizontal;
	}
	
	public List<OSConfigBean> getListOsConfig() {
		return listOsConfig;
	}
	
	public void setListOsConfig(List<OSConfigBean> listOsConfig) {
		this.listOsConfig = listOsConfig;
	}

	public List<AppBean> getListAppBean() {
		return listAppBean;
	}

	public void setListAppBean(List<AppBean> listAppBean) {
		this.listAppBean = listAppBean;
	}
}
