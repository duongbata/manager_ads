package app.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import manager.ADV03.bean.BannerBean;
import manager.common.bean.RedisConstant;
import manager.common.bean.UserBean;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import app.action.APP01Action;
import app.bean.AppBean;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.bean.PropertyAppBean;
import app.dao.APP01Dao;
import app.dao.APP05Dao;
import app.logic.App05LogicIF;

@Service
public class App05LogicImpl implements App05LogicIF{
	private final static int NUMBER_OS = 3;
	@Autowired
	private StringRedisTemplate template;
	
	@Autowired
	private APP01Dao app01Dao;
	
	@Autowired
	private APP05Dao app05Dao;
	
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
			
			//List App in Group
			List<AppBean> listApp = new ArrayList<AppBean>();
			String patternAppOfGroup = RedisConstant.DB_ADS_DEV + ":uid:*:group:"+groupApp.getGroupId()+":app:*";
			Set<String> sAppKey = template.keys(patternAppOfGroup);
			for (String appKey : sAppKey) {
				String uid = appKey.split(":")[4];
				AppBean app = new AppBean();
				app.setGroupId(groupApp.getGroupId());
				app.setOsId(String.valueOf(template.opsForHash().get(appKey, "osId")));
				app.setAppId(String.valueOf(template.opsForHash().get(appKey, "appId")));
				app.setUid(uid);
				/*List<PropertyAppBean> listPropertyApp = new ArrayList<PropertyAppBean>();
				Set<Object> setObjKeyOfApp = template.opsForHash().keys(appKey);
				for (Object objKeyOfApp : setObjKeyOfApp) {
					String strKey = (String)objKeyOfApp;
					Object value = template.opsForHash().get(appKey, strKey);
					String strValue = value.toString();
					if ("appId".equals(strKey)) {
						app.setAppId(strValue);
					} else if ("osId".equals(strKey)) {
						app.setOsId(strValue);
					} else if ("version".equals(strKey)) {
						app.setVersion(strValue);
					} else if ("url".equals(strKey)) {
						app.setUrl(strValue);
					} else if ("config".equals(strKey)) {
						app.setConfig(strValue);
					} else {
						PropertyAppBean propApp = new PropertyAppBean(strKey, strValue);
						listPropertyApp.add(propApp);
					}
				}
				app.setListProperty(listPropertyApp);
				*/
				listApp.add(app);
			}
			groupApp.setListAppBean(listApp);
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
				OSConfigBean iosConfig;
				try {
					iosConfig = objMapper.readValue(json, OSConfigBean.class);
					iosConfig.setOsId(APP01Action.OS_IOS_ID);
					listOsConfig.add(iosConfig);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			boolean hasAndroid = template.opsForHash().hasKey(key, "android");
			if (hasAndroid) {
				String json = (String)template.opsForHash().get(key,"android");
				try {
					OSConfigBean androidConfig = objMapper.readValue(json, OSConfigBean.class);
					androidConfig.setOsId(APP01Action.OS_ANDROID_ID);
					listOsConfig.add(androidConfig);
				} catch(IOException e) {
					e.printStackTrace();
				}
				
			}
			
			boolean hasWindows = template.opsForHash().hasKey(key, "windows");
			if (hasWindows) {
				String json = (String)template.opsForHash().get(key, "windows");
				try {
					OSConfigBean windowsConfig = objMapper.readValue(json, OSConfigBean.class);
					windowsConfig.setOsId(APP01Action.OS_WINDOWS_ID);
					listOsConfig.add(windowsConfig);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			groupApp.setListOsConfig(listOsConfig);
			return groupApp;
		} else {
			return null;
		}
	}
	
	@Override
	public String validateGroupAppEdit(GroupAppBean groupAppEdit) {
		List<OSConfigBean> listOsConfig = groupAppEdit.getListOsConfig();
		if (listOsConfig != null && listOsConfig.size() > 0) {
			if (listOsConfig.size() > NUMBER_OS) {
				return "Chỉ được config cho nhiều nhất "+ NUMBER_OS +" hệ điều hành";
			}
			for (int idx = 0;idx < listOsConfig.size(); idx++) {
				OSConfigBean osConfig = listOsConfig.get(idx);
				if ("-1".equals(osConfig.getOsId())) {
					return "Hãy chọn hệ điều hành cho Config thứ " + (idx+1);
				}
				if ("-1".equals(osConfig.getUid())) {
					return "Hãy chọn dev cho Config thứ " + (idx+1);
				}
				for (int j = idx+1;j<listOsConfig.size();j++) {
					OSConfigBean osConfigNext = listOsConfig.get(j);
					if (osConfig.getOsId().equals(osConfigNext.getOsId())) {
						return "Config thứ " +(idx+1) + " và " + (j+1) + " cùng hệ điều hành. Hãy chọn lại";
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public List<UserBean> listAllDev() {
		return app01Dao.selectAllDev();
	}
	
	@Override
	public void updateGroupApp(GroupAppBean groupAppEdit, String adminId) throws IOException{
		List<OSConfigBean> listOsConfigOld = getGroupAppById(groupAppEdit.getGroupId()).getListOsConfig();
		//Update for group
		String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupAppEdit.getGroupId();
		template.opsForHash().put(queryDetailGroup, "groupId", groupAppEdit.getGroupId());
		template.opsForHash().put(queryDetailGroup, "groupName", groupAppEdit.getGroupName());
		template.opsForHash().put(queryDetailGroup, "groupIcon", groupAppEdit.getGroupIcon());
		template.opsForHash().put(queryDetailGroup, "groupTitle", groupAppEdit.getGroupTitle());
		template.opsForHash().put(queryDetailGroup, "groupDescription", groupAppEdit.getGroupDescription());
		template.opsForHash().put(queryDetailGroup, "imgBanner", groupAppEdit.getImgBanner());
		template.opsForHash().put(queryDetailGroup, "imgVertical", groupAppEdit.getImgVertical());
		template.opsForHash().put(queryDetailGroup, "imgHorizontal", groupAppEdit.getImgHorizontal());
		
		List<OSConfigBean> listOsConfigNew = groupAppEdit.getListOsConfig();
		updateInfoOfOsGroup(groupAppEdit.getGroupId(), listOsConfigOld, listOsConfigNew);
		//Update info for banner sample
		String queryBannerSample = RedisConstant.DB_ADS_CUSTOMER + ":uid:" + adminId + ":sample:banners" ;
		Set<String> bannerSampleIds = template.opsForSet().members(queryBannerSample);
		for (String bannerId : bannerSampleIds) {
			BannerBean bannerSample = app05Dao.selectBannerSampleById(bannerId);
			
			String bannerGroupSample = "banner_group_" + groupAppEdit.getGroupId();
			String popupGroupSample = "popup_group_" + groupAppEdit.getGroupId();
			if (bannerGroupSample.equals(bannerSample.getBannerName())) {
				bannerSample.setUserId(Integer.parseInt(adminId));
				bannerSample.setBannerDescription(groupAppEdit.getGroupDescription());
				bannerSample.setImage1(groupAppEdit.getImgBanner());
				app05Dao.updateBannerSample(bannerSample);
				updateDetailBanner(bannerSample);
				
			} else if (popupGroupSample.equals(bannerSample.getBannerName())){
				bannerSample.setUserId(Integer.parseInt(adminId));
				bannerSample.setBannerDescription(groupAppEdit.getGroupDescription());
				bannerSample.setImage1(groupAppEdit.getImgHorizontal());
				bannerSample.setImage2(groupAppEdit.getImgVertical());
				app05Dao.updateBannerSample(bannerSample);
				updateDetailBanner(bannerSample);
			}
		}
	}
	
	public void updateDetailBanner(BannerBean bannerSample) {
		String queryDetailBannerSample = RedisConstant.DB_ADS_BANNER_SAMPLE + ":" + bannerSample.getBannerId();
		if (template.hasKey(queryDetailBannerSample)) {
			template.delete(queryDetailBannerSample);
		}
		
		template.opsForHash().put(queryDetailBannerSample, "bannerId", String.valueOf(bannerSample.getBannerId()));
		template.opsForHash().put(queryDetailBannerSample, "bannerName", bannerSample.getBannerName());
		template.opsForHash().put(queryDetailBannerSample, "campaignId", String.valueOf(bannerSample.getCampaignId()));
		template.opsForHash().put(queryDetailBannerSample, "description", bannerSample.getBannerDescription() == null ? "" : bannerSample.getBannerDescription());
		template.opsForHash().put(queryDetailBannerSample, "bannerType", String.valueOf(bannerSample.getBannerType()));
//		template.opsForHash().put(queryDetailBannerSample, "bannerTypeName", bannerInsert.getBannerTypeName());
		template.opsForHash().put(queryDetailBannerSample, "startTime", String.valueOf(bannerSample.getStartTime().getTime()));
		template.opsForHash().put(queryDetailBannerSample, "stopTime", String.valueOf(bannerSample.getStopTime().getTime()));
		template.opsForHash().put(queryDetailBannerSample, "image1", bannerSample.getImage1() == null ? "" : bannerSample.getImage1());
		template.opsForHash().put(queryDetailBannerSample, "image2", bannerSample.getImage2() == null ? "" : bannerSample.getImage1());
		template.opsForHash().put(queryDetailBannerSample, "androidUrl", bannerSample.getAndroidUrl() == null ? "" : bannerSample.getAndroidUrl());
		template.opsForHash().put(queryDetailBannerSample, "iosUrl", bannerSample.getIosUrl() == null ? "" : bannerSample.getIosUrl());
		template.opsForHash().put(queryDetailBannerSample, "windowsUrl", bannerSample.getWindowsUrl() == null ? "" : bannerSample.getWindowsUrl());
	}
	
	public void updateInfoOfOsGroup(String groupId, List<OSConfigBean> listOsConfigOld, List<OSConfigBean> listOsConfigNew)throws IOException {
//		String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupId;
		int sizeOld = listOsConfigOld.size();
		int sizeNew = listOsConfigNew.size();
		
		if (sizeOld == 0 && sizeNew ==0) {
			return;
		} else if (sizeOld == 0 && sizeNew >0) {
			//Insert os Group
			for (OSConfigBean osNew : listOsConfigNew) {
				insertOsGroup(groupId,osNew);
			}
		} else if (sizeOld >0 && sizeNew ==0) {
			//Delete os Group
			for (OSConfigBean osOld : listOsConfigOld) {
				deleteOsGroup(groupId, osOld);
			}
		} else {
			//Update os Group
			for (OSConfigBean osOld : listOsConfigOld) {
				deleteOsGroup(groupId, osOld);
			}
			for (OSConfigBean osNew : listOsConfigNew) {
				insertOsGroup(groupId, osNew);
			}
		}
	}
	
	//Delete OsConfig in groupId
	public void deleteOsGroup(String groupId, OSConfigBean osConfig) {
		String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupId;
		String queryDev = null;
		if (APP01Action.OS_IOS_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:ios:groups";
			template.opsForHash().delete(queryDetailGroup, "ios");
		} else if (APP01Action.OS_ANDROID_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:android:groups";
			template.opsForHash().delete(queryDetailGroup, "android");
		} else if (APP01Action.OS_WINDOWS_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:windows:groups";
			template.opsForHash().delete(queryDetailGroup, "windows");
		}
		template.opsForSet().remove(queryDev, groupId);
	}
	
	//Insert osConfig to groupId
	public void insertOsGroup(String groupId, OSConfigBean osConfig) throws IOException {
		String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupId;
		ObjectMapper jackson = new ObjectMapper();
		String osConfigJson = jackson.writeValueAsString(osConfig);
		String queryDev = null;
		if (APP01Action.OS_IOS_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:ios:groups";
			template.opsForHash().put(queryDetailGroup, "ios", osConfigJson);
		} else if (APP01Action.OS_ANDROID_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:android:groups";
			template.opsForHash().put(queryDetailGroup, "android", osConfigJson);
		} else if (APP01Action.OS_WINDOWS_ID.equals(osConfig.getOsId())) {
			queryDev = RedisConstant.DB_ADS_DEV + ":uid:" + osConfig.getUid() + ":os:windows:groups";
			template.opsForHash().put(queryDetailGroup, "windows", osConfigJson);
		}
		template.opsForSet().add(queryDev, groupId);
	}
	
	@Override
	public AppBean getAppBeanByUserAndId(String appId, String uid) {
		AppBean appBean = new AppBean();
		List<PropertyAppBean> listProp = new ArrayList<PropertyAppBean>();
		appBean.setUid(uid);
		String os = appId.split("_")[0];
		String groupId = appId.substring(os.length()+1);
		appBean.setGroupId(groupId);
		
		String queryAppOfGroup = RedisConstant.DB_ADS_DEV + ":uid:" + uid + ":group:" + groupId + ":app:" + appId;
		Set<Object> setKeyOfApp = template.opsForHash().keys(queryAppOfGroup);
		for (Object key : setKeyOfApp) {
			String strKey = (String)key;
			String strValue = (String)template.opsForHash().get(queryAppOfGroup, strKey);
			if ("appId".equals(strKey)) {
				appBean.setAppId(strValue);
			} else if ("osId".equals(strKey)) {
				appBean.setOsId(strValue);
			} else if ("version".equals(strKey)) {
				appBean.setVersion(strValue);
			} else if ("url".equals(strKey)) {
				appBean.setUrl(strValue);
			} else if ("config".equals(strKey)) {
				appBean.setConfig(strValue);
			} else {
				PropertyAppBean prop = new PropertyAppBean();
				prop.setPropertyName(strKey);
				prop.setPropertyValue(strValue);
				listProp.add(prop);
			}
		}
		appBean.setListProperty(listProp);
		return appBean;
	}
	
	@Override
	public boolean updateApp(AppBean appBean, String devIdOld) {
		try {
			String queryAppDetail = RedisConstant.DB_ADS_DEV + ":uid:" + appBean.getUid() + ":group:" + appBean.getGroupId() + ":app:" + appBean.getAppId();
			
			if (!devIdOld.equals(appBean.getUid())) {
				//Delete info of devOld
				String queryListAppOfDevOld = RedisConstant.DB_ADS_DEV + ":uid:" + devIdOld + ":group:" + appBean.getGroupId() + ":apps";
				String queryAppDetailOld = RedisConstant.DB_ADS_DEV + ":uid:" + devIdOld + ":group:" + appBean.getGroupId() + ":app:" + appBean.getAppId();
				template.delete(queryAppDetailOld);
				template.opsForSet().remove(queryListAppOfDevOld, appBean.getAppId());
				
				//Add appId for list app of devNew
				String queryListAppOfDevNew = RedisConstant.DB_ADS_DEV + ":uid:" + appBean.getUid() + ":group:" + appBean.getGroupId() + ":apps";
				template.opsForSet().add(queryListAppOfDevNew, appBean.getAppId());
				
			} 
			
			if (template.hasKey(queryAppDetail)) {
				template.delete(queryAppDetail);
			}
			
			//Insert new
			template.opsForHash().put(queryAppDetail, "appId", appBean.getAppId());
			template.opsForHash().put(queryAppDetail, "url", appBean.getUrl());
			template.opsForHash().put(queryAppDetail, "version", appBean.getVersion());
			template.opsForHash().put(queryAppDetail, "config", appBean.getConfig());
			template.opsForHash().put(queryAppDetail, "osId", appBean.getOsId());
			if (appBean.getListProperty() != null && appBean.getListProperty().size() > 0) {
				for (PropertyAppBean prop : appBean.getListProperty()) {
					template.opsForHash().put(queryAppDetail, prop.getPropertyName(), prop.getPropertyValue());
				}
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	@Override
	/*
	 * (non-Javadoc) Chỉ xóa ở detail app
	 * @see app.logic.App05LogicIF#deleteAppById(java.lang.String)
	 */
	public void deleteAppById(String appId) {
		String os = appId.split("_")[0];
		String groupId = appId.substring(os.length() + 1);
		//Delete info app : db:ads:dev:uid:{uid}:group:{groupId}:app:{appId}
		String patternAppDetail = "db:ads:dev:uid:*:group:"+groupId+":app:"+appId;
		Set<String> sKeyApp = template.keys(patternAppDetail); 
		for (String keyApp : sKeyApp) {
			template.delete(keyApp);
		}
		//Delete app in list app of group : db:ads:dev:uid:{uid}:group:{groupId}:apps
		/*String patternGroupOfApp = "db:ads:dev:uid:*:group:"+groupId+":apps";
		Set<String> sKeyAppOfGroup = template.keys(patternGroupOfApp);
		for (String keyAppOfGroup : sKeyAppOfGroup) {
			if (template.opsForSet().isMember(keyAppOfGroup, appId)) {
				template.opsForSet().remove(keyAppOfGroup, appId);
			}
			//If list app of group == 0 -> delete key
			if (template.opsForSet().members(keyAppOfGroup).size() == 0) {
				template.delete(keyAppOfGroup);
			}
			
		}*/
		
		//Delete in group : db:ads:group:{groupId}
		/*String keyGroup = "db:ads:group:"+groupId;
		template.opsForHash().delete(keyGroup, os);*/
	}
	
	@Override
	public void deleteAppByGroupId(String groupId, String uid) {
		String keyOfGroup = "db:ads:group:"+groupId;
		String bannerSampleId = (String) template.opsForHash().get(keyOfGroup, "banner_sample");
		String popupSampleId = (String) template.opsForHash().get(keyOfGroup, "popup_sample");
		//Delete all key has groupId
		String patternGroup = "db:ads:*"+groupId+"*";
		Set<String> sKey = template.keys(patternGroup);
		for (String key : sKey) {
			template.delete(key);
		}
		//Delete groupId in db:ads:dev:uid:{uid}:os:{osId}:groups
		String patternGroupOfDev = "db:ads:dev:uid:*:os:*:groups";
		Set<String> sKeyGroupOfDev = template.keys(patternGroupOfDev);
		for (String keyGroupOfDev : sKeyGroupOfDev) {
			if (template.opsForSet().isMember(keyGroupOfDev, groupId)) {
				template.opsForSet().remove(keyGroupOfDev, groupId);
			}
		}
		
		//Delete groupId in db:ads:groups
		String patterListGroupId = "db:ads:groups";
		template.opsForSet().remove(patterListGroupId, groupId);
		
		//Delete banner sample
		app05Dao.deleteBannerById(bannerSampleId);
		app05Dao.deleteBannerById(popupSampleId);
		String keyListBannerSample = "db:ads:customer:uid:" + uid + ":sample:banners";
		template.opsForSet().remove(keyListBannerSample, bannerSampleId);
		template.opsForSet().remove(keyListBannerSample, popupSampleId);
	}
	
	
}
