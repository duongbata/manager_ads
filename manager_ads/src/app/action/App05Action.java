package app.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.App05DataTrans;
import app.bean.AppBean;
import app.bean.AppConfigBean;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.logic.App02LogicIF;
import app.logic.App05LogicIF;
import manager.common.bean.InfoValue;
import manager.common.bean.UserBean;

@Controller
public class App05Action {
	private InfoValue info;
	
	private GroupAppBean groupAppEdit;
	
	private AppBean appUpdate;
	
	private AppConfigBean appConfig;
	
	@Autowired
	private App05LogicIF app05Logic;
	
	@Autowired
	private App02LogicIF app02Logic;
	
	@Action(value="/APP05_init"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String init() {
		App05DataTrans dataTrans = new App05DataTrans();
		dataTrans.setListGroupApp(app05Logic.listAllGroup());
		info.setDataTrans(dataTrans);
		
		return "success";
	}
	
	@Action(value="/APP05_initEditGroupApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_2",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String initEditGroupApp() {
		String groupId = ServletActionContext.getRequest().getParameter("groupId");
		groupAppEdit = app05Logic.getGroupAppById(groupId);
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		
		Map<String, String> mapOs = new HashMap<String, String>();
		mapOs.put(APP01Action.OS_IOS_ID, "Ios");
		mapOs.put(APP01Action.OS_ANDROID_ID, "Android");
		mapOs.put(APP01Action.OS_WINDOWS_ID, "Windows");
		mapOs.put("-1", "");
		dataTrans.setMapOs(mapOs);
		
		List<UserBean> listAllDev = new ArrayList<UserBean>();
		listAllDev.addAll(app05Logic.listAllDev());
		dataTrans.setListDev(listAllDev);
		
		return "success";
	}
	
	@Action(value="/APP05_editGroupApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_2",type="tiles")
				, @Result(name="failure",location="APP05_2", type="tiles")
			})
	public String editGroupApp(){
		//Validate
		String message = app05Logic.validateGroupAppEdit(groupAppEdit);
		if (message != null) {
			App05DataTrans app05DataTrans = (App05DataTrans) info.getDataTrans();
			app05DataTrans.setMessage(message);
			return "failure";
		}
		if (groupAppEdit.getListOsConfig() == null) {
			groupAppEdit.setListOsConfig(new ArrayList<OSConfigBean>());
		}
		//update
		try {
			app05Logic.updateGroupApp(groupAppEdit, String.valueOf(info.getUser().getId()));
		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return "success";
	}
	
	@Action(value="/APP05_initDetailApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_3",type="tiles")
				, @Result(name="failure",location="APP05_2", type="tiles")
			})
	public String initDetailApp() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String uid = request.getParameter("uid");
		String appId = request.getParameter("appId");
		if (uid == null || appId == null) {
			return "failure";
		}
		appUpdate = app05Logic.getAppBeanByUserAndId(appId, uid);
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		dataTrans.setAppDetail(appUpdate);
		
		Map<String, String> mapOs = new HashMap<String, String>();
		mapOs.put(APP01Action.OS_IOS_ID, "Ios");
		mapOs.put(APP01Action.OS_ANDROID_ID, "Android");
		mapOs.put(APP01Action.OS_WINDOWS_ID, "Windows");
		mapOs.put("-1", "");
		dataTrans.setMapOs(mapOs);
		
		List<UserBean> listAllDev = new ArrayList<UserBean>();
		listAllDev.addAll(app05Logic.listAllDev());
		dataTrans.setListDev(listAllDev);
		
		return "success";
	}
	
	@Action(value="/APP05_getConfigBean"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",type="json",params={"root","appConfig"})
			})
	public String getConfigBean() {
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		AppBean detailApp = dataTrans.getAppDetail();
		String config = detailApp.getConfig();
		
		appConfig = app02Logic.createAppConfigFromStr(config);
		appConfig.setNameConfig("Config");
		/*ObjectMapper jackson = new ObjectMapper();
		try {
			appConfig = jackson.readValue(config, AppConfigBean.class);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		dataTrans.setMessage(null);
		return "success";
	}
	
	@Action(value="/APP05_updateApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_3",type="tiles")
				, @Result(name="failure",location="APP05_3", type="tiles")
			})
	public String updateApp() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String dataConfig = request.getParameter("dataConfig");
		
		AppConfigBean appConfig = new ObjectMapper().readValue(dataConfig, AppConfigBean.class);
		String config = app02Logic.createJSONObjectFromAppConfig(appConfig).toString();
		
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		AppBean appDetail = dataTrans.getAppDetail();
		String devIdOld = appDetail.getUid();
		appUpdate.setConfig(config);
		boolean isUpdated = app05Logic.updateApp(appUpdate, devIdOld);
		if (isUpdated) {
			dataTrans.setMessage("Success");
			dataTrans.setAppDetail(appUpdate);
			return "success";
		} else {
			dataTrans.setMessage("Failure");
			return "failure";
		}
	}
	
	@Action(value="/APP05_deleteAppByAppId"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",type="json",params={"root","Delete"})
			})
	public String deleteAppByAppId() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String appId = request.getParameter("appId");
		try {
			app05Logic.deleteAppById(appId);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return "success";
	}
	
	@Action(value="/APP05_deleteGroupByGroupId"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",type="json",params={"root","Delete"})
			})
	public String deleteGroupByGroupId() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupId = request.getParameter("groupId");
		try {
			app05Logic.deleteAppByGroupId(groupId,String.valueOf(info.getUser().getId()));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}

	public GroupAppBean getGroupAppEdit() {
		return groupAppEdit;
	}

	public void setGroupAppEdit(GroupAppBean groupAppEdit) {
		this.groupAppEdit = groupAppEdit;
	}

	public AppBean getAppUpdate() {
		return appUpdate;
	}

	public void setAppUpdate(AppBean appUpdate) {
		this.appUpdate = appUpdate;
	}

	public AppConfigBean getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfigBean appConfig) {
		this.appConfig = appConfig;
	}
}
