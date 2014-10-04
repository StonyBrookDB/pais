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
	html+='<th scope="col" >Raw Image</th>';
	html+='<th scope="col" >Ground Truth</th>';
	html+='<th scope="col" >Segmentation</th>';
	html+='<th scope="col" >Overlay</th>';
	html+='<th scope="col">Result Stats</th>';
	html+='</tr>';
	
	var username = getarg('user');
    for(var i=0;i<21;i++)
    {
      var imagename = "path-image-2";
      if(i<10) imagename = imagename + "0";
      imagename = imagename + i;
      //var filename = "/tmp/Thumbs/"+images[i].getAttribute("NAME")+".svs.dzi.tif.thumb.jpg";
      var filename = "/miccai/validation/overlay;imageuid="+ imagename + ";user=" +username;
      html+='<tr>';	
      html+='<td>'+imagename+'</td>';
      //html+="<td class='inlinelink' ><a href='http://cancer.digitalslidearchive.net/index_mskcc.php?slide_name="+imagename+"'><img src=\""+filename+"\" width=60px height=40px class=\"thumbnail\"/></a></td>\n";		
      
      html+="<td class='inlinelink1'><a href='"+filename+";showid=0'><img src=\"include/img/thumb/re_" +imagename+ "-0.jpg\" width=60px height=40px class=\"thumbnail\" title='thumbnail'/></a></td>\n";
      html+="<td class='inlinelink1'><a href='"+filename+";showid=1'>ground truth</a></td>\n";	
      html+="<td class='inlinelink1'><a href='"+filename+";showid=2'>segmentation result</a></td>\n";
      html+="<td class='inlinelink1'><a href='"+filename+";showid=3'>overlay image</a></td>\n";
      html+="<td class='inlinelink1'><a href=\"/miccai/validation/segmentationbyimage;format=html;user=" + username + ";imageid=" + imagename + "\" id=\""+imagename+"\">See Stats</a></td>";
      html+='</tr>';
	}
    $('#box-table-a').html(html);
    $('.inlinelink1 a').click(function(){
    	window.open($(this).attr('href'));
    	return false; 
    });
    $('.thumbnail').hover(
    		  function()
    		  {
    		  $(this).stop().animate(
    		  {
    		    width:'100%',
    			height:'100%'
    		  },
    		  500,
    		  'easeInSine'
    		  );
    		  },
    		  function()
    		  {
    		  $(this).stop().animate(
    		  {
    			  width:'60px',
    			  height:'40px'
    		  },
    		  1500,
    		  'easeOutBounce'
    		  );
    		  }
    		  );//end hover
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
	<a href="http://miccai2014.org/"><img src="include/img/logo_miccai.jpg" alt="Logo" /></a>
	<p class='headText'>MICCAI 2014 Digital Pathology Challenge</p>
</div>
<h2>&nbsp;&nbsp;Segmentation result of <%=user_name%></h2>
<div id="container">
<br>
   <p>&nbsp;(For the overlay image, green color means the ground truth mask, the blue color means result mask.)</p>
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
