function ajaxFun(){
	var input = $('#input_element').val();
    $.ajax({
        type: "get",
        url: "./RequestUserInfos?method=qryUserInfos",
        data: {"user_name" : input},
        success: function(data){
        	var jsonObj = JSON.parse(data);
        	$("#cust_name").css("display","block");
        	$("#cust_name").html(jsonObj.CUST_NAME);
        	$("#birthday").css("display","block");
        	$("#birthday").html(jsonObj.BIRTHDAY);
        }
    })
}