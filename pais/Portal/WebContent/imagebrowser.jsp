<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#imageviewerContainer{
cloat:left;
display:block;
}

#tableContainer{
padding-top:5px
}

#pagectl a{
text-decoration:none;
font-size:17px
}

#content-container
{
height:1100px;
}

#pagectl a:link, #pagectl a:visited,#pagectl a:active {color:#000000;}
#pagectl a:hover {color:#1888E0;} 
#navigation{width: 1000px;}
   
</style>
<script src="include/js/jquery-1.10.2.min.js"></script>
<script src="include/js/jquery.easing.1.3.js"></script>
<script src="include/js/jquery.color.js"></script>
<script src="include/js/main.js"></script>
<script>
$(document).ready(function() {
  patient=getarg('patientid');
  cpage=getarg('cpage');
  if(patient!=undefined)//see images for one patient
  {
	  $('#content-container').css('height','1300px');
  }
  if(cpage==undefined)
	  cpage=1;
  initiate();
  
}); // end ready

var patient;
var images;
var numentry;
var perpage=50;
var cpage;
var tpage;

function initiate()
{ 		
	    var url;
	    if(patient!=undefined)
	      url='/tcga/images/list/imageuids;patientid='+patient+';format=xml;';
	    else
	      url='/tcga/images/list/imageuids;format=xml;details=true';
	    $.get(url,function(data){
		images=data.documentElement.getElementsByTagName("row");
		numentry=images.length;
		tpage=Math.ceil(numentry/perpage);
        changePage(cpage);        
        });
}

function changePage(page)
{
	cpage=page;
	var selstr='<div style="float:right;padding-right:10px"><select id=\'perpage\' onchange="changeperpage()"><option>30</option><option>50</option><option>100</option></select>&nbsp&nbspperpage</div>';
	var pagestr="<ul  class='pagination'>";
	if (cpage==1)
		pagestr+="<li class=\"prev\"><a>&lt;</a></li>";
	else{
		pagestr+="<li class=\"prev\"><a href=\""+(+cpage-1)+"\">&lt;</a></li>";
	}
	
	for (var i=1;i<=tpage;i++){
		if (i==cpage)
			pagestr+="<li class=\"current\">"+i+"</li>";
		else pagestr+="<li><a href=\""+i+"\">"+i+"</a></li>";
	}
	if (cpage==tpage)
		pagestr+="<li class=\"next\"><a>&gt;</a></li>";
	else{
		pagestr+="<li class=\"next\"><a href=\""+(+cpage+1)+"\">&gt;</a></li>";
	}
	pagestr+="</ul>";
	if(tpage>1)
	{
		$('#paginationdiv1').html(selstr+pagestr);
		$('#paginationdiv2').html(pagestr);
	}else{
		$('.paginationdiv').hide();
		
	}
	var html='';
	html+='<tr>';
	html+='<th scope="col">Name</th>';
	html+='<th scope="col" width="200px">Thumbnail</th>';
	html+='<th scope="col">Resolution</th>';
	html+='<th scope="col">Patient ID</th>';
	html+='<th scope="col">Download</th>';
	html+='<th scope="col">Details</th>';
	html+='</tr>';
	var length=page*perpage<=images.length?page*perpage:images.length;
    for(var i=perpage*(cpage-1);i<length;i++)
    {
      var patienttmp=patient;
      var uid = images[i].getAttribute("IMAGEREFERENCE_UID");
      if(patient==undefined)
      {
        	patienttmp=images[i].getAttribute("PATIENTID");
      }	
      var imagename = uid.substring(0,uid.lastIndexOf("_"));
      var resolution = uid.substring(uid.lastIndexOf("_")+1,uid.length);
      //var filename = "/tmp/Thumbs/"+images[i].getAttribute("NAME")+".svs.dzi.tif.thumb.jpg";
      var filename = "/tcga/images/thumbnail/wsi;imageuid="+images[i].getAttribute("IMAGEREFERENCE_UID")+";format=jpg;length=200";
      html+='<tr>';	
      html+='<td>'+imagename+'</td>';
      //html+="<td class='inlinelink' ><a href='http://cancer.digitalslidearchive.net/index_mskcc.php?slide_name="+imagename+"'><img src=\""+filename+"\" width=60px height=40px class=\"thumbnail\"/></a></td>\n";		
      html+="<td class='inlinelink' ><a href='imageviewer.jsp?img="+imagename+"'><img src=\""+filename+"\" width=60px height=40px class=\"thumbnail\" title='thumbnail'/></a></td>\n";		
      html+='<td>'+resolution+'</td>';
      html+='<td>'+patienttmp+'</td>';
      html+="<td><a href=/tcga/images/image/wsi;imageuid="+uid+" class='viewdetails'>Download Image</a></td>\n";
      html+="<td class='inlinelink' ><a href='detail.jsp?img="+imagename+"_20x_20x_NS-MORPH_1'"+">Feature Summary</a></td>\n";
      html+='</tr>';
	}
    $('#box-table-a').html(html);	
    
    if(tpage>1)
    {
    var selectoption=0;
    switch(+perpage)
    {
    case 30:selectoption=0;break;
    case 50:selectoption=1;break;
    case 100:selectoption=2;break;
    }
    document.getElementById('perpage').options[selectoption].selected='selected';   
    }
    $(".pagination li a").click(function(){
    	var page=$(this).attr('href');
    	if(0<page&&page<tpage+1)
    	changePage(page);
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
    		  
    		  
    		  $('.inlinelink a').click(function(){
    			  $('#imageContainer').fadeOut(200);
    			  var url=$(this).attr('href');
    			  var html = '<div id="pagectl"><a onclick="goback()" href=#><img src="include/img/back.png" width=40px height=40px>Back to Image List</a>';
    			  html+='&nbsp&nbsp&nbsp&nbsp&nbsp';
    			  html+='<a onclick="fullscreen(\''+url+';fullscreen=true\')" href=#><img src="include/img/full_screen.png" width=50px height=50px>Open in new Window</a>';
    			  html+='</div><br>';
    			  html+="<div ><iframe height=1100px width=1000px frameborder='0' scrolling='no' src='"+url+"'></iframe></div>";

    			  $('#imageviewerContainer').html(html);
    			  $('#imageviewerContainer').fadeIn(300);
    			  return false;
    			  
    		  });
};//changepage

function changeperpage()
{
	perpage=$('#perpage').val();
	tpage=Math.ceil(numentry/perpage);
	changePage(1);
};

function goback(){
	  $('#imageviewerContainer').fadeOut(200);
	  $('#imageviewerContainer').html('');
	  $('#imageContainer').fadeIn(300);
	  return false;
};

function fullscreen(src){
	window.open(src);
	  return false;
};
 
</script>
</head>
<body>
	<div id="container">
	<%
	String id=request.getParameter("patientid");
	boolean inline=true;
	if(id!=null)inline=false;
	
	if(!inline)
	{
	%>
		<%@include file="include/header.html"%>
	<%
	}
	%>
   <div id="content-container">
	  <div id="content">
		<div id="imageContainer">
		<%
		     if(!inline)
	         {
	           out.write("<h2>Images for Patient "+id+"</h2>");
	         }
	    %>
		     <div id='paginationdiv1' class='paginationdiv'></div>
		     <div id='tableContainer'>
		      <table id="box-table-a" width="100%" >
			  </table>
			 </div>
             <div id='paginationdiv2' class='paginationdiv'></div>
		</div>
		<div id='imageviewerContainer' style='overflow:hidden' style="display:none;position:absolute;">
		</div>
	 </div>
  </div>
        <%
		if(!inline)
	{
	%>
		<%@include file="include/footer.html"%>
	<%
	}

	%>
	</div>
</body>

</html>