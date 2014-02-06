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
var patient_feature_primary_db = new Array("BCR_PATIENT_BARCODE","GENDER","AGE_AT_INITIAL_PATHOLOGIC_DIAGNOSIS","ANATOMIC_ORGAN_SUBDIVISION","VITAL_STATUS","DAYS_TO_DEATH");
var patient_feature_primary_show = new Array("patient_id","gender","age*","organ","vital status","days of survival**");
var patient_feature_detail_db = new Array(
		"CHEMO_THERAPY","HORMONAL_THERAPY","IMMUNO_THERAPY","RADIATION_THERAPY","TARGETED_MOLECULAR_THERAPY",
		"HISTOLOGICAL_TYPE","INITIAL_PATHOLOGIC_DIAGNOSIS_METHOD","PERSON_NEOPLASM_CANCER_STATUS",
		"PRETREATMENT_HISTORY","PRIOR_GLIOMA","TUMOR_TISSUE_SITE");
var patient_feature_detail_show = new Array(
		"chemo therapy","hormonal threapy","immuno therapy","radiation therapy","targeted molecular therapy",
		"histological type","diagnosis method","neoplasm cancer status",
		"pretreatment history","prior glioma","tumor tissue site");
var primary_patient_length = patient_feature_primary_db.length;
var detail_patient_length = patient_feature_detail_db.length;

/*
 * 
 BCR_PATIENT_BARCODE
 ADDITIONAL_CHEMO_THERAPY
 ADDITIONAL_DRUG_THERAPY
 ADDITIONAL_HORMONE_THERAPY
 ADDITIONAL_IMMUNO_THERAPY
 ADDITIONAL_RADIATION_THERAPY
 AGE_AT_INITIAL_PATHOLOGIC_DIAGNOSIS
 ANATOMIC_ORGAN_SUBDIVISION
 CHEMO_THERAPY
 DAYS_TO_BIRTH
 DAYS_TO_DEATH
 DAYS_TO_INITIAL_PATHOLOGIC_DIAGNOSIS
 DAYS_TO_LAST_FOLLOWUP
 DAYS_TO_TUMOR_PROGRESSION
 DAYS_TO_TUMOR_RECURRENCE
 GENDER
 HISTOLOGICAL_TYPE
 HORMONAL_THERAPY
 IMMUNO_THERAPY
 INFORMED_CONSENT_VERIFIED
 INITIAL_PATHOLOGIC_DIAGNOSIS_METHOD
 PERSON_NEOPLASM_CANCER_STATUS
 PRETREATMENT_HISTORY
 PRIOR_GLIOMA
 RADIATION_THERAPY
 TARGETED_MOLECULAR_THERAPY
 TUMOR_TISSUE_SITE
 VITAL_STATUS
 CPATIENTID
 
 */
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
	
	for(var i=0;i<primary_patient_length;i++)
	{
	   html+='<th>'+patient_feature_primary_show[i]+'</th>';
	}

	html+='<th></th>';
	html+='</tr>';
	var length=page*perpage<=patients.length?page*perpage:patients.length;
    for(var i=perpage*(cpage-1);i<length;i++)
    {
      html+='<tr class="oddrow">';	
      for(var j=0;j<primary_patient_length;j++)
      {
      html+='<td>'+patients[i].getAttribute(patient_feature_primary_db[j])+'</td>';
      }

      html+='<td><div class="arrow"></div></td>';
      html+='</tr>';
    
      html+='<tr class="evenrow">';
	  html+='<td colspan="7" style="padding-left:100px">';
	  html+='<table id="details" width="750px" height="100px">';
	  html+='<tr>';
	  html+='<td colspan="2"><a href=# onclick=\'window.open("imagebrowser.jsp?patientid='+patients[i].getAttribute("BCR_PATIENT_BARCODE")+'")\'>View Images</a></td>';
	  html+='<td/><td/>';
	  html+='</tr>';
	
	  for(var j=0;j<detail_patient_length;j++)
	  {
	    if(j%2==0)
	    {
		 html+='<tr>';
		 html+='<td>'+patient_feature_detail_show[j]+': </td><td>'+patients[i].getAttribute(patient_feature_detail_db[j])+'</td>';
		 if(j==detail_patient_length-1)
		   html+='<td/><td/></tr>';
	    }
	    else if(j%2==1)
	    {
		 html+='<td style="text-indent:5em">'+patient_feature_detail_show[j]+': </td><td>'+patients[i].getAttribute(patient_feature_detail_db[j])+'</td>';
		 html+='</tr>';
	    }
	    

	  }
      html+='</table>';
      html+='</td>'; 
      html+='</tr>';
    }
    $('#box-table-a').html(html);	
    $(".oddrow").addClass("odd");
    $(".evenrow").hide();
    $(".oddrow").click(function(){
  	   $(this).next('.evenrow').slideToggle(300);
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