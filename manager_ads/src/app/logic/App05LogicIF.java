package app.logic;

import java.util.List;

import app.bean.GroupAppBean;

public interface App05LogicIF {
	List<GroupAppBean> listAllGroup();
	
	GroupAppBean getGroupAppById(String groupId);
}
