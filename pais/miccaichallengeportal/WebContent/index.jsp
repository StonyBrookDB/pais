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
color:#6600CC
}
</style>
<title>MICCAI Challenge - Error page</title>
</head>
<body>
<%
   	String name = request.getParameter( "user" );
   	String token = request.getParameter( "token" );
    
   	if((name!=null) && (token!=null)) {
   		
   		InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("validation.properties"); 
   	    Properties props = new Properties();
   	    props.load(inStream);
   	 
   	   	String user_token=props.getProperty(name);	
     	if(user_token!=null && user_token.equals(token)) {
     		//session.setAttribute( "username", name );
            response.sendRedirect( "showinfo.jsp?user="+name );
     	}
   	}
%>
<div id="framecontainer">
	<div id="header">
		<!-- a href="http://cci.emory.edu/cms/index.html"><img src="include/img/logo.png" alt="Logo" /></a -->
		<p class='headText'>    MICCAI 2014 Digital Pathology Challenge</p>
	</div>
    <h2>&nbsp&nbspERROR </h2>	
    <div id="errormessage"><br>
    <p class='msgtxt'>
       &nbsp&nbspPlease input the right token of name <%= name %>.
    </p>
    </div>
	<div id="footer">
		Copyright &copy 2014-2015 Emory University - All Rights Reserved
	</div>
</div>
</body>
</html>
