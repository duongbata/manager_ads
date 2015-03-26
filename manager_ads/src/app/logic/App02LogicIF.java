package app.logic;

import java.util.Map;

import org.json.JSONObject;

import manager.common.bean.UserBean;
import app.bean.AppBean;
import app.bean.AppConfigBean;

public interface App02LogicIF {
	Map<String, String> getGroupsOfUser(String uid);
	
	void insertAppBean(AppBean appInsert, String uid, String adminId);
	
	Map<String, String> getMapGroupByOs(String osId, String uid);
	
	Map<String, String> getMapAllGroup();
	
	Map<String, String> getOsOfGroup(String groupId);
	
	UserBean getDevByOsAndGroup(String groupId, String osId);
	
	AppConfigBean createAppConfigFromStr(String str);
	
	JSONObject createJSONObjectFromAppConfig(AppConfigBean appConfig);
}
