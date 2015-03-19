$(document).ready(function () {
    $('.sync-pagination').twbsPagination({
        totalPages: $('#numPage').val(),
        onPageClick: function (evt, page) {
        	/*getRecordByPage(page);*/
        	alert(page);
        }
    });
});

