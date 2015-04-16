package app.logic;

import java.io.IOException;
import java.util.List;

import manager.common.bean.UserBean;
import app.bean.AppBean;
import app.bean.GroupAppBean;

public interface App05LogicIF {
	List<GroupAppBean> listAllGroup();
	
	GroupAppBean getGroupAppById(String groupId);
	
	List<UserBean> listAllDev();
	
	void updateGroupApp(GroupAppBean groupAppEdit, String adminId) throws IOException;
	
	String validateGroupAppEdit(GroupAppBean groupAppEdit);
	
	AppBean getAppBeanByUserAndId(String appId, String uid);
	
	boolean updateApp(AppBean appBean, String devIdOld);
	
	void deleteAppById(String appId);
	
	void deleteAppByGroupId(String groupId,String uid);
}
