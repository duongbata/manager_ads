$(document).ready(function(){
    $('.filterable .btn-filter').click(function(){
        /*var $panel = $(this).parents('.filterable');
        $filters = $panel.find('.filters input'),
        $tbody = $panel.find('.table tbody');*/
        if ($('.filters input').prop('disabled') == true) {
        	$('.filters input').attr('disabled', false);
        	$('.filters input').first().focus();
        } else {
        	$('.filters input').val('');
        	$('.filters input').attr('disabled', true);
            $('.table tbody').find('.no-result').remove();
            $('.table tbody').find('tr').show();
        }
    });

    $('.filterable .filters input').keyup(function(e){
        /* Ignore tab key */
        var code = e.keyCode || e.which;
        if (code == '9') return;
        /* Useful DOM data and selectors */
        var $input = $(this),
        inputContent = $input.val().toLowerCase(),
        $panel = $input.parents('.filterable'),
        column = $panel.find('.filters th').index($input.parents('th')),
        $table = $panel.find('.table'),
        $rows = $table.find('tbody tr');
        /* Dirtiest filter function ever ;) */
        var $filteredRows = $rows.filter(function(){
            var value = $(this).find('td').eq(column).text().toLowerCase();
            return value.indexOf(inputContent) === -1;
        });
        /* Clean previous no-result if exist */
        $table.find('tbody .no-result').remove();
        /* Show all rows, hide filtered ones (never do that outside of a demo ! xD) */
        $rows.show();
        $filteredRows.hide();
        /* Prepend no-result row if all rows are filtered */
        if ($filteredRows.length === $rows.length) {
            $table.find('tbody').prepend($('<tr class="no-result text-center"><td colspan="'+ $table.find('.filters th').length +'">Không có kết quả</td></tr>'));
        }
    });
    
    $('.btnMore').click(function(){
    	var bannerId= $(this).closest('tr').find(':hidden').val();
    	popupMoreInfoBanner(bannerId);
    });
    $('#btnAdd').click(function(){
    	popupListMoreBanner();
    });
    
    $('.btnDel').click(function(){
    	var tr = $(this).closest('tr');
    	ajaxDelBanner(tr);
    });
    
    /*$('.btnMoreInfo').click(function(){
    	var isClickDisabled = $(this).closest('tr').find('.txtMaxClick').prop('disabled');
    	if(isClickDisabled) {
    		$(this).closest('tr').find('.txtMaxClick').removeAttr('disabled');
    	} else {
    		$(this).closest('tr').find('.txtMaxClick').prop('disabled','disabled');
    	}
    	
    	var isViewDisabled = $(this).closest('tr').find('.txtMaxView').prop('disabled');
    	if(isViewDisabled) {
    		$(this).closest('tr').find('.txtMaxView').removeAttr('disabled');
    	} else {
    		$(this).closest('tr').find('.txtMaxView').prop('disabled','disabled');
    	}
    	
    	var isStartTimeDisabled = $(this).closest('tr').find('.startTime').prop('disabled');
    	if(isStartTimeDisabled) {
    		$(this).closest('tr').find('.startTime').removeAttr('disabled');
    	} else {
    		$(this).closest('tr').find('.startTime').prop('disabled','disabled');
    	}
    	
    	var isStopTimeDisabled = $(this).closest('tr').find('.stopTime').prop('disabled');
    	if(isStopTimeDisabled) {
    		$(this).closest('tr').find('.stopTime').removeAttr('disabled');
    	} else {
    		$(this).closest('tr').find('.stopTime').prop('disabled','disabled');
    	}
    });*/
    
    $('.btnEdit').click(function(){
    	var tr = $(this).closest('tr');
    	ajaxEditBannerConfig(tr);
    });
});

//Datepicker from jsp
$(function () {
    $('.datetimepicker').datetimepicker();
});

function ajaxEditBannerConfig(tr){
	var banner = new Object();
	banner.maxClick = $(tr).find('.txtMaxClick').val();
	banner.maxView = $(tr).find('.txtMaxView').val();
	banner.bannerId = $(tr).find('.bannerId').val();
	banner.startTime = $('.startTime').val() + ':00';
	banner.stopTime = $('.stopTime').val() + ':00';
	banner.campaignId = $('#campaignId').val();
	
	var link = CONTEXT_PATH + '/ADV03_editBannerConfig';
	$.ajax({
		type : "POST",
		url : link,
		data : "bannerBean="+JSON.stringify(banner),
		dataType : 'json',
		success : function(result) {
			$('#formListBanner').submit();
		},
		error : function(e) {
			alert ('Lỗi trong quá trình sửa');
		}
	});
}

function ajaxDelBanner(tr) {
	var bannerId = $(tr).find(':hidden').val();
	var campaignId = $('#campaignId').val();
	var isDel = confirm('Xóa banner có id là ' + bannerId);
	if (!isDel) {
		return;
	}
	var banner = new Object();
	banner.bannerId = bannerId;
	banner.campaignId = campaignId;
	var link = CONTEXT_PATH + '/ADV03_deleteBanner';
	$.ajax({
		type : "POST",
		url : link,
		data : "banner="+JSON.stringify(banner),
		dataType : 'json',
		success : function(result) {
			$('#formListBanner').submit();
			alert ('Xóa banner '+bannerId + ' thành công!');
		},
		error : function(e) {
			alert ('Lỗi trong quá trình xóa');
		}
	});
}

function popupListMoreBanner() {
	var userId = $('#userId').val();
	var campaignId = $('#campaignId').val();
	var url = CONTEXT_PATH+'/ADV03_listMoreBanner.action?uid='+userId+'&campaignid='+campaignId;
	var w = 600;
	var h = 600;
	var left = (screen.width/2)-(w/2);
	var top = (screen.height/2)-(h/2);
	var childWin = window.open(url, 'Thêm banner', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	if(window.focus()) {
		childWin.focus();
	}
}

function popupMoreInfoBanner(id) {
	var url = CONTEXT_PATH+'/ADV03_moreInfo.action?bannerId='+id;
	var w = 600;
	var h = 600;
	var left = (screen.width/2)-(w/2);
	var top = (screen.height/2)-(h/2);
	var childWin = window.open(url, 'Thông tin chi tiết của banner', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	if(window.focus()) {
		childWin.focus();
	}
}

function getBanner() {
	$('#formListBanner').submit();
}

function listBannerOfCampaign(campaignId) {
	var input = $('<input>').attr('type','hidden')
							.attr('name',"id")
							.val(campaignId);
	$('#formListBanner').append($(input));
	$('#formListBanner').submit();
}