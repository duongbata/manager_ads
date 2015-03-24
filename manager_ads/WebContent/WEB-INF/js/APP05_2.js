$(document).ready(function(){
	//List Dev
	var tr = $('#tblDev tbody').children();
	$.each(tr,function(key,value){
		var devId = $(value).find('.devId').val();
		var devName = $(value).find('.devName').val();
		var dev = new Object();
		dev.id = devId;
		dev.name = devName;
		listDev.push(dev);
	});
	
	//List Os
	var tr = $('#tblOs tbody').children();
	$.each(tr,function(key,value){
		var osId = $(value).find('.osId').val();
		var osName = $(value).find('.osName').val();
		var os = new Object();
		os.id = osId;
		os.name = osName;
		listOs.push(os);
	});
	
	$('#addOsConfig').click(function(){
		addOsConfig();
	});
});
//List Os
var listOs = [];
//List Dev
var listDev = [];

$(document).on('click','.btnDelOsConfig',function(){
	var isDeleted = confirm('Xóa Config ?');
	if (!isDeleted) {
		return;
	}
	var num = $('#tblOsConfig >tbody >tr').length;
	
	var btnName = $(this).prop('name');
	var idx = parseInt(btnName.split('_')[1]);
	if (idx == (num-1)) {
		$(this).closest('tr').remove();
		return;
	} else {
		//Update name
		for (var i = (idx+1);i<num;i++) {
			var nextRow = $('#config_'+i);
			var selOsId = nextRow.find('.selOsId');
			selOsId.prop('name','groupAppEdit.listOsConfig['+(i-1)+'].osId');
			
			var txtIBanner = nextRow.find('.idBanner');
			txtIBanner.prop('name','groupAppEdit.listOsConfig['+(i-1)+'].admodIdBanner');
			
			var txtIPopup = nextRow.find('.idPopup');
			txtIPopup.prop('name','groupAppEdit.listOsConfig['+(i-1)+'].admodIdPopup');
			
			var selDev = nextRow.find('.uid');
			selDev.prop('name','groupAppEdit.listOsConfig['+(i-1)+'].uid');
			
			var btnDel = nextRow.find('.btnDelOsConfig');
			btnDel.prop('name','delConfig_'+(i-1));
			
			nextRow.prop('id','config_'+(i-1));
		}
		$(this).closest('tr').remove();
	}
});

function addOsConfig() {
	var num = $('#tblOsConfig >tbody >tr').length;
	var tr = $('<tr id="config_'+num+'"></tr>');
	
	//Os Name
	var tdNameOs = $('<td class="info" style="vertical-align: middle;width: 200px;"></td>');
	
	var osSelect = $('<select name = "groupAppEdit.listOsConfig['+num+'].osId" class="selOsId" style="width:100px; margin : 5px"></select>');
	
	$.each(listOs,function(key,value) {
		var osOption;
		if (value.id == -1) {
			osOption = $('<option value="' + value.id +'" selected = "selected">'+value.name+'</option>');
		} else {
			osOption = $('<option value="' + value.id +'">'+value.name+'</option>');
		}
		osSelect.append(osOption);
	});
	tdNameOs.append(osSelect);
	
	var btnDel = $('<button type="button" class="btn btn-default btn-xs btn-danger btnDelOsConfig" name="delConfig_'+num+'"><span class="glyphicon glyphicon-remove"></span></button>');
	tdNameOs.append(btnDel);
	
	tr.append(tdNameOs);
	
	//Os Config
	var tdConfig = $('<td></td>');
	var tblConfig = $('<table class="table-hover"></table>');
	
	var trBanner= $('<tr></tr>');
	var tdBannerName = $('<td width="150px;">AdmodIDBanner</td>');
	trBanner.append(tdBannerName);
	var tdBannerValue = $('<td></td>');
	var txtBannerValue = $('<input type="text" name="groupAppEdit.listOsConfig['+ num +'].admodIdBanner" class="form-control idBanner" style="width:200px;">');
	tdBannerValue.append(txtBannerValue);
	trBanner.append(tdBannerValue);
	tblConfig.append(trBanner);
	
	var trPopup = $('<tr></tr>');
	var tdPopupName = $('<td width="150px;">AdmodIDPopup</td>');
	trPopup.append(tdPopupName);
	var tdPopupValue = $('<td></td>');
	var txtPopupValue = $('<input type="text" name="groupAppEdit.listOsConfig['+ num +'].admodIdPopup" class="form-control idPopup" style="width:200px;">');
	tdPopupValue.append(txtPopupValue);
	trPopup.append(tdPopupValue);
	tblConfig.append(trPopup);
	
	var trUser = $('<tr></tr>');
	var tdUserName = $('<td>User</td>');
	trUser.append(tdUserName);
	var tdUserSelect = $('<td></td>');
	var devSelect = $('<select name="groupAppEdit.listOsConfig['+num+'].uid" class="form-control uid" style="width:200px;"></select>');
	var devOptionDef = $('<option value="-1"></option>');
	devSelect.append(devOptionDef);
	$.each(listDev,function(key,value){
		var devOption = $('<option value="' + value.id +'">'+value.name+'</option>');
		devSelect.append(devOption);
	});
	
	tdUserSelect.append(devSelect);
	trUser.append(tdUserSelect);
	tblConfig.append(trUser);
	
	tdConfig.append(tblConfig);
	tr.append(tdConfig);
	$('#tblOsConfig').append(tr);
}