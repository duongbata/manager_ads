package app.logic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import manager.common.bean.RedisConstant;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import app.action.APP01Action;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.logic.App05LogicIF;

@Service
public class App05LogicImpl implements App05LogicIF{
	@Autowired
	private StringRedisTemplate template;
	
	@Override
	public List<GroupAppBean> listAllGroup() {
		List<GroupAppBean> listGroup = new ArrayList<GroupAppBean>();
		String paternGroupDetail = RedisConstant.DB_ADS_GROUP + ":*";
		Set<String> sKeys = template.keys(paternGroupDetail);
		for (String key : sKeys) {
			GroupAppBean groupApp = new GroupAppBean();
			groupApp.setGroupId((String)(template.opsForHash().get(key, "groupId")));
			groupApp.setGroupName((String)(template.opsForHash().get(key, "groupName")));
			groupApp.setGroupDescription((String)(template.opsForHash().get(key, "groupDescription")));
			groupApp.setGroupTitle((String)(template.opsForHash().get(key, "groupTitle")));
			groupApp.setGroupIcon((String)(template.opsForHash().get(key, "groupIcon")));
			listGroup.add(groupApp);
		}
		return listGroup;
	}
	
	@Override
	public GroupAppBean getGroupAppById(String groupId) {
		String key = RedisConstant.DB_ADS_GROUP + ":" + groupId;
		
		boolean hasKey = template.hasKey(key);
		if (hasKey) {
			GroupAppBean groupApp = new GroupAppBean();
			groupApp.setGroupId(groupId);
			groupApp.setGroupName((String)(template.opsForHash().get(key, "groupName")));
			groupApp.setGroupDescription((String)(template.opsForHash().get(key, "groupDescription")));
			groupApp.setGroupTitle((String)(template.opsForHash().get(key, "groupTitle")));
			groupApp.setGroupIcon((String)(template.opsForHash().get(key, "groupIcon")));
			groupApp.setImgBanner((String)(template.opsForHash().get(key, "imgBanner")));
			groupApp.setImgVertical((String)(template.opsForHash().get(key, "imgVertical")));
			groupApp.setImgHorizontal((String)(template.opsForHash().get(key, "imgHorizontal")));
			
			List<OSConfigBean> listOsConfig = new ArrayList<OSConfigBean>();
			ObjectMapper objMapper = new ObjectMapper();
			
			boolean hasIosApp = template.opsForHash().hasKey(key, "ios");
			if (hasIosApp) {
				String json = (String) template.opsForHash().get(key, "ios");
				OSConfigBean iosConfig = objMapper.convertValue(json, OSConfigBean.class);
				iosConfig.setOsId(APP01Action.OS_IOS_ID);
				listOsConfig.add(iosConfig);
			}
			
			boolean hasAndroid = template.opsForHash().hasKey(key, "android");
			if (hasAndroid) {
				String json = (String)template.opsForHash().get(key,"android");
				OSConfigBean androidConfig = objMapper.convertValue(json, OSConfigBean.class);
				androidConfig.setOsId(APP01Action.OS_ANDROID_ID);
				listOsConfig.add(androidConfig);
			}
			
			boolean hasWindows = template.opsForHash().hasKey(key, "windows");
			if (hasWindows) {
				String json = (String)template.opsForHash().get(key, "windows");
				OSConfigBean windowsConfig = objMapper.convertValue(json, OSConfigBean.class);
				windowsConfig.setOsId(APP01Action.OS_WINDOWS_ID);
				listOsConfig.add(windowsConfig);
			}
			
			groupApp.setListOsConfig(listOsConfig);
			return groupApp;
		} else {
			return null;
		}
	}
}
