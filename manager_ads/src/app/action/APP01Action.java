package app.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import manager.common.bean.InfoValue;
import manager.common.bean.UserBean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.App01DataTrans;
import app.bean.App05DataTrans;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.logic.App01LogicIF;
import app.logic.App05LogicIF;

@Controller
public class APP01Action{
	public static final String USER_DEFAULT_ID = "-1";
	
	public static final String OS_IOS_ID = "1"; 
			
	public static final String OS_ANDROID_ID = "2";
	
	public static final String OS_WINDOWS_ID = "3";
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 2796501846906521989L;

	private InfoValue info = new InfoValue();
	
	private GroupAppBean groupAppInsert;
	
	@Autowired
	private App05LogicIF app05Logic;
	
	private String groupId;
	
//	private List<UserBean> listDev;
	
	@Autowired
	private App01LogicIF app01Logic;
	
	@Action(value="/APP01_init"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP01",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String init() {
		/*info.setMessage(null);*/
		groupAppInsert = new GroupAppBean();
		List<OSConfigBean> listOsConfig = new ArrayList<OSConfigBean>();
		OSConfigBean iosConfig = new OSConfigBean(null, null, null, OS_IOS_ID);
		listOsConfig.add(iosConfig);
		OSConfigBean androidConfig = new OSConfigBean(null, null, null, OS_ANDROID_ID);
		listOsConfig.add(androidConfig);
		OSConfigBean windowsConfig = new OSConfigBean(null, null, null, OS_WINDOWS_ID);
		listOsConfig.add(windowsConfig);
		groupAppInsert.setListOsConfig(listOsConfig);
		App01DataTrans dataTrans = null;
		try {
			dataTrans = (App01DataTrans) info.getDataTrans();
		} catch (ClassCastException ex) {
			dataTrans = null;
		}
		
		if (dataTrans == null) {
			dataTrans = new App01DataTrans();
		} else {
			return "success";
		}
		
		List<UserBean> listDev = new ArrayList<UserBean>();
		UserBean userDefault = new UserBean();
		userDefault.setId(USER_DEFAULT_ID);
		userDefault.setName("");
		listDev.add(userDefault);
		listDev.addAll(app01Logic.getAllDev());
		dataTrans.setListDev(listDev);
		
		dataTrans.setSetGroupId(app01Logic.getSetGroupId());
		
		info.setDataTrans(dataTrans);
		return "success";
	}
	
	@Action(value="/APP01_insertGroup"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP01_init",type="redirectAction")
				, @Result(name="failure",location="APP01", type="tiles")
			})
	public String insertGroup() throws Throwable{
		GroupAppBean groupApp = groupAppInsert;
		groupApp.setGroupId(groupApp.getGroupId().trim());
		App01DataTrans dataTrans = (App01DataTrans) info.getDataTrans();
		boolean existGroup = app01Logic.existGroupId(groupApp.getGroupId());
		if (existGroup) {
			dataTrans.setMessage("Group " + groupApp.getGroupId() + " đã tồn tại.");
			dataTrans.setExistGroup(existGroup);
			return "failure";
		}
		app01Logic.insertGroupApp(groupApp,info.getUser().getId());
		dataTrans.setExistGroup(false);
		dataTrans.setMessage("Success");
		info.setDataTrans(dataTrans);
		return "success";
	}
	
	@Action(value="/APP01_redirectToEditGroup"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_initEditGroupApp",type="redirectAction",params={"groupId","${groupId}"})
			})
	public String redirectToEditGroup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		App05DataTrans dataTrans = new App05DataTrans();
		dataTrans.setListGroupApp(app05Logic.listAllGroup());
		info.setDataTrans(dataTrans);
		groupId = request.getParameter("groupId");
		return "success";
	}
	
	@Action(value="/APP01_getAllGroupId"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",type="json",params={"root","info.dataTrans.setGroupId"})
			})
	public String getAllGroupId() {
		App01DataTrans dataTrans = (App01DataTrans) info.getDataTrans();
		dataTrans.setSetGroupId(app01Logic.getSetGroupId());
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}

	public GroupAppBean getGroupAppInsert() {
		return groupAppInsert;
	}

	public void setGroupAppInsert(GroupAppBean groupAppInsert) {
		this.groupAppInsert = groupAppInsert;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

//	public List<UserBean> getListDev() {
//		return listDev;
//	}
//
//	public void setListDev(List<UserBean> listDev) {
//		this.listDev = listDev;
//	}
}
