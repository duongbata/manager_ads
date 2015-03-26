package app.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import manager.common.bean.InfoValue;
import manager.common.bean.UserBean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.AppBean;
import app.bean.AppConfigBean;
import app.bean.FieldBean;
import app.bean.PropertyAppBean;
import app.logic.App01LogicIF;
import app.logic.App02LogicIF;

@Controller
public class APP02Action {
	private InfoValue info;
	
	private AppBean appInsert;
	
	private Map<String, String> mapOS = new HashMap<String, String>();
	
	private Map<String, String> mapGroup = new HashMap<String,String>();
	
	private String message;
	
//	private List<AppConfigBean> listAppConfigBean;
	
	private AppConfigBean appConfig;
	
	private UserBean dev;
	
//	private List<UserBean> listDev;
	
	@Autowired
	private App02LogicIF app02Logic;
	
	@Autowired
	private App01LogicIF app01Logic;
	
	@Action(value="/APP02_init"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP02",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String init() {
		appInsert = new AppBean();
		/*listDev = new ArrayList<UserBean>();
		UserBean userDefault = new UserBean();
		userDefault.setId(APP01Action.USER_DEFAULT_ID);
		userDefault.setName("");
		listDev.add(userDefault);
		listDev.addAll(app01Logic.getAllDev());*/
		mapGroup = app02Logic.getMapAllGroup();
		mapGroup.put("-1", "");
		
		mapOS = new HashMap<String,String>();
		
		dev = new UserBean();
		
		//PropertyAppBean
		PropertyAppBean property = new PropertyAppBean();
		property.setPropertyName("name");
		property.setPropertyValue("value");
		List<PropertyAppBean> listProperty = new ArrayList<PropertyAppBean>();
		listProperty.add(property);
		appInsert.setListProperty(listProperty);
		
//		appInsert.setAppConfig(appConfig);
		return "success";
	}
	
	@Action(value="/APP02_insertApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP02_init",type="redirectAction",params={"message","${message}"})
				, @Result(name="failure",location="APP02", type="tiles")
			})
	public String insertApp() throws JsonParseException, JsonMappingException, IOException {
		String dataConfig = ServletActionContext.getRequest().getParameter("dataConfig");
		//To String
		AppConfigBean appConfig = new ObjectMapper().readValue(dataConfig, AppConfigBean.class);
		String config = app02Logic.createJSONObjectFromAppConfig(appConfig).toString();
		appInsert.setConfig(config);
		
		appInsert.setUid(String.valueOf(dev.getId()));
		try {
			app02Logic.insertAppBean(appInsert, appInsert.getUid(), String.valueOf(info.getUser().getId()));
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			setMessage("Failure");
			return "failure";
		}
		setMessage("Success");
		return "success";
	}
	
	@Action(value="/APP02_getListGroupByOS"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			,results={@Result(name="success",type="json")})
	public String getListGroupByOS() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String osId = request.getParameter("osId");
		String devId = request.getParameter("devId");
		mapGroup = app02Logic.getMapGroupByOs(osId, devId);
		
		return "success";
	}
	
	@Action(value="/APP02_getConfigBean"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			,results={@Result(name="success",type="json")})
	public String getConfigBean() {
		appConfig = new AppConfigBean();
		appConfig.setNameConfig("Config");
		
		//Object update
		AppConfigBean update = new AppConfigBean();
		update.setNameConfig("Update");
		List<FieldBean> listFieldOfUpdate = new ArrayList<FieldBean>();
		listFieldOfUpdate.add(new FieldBean("ForceUpdate", "1"));
		listFieldOfUpdate.add(new FieldBean("Message", "Cập nhật bản mới để có nhiều tính năng hấp dẫn"));
		update.setListFieldBean(listFieldOfUpdate);
		
		//Object fb
		AppConfigBean fb = new AppConfigBean();
		fb.setNameConfig("FB");
		List<FieldBean> listFieldOfFb = new ArrayList<FieldBean>();
		listFieldOfFb.add(new FieldBean("Page", "https://www.facebook.com/5play.mobi"));
		listFieldOfFb.add(new FieldBean("AppID", ""));
		fb.setListFieldBean(listFieldOfFb);
		
		//Object frequent_ads
		AppConfigBean frequentAds = new AppConfigBean();
		frequentAds.setNameConfig("FrequentAds");
		List<FieldBean> listFieldOfFrequent = new ArrayList<FieldBean>();
		listFieldOfFrequent.add(new FieldBean("Banner", "10"));
		listFieldOfFrequent.add(new FieldBean("Popup", "10"));
		listFieldOfFrequent.add(new FieldBean("AppOpen", "1"));
		listFieldOfFrequent.add(new FieldBean("AppExit", "1"));
		frequentAds.setListFieldBean(listFieldOfFrequent);
		
		List<AppConfigBean> listChildConfig = new ArrayList<AppConfigBean>();
		listChildConfig.add(update);
		listChildConfig.add(fb);
		listChildConfig.add(frequentAds);
		
		appConfig.setListConfigBean(listChildConfig);
		
		return "success";
	}
	
	@Action(value="/APP02_getAllOsOfGroup"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			,results={@Result(name="success",type="json")})
	public String getAllOsOfGroup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupId= request.getParameter("groupId");
		
		mapOS = app02Logic.getOsOfGroup(groupId);
		
		return "success";
	}
	
	@Action(value="/APP02_getDevByOsAndGroup"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			,results={@Result(name="success",type="json")})
	public String getDevByOsAndGroup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupId= request.getParameter("groupId");
		String osId = request.getParameter("osId");
		
		dev = app02Logic.getDevByOsAndGroup(groupId, osId);
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}

	public AppBean getAppInsert() {
		return appInsert;
	}

	public void setAppInsert(AppBean appInsert) {
		this.appInsert = appInsert;
	}

	public Map<String, String> getMapOS() {
		/*mapOS.put(APP01Action.OS_IOS_ID, "IOS");
		mapOS.put(APP01Action.OS_ANDROID_ID, "Android");
		mapOS.put(APP01Action.OS_WINDOWS_ID, "Windows");
		mapOS.put("-1", "");*/
		
		return mapOS;
	}

	public void setMapOS(Map<String, String> mapOS) {
		this.mapOS = mapOS;
	}
	
	public Map<String, String> getMapGroup() {
		/*mapGroup = app02Logic.getMapAllGroup();
		mapGroup.put("-1", "");*/
		return mapGroup;
	}
	
	public void setMapGroup(Map<String, String> mapGroup) {
		this.mapGroup = mapGroup;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AppConfigBean getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfigBean appConfig) {
		this.appConfig = appConfig;
	}

	public UserBean getDev() {
		return dev;
	}

	public void setDev(UserBean dev) {
		this.dev = dev;
	}

/*	public List<UserBean> getListDev() {
		return listDev;
	}

	public void setListDev(List<UserBean> listDev) {
		this.listDev = listDev;
	}*/

//	public List<AppConfigBean> getListAppConfigBean() {
//		return listAppConfigBean;
//	}
//
//	public void setListAppConfigBean(List<AppConfigBean> listAppConfigBean) {
//		this.listAppConfigBean = listAppConfigBean;
//	}
}
