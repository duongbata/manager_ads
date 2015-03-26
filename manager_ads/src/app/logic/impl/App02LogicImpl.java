package app.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manager.ADV03.bean.BannerBean;
import manager.ADV03.logic.ADV03LogicIF;
import manager.common.bean.ConstantBean;
import manager.common.bean.RedisConstant;
import manager.common.bean.UserBean;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import app.action.APP01Action;
import app.bean.AppBean;
import app.bean.AppConfigBean;
import app.bean.FieldBean;
import app.bean.PropertyAppBean;
import app.dao.APP02Dao;
import app.logic.App02LogicIF;

@Service
public class App02LogicImpl implements App02LogicIF{
	@Autowired
	private StringRedisTemplate template;
	
	@Autowired
	private ADV03LogicIF adv03Logic;
	
	@Autowired
	private APP02Dao app02Dao;
	
	@Override
	public Map<String, String> getGroupsOfUser(String uid) {
		Map<String, String> mapGroups = new HashMap<String, String>();
		String queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + uid + ":groups";
		if (template.hasKey(queryDev)) {
			Set<String> setGroupId = template.opsForSet().members(queryDev);
			if (setGroupId != null && setGroupId.size() >0) {
				for (String groupId : setGroupId) {
					String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupId;
					String groupName = (String) template.opsForHash().get(queryDetailGroup, "groupName");
					mapGroups.put(groupId, groupName);
				}
			}
		}
		return mapGroups;
	}
	
	@Override
	public void insertAppBean(AppBean appInsert, String uid, String adminId) {
		if (APP01Action.OS_IOS_ID.equals(appInsert.getOsId())) {
			appInsert.setAppId("ios_"+appInsert.getGroupId());
			appInsert.setOsId("ios");
		} else if (APP01Action.OS_ANDROID_ID.equals(appInsert.getOsId())) {
			appInsert.setAppId("android_"+appInsert.getGroupId());
			appInsert.setOsId("android");
		} else if (APP01Action.OS_WINDOWS_ID.equals(appInsert.getOsId())) {
			appInsert.setAppId("windows_"+appInsert.getGroupId());
			appInsert.setOsId("windows");
		}
		String queryApp = RedisConstant.DB_ADS_DEV + ":uid:" + uid + ":group:" + appInsert.getGroupId() + ":apps";
		template.opsForSet().add(queryApp, appInsert.getAppId());
		String queryDetailApp = RedisConstant.DB_ADS_DEV + ":uid:" + uid + ":group:" + appInsert.getGroupId() + ":app:" + appInsert.getAppId();
		template.opsForHash().put(queryDetailApp, "appId", appInsert.getAppId());
		template.opsForHash().put(queryDetailApp, "url", appInsert.getUrl());
		template.opsForHash().put(queryDetailApp, "version", appInsert.getVersion());
		template.opsForHash().put(queryDetailApp, "osId", appInsert.getOsId());
		template.opsForHash().put(queryDetailApp, "config", appInsert.getConfig());
		if (appInsert.getListProperty() != null && appInsert.getListProperty().size() > 0) {
			for (PropertyAppBean propertyApp : appInsert.getListProperty()) {
				template.opsForHash().put(queryDetailApp, propertyApp.getPropertyName(), propertyApp.getPropertyValue());
			}
		}
		
		//Update Banner Sample of Group and url in Detail Banner (See APP01LogicImpl.insertGroupApp)
		BannerBean bannerSearch = new BannerBean();
		bannerSearch.setUserId(Integer.valueOf(adminId));
		List<BannerBean> listBannerSample = adv03Logic.listSampleBannerOfUser(bannerSearch);
		List<BannerBean> listBannerSampleOfGroup = new ArrayList<BannerBean>();
		if (listBannerSample != null && listBannerSample.size() > 0) {
			String bannerOfGroupName = "banner_group_" + appInsert.getGroupId();
			String popupOfGroupName = "popup_group_" + appInsert.getGroupId();
			for (BannerBean banner : listBannerSample) {
				if (bannerOfGroupName.equals(banner.getBannerName()) || popupOfGroupName.equals(banner.getBannerName())) {
					//Update url BannerSample
					if ("ios".equals(appInsert.getOsId())) {
						banner.setIosUrl(appInsert.getUrl());
					} else if ("android".equals(appInsert.getOsId())) {
						banner.setAndroidUrl(appInsert.getUrl());
					} else {
						banner.setWindowsUrl(appInsert.getUrl());
					}
					app02Dao.updateUrlOfBanner(banner);
					
					listBannerSampleOfGroup.add(banner);
				} 
			}
		}
//		updateDetailBanner(listBannerSampleOfGroup, adminId);
	}
	
	//Update url for For Detail Banner
	public void updateDetailBanner(List<BannerBean> listBannerSample, String adminId) {
		String bannerInCampaign = RedisConstant.DB_ADS_CUSTOMER +  ":uid:"+adminId+":campaign:*:banner:*";
		Set<String> keys = template.keys(bannerInCampaign);
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				String bannerSampleId = (String)template.opsForHash().get(key, "bannerSampleId");
				String bannerId = (String)template.opsForHash().get(key, "bannerId");
				for (BannerBean bannerSample : listBannerSample) {
					if (bannerSampleId.equals(String.valueOf(bannerSample.getBannerId()))) {
						BannerBean banner = new BannerBean();
						banner.setBannerId(Integer.valueOf(bannerId));
						//To redis
						if (bannerSample.getIosUrl() != null) {
							template.opsForHash().put(key, "iosUrl", bannerSample.getIosUrl());
							banner.setIosUrl(bannerSample.getIosUrl());
						} else if (bannerSample.getAndroidUrl() != null) {
							template.opsForHash().put(key, "androidUrl", bannerSample.getAndroidUrl());
							banner.setAndroidUrl(bannerSample.getAndroidUrl());
						} else if (bannerSample.getWindowsUrl() != null) {
							template.opsForHash().put(key, "windows", bannerSample.getWindowsUrl());
							banner.setWindowsUrl(bannerSample.getWindowsUrl());
						}
						
						app02Dao.updateUrlOfBanner(banner);
					}
				}
			}
		}
	}
	
	@Override
	public Map<String, String> getMapGroupByOs(String osId, String uid) {
		Map<String, String> mapGroup = new HashMap<String, String>();
		String osName = null;
		if (APP01Action.OS_IOS_ID.equals(osId)) {
			osName = "ios";
		} else if(APP01Action.OS_ANDROID_ID.equals(osId)) {
			osName = "android";
		} else if (APP01Action.OS_WINDOWS_ID.equals(osId)) {
			osName = "windows";
		}
		String queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + uid + ":os:" + osName + ":groups";
		Set<String> setGroupId = template.opsForSet().members(queryDev);
		if (setGroupId != null && setGroupId.size() > 0) {
			for (String groupId : setGroupId) {
				String queryGroupDetail = RedisConstant.DB_ADS_GROUP + ":" + groupId;
				String groupName = (String) template.opsForHash().get(queryGroupDetail, "groupName");
				mapGroup.put(groupId, groupName);
			}
		}
		return mapGroup;
	}
	
	@Override
	public Map<String, String> getMapAllGroup() {
		Map<String,String> allGroup = new HashMap<String,String>();
		String patternGroup = RedisConstant.DB_ADS_GROUP + ":*";
		Set<String> keys = template.keys(patternGroup);
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				String groupId = (String) template.opsForHash().get(key, "groupId");
				String groupName = (String) template.opsForHash().get(key, "groupName");
				allGroup.put(groupId, groupName);
			}
		}
		return allGroup;
	}
	
	@Override
	public Map<String, String> getOsOfGroup(String groupId) {
		Map<String, String> allOs = new HashMap<String, String>();
		/*String patternGroup =RedisConstant.DB_ADS_DEV + ":uid:*:group:"+groupId+":apps";
		Set<String> keys = template.keys(patternGroup);
		for (String key : keys) {
			Set<String> apps = template.opsForSet().members(key);
			if (apps != null && apps.size() > 0) {
				for (String app : apps) {
					String os = app.split("_")[0];
					if (ConstantBean.IOS.equals(os)) {
						allOs.put(APP01Action.OS_IOS_ID, os);
					} else if (ConstantBean.ANDROID.equals(os)) {
						allOs.put(APP01Action.OS_ANDROID_ID, os);
					} else if (ConstantBean.WINDOWS.equals(os)) {
						allOs.put(APP01Action.OS_WINDOWS_ID, os);
					}
				}
			}
		}*/
		String patternGroup = RedisConstant.DB_ADS_DEV + ":uid:*:os:*:groups";
		Set<String> keys = template.keys(patternGroup);
		for (String key : keys) {
			Set<String> groupOfUser = template.opsForSet().members(key);
			if (groupOfUser != null && groupOfUser.size() > 0) {
				for (String group:groupOfUser) {
					String os = key.split(":")[6];
					if (group.equals(groupId)) {
						if ("android".equals(os)) {
							allOs.put(APP01Action.OS_ANDROID_ID, os);
						} else if ("ios".equals(os)) {
							allOs.put(APP01Action.OS_IOS_ID, os);
						} else if ("windows".equals(os)) {
							allOs.put(APP01Action.OS_WINDOWS_ID, os);
						}
					}
				}
			}
		}
		return allOs;
	}
	
	@Override
	public UserBean getDevByOsAndGroup(String groupId, String osId) {
		String patternDevGroup = null;
		if (APP01Action.OS_IOS_ID.equals(osId)) {
			patternDevGroup = RedisConstant.DB_ADS_DEV + ":uid:*:os:ios:groups";
		} else if (APP01Action.OS_ANDROID_ID.equals(osId)) {
			patternDevGroup = RedisConstant.DB_ADS_DEV + ":uid:*:os:android:groups";
		} else if (APP01Action.OS_WINDOWS_ID.equals(osId)) {
			patternDevGroup = RedisConstant.DB_ADS_DEV + ":uid:*:os:windows:groups";
		}
		if (patternDevGroup == null) {
			return null;
		}
		
		Set<String> keys = template.keys(patternDevGroup);
		if (keys != null && keys.size() > 0) {
			String userId = null;
			for (String key : keys) {
				Set<String> listGroup = template.opsForSet().members(key);
				if(listGroup != null && listGroup.size() > 0 && listGroup.contains(groupId)) {
					userId = key.split(":")[4];
					break;
				}
			}
			
			if (userId != null) {
				return app02Dao.getUserById(userId);
			} else {
				return null;
			}
		} else {
			return null;
		}
		
	}
	
	@Override
	public AppConfigBean createAppConfigFromStr(String str) {
		AppConfigBean appConfig = new AppConfigBean();
		JSONObject jsonObj = new JSONObject(str);
		Set<String> keys = jsonObj.keySet();
		if (keys != null && keys.size() >0) {
			List<AppConfigBean> listAppConfig = new ArrayList<AppConfigBean>();
			List<FieldBean> listField = new ArrayList<FieldBean>();
			for (String key : keys) {
				Object obj = jsonObj.get(key);
				if (obj instanceof JSONObject) {
					AppConfigBean app = createAppConfigFromStr(obj.toString());
					app.setNameConfig(key);
					listAppConfig.add(app);
				} else {
					FieldBean field = new FieldBean();
					field.setName(key);
					field.setValue(obj.toString());
					listField.add(field);
				}
			}
			appConfig.setListConfigBean((listAppConfig.size() == 0) ? null : listAppConfig);
			appConfig.setListFieldBean((listField.size() == 0)? null : listField);
		}
		
		return appConfig;
	}
	
	@Override
	public JSONObject createJSONObjectFromAppConfig(AppConfigBean appConfig) {
		JSONObject jsonObj = new JSONObject();
		List<AppConfigBean> listAppConfig = appConfig.getListConfigBean();
		if (listAppConfig != null && listAppConfig.size() > 0) {
			for (AppConfigBean app : listAppConfig) {
				JSONObject toJson = createJSONObjectFromAppConfig(app);
				jsonObj.put(app.getNameConfig(), toJson);
			}
		}
		List<FieldBean> listField = appConfig.getListFieldBean();
		if (listField != null && listField.size() > 0) {
			for (FieldBean field : listField) {
				jsonObj.put(field.getName(), field.getValue());
			}
		}
		return jsonObj;
	}
}
