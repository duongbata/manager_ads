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
});

$(document).on('click','.btnDelGroup',function(){
	var isDeleted = confirm('Xóa group ?');
	if (!isDeleted) {
		return;
	}
	var groupId = $(this).closest('tr').find('.groupId').val();
	var link = CONTEXT_PATH + '/APP05_deleteGroupByGroupId';
	$.ajax({
		type : "POST",
		url : link,
		data : "groupId="+groupId,
		dataType : 'json',
		success : function(result) {
			 
		},
		error : function(e) {
			alert ('Lỗi trong quá trình xóa');
		}
	});
	$(this).closest('tr').remove();
});

$(document).on('click','.btnDelApp',function(){
	var isDeleted = confirm('Xóa app ?');
	if (!isDeleted) {
		return;
	}
	var appId = $(this).prop('id');
	var link = CONTEXT_PATH + '/APP05_deleteAppByAppId';
	$.ajax({
		type : "POST",
		url : link,
		data : "appId="+appId,
		dataType : 'json',
		success : function(result) {
			 
		},
		error : function(e) {
			alert ('Lỗi trong quá trình xóa');
		}
	});
	$(this).closest('td').find('.link_'+appId).remove();
	$(this).remove();
});
