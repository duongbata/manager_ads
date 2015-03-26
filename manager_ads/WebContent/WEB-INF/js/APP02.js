$(document).ready(function(){
	$('#groupId').change(function(){
		getAllOsOfGroup();
	});
	$('#osId').change(function() {
		getUserByOsAndGroup();
	});
	
	$('#addProperty').click(function(){
		addRowProperty();
	});
	loadConfigBean();
});

//Data load config of page
var objConfig = new Object();

$(document).on('click','.btnDelProp',function(){
	var numProp = $(this).closest('tr').prop('id');
	var rowId = parseInt(numProp.split('_')[0]);
	
	var numRow = $('#tblProperty tbody').children().length;
	if (rowId == (numRow - 1)) {
		$(this).closest('tr').remove();
		return;
	} else if (rowId < (numRow - 1)) {
		for (var i = (rowId+1); i <numRow; i++) {
			var nextPropId = i + '_prop';
			var nextRow = $('#'+nextPropId);
			
			var txtPropName = nextRow.find('.txtPropName');
			txtPropName.attr('name','appInsert.listProperty['+(i-1)+'].propertyName');
			
			var txtPropValue = nextRow.find('.txtPropValue');
			txtPropValue.attr('name','appInsert.listProperty['+(i-1)+'].propertyValue');
			
			nextRow.attr('id',(i-1)+'_prop');
		}
		$(this).closest('tr').remove();
	}
	
});

function addRowProperty(){
	var numRow = $('#tblProperty tbody').children().length;
	//Add row
	var row = $('<tr id="'+numRow+'_prop"></tr>');
	
	var cellName = $('<td width="200px"></td>');
	var txtName = $('<input>').attr('type','text')
							  .attr('name', 'appInsert.listProperty['+numRow+'].propertyName')
							  .attr('class','form-control txtPropName');
	cellName.append(txtName);
	row.append(cellName);
	
	var cellValue = $('<td></td>');
	var txtValue = $('<input>').attr('type','text')
	  						   .attr('name', 'appInsert.listProperty['+numRow+'].propertyValue')
	  						   .attr('class','form-control txtPropValue');
	cellValue.append(txtValue);
	row.append(cellValue);
	
	var cellBtnDel = $('<td style="vertical-align: middle;"></td>');
	var btnDel = $('<input type="button" class="btn btn-default btn-xs btn-primary btnDelProp" value = "Delete">');
	cellBtnDel.append(btnDel);
	row.append(cellBtnDel);
	
	$('#tblProperty').append(row);
}

function validateInsert() {
	/*var appId = $('#appId').val();
	if (appId == null || appId.trim() == '') {
		alert('Hãy nhập AppId');
		return false;
	}*/
	var devId = $('#devId').val();
	if(devId == null || devId == '') {
		alert('Hãy chọn dev');
		return false;
	}
	var osId = $('#osId').val();
	if (osId == null || osId == '') {
		alert('Hãy chọn OS');
		return false;
	}
	
	var groupId = $('#groupId').val();
	if (groupId == null || groupId == '') {
		alert('Hãy chọn Group');
		return false;
	}
	updateDataConfigBeforeSubmit(objConfig, objConfig.nameConfig);
	
	//Send json
	var dataConfig = $('<input>').attr('type','hidden').attr('name','dataConfig').val(JSON.stringify(objConfig));
	$('#formInsertApp').append(dataConfig);
	return true;
}

function updateDataConfigBeforeSubmit(data, strParent) {
	if (data.listFieldBean != null && data.listFieldBean != 'undefined' && data.listFieldBean.length > 0) {
		var tableField = $('.'+strParent + '_tblField');
		$.each(data.listFieldBean,function(key,value){
			var pName = tableField.find('.'+value.name+'_fieldName');
			var txtValue = pName.closest('tr').find('.'+strParent+'_fieldValue').val();
			value.value = txtValue;
		});
	}
	if (data.listConfigBean != null && data.listConfigBean != 'undefiend' && data.listConfigBean.length > 0) {
		$.each(data.listConfigBean,function(key,value){
			var newStrParent = strParent + '_' + value.nameConfig;
			updateDataConfigBeforeSubmit(value, newStrParent);
		});
	}
}

//Add Object
$(document).on('click','.btnAddObject',function(){
	var txtObjName = $(this).closest('table').find('.txtObjName').val();
	if (txtObjName == null || txtObjName.trim() == '') {
		alert ('Hãy nhập tên của Object');
		return;
	}
	if(/^[a-zA-Z0-9- ]*$/.test(txtObjName) == false) {
	    alert('Không được nhập kí tự đặc biệt vào Object Name');
	    return false;
	}
	var nameBtnObject = $(this).attr('name');
	//add to parentConfig
	var parentConfig = getAppConfigByNameOfButton(objConfig, nameBtnObject.substring(nameBtnObject.indexOf('_') + 1));
	
	var newObject = new Object();
	newObject.nameConfig = txtObjName;
	newObject.listFieldBean = null;
	newObject.listConfigBean = null;
	
	if (parentConfig.listConfigBean == null || parentConfig.listConfigBean == 'undefined') {
		parentConfig.listConfigBean = [];
	} else {
		var idx = -1;
		$.each(parentConfig.listConfigBean, function(key,value){
			if (txtObjName == value.nameConfig) {
				idx = key;
				return false;
			}
		});
		if (idx != -1) {
			alert('Object ' +txtObjName + ' đã xuất hiện trong '+ parentConfig.nameConfig);
			return false;
		}
	}
	parentConfig.listConfigBean.push(newObject);
	
	//Add Screen
	var cellConfigObject = $(this).closest('table').parent();
	var tableObject = $('<table class="table table-condensed table-hover table-bordered '+nameBtnObject.substring(0,nameBtnObject.lastIndexOf('_'))+'_tblObject" name="'
						+nameBtnObject.substring(0,nameBtnObject.lastIndexOf('_'))+'_tblObject" style="margin:0px;"></table>');
	
	var rowObject = $('<tr></tr>');
	var cellObjectName = $('<td class="info" width="100px" style="vertical-align: middle;"></td>');
	var pObjectName = $('<p class="objName">'+txtObjName+'</p>');
	cellObjectName.append(pObjectName);
	var btnDelObject = $('<input type="button" class="btn btn-default btn-xs btn-danger btnDelObject" name="'+nameBtnObject.substring(0,nameBtnObject.lastIndexOf('_'))+ '_' + txtObjName+'_btnDelObject" value="Delete Object"/>');
	cellObjectName.append(btnDelObject);
	rowObject.append(cellObjectName);
	
	var parentName = nameBtnObject.substring(0,nameBtnObject.lastIndexOf('_')) + '_' + txtObjName;
	var cellObject = $('<td style="padding:0px;"></td>');
	var tableBtn = $('<table class="table table-condensed table-hover table-bordered" style="margin:0px;"></table>');
	var rowBtnAddField = $('<tr></tr>');
	var cellBtnAddField = $('<td width="100px" style="vertical-align: middle;"><input type="button" name="'+parentName+'_btnField" class="btn btn-default btn-xs btn-success btnAddField" value="Add Field"></td>');
	rowBtnAddField.append(cellBtnAddField);
	var cellTxtAddField = $('<td><input type="text" class="form-control txtFieldName"/></td>');
	rowBtnAddField.append(cellTxtAddField);
	tableBtn.append(rowBtnAddField);
	
	var rowBtnAddObject = $('<tr></tr>');
	var cellBtnAddObject = $('<td width="100px" style="vertical-align: middle;"><input type="button" name= "'+parentName+'_btnObject" class="btn btn-default btn-xs btn-success btnAddObject" value="Add Object"></td>');
	rowBtnAddObject.append(cellBtnAddObject);
	var cellTxtAddObject = $('<td><input type="text" class="form-control txtObjName"/></td>');
	rowBtnAddObject.append(cellTxtAddObject);
	tableBtn.append(rowBtnAddObject);
	cellObject.append(tableBtn);
	rowObject.append(cellObject);
	tableObject.append(rowObject);
	
//	cellConfigObject.prepend(tableObject);
	tableObject.insertBefore($(this).closest('table'));
	//reset txtObjName
	$(this).closest('table').find('.txtObjName').val('');
});

//Delete Object
$(document).on('click','.btnDelObject',function(){
	var nameObject = $(this).closest('td').find('.objName').text();
	var isDelete = confirm('Xóa Object ' + nameObject + '?');
	if (!isDelete) {
		return false;
	}
	
	var nameBtnObject = $(this).attr('name');
	var strNameBtnObject = nameBtnObject.substring(nameBtnObject.indexOf('_') + 1);
	var delConfig = getAppConfigByNameOfButton(objConfig,strNameBtnObject);
	var parentOfDelConfig = getAppConfigByNameOfButton(objConfig, strNameBtnObject.substring(0,strNameBtnObject.lastIndexOf('_')));
	var idx = -1;
	$.each(parentOfDelConfig.listConfigBean,function(key,value){
		if (value.nameConfig == delConfig.nameConfig) {
			idx = key;
			return false;
		}
	});
	if (idx != -1) {
		var arrDel = parentOfDelConfig.listConfigBean.splice(idx,1);
		if (arrDel.length == 1) {
			var table = $(this).closest('table');
			table.remove();
		}
	}
});

// obj : AppConfig
function getAppConfigByName(obj, name){
	if (name == obj.nameConfig) {
		return obj;
	} else {
		var result = null;
		$.each(obj.listConfigBean,function(key,value){
			result = getAppConfigByName(value, name);
			if (result != null && result != 'undefined'){
				return false;
			}
		});
		return result;
	}
}

//Add Field
$(document).on('click','.btnAddField',function(){
	var txtFieldName = $(this).closest('table').find('.txtFieldName').val();
	if (txtFieldName == null || txtFieldName.trim() == '') {
		alert('Hãy nhập tên của Field');
		return;
	}
	var nameBtnField = $(this).attr('name');
	var parentConfig = getAppConfigByNameOfButton(objConfig, nameBtnField.substring(nameBtnField.indexOf('_') + 1));
	
	//Add to parentConfig
	var newField = new Object();
	newField.name = txtFieldName;
	newField.value = '';
	if (parentConfig.listFieldBean == null || parentConfig.listFieldBean == 'undefined') {
		parentConfig.listFieldBean = [];
	} else {
		//validate txtFieldName
		var idx = -1;
		$.each(parentConfig.listFieldBean, function(key, value) {
			if (txtFieldName == value.name) {
				idx = key;
				//break each
				return false;
			}
		});
		if (idx != -1) {
			alert('Field ' +txtFieldName + ' đã xuất hiện trong ' + parentConfig.nameConfig);
			return false;
		}
	}
	parentConfig.listFieldBean.push(newField);
	
	//Add Screen
	var cellConfigObject = $(this).closest('table').parent();
	var tableField = cellConfigObject.find('.'+nameBtnField.substring(0,nameBtnField.lastIndexOf('_'))+'_tblField').get(0);
	if (tableField == 'undefined' || tableField == null) {
		tableField = $('<table class="table table-condensed table-hover table-bordered '+nameBtnField.substring(0,nameBtnField.lastIndexOf('_'))+'_tblField" name="'
						+nameBtnField.substring(0,nameBtnField.lastIndexOf('_'))+'_tblField" style="margin:0px;"></table>');
//		cellConfigObject.append(tableField);
		tableField.insertBefore($(this).closest('table'));
	}
	var rowField = $('<tr></tr>');
	var cellFieldName = $('<td class="warning" width="100px" style="vertical-align: middle;"></td>');
	var pFieldName = $('<p class="fieldName '+txtFieldName+'_fieldName">'+txtFieldName+'</p>');
	cellFieldName.append(pFieldName);
	var btnDelField = $('<input type="button" class="btn btn-default btn-xs btn-danger btnDelField" name="'+nameBtnField.substring(0,nameBtnField.lastIndexOf('_'))+'_btnDelField" value="Delete Field"/>');
	cellFieldName.append(btnDelField);
	rowField.append(cellFieldName);
	
	var cellFieldValue = $('<td style="vertical-align: middle;"></td>');
	var txtFieldValue = $('<input type = "text" class="form-control '+nameBtnField.substring(0,nameBtnField.lastIndexOf('_'))+'_fieldValue"/>');
	cellFieldValue.append(txtFieldValue);
	rowField.append(cellFieldValue);
	$(tableField).append(rowField);
	
	// Empty txtFieldName
	$(this).closest('table').find('.txtFieldName').val('');
});

/*nameBtn =nameConfig1_nameConfig2_btnXXX';*/
function getAppConfigByNameOfButton(obj,nameBtn) {
	if (nameBtn.indexOf('_') != -1) {
		var childName = nameBtn.substring(0,nameBtn.indexOf('_'));
		var objFind;
		$.each(obj.listConfigBean, function(index, value) {
			if (childName == value.nameConfig) {
				objFind = getAppConfigByNameOfButton(value, nameBtn.substring(nameBtn.indexOf('_') + 1));
			}
		});
		return objFind;
	} else {
		return obj;
	}	
}

//Delete Field
$(document).on('click','.btnDelField',function(){
	var nameField = $(this).closest('td').find('.fieldName').text();
	var isDelete = confirm('Xóa Field ' + nameField + '?');
	if (!isDelete) {
		return false;
	}
	
	var nameBtn = $(this).attr('name');
	var parentConfig = getAppConfigByNameOfButton(objConfig, nameBtn.substring(nameBtn.indexOf('_') + 1));
	
	var idx = -1;
	$.each(parentConfig.listFieldBean,function(key,value){
		if (nameField == value.name) {
			idx = key;
			//break each
			return false;
		}
	});
	//Remove element
	if (idx != -1) {
		var arrDel = parentConfig.listFieldBean.splice(idx,1);
		if (arrDel.length == 1) {
			var table = $(this).closest('table');
			var numRow = table.children('tbody').children('tr').length;
			if (numRow == 1) {
				table.remove();
			} else {
				$(this).closest('tr').remove();
			}
		}
	}
	
});
function loadConfigBean(){
	var link = CONTEXT_PATH + '/APP02_getConfigBean';
	$.ajax({
		type : "POST",
		url : link,
		dataType : 'json',
		success : function(result) {
			objConfig = result.appConfig;
			var table = createTableByAppConfigBean(result.appConfig, '');
			$('#configTable').append(table);
		},
		error : function(e) {
			alert ('Lỗi');
		}
	});
}

function createTableByAppConfigBean(data,parentName) {
	var nameConfig = data.nameConfig;
	if (parentName != '') {
		parentName = parentName + '_' + nameConfig;
	} else {
		parentName = nameConfig;
	}
	
	var table = $('<table class="table table-condensed table-hover table-bordered '+parentName+'_tblObject" name="'+parentName+'_tblObject" style="margin:0px;"></table>');
	var row = $('<tr></tr>');
	
	var cellName = $('<td class="info" width="100px" style="vertical-align: middle;"></td>');
	var pObjectName = $('<p class="objName">'+nameConfig+'</p>');
	cellName.append(pObjectName);
	if (nameConfig != 'Config') {
		var btnDelObject = $('<input type="button" class="btn btn-default btn-xs btn-danger btnDelObject" name="'+parentName+'_btnDelObject" value="Delete Object"/>');
		cellName.append(btnDelObject);
	}
	
	row.append(cellName);
	
	var cellConfigObject = $('<td style="padding:0px;"></td>');
	
	var listConfigBean = data.listConfigBean;
	if (listConfigBean != null && listConfigBean.length != 0) {
		for (var i=0;i<listConfigBean.length;i++) {
			var dataChild = listConfigBean[i];
			var tableChild = createTableByAppConfigBean(dataChild, parentName);
			cellConfigObject.append(tableChild);
		}
	}
	
	var listFieldBean = data.listFieldBean;
	if (listFieldBean != null && listFieldBean.length != 0) {
		var tableField = $('<table class="table table-condensed table-hover table-bordered '+parentName+'_tblField" name="'+parentName+'_tblField" style="margin:0px;"></table>');
		for (var i =0;i<listFieldBean.length;i++) {
			var fieldChild = listFieldBean[i];
			var rowField = $('<tr></tr>');
			var cellFieldName = $('<td class="warning" width="100px" style="vertical-align: middle;"></td>');
			var pFieldName = $('<p class="fieldName '+fieldChild.name+'_fieldName">'+fieldChild.name+'</p>');
			cellFieldName.append(pFieldName);
			var btnDelField = $('<input type="button" class="btn btn-default btn-xs btn-danger btnDelField" name="'+parentName+'_btnDelField" value="Delete Field"/>');
			cellFieldName.append(btnDelField);
			rowField.append(cellFieldName);
			var cellFieldValue = $('<td style="vertical-align: middle;"></td>');
			var txtFieldValue = $('<input type = "text" value="'+fieldChild.value+'" class="form-control '+parentName+'_fieldValue"/>');
			cellFieldValue.append(txtFieldValue);
			rowField.append(cellFieldValue);
			tableField.append(rowField);
		}
		cellConfigObject.append(tableField);
	}
	
	var tableBtn = $('<table class="table table-condensed table-hover table-bordered" style="margin:0px;"></table>');
	var rowBtnAddField = $('<tr></tr>');
	var cellBtnAddField = $('<td width="100px" style="vertical-align: middle;"><input type="button" name="'+parentName+'_btnField" class="btn btn-default btn-xs btn-success btnAddField" value="Add Field"></td>');
	rowBtnAddField.append(cellBtnAddField);
	var cellTxtAddField = $('<td><input type="text" class="form-control txtFieldName"/></td>');
	rowBtnAddField.append(cellTxtAddField);
	tableBtn.append(rowBtnAddField);
	
	var rowBtnAddObject = $('<tr></tr>');
	var cellBtnAddObject = $('<td width="100px" style="vertical-align: middle;"><input type="button" name= "'+parentName+'_btnObject" class="btn btn-default btn-xs btn-success btnAddObject" value="Add Object"></td>');
	rowBtnAddObject.append(cellBtnAddObject);
	var cellTxtAddObject = $('<td><input type="text" class="form-control txtObjName"/></td>');
	rowBtnAddObject.append(cellTxtAddObject);
	tableBtn.append(rowBtnAddObject);
	cellConfigObject.append(tableBtn);
	
	row.append(cellConfigObject);
	table.append(row);
	return table;
}

/*function listGroupByOsAndDev() {
	$('#groupId').empty();
	var osId = $('#osId').val();
	if (osId == null || devId == -1) {
		return false;
	}
	var devId = $('#devId').val();
	if (devId == null || devId == -1) {
		return false;
	}
	var link = CONTEXT_PATH + '/APP02_getListGroupByOS';
	$.ajax({
		type : "POST",
		url : link,
		data : {"osId":osId, "devId" : devId},
		dataType : 'json',
		success : function(result) {
			var groups = result.mapGroup;
			for (var name in groups) {
				$('#groupId').append('<option value="'+name+'">'+groups[name]+'</option>');
			}
		},
		error : function(e) {
			alert ('Lỗi');
		}
	});
}*/

function getAllOsOfGroup() {
	$('#osId').empty();
	$('#devId').empty();
	var groupId = $('#groupId').val();
	if (groupId == -1 || groupId == null) {
		return;
	}
	var link = CONTEXT_PATH + '/APP02_getAllOsOfGroup';
	$('#osId').append('<option value="-1"></option>');
	$.ajax({
		type : "POST",
		url : link,
		data : {"groupId":groupId},
		dataType : 'json',
		success : function(result) {
			$.each(result.mapOS, function(key,value){
				$('#osId').append('<option value="'+key+'">'+value+'</option>');
			});
		},
		error : function(e) {
			alert ('Lỗi');
		}
	});
}

function getUserByOsAndGroup(){
	var groupId = $('#groupId').val();
	var osId = $('#osId').val();
	if (groupId == '-1' || groupId == null) {
		return;
	}
	if (osId == '-1' || osId == null) {
		return;
	}
	
	var link = CONTEXT_PATH + "/APP02_getDevByOsAndGroup";
	$.ajax({
		type : "POST",
		url : link,
		data : {"groupId":groupId, "osId":osId},
		dataType : 'json',
		success : function(result) {
			var dev = result.dev;
			if (dev == null) {
				alert('Không có user thỏa mãn!');
			} else {
				$('#devName').val(dev.name);
				$('#devId').val(dev.id);
			}
		},
		error : function(e) {
			alert ('Lỗi');
		}
	});
}


