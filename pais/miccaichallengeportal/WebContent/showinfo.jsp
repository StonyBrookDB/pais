<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />

<style type="text/css">

#tableContainer{
padding-top:5px
}

#content-container
{

}

.result{
font-size:20px;
color:#6600CC
}
</style>

<script src="include/js/jquery-1.10.2.min.js"></script>
<script src="include/js/jquery.easing.1.3.js"></script>
<script src="include/js/jquery.color.js"></script>
<script src="include/js/main.js"></script>

<script>
$(document).ready(function() {
	//$('#classification_result').html("<html><body><p>hello world</p></body></html>");
  	initiate();
}); // end ready

function initiate()
{ 
	var html='';
	html+='<tr>';
	html+='<th scope="col">Image Name</th>';
	html+='<th scope="col" width="200px">Overlay Image</th>';
	html+='<th scope="col">Result Stats</th>';
	html+='</tr>';
	
	var username = getarg('user');
    for(var i=0;i<35;i++)
    {
      var imagename = "path-image-1";
      if(i<10) imagename = imagename + "0";
      imagename = imagename + i.toString();
      //var filename = "/tmp/Thumbs/"+images[i].getAttribute("NAME")+".svs.dzi.tif.thumb.jpg";
      var filename = "/miccai/validation/overlay;imageuid="+ imagename + ";user=" +username;
      html+='<tr>';	
      html+='<td>'+imagename+'</td>';
      //html+="<td class='inlinelink' ><a href='http://cancer.digitalslidearchive.net/index_mskcc.php?slide_name="+imagename+"'><img src=\""+filename+"\" width=60px height=40px class=\"thumbnail\"/></a></td>\n";		
      html+="<td class='inlinelink' ><a href='"+filename+"'>See Overlay</a></td>\n";		
      html+="<td class='inlinelink' ><a href=\"/miccai/validation/segmentationbyimage;format=html;user=" + username + ";imageid=" + imagename + "\">See Stats</a></td>";
      html+='</tr>';
	}
    $('#box-table-a').html(html);
    $('.inlinelink a').click(function(){
    	window.open($(this).attr('href'));
    	return false; 
    });
};
</script>
<title>MICCAI Challenge - Info page</title>
</head>

<body>
<% 
    String user_name = request.getParameter( "user" );
%>
<div id="framecontainer">
<div id="header">
	<!-- a href="http://cci.emory.edu/cms/index.html"><img src="include/img/logo.png" alt="Logo" /></a -->
	<p class='headText'>MICCAI 2014 Digital Pathology Challenge</p>
</div>
<h2>&nbsp&nbspHi, <%= user_name %></h2>
<div id="container">

   <p class='result'>1. classification Result</p><br>
   <div id="classification_result"> <a href="/miccai/validation/classification;format=html;user=<%= user_name %>" id="content1">See classification result</a></div>
   <br>
   <p class='result'>2. Segmentation Result</p>
   <div id="content-container">
	  <div id="content">
		<div id='tableContainer'>
		   <table id="box-table-a" width="100%" ></table>
		</div>
	 </div>
  </div>
  
  
</div>

<div id="footer">
Copyright &copy 2014-2015 Emory University - All Rights Reserved
</div>
</div>
</body>

</html>
