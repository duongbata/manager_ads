package app.dao;

import manager.ADV03.bean.BannerBean;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class APP05Dao {
	private final static String SELECT_BANNER_SAMPLE_BY_ID = "selectBannerSampleById";
	
	private final static String UPDATE_BANNER_SAMPLE = "updateBannerSample";
	
	private final static String DELETE_BANNER_BY_ID = "deleteBannerById";
	
	private String namespace;
	
	@Autowired
	private SqlSessionTemplate managerAppsSqlSession;
	
	public APP05Dao() {
		setNamespace("app.dao.APP05Dao");
	}
	
	public BannerBean selectBannerSampleById(String bannerId) {
		String query = namespace + "." + SELECT_BANNER_SAMPLE_BY_ID;
		return managerAppsSqlSession.selectOne(query, bannerId);
	}
	
	public void updateBannerSample(BannerBean bannerSample){
		String query = namespace +"." + UPDATE_BANNER_SAMPLE;
		managerAppsSqlSession.update(query, bannerSample);
	}
	
	public void deleteBannerById(String bannerId) {
		String query = namespace + "." + DELETE_BANNER_BY_ID;
		managerAppsSqlSession.delete(query, bannerId);
	}
	
	private void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
