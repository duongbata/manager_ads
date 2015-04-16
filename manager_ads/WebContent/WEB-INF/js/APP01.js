$(document).ready(function(){
/*	$('#btnCall').click(function(){
		var link = CONTEXT_PATH + "/APP01_getAllGroupId";
		$.ajax({
			type : "GET",
			url : link,
			dataType : 'json',
			success : function(result) {
				$.each(result,function(key,value){
					data.push(value);
				});
			},
			error : function(e) {
				alert ('Lỗi');
			}
		});
	});*/
//	$('input#txtGroupId').quickselect({url:CONTEXT_PATH+"/APP01_getAllGroupId"});
	getListGroupId();
});

var data = [];

function getListGroupId() {
	data = [];
	var link = CONTEXT_PATH + "/APP01_getAllGroupId";
	$.ajax({
		type : "GET",
		url : link,
		dataType : 'json',
		success : function(result) {
			$.each(result,function(key,value){
				data.push(value);
			});
			
		},
		error : function(e) {
			alert ('Lỗi');
		}
	});
	var input = document.getElementById("txtGroupId");
	var awesomplete = new Awesomplete(input);
	awesomplete.list = data;
}

function validateInsert() {
	var groupId = $('#txtGroupId').val();
	if (groupId == null || groupId == '' || groupId.trim() =='') {
		alert('Hãy nhập id của group.');
		$('#txtGroupId').val('');
		return false;
	}
	
	var groupName = $('#txtGroupName').val();
	if (groupName == null || groupName == '' || groupName.trim() == '') {
		alert('Hãy nhập tên của group.');
		$('#txtGroupName').val('');
		return false;
	}
	
	return true;
}