<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<link href="fancybox/jquery.fancybox-1.3.4.css" rel="stylesheet"/>
<style>
#tiledimage {
	float: left;
	width: 130px;
	margin-right: 30px;
	margin-left: 10px;
	border-right: white 1px dotted;	
	height:800px;
	overflow-y:auto;
	overflow-x:hidden
}
#tiledimage img {
	display: inline-block;
	margin: 0 0 10px 0;
	border: 1px solid rgb(0,0,0);
}

#content-container
{
width:1000px;
overflow:auto;
}

h1{
font-size:20px
}
</style>
<script src="include/js/jquery-1.7.2.min.js"></script>
<script src="include/js/jquery.easing.1.3.js"></script>
<script src="fancybox/jquery.fancybox-1.3.4.min.js"></script>
<script src="include/js/jquery.lazyload.min.js"></script>
<script src='include/js/hover.js'></script>
<script src="include/js/main.js"></script>
<script>
var featureNum=10;
var features = [];
features[0]="Area";
features[1]="Perimeter";
features[2]="Eccentricity";
features[3]="Max Intensity";
features[4]="Mean Intensity";
features[5]="Min Intensity";
features[6]="Std Intensity";
features[7]="Circularity";
features[8]="Entropy";
features[9]="Energy";
var paisuid;
var barcode;
var featurevalue = [];
$(document).ready(function() {
  
	 paisuid=getarg('img');
	 barcode=paisuid.substring(0,12);
	 $('#imagedetailshead').text('Image Details for Image PAIS_UID '+paisuid);
	 refreshFeatureTable();
	 refreshPatientInfo();
	 refreshTiledImage();
	
}); // end ready
function refreshFeatureTable()
{
    var url="/tcga/pais/features/aggregation/document;paisuid="+paisuid+";format=xml";
    $.get(url,function(data){
    	var result=data.documentElement.getElementsByTagName("row")[0];
		featurevalue[0]=result.getAttribute("AVG_AREA");				 
		featurevalue[1]=result.getAttribute("AVG_PERIMETER");
		featurevalue[2]=result.getAttribute("AVG_ECCENTRICITY");
		featurevalue[3]=result.getAttribute("AVG_MAX_INTENSITY");
		featurevalue[4]=result.getAttribute("AVG_MEAN_INTENSITY");
		featurevalue[5]=result.getAttribute("AVG_MIN_INTENSITY");
		featurevalue[6]=result.getAttribute("AVG_STD_INTENSITY");
		featurevalue[7]=result.getAttribute("AVG_CIRCULARITY");
		featurevalue[8]=result.getAttribute("AVG_ENTROPY");
		featurevalue[9]=result.getAttribute("AVG_ENERGY");
    	var html='';
    	html+='<colgroup>';
    	html+='<col class="oce-first" />';
    	html+='</colgroup>';
    	html+='<thead>';
   		html+='<tr>';
    	html+='<th width="140px">Feature</th>';
    	html+='<th>Value</th>';
   		html+='<th>Histogram</th>';
   		html+='</tr>';
   		html+='</thead>';
        
        var uri = '';
        for(var i=0;i<featureNum;i++)
        {
        html+="<tr>";
        html+="<td>"+features[i]+"</td><td>"+featurevalue[i]+"</td>";
        html+="<td>";
        uri = "/tcga/pais/features/histogram;paisuid="+paisuid+";feature="+features[i].toLowerCase().replace(' ','_')+";width=900;height=525;format=jpg";
        html+="<a href='"+uri+"' rel='histogramGal' title='"+features[i]+"'  uid=imguid feature='"+features[i].toLowerCase().replace(' ','_')+"' class='histogram'>";
        html+="<img src='"+uri+"' id='histogram' onmouseover='hoverbox_show(this, \""+uri+"\")' onmouseout='hoverbox_hide()' width='50px' height='40px' alt='"+features[i]+"'/>";
        html+="</a>";
        html+="</td>";
        html+="</tr>";
        };
        
        html+="<tr>";
        html+="<td></td>";
    	html+="<td>";
    	html+="<a href='/tcga/pais/features/aggregation/document;paisuid="+paisuid+";format=json'>Download mean features in JSON</a>";
    	html+="<br/>";
    	html+="<a href='/tcga/pais/features/aggregation/document;paisuid="+paisuid+";format=xml'>Download mean features in XML</a>";
    	html+="</td>";
    	html+="</tr>";
        $('#one-column-emphasis').html(html);
        
        $('.histogram').fancybox({
        	overlayColor:'#060',
        	overlayOpacity:.3,
        	transitionIn:'elastic',
        	transitionOut:'elastic',
        	easingIn:'easeInSine',
        	easingOut:'easeOutSine',
        	titleSolution:'outside',
        	cyclic:true,
        	type:'image'
        	});
        $('.histogram').each(function(){
        		 var img = new Image();
        		 img.src=$(this).attr('href');
          });
        
    });

}
function refreshPatientInfo()
{
	var url="/tcga/patient/features;format=xml;barcode="+barcode;
	$.get(url,function(data){
		var patientinfo=data.documentElement.getElementsByTagName("row")[0];
		var html='';
		html+='<tr>';
		html+='<th scope="col">BAR Code</th>';
		html+='<th scope="col">Sub-type</th>';
		html+='<th scope="col">Gender</th>';
		html+='<th scope="col">Vital Status</th>';
		html+='<th scope="col">Kran Score</th>';
		html+='<th scope="col">Age</th>';
		html+='<th scope="col">Survival Days</th>';
		html+='<th scope="col">Tumor Nuclei Percent</th>';
		html+='<th scope="col">Methalation Status</th>';
		html+='<th scope="col"></th>';
		html+='</tr>';
		
		html+='<tr>';
		html+='<td>'+patientinfo.getAttribute("BCRPATIENTBARCODE")+'</td>';
		html+='<td>'+patientinfo.getAttribute("SUBTYPE")+'</td>';
		html+='<td>'+patientinfo.getAttribute("GENDER")+'</td>';
		html+='<td>'+patientinfo.getAttribute("VITALSTATUS")+'</td>';
		html+='<td>'+patientinfo.getAttribute("KARNSCORE")+'</td>';
		html+='<td>'+patientinfo.getAttribute("AGEATFIRSTDIAGNOSIS")+'</td>';
		html+='<td>'+patientinfo.getAttribute("SURVIVAL_DAYS")+'</td>';
		html+='<td>'+patientinfo.getAttribute("TUMOR_NUCLEI_PERCENT")+'</td>';
		html+='<td>'+patientinfo.getAttribute("MGMT_METHYLATED")+'</td>';
		
		html+='<td><a onclick="window.open(\'imagebrowser.jsp?patientid='+patientinfo.getAttribute("PATIENT_ID")+'\')" href=#>View all images for this patient</a></td>';
		html+='</tr>';
		$('#box-table-a').html(html);
	});
};

function refreshTiledImage()
{
	var url='/tcga/pais/list/tile/regions;format=xml;paisuid='+paisuid;
	var html='';
	$.get(url,function(data){
		tiledimages=data.documentElement.getElementsByTagName("row");
		html+="<p>tiled images</p>";
		var count = 0;
		var src='';
		var x,y,w,h;
        for(count=0;count<tiledimages.length;count++)
        {
        x=Math.floor(tiledimages[count].getAttribute("X"));
        y=Math.floor(tiledimages[count].getAttribute("Y"));
        w=Math.floor(tiledimages[count].getAttribute("WIDTH"));
        h=Math.floor(tiledimages[count].getAttribute("HEIGHT"));
		src = "/tcga/images/thumbnail/tiledimage;paisuid="+paisuid+";x="+x+";y="+y+";w="+w+";h="+h+";format=jpg;";
		html+="<div class='tiledimagethumb'>"+x+","+y;
        html+="<a class='opendl' href=\"#downloadtiledimage_"+count+ "\"><img class='lazyimage' src='"+src+"length=50' data-original='"+src+"length=400' width='100' height='100' alt='tiled image'></a></div>";
		
	    html+="<div style='width:550px;height:600px;display:none;'>";
        html+="<div style='overflow-x:hidden;' class='downloadtiledimage' id='downloadtiledimage_"+count+"' rel='dltiledimage' title='download tile image'>";
	    
        html+="<div><img src='"+src+"length=400'  class='bigthumbnail'></div>";
	    
        html+="<div class='downloaddiv'>";
	    var downloadurl='';
	    downloadurl+= "/tcga/images/image/window;paisuid="+paisuid+";x="+x+";y="+y+";w="+w+";h="+h+";format=jpg;attachment=true;";
	    html+="<a href='"+downloadurl+"'>Download Tile Image</a>";
	    html+="</div>";
	    
	    html+="<div class='downloaddiv'>";
	    downloadurl = "/tcga/pais/documents/tile;paisuid="+paisuid+";tilename="+tiledimages[count].getAttribute("NAME");
	    html+="<a href='"+downloadurl+"'>Download Markup File</a>";
	    html+="</div>";
	    		
	    html+="<div class='downloaddiv' id='featureDownload'>";
	    downloadurl = "/tcga/pais/features/tile;paisuid="+paisuid+";tilename="+tiledimages[count].getAttribute("NAME");
	    html+="<a href='"+downloadurl+"'>Download Features in</a>";
	    html+="<select><option value='json'>JSON</option><option value='xml'>XML</option></select>";
	    html+="</div>";
	    
	    html+="</div>";
	    html+="</div>";
        };
		$('#tiledimage').html(html);
		
		$(".opendl").fancybox({
			maxWidth	: 800,
			maxHeight	: 600,
			fitToView	: false,
			width		: '70%',
			height		: '70%',
			autoDimension : false,
			overlayColor:'#060',
			overlayOpacity:.3,
			transitionIn:'elastic',
			transitionOut:'elastic',
			easingIn:'easeInSine',
			easingOut:'easeOutSine',
			titleSolution:'outside',
			cyclic:true
		});
		var bigthumbs = [];
		var count = 0;
		 $('.bigthumbnail').each(function(){
			 bigthumbs[count]=new Image();
			 bigthumbs[count].src=$(this).attr('src');
			 count++;
			 
		 });
		 


		$('.lazyimage').lazyload({
			threshold:1000,
			effect:'fadeIn',
			event:'mouseon'
		});
	   
		$('.tiledimagethumb a').each(function(){
			    var img = new Image(); 
			    img.src = $(this).attr('href');
			   
			    if (img.complete) { 
			        $(this).find('img').attr('src',img.src);
			        return; 
			    };

			    img.onload = function () { 
			    	 $(this).find('img').attr('src',img.src);
			    	 //alert(img.src);
			    };

		});
		
		$('#featureDownload a').click(function(){
			window.open($(this).attr('href')+';format='+$(this).next('select').val());
			return false;
		});
	});
	
}

</script>
<title>Image Details</title>
</head>

<body>
	<div id="container">
		<div id="content-container">

		     <div id="content">
		       <h1 id='imagedetailshead'>Image Details</h1>
		       <div id='tiledimage'></div>
               <table id="one-column-emphasis"></table>
               <h1>Patient Information</h1>
               <table id="box-table-a" width=100%></table>
            </div>
       </div>
  </div>
</body>
<div id="hoverbox"></div>
</html>