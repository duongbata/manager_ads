package app.logic;

import java.util.List;
import java.util.Set;

import app.bean.GroupAppBean;
import manager.common.bean.UserBean;

public interface App01LogicIF {
	List<UserBean> getAllDev();
	
	void insertGroupApp(GroupAppBean groupAppInsert,int userId) throws Throwable;
	
	boolean existGroupId(String groupId);
	
	Set<String> getSetGroupId();
}