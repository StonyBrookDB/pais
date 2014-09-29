<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="utf-8"%>
<%@ page import = "java.util.ResourceBundle" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.msgtxt{
font-size:20px;
color:#6600CC;
text-align:center;
}
#errormessage
{
	margin: 0 auto;
	height: 800px;
}
</style>
<title>MICCAI Challenge - front page</title>
</head>
<body>

<div id="framecontainer">
	<div id="header">
		<a href="http://miccai2014.org/"><img src="include/img/logo_miccai.jpg" alt="miccai 2014" /></a>
		<p class='headText'>MICCAI 2014 Digital Pathology Challenge</p>
	</div>
    <h2>&nbsp;&nbsp;&nbsp;&nbsp;Segmentation challenge result</h2>
    <div id="errormessage"><br>
    <p class='msgtxt'>
       Please select one dataset
    </p>
    <div class='msgtxt'><a href="showinfo.jsp?user=dataset1">dataset1</a></div>
    <div class='msgtxt'><a href="showinfo.jsp?user=dataset2">dataset2</a></div>
    <div class='msgtxt'><a href="showinfo.jsp?user=dataset3">dataset3</a></div>
    
    </div>
 	<div id="footer">
		Copyright &copy 2014-2015 Emory University - All Rights Reserved
	</div>
</div>
</body>
</html>
