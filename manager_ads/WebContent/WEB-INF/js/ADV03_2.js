$(document).ready(function(){
	$('#btnOk').click(function(){
		ajaxEditBanner();
	});
});

function ajaxEditBanner(){
	var banner = new Object();
	banner.bannerId = $('#id').val();
	banner.bannerName = $('#name').val();
	banner.bannerDescription = $('#description').val();
	banner.campaignId = $('#campaignId').val();
	banner.startTime = $('#startTime').val() + ':00';
	banner.stopTime = $('#stopTime').val() + ':00';
	banner.image1 = escape($('#image1').val());
	banner.image2 = escape($('#image2').val());
	banner.bannerType = $('#bannerType').val();
	banner.maxClick = $('#maxClick').val();
	banner.maxView = $('#maxView').val();
	banner.androidUrl = escape($('#androidUrl').val());
	banner.iosUrl = escape($('#iosUrl').val());
	banner.windowsUrl = escape($('#windowsUrl').val());
	banner.maxClickPerDay = $('#maxClickPerDay').val();
	banner.maxViewPerDay = $('#maxViewPerDay').val();
	banner.statusId = $('#statusId').val();
	var link = CONTEXT_PATH + '/ADV03_editBanner';
	$.ajax({
		type : "POST",
		url : link,
		data : "banner="+JSON.stringify(banner),
		dataType : 'json',
		success : function(result) {
			try {
		        window.opener.getBanner();
		    }
		    catch (err) {}
		    window.opener.focus();
		    window.close();
		},
		error : function(e) {
			alert ('Lỗi trong quá trình edit');
		}
	});
}