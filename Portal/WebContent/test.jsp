<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Insert title here</title>

<script src="include/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script type="text/javascript">  
$(document).ready(function(){
	
	var json = 
		{
			"imagereference_uid":"terry",
	        "boundary":"(0 0,10 10,20 0)",
	        "description":"terry is good",
		 }
	var xml = "<name>terry</name>";
	var url = '/tcga/pais/markups/humanannotation/annotation/add';
	
	$.ajax({
		type:"post",
		contentType:"application/json",
		url:'/tcga/pais/markups/humanannotation/annotation/add',
		data:json
	});
		
	/*
	$.post(url,xml,function(data,status){
		console.log('terry');
		console.log(status);
	});
	*/
	/*
	var myData={"name": "John"};
	var request = $.ajax({
	    url: 'http://localhost:8080/tcga/pais/markups/humanannotation/annotation/add',
	    type: "post",
	    data: myData
	});

	request.done(function (response, textStatus, jqXHR){
	    console.log("Response from server: " + response);
	});
	*/

});
</script>
</head>
<body>

</body>
</html>