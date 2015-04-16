package manager.ADV03.logic.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import manager.ADV03.bean.BannerBean;
import manager.ADV03.bean.CampaignBean;
import manager.ADV03.dao.ADV03Dao;
import manager.ADV03.logic.ADV03LogicIF;
import manager.common.bean.RedisConstant;

@Service
public class ADV03LogicImpl implements ADV03LogicIF{
	private static final String STATUS_WAITING_NAME = "waiting";
	
	private static final int STATUS_WAITING_ID = 4;

	@Autowired
	private ADV03Dao adv03Dao;
	
	@Autowired
	private StringRedisTemplate template;
	
	@Override
	public List<BannerBean> getListBanner(CampaignBean campaign) {
		List<BannerBean> listBanner = adv03Dao.getListBanner(campaign);
		/*for (BannerBean bannerBean : listBanner) {
			if (bannerBean.getOsTarget() != null && !"".equals(bannerBean.getOsTarget())) {
				setAllOsOfBanner(bannerBean);
			}
		}*/
		return listBanner;
	}
	
	//Convert from osTarget to allOs
	/*private void setAllOsOfBanner(BannerBean bannerBean) {
		String[] arrOs = bannerBean.getOsTarget().split(",");
		Map<String, Boolean> allOs = new HashMap<String, Boolean>();
		for (String os : arrOs) {
			if ("1".equals(os)) {
				allOs.put(ANDROID, true);
			} else if ("2".equals(os)) {
				allOs.put(IOS, true);
			} else if ("3".equals(os)) {
				allOs.put(WINPHONE, true);
			}
		}
		bannerBean.setAllOs(allOs);
	}*/
	
	@Override
	public BannerBean getBannerById(int bannerId) {
		BannerBean bannerBean = adv03Dao.getBannerById(bannerId);
		if (bannerBean == null) {
			return bannerBean;
		}
		/*Map<String, Boolean> allOs = new HashMap<String, Boolean>();
		if (bannerBean.getOsTarget() != null && !"".equals(bannerBean.getOsTarget())) {
			String[] arrOs = bannerBean.getOsTarget().split(",");
			for (String os : arrOs) {
				if ("1".equals(os)) {
					allOs.put(ANDROID, true);
				} else if ("2".equals(os)) {
					allOs.put(IOS, true);
				} else if ("3".equals(os)) {
					allOs.put(WINPHONE, true);
				}
			}
		}
		if(!allOs.keySet().contains(ANDROID)) {
			allOs.put(ANDROID, false);
		}
		if(!allOs.keySet().contains(IOS)) {
			allOs.put(IOS, false);
		}
		if(!allOs.keySet().contains(WINPHONE)) {
			allOs.put(WINPHONE, false);
		}
		bannerBean.setAllOs(allOs);*/
		return bannerBean;
	}
	
	@Override
	public Map<Integer, String> getAllCampaign() {
		return adv03Dao.getAllCampaign();
	}
	
	@Override
	public Map<Integer, String> getAllBannerStatus() {
		return adv03Dao.getAllBannerStatus();
	}
	
	@Override
	public Map<Integer, String> getAllBannerType() {
		return adv03Dao.getAllBannerType();
	}
	
	@Override
	public void editBanner(BannerBean bannerEdit) {
		/*Map<String, Boolean> allOs = bannerEdit.getAllOs();
		if (allOs != null) {
			String osTarget = "";
			int size = allOs.keySet().size();
			int i = 0;
			for (String key : allOs.keySet()) {
				if (ANDROID.equals(key)) {
					osTarget += "1";
				} else if (IOS.equals(key)) {
					osTarget += "2";
				} else if (WINPHONE.equals(key)) {
					osTarget += "3";
				}
				if (i < (size -1)) {
					osTarget += ",";
				}
				i++;
			} 
			bannerEdit.setOsTarget(osTarget);
		}*/
		bannerEdit.setBannerTypeName(adv03Dao.getBannerTypeNameById(bannerEdit.getBannerType()));
		bannerEdit.setStatusName(adv03Dao.getStatusNameById(bannerEdit.getStatusId()));
		adv03Dao.editBanner(bannerEdit);
		
		//To redis
		String query = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(bannerEdit.getUserId())
													 + ":campaign:" + String.valueOf(bannerEdit.getCampaignId())
													 +":banner:" + String.valueOf(bannerEdit.getBannerId());
		boolean isExists = template.hasKey(query);
		if (isExists) {
			template.opsForHash().put(query, "bannerName", bannerEdit.getBannerName());
			template.opsForHash().put(query, "startTime", String.valueOf(bannerEdit.getStartTime().getTime()));
			template.opsForHash().put(query, "stopTime", String.valueOf(bannerEdit.getStopTime().getTime()));
			template.opsForHash().put(query, "maxClick", bannerEdit.getMaxClick());
			template.opsForHash().put(query, "maxView", bannerEdit.getMaxView());
			template.opsForHash().put(query, "bannerType", String.valueOf(bannerEdit.getBannerType()));
			template.opsForHash().put(query, "description", bannerEdit.getBannerDescription());
//			template.opsForHash().put(query, "bannerTypeName", bannerEdit.getBannerTypeName());
			template.opsForHash().put(query, "image1", bannerEdit.getImage1());
			template.opsForHash().put(query, "image2", bannerEdit.getImage2());
			template.opsForHash().put(query, "androidUrl", bannerEdit.getAndroidUrl());
			template.opsForHash().put(query, "iosUrl", bannerEdit.getIosUrl());
			template.opsForHash().put(query, "windowsUrl", bannerEdit.getWindowsUrl());
			template.opsForHash().put(query, "maxViewPerDay", bannerEdit.getMaxViewPerDay());
			template.opsForHash().put(query, "maxClickPerDay", bannerEdit.getMaxClickPerDay());
			template.opsForHash().put(query, "statusId", String.valueOf(bannerEdit.getStatusId()));
			template.opsForHash().put(query, "statusName", bannerEdit.getStatusName());
		}
	}
	
	@Override
	public void insertSampleBanner(BannerBean bannerInsert) {
		/*Map<String,Boolean> allOs = bannerInsert.getAllOs();
		if (allOs != null) {
			String osTarget = "";
			int size = allOs.keySet().size();
			int i = 0;
			for (String key : allOs.keySet()) {
				if (ANDROID.equals(key)) {
					osTarget += "1";
				} else if (IOS.equals(key)) {
					osTarget += "2";
				} else if(WINPHONE.equals(key)) {
					osTarget += "3";
				}
				if (i < (size - 1)) {
					osTarget += ",";
				}
				i ++;
			}
			bannerInsert.setOsTarget(osTarget);
		}*/
		int maxId = adv03Dao.getMaxIdOfBanner()+1;
		bannerInsert.setBannerId(maxId);
		bannerInsert.setStartTime(new Date());
		bannerInsert.setStopTime(new Date());
		adv03Dao.insertSampleBanner(bannerInsert);
		//To redis
		String query = RedisConstant.DB_ADS_CUSTOMER + ":uid:" + bannerInsert.getUserId() + ":sample:banners" ;
		template.opsForSet().add(query, String.valueOf(maxId));
		
		bannerInsert.setCampaignId(-1);
		String queryDetailBannerSample = RedisConstant.DB_ADS_BANNER_SAMPLE + ":" + bannerInsert.getBannerId();
		template.opsForHash().put(queryDetailBannerSample, "bannerId", String.valueOf(bannerInsert.getBannerId()));
		template.opsForHash().put(queryDetailBannerSample, "bannerName", bannerInsert.getBannerName());
		template.opsForHash().put(queryDetailBannerSample, "campaignId", String.valueOf(bannerInsert.getCampaignId()));
		template.opsForHash().put(queryDetailBannerSample, "description", bannerInsert.getBannerDescription() == null ? "" : bannerInsert.getBannerDescription());
		template.opsForHash().put(queryDetailBannerSample, "bannerType", String.valueOf(bannerInsert.getBannerType()));
//		template.opsForHash().put(queryDetailBannerSample, "bannerTypeName", bannerInsert.getBannerTypeName());
		template.opsForHash().put(queryDetailBannerSample, "startTime", String.valueOf(bannerInsert.getStartTime().getTime()));
		template.opsForHash().put(queryDetailBannerSample, "stopTime", String.valueOf(bannerInsert.getStopTime().getTime()));
		template.opsForHash().put(queryDetailBannerSample, "image1", bannerInsert.getImage1() == null ? "" : bannerInsert.getImage1());
		template.opsForHash().put(queryDetailBannerSample, "image2", bannerInsert.getImage2() == null ? "" : bannerInsert.getImage1());
		template.opsForHash().put(queryDetailBannerSample, "androidUrl", bannerInsert.getAndroidUrl() == null ? "" : bannerInsert.getAndroidUrl());
		template.opsForHash().put(queryDetailBannerSample, "iosUrl", bannerInsert.getIosUrl() == null ? "" : bannerInsert.getIosUrl());
		template.opsForHash().put(queryDetailBannerSample, "windowsUrl", bannerInsert.getWindowsUrl() == null ? "" : bannerInsert.getWindowsUrl());
	}
	
	@Override
	public void deleteBanner(BannerBean banner) {
		adv03Dao.deleteBanner(banner.getBannerId());
		//To redis
		//1.Delete in set
		String query = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(banner.getUserId())
				 + ":campaign:" + String.valueOf(banner.getCampaignId())
				 +":banners";
		boolean isExistKey = template.hasKey(query);
		if(isExistKey) {
			String bannerId = String.valueOf(banner.getBannerId());
			boolean hasMember = template.opsForSet().isMember(query, bannerId);
			if(hasMember) {
				template.opsForSet().remove(query, String.valueOf(String.valueOf(banner.getBannerId())));
			}
		}
		//2.Delete information in hash
		String query2 = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(banner.getUserId())
													  + ":campaign:" + String.valueOf(banner.getCampaignId())
													  + ":banner:" + String.valueOf(banner.getBannerId());
		boolean isExistKey2 = template.hasKey(query2);
		if(isExistKey2) {
			template.delete(query2);
		}
	}
	
	@Override
	public List<CampaignBean> listCampaignOfUser(int userId) {
		return adv03Dao.listCampaignOfUser(userId);
	}
	
	@Override
	public List<BannerBean> listSampleBannerOfUser(BannerBean banner) {
		return adv03Dao.listSampleBannerOfUser(banner);
	}
	
	@Override
	public void insertBannerToCampaign(List<BannerBean> listBanner,
			int campaignId, int userId) {
		String bannerInCampaign = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(userId)+":campaign:"+String.valueOf(campaignId) + ":banner:";
		String sBanner = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(userId)+":campaign:"+String.valueOf(campaignId) + ":banners";
		for (BannerBean bannerBean : listBanner) {
			
			bannerBean.setUserId(userId);
			BannerBean bannerInsert = adv03Dao.getSampleBannerById(bannerBean);
			int id = adv03Dao.getMaxIdOfBanner() + 1;
			bannerInsert.setBannerId(id);
			bannerInsert.setCampaignId(campaignId);
			bannerInsert.setStatusId(STATUS_WAITING_ID);
			bannerInsert.setStatusName(STATUS_WAITING_NAME);
			adv03Dao.insertBannerToCampaign(bannerInsert);
			//To redis
			template.opsForSet().add(sBanner, String.valueOf(id));
			String query = bannerInCampaign + String.valueOf(id);
			template.opsForHash().put(query, "bannerId", String.valueOf(id));
			template.opsForHash().put(query, "bannerName", bannerInsert.getBannerName() == null ? "" : bannerInsert.getBannerName());
			template.opsForHash().put(query, "description", bannerInsert.getBannerDescription() == null ? "" : bannerInsert.getBannerDescription());
			template.opsForHash().put(query, "bannerType", String.valueOf(bannerInsert.getBannerType()));
//			template.opsForHash().put(query, "bannerTypeName", bannerInsert.getBannerTypeName() == null ? "" : bannerInsert.getBannerTypeName());
			template.opsForHash().put(query, "image1", bannerInsert.getImage1() == null ? "" : bannerInsert.getImage1());
			template.opsForHash().put(query, "image2", bannerInsert.getImage2() == null ? "" : bannerInsert.getImage2());
			template.opsForHash().put(query, "androidUrl", bannerInsert.getAndroidUrl() == null ? "" : bannerInsert.getAndroidUrl());
			template.opsForHash().put(query, "iosUrl", bannerInsert.getIosUrl() == null ? "" : bannerInsert.getIosUrl());
			template.opsForHash().put(query, "windowsUrl", bannerInsert.getWindowsUrl() == null ? "" : bannerInsert.getWindowsUrl());
			template.opsForHash().put(query, "startTime", String.valueOf(bannerInsert.getStartTime().getTime()));
			template.opsForHash().put(query, "stopTime", String.valueOf(bannerInsert.getStopTime().getTime()));
			template.opsForHash().put(query, "statusId", String.valueOf(bannerInsert.getStatusId()));
			template.opsForHash().put(query, "statusName", bannerInsert.getStatusName() == null ? "" : bannerInsert.getStatusName());
			//just redis
			template.opsForHash().put(query, "bannerSampleId", String.valueOf(bannerBean.getBannerId()));
		}
	}
	
	@Override
	public void updateBannerConfig(BannerBean banner) {
		adv03Dao.updateBannerBeanConfig(banner);
		//To redis
		String query = RedisConstant.DB_ADS_CUSTOMER + ":uid:"+String.valueOf(banner.getUserId())
													 + ":campaign:" + String.valueOf(banner.getCampaignId())
													 +":banner:" + String.valueOf(banner.getBannerId());
		boolean isExists = template.hasKey(query);
		if(isExists) {
			template.opsForHash().put(query, "maxClick", banner.getMaxClick());
			template.opsForHash().put(query, "maxView", banner.getMaxView());
			template.opsForHash().put(query, "startTime",String.valueOf( banner.getStartTime().getTime()));
			template.opsForHash().put(query, "stopTime",String.valueOf( banner.getStopTime().getTime()));
		}
	}
	
	@Override
	public void saveNewCampaign(List<CampaignBean> listNewCampaign, int userId) {
		String queryListCampaign = RedisConstant.DB_ADS_CUSTOMER + ":uid:" + String.valueOf(userId) + ":campaigns";
		
		for (CampaignBean campaignBean : listNewCampaign) {
			int maxId = adv03Dao.getMaxIdOfCampign() + 1;
			campaignBean.setCampaignId(maxId);
			campaignBean.setUserId(userId);
			adv03Dao.insertCampaign(campaignBean);
			//To redis
			template.opsForSet().add(queryListCampaign, String.valueOf(campaignBean.getCampaignId()));
			String queryDetailCampaign = RedisConstant.DB_ADS_CUSTOMER + ":uid:" + String.valueOf(userId) 
																	   + ":campaign:" + String.valueOf(campaignBean.getCampaignId());
			template.opsForHash().put(queryDetailCampaign, "campaignId", String.valueOf(campaignBean.getCampaignId()));
			template.opsForHash().put(queryDetailCampaign, "campaignName", campaignBean.getCampaignName());
			template.opsForHash().put(queryDetailCampaign, "startTime", String.valueOf(campaignBean.getStartTime().getTime()));
			template.opsForHash().put(queryDetailCampaign, "stopTime", String.valueOf(campaignBean.getStopTime().getTime()));
		}
	}
}
