package app.logic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import manager.ADV03.bean.BannerBean;
import manager.ADV03.logic.ADV03LogicIF;
import manager.common.bean.ConstantBean;
import manager.common.bean.RedisConstant;
import manager.common.bean.UserBean;
import app.action.APP01Action;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.dao.APP01Dao;
import app.logic.App01LogicIF;

@Service
public class App01LogicImpl implements App01LogicIF{
	@Autowired
	private APP01Dao app01Dao;
	
	@Autowired
	private StringRedisTemplate template;
	
	@Autowired
	private ADV03LogicIF adv03Logic;
	
	@Override
	public List<UserBean> getAllDev() {
		return app01Dao.selectAllDev();
	}
	
	@Override
	public void insertGroupApp(GroupAppBean groupAppInsert, int userId) throws Throwable{
		//To Redis
		String queryDetailGroup = RedisConstant.DB_ADS_GROUP + ":" + groupAppInsert.getGroupId();
		template.opsForHash().put(queryDetailGroup, "groupId", groupAppInsert.getGroupId());
		template.opsForHash().put(queryDetailGroup, "groupName", groupAppInsert.getGroupName());
		template.opsForHash().put(queryDetailGroup, "groupIcon", groupAppInsert.getGroupIcon());
		template.opsForHash().put(queryDetailGroup, "groupTitle", groupAppInsert.getGroupTitle());
		template.opsForHash().put(queryDetailGroup, "groupDescription", groupAppInsert.getGroupDescription());
		template.opsForHash().put(queryDetailGroup, "imgBanner", groupAppInsert.getImgBanner());
		template.opsForHash().put(queryDetailGroup, "imgVertical", groupAppInsert.getImgVertical());
		template.opsForHash().put(queryDetailGroup, "imgHorizontal", groupAppInsert.getImgHorizontal());
		List<OSConfigBean> listOsConfig = groupAppInsert.getListOsConfig();
		ObjectMapper jackson = new ObjectMapper();
		for (OSConfigBean osConfig : listOsConfig) {
			if (!APP01Action.USER_DEFAULT_ID.equals(osConfig.getUid())) {
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
				template.opsForSet().add(queryDev, groupAppInsert.getGroupId());
			}
		}
		
		//Save to Set groupID
		String queryOfGroups = "db:ads:groups";
		template.opsForSet().add(queryOfGroups, groupAppInsert.getGroupId());
		
		//Create 2 Banner Sample for group (name : 'banner_group_'XXX & 'popup_group_'XXX)
		BannerBean banner = new BannerBean();
		banner.setBannerName("banner_group_"+groupAppInsert.getGroupId());
		banner.setBannerDescription(groupAppInsert.getGroupDescription());
		banner.setBannerType(ConstantBean.BANNER_TYPE_BANNER);
		banner.setBannerTypeName(ConstantBean.BANNER_TYPE_NAME_BANNER);
		banner.setImage1(groupAppInsert.getImgBanner());
		banner.setUserId(userId);
		
		BannerBean popup = new BannerBean();
		popup.setBannerName("popup_group_"+groupAppInsert.getGroupId());
		popup.setBannerDescription(groupAppInsert.getGroupDescription());
		popup.setBannerType(ConstantBean.BANNER_TYPE_POPUP);
		popup.setBannerTypeName(ConstantBean.BANNER_TYPE_NAME_POPUP);
		popup.setImage1(groupAppInsert.getImgHorizontal());
		popup.setImage2(groupAppInsert.getImgVertical());
		popup.setUserId(userId);
		
		adv03Logic.insertSampleBanner(banner);
		adv03Logic.insertSampleBanner(popup);
		
		template.opsForHash().put(queryDetailGroup, "banner_sample", String.valueOf(banner.getBannerId()));
		template.opsForHash().put(queryDetailGroup, "popup_sample", String.valueOf(popup.getBannerId()));
	}
	
	@Override
	public boolean existGroupId(String groupId) {
		String key = "db:ads:groups";
		if (template.opsForSet().isMember(key, groupId)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Set<String> getSetGroupId() {
		return template.opsForSet().members("db:ads:groups");
	}
}
