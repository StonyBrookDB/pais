<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<style>
.oddrow td {
	padding: 8px;
	background: #e8edff;
	border-bottom: 1px solid #fff;
	color: #669;
	border-top: 1px solid transparent;
}

.oddrow:hover td {
	background: #d0dafd;
	color: #339;
}

#box-table-a h4 {
	margin: 0px;
	padding: 0px;
}

#box-table-a ul {
	margin: 10px 0 10px 40px;
	padding: 0px;
}

#box-table-a td {
	background: #f5ffff repeat-x scroll center left;
}

#box-table-a div.arrow {
	background: transparent url('include/img/arrows.png') no-repeat scroll 0px -16px;
	width: 16px;
	height: 16px;
	display: block;
}

#box-table-a div.up {
	background-position: 0px 0px;
}

#details {
	font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
	font-size: 12px;
	margin: 10px;
	text-align: left;
	border-collapse: collapse;
	background: #009900;
}

#details td {
	padding: 0px;
	background: #f5ffff; 
	border-bottom: 0px;
	color: #000;
	border-top: 1px solid transparent;
}
.evenrow{
background: #00FFFF; 

}
.oddrow{
cursor:pointer
}

</style>
<script src="include/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script type="text/javascript">  
$(document).ready(function(){
        	initiate();
        });//end ready
var patients;
var numentry;
var perpage=30;
var cpage=1;
var tpage;
function initiate()
{ 				
	    var url='/tcga/patient/features';
	    $.get(url,function(data){
		patients=data.documentElement.getElementsByTagName("row");
		numentry=patients.length;
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
		$('paginationdiv').hide();
		
	}
	var html='';
	html+='<tr>';
	html+='<th>Patient ID</th>';
	html+='<th>Sub-type</th>';
	html+='<th>Gender</th>';
	html+='<th>Vital Status</th>';
	html+='<th>Age*</th>';
	html+='<th>Length of Survival**</th>';
	html+='<th></th>';
	html+='</tr>';
	var length=page*perpage<=patients.length?page*perpage:patients.length;
    for(var i=perpage*(cpage-1);i<length;i++)
    {
    html+='<tr class="oddrow">';	
    html+='<td>'+patients[i].getAttribute("BCRPATIENTBARCODE")+'</td>';
    html+='<td>'+patients[i].getAttribute("SUBTYPE")+'</td>';
    html+='<td>'+patients[i].getAttribute("GENDER")+'</td>';
    html+='<td>'+patients[i].getAttribute("VITALSTATUS")+'</td>';
    html+='<td>'+patients[i].getAttribute("AGEATFIRSTDIAGNOSIS")+'</td>';
    html+='<td>'+patients[i].getAttribute("SURVIVAL_DAYS")+'</td>';
    html+='<td><div class="arrow"></div></td>';
    html+='</tr>';
    
    html+='<tr class="evenrow">';
	html+='<td colspan="7" style="padding-left:250px">';
	html+='<table id="details" width="500px" height="100px">';
	html+='<tr>';
	html+='<td colspan="2"><a href=# onclick=\'window.open("imagebrowser.jsp?patientid='+patients[i].getAttribute("PATIENT_ID")+'")\'>View Images</a></td>';
	html+='<td style="text-indent:5em">Tumor Site: </td><td>'+patients[i].getAttribute("TUMOR_TISSUE_SITE")+'</td>';
	html+='</tr>';
	html+='<tr>';
	html+='<td>Karn Score: </td><td>'+patients[i].getAttribute("KARNSCORE")+'</td>';
	html+='<td style="text-indent:5em">Chemo Therapy: </td><td>'+patients[i].getAttribute("CHEMO_THERAPY")+'</td>';
	html+='</tr>';
	html+='<tr>';
	html+='<td>Methylation Status: </td><td>'+patients[i].getAttribute("MGMT_METHYLATED")+'</td>';
	html+='<td style="text-indent:5em">Radiation Therapy: </td><td>'+patients[i].getAttribute("ADDITIONAL_RADIATION_THERAPY")+'</td>';
	html+='</tr>';
	html+='<tr>';
	html+='<td>Tumor Nuclei Percent: </td><td>'+patients[i].getAttribute("TUMOR_NUCLEI_PERCENT")+'</td>';
	html+='<td style="text-indent:5em"> Drug Therapy: </td><td>'+patients[i].getAttribute("ADDITIONAL_DRUG_THERAPY")+'</td>';
	html+='</tr>';
    html+='</table>';
    html+='</td>'; 
    html+='</tr>';
    }
    $('#box-table-a').html(html);	
    $(".oddrow").addClass("odd");
    $(".evenrow").hide();
    $(".oddrow").click(function(){
  	   $(this).next('.evenrow').slideToggle(500);
        $(this).find(".arrow").toggleClass("up");
     });
    $(".pagination li a").click(function(){
    	var page=$(this).attr('href');
    	if(0<page&&page<tpage+1)
    	changePage(page);
    	return false;
    });
    var selectoption=0;
    switch(+perpage)
    {
    case 30:selectoption=0;break;
    case 50:selectoption=1;break;
    case 100:selectoption=2;break;
    }
    document.getElementById('perpage').options[selectoption].selected='selected';   
    $('#notice').html('*: At first diagnosis<br />**: In days<br />');
}

function changeperpage()
{
	perpage=$('#perpage').val();
	tpage=Math.ceil(numentry/perpage);
	changePage(1);
}
</script>       
</head>
<body>
	<div id="container">
		<div id="content-container">
			<div id="content">
			
            <div class="paginationdiv" id='paginationdiv1'></div>
            
			<div id='tablecontainer' style='padding-top:5px'>
			<table id="box-table-a" width="100%" >
			</table>
			</div>
			
			<div class="paginationdiv" id="paginationdiv2"></div>
            
			</div>
			<div id='notice'></div>
		</div>	
	</div>
</body>
</html>