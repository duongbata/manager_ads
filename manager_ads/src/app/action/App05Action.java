package app.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.App05DataTrans;
import app.bean.GroupAppBean;
import app.logic.App05LogicIF;
import manager.common.bean.InfoValue;

@Controller
public class App05Action {
	private InfoValue info;
	
	@Autowired
	private App05LogicIF app05Logic;
	
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
		GroupAppBean groupApp = app05Logic.getGroupAppById(groupId);
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		dataTrans.setGroupAppEdit(groupApp);
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}
}
