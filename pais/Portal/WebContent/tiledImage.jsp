<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<link href="include/css/jquery.iviewer.css" rel="stylesheet" type="text/css"/>
<link href="include/css/iviewer.lightbox.css" rel="stylesheet" type="text/css"/>
<style>

#getRegionImage label.error{
font-size:0.8em;
color:#F00;
font-weight:bold;
display:block;
}

#getRegionImage input.error,#getRegionImage select.error
{
  background:#FFA988;
  border:1px solid red;
}

#content-container
{
width:1000px;
height:900px
}

.formpair{
display:block;
}

.formlabel{
display:block;
width:10px;
}
.wrapper{
  TEXT-ALIGN: left;
  width:100%;
  padding:10px; 
  margin:0 auto; 
}
#formdiv { 
padding-left:10px;
padding-top:30px;

width:25%;
display:block;
float:left;
}
.workbench { 
padding-left:10px;
padding-top:35px;
float:left;
width:70%;
display:block;
}

canvas{
cursor:crosshair
}

#imageloader{
padding-top:100px;
TEXT-ALIGN: center;
font-size:17px;

}

#methodInfodiv{
width:30%;
}

.viewer
{
width: 100%;
height: 800px;
position: relative;
border:none;
}

</style>
<script src="include/js/jquery-1.7.2.min.js"></script>
<script src="include/js/jquery.validate.min.js"></script>
<script src="include/js/jquery.easing.1.3.js"></script>
<script src="include/js/jqueryui.js" ></script>
<script src="include/js/jquery.mousewheel.min.js" ></script>
<script src="include/js/jquery.iviewer.js" ></script>
<script>
$(document).ready(function() {

    var url='/tcga/images/list/imageuids;format=xml';
    var imageuidoptions='<option>Select One Image</option>';
    
    $.get(url,function(data){
		 var imageuids=data.documentElement.getElementsByTagName("row");
		 var imageuid;
		 for(var i=0;i<imageuids.length;i++)
		 {
			 imageuid=imageuids[i].attributes[0].nodeValue;
			 imageuidoptions+="<option value=\""+imageuid+"\">"+imageuid+"</option>";
		 }
		 $('#ImageUID').html(imageuidoptions);
		changeImage();
    });
	
	var canvas=document.getElementById("thumbnail");
	var context=canvas.getContext("2d");
	
	var $thumbnail=$('#thumbnail');
	//start validation

	$('#getRegionImage').validate({
		rules:{
		   textX:{
		   required:true,
		   number:true
		   },
		   textY:{
		   required:true,
		   number:true
		   },
		   textW:{
		   required:true,
		   number:true,
		   range:[1,16777216]
		   },
		   textH:{
		   required:true,
		   number:true,
		   range:[1,16777216]
		   }
		   
		},//end rules
		messages:{
		   textX:{
		   required:"cannot be empty",
		   number:"please input a number",
		   },
		   textY:{
		   required:"cannot be empty",
		   number:"please input a number",
		   },
		   textW:{
		   required:"cannot be empty",
		   number:"please input a number",
		   range:"please input a number between 1 and 16777216"
		   },
		   textH:{
		   required:"cannot be empty",
		   number:"please input a number",
		   range:"please input a number between 1 and 16777216"
		   }
		}//end message
	});//end validation

	var startx=0;
	var starty=0;
	var endx=0;
	var endy=0;
	var moused=false;
	var validxywh=false;
	$thumbnail.mousedown(function(e){
		var pos = getMousePos(document.getElementById("thumbnail"), e);
		//startx = e.clientX - $(e.target).offset().left;
		//starty = e.clientY - $(e.target).offset().top;
		startx=pos.x;
		starty=pos.y;
		startx = startx>=0?startx:0;
		starty = starty>=0?starty:0;
		var maxx = $('#maxX').attr('value');
		var maxy = $('#maxY').attr('value');
		if(maxx<=0||maxy<=0)
		{
			return false;
		}
		var xrate = maxx/$thumbnail.width();
		var yrate = maxy/$thumbnail.height();
        setXYWH(Math.ceil(startx*xrate),Math.ceil(starty*yrate),0,0);
		moused=true;
		return false;
	});//end mousedown
	$(document).mouseup(function(e){
		moused=false;
		return false;
	});
	
	$thumbnail.mousemove(function(e)
	{
		    var pos = getMousePos(document.getElementById("thumbnail"), e);
			//endx = e.clientX - $(e.target).offset().left;
			//endy = e.clientY - $(e.target).offset().top;
			endx=pos.x;
			endy=pos.y;
			endx = endx>=0?endx:0;
			endy = endy>=0?endy:0;
			var width = endx-startx;
			var height = endy-starty;
			var maxx = $('#maxX').attr('value');
			var maxy = $('#maxY').attr('value');
			if(maxx<=0||maxy<=0)
			{
				return false;
			}
				
			var xrate = maxx/$thumbnail.width();
			var yrate = maxy/$thumbnail.height();
			if(moused)
			{	
			//alert(startx+'|'+starty+'|'+width+'|'+height);
			var x=startx,y=starty;
			if(width<0)
			{
				width=Math.abs(width);
				x=endx;
			}
			if(height<0)
			{
				height=Math.abs(height);
				y=endy;
			}
			
	        setXYWH(Math.ceil(x*xrate),Math.ceil(y*yrate),Math.ceil(width*xrate),Math.ceil(height*yrate));
	        validxywh=true;
	        context.clearRect(0,0,canvas.width,canvas.height);
	        context.strokeStyle = 'black';
			context.strokeRect(x,y, width, height);
			var remain = (16777216-Math.ceil(width*xrate)*Math.ceil(height*yrate));
			remain=remain<=16777216?remain:0;
			remain=remain>0?remain:0;
			$('#pixelRemain').text(' Pixels Remaining:'+ (remain));
		    }
			$("#mouselocx").text('X:  '+parseInt(endx*xrate,10));
			$("#mouselocy").text('Y:  '+parseInt(endy*yrate,10));
	});
	$thumbnail.mouseup(function(){
		
		if(validxywh)
		{
		var dftop=$('#defaultop').get(0).selectedIndex;
		if(dftop!=0)
		gettiledImage(dftop);	
		
		}
		validxywh==false;
		moused=false;
		startx=0;
		starty=0;
		endx=0;
		endy=0;
		maxx=0;
		maxy=0;
		xrate=0;
		yrate=0;
		
		return false;
	});
	$thumbnail.mouseout(function(){
		
		$("#mouselocx").text('');
		$("#mouselocy").text('');
	});
	//start iviewer lightbox
    $("#iviewer .zoomin").click(function(e) {
        e.preventDefault();
        viewer.iviewer('zoom_by', 1);
    });

    $("#iviewer .zoomout").click(function(e) {
        e.preventDefault();
        viewer.iviewer('zoom_by', -1);
    });
	
    $("#iviewer .close").click(function(e) {
        e.preventDefault();
        close();
    });

    $("#iviewer").bind('fadein', function() {
        $(window).keydown(function(e) {
            if (e.which == 27) close();
        });
    });//end iviewer lightbox
   
    viewer = $("#iviewer .viewer").width($(document).width() - 80).height($(document).height()).iviewer({
       ui_disabled : true,
       zoom : 'fit',
       onFinishLoad : function(ev) {
        $("#iviewer .loader").fadeOut();
        $("#iviewer .viewer").fadeIn();
       }
     });//end iviewer
     
     $("#getoverlay").click(function(){
    	 gettiledImage(2);
     });
     $("#gettiledimage").click(function(){
    	 gettiledImage(1);
     });
     
    //hover on show details 
    $('#showMethodInfo').mouseover(function(){
   
    var obj = document.getElementById('showMethodInfo');
	var div = document.getElementById('methodInfodiv');
    var oTop = -30,
        oLeft = 70;
    do {
        oLeft += obj.offsetLeft;
        oTop += obj.offsetTop;
    } while (obj = obj.offsetParent);

    div.style.top  = (oTop  + 20) + 'px';
    div.style.left = (oLeft + 20) + 'px';
    div.style.visibility = 'visible';
	$('#methodInfodiv').slideDown(300);
   });
    $('#showMethodInfo').mouseout(function(){
	$('#methodInfodiv').fadeOut(200);
    });
    $('#showMethodInfo').click(function(){
    	return false;
    });

}); // end ready

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
      x: evt.clientX - rect.left,
      y: evt.clientY - rect.top
    };
  }
  
function preImage(img,callback){ 
	var i=0;
   while(i<10){
   i++;
   if (img.complete) {
        callback.call(img); 
       return; 
    }  
    img.onload = function () {
        callback.call(img);
    };  
   }
}  
function setXYWH(X,Y,W,H)
{
	document.getElementById("textX").value=X;
    document.getElementById("textY").value=Y;
    document.getElementById("textW").value=W;
    document.getElementById("textH").value=H;
}

var viewer;
var pais_uids=new Array();

function opensrc(src) {
    $("#iviewer").fadeIn().trigger('fadein');
    $("#iviewer .loader").show();
    $("#iviewer .viewer").hide();
    viewer.iviewer('loadImage', src)
        .iviewer('set_zoom', 'fit');
};

function close() {
    $("#iviewer").fadeOut().trigger('fadeout');
};
function changeImage(){
	var ImageUID = $("#ImageUID").val();
	var canvas=document.getElementById("thumbnail");
//	var context=canvas.getContext("2d");

	if($("#ImageUID").get(0).selectedIndex!=0)//select one image
	{
	//http://europa.cci.emory.edu
	 $('.workbench').fadeOut(100);
	 $('#imageloader').fadeIn(300);
	 $('#imageloader').html('<p>loading image '+$("#ImageUID").val()+' please wait!</p>');
	 var url = '/tcga/images/image/dimension;imagereferenceuid='+ImageUID+';format=xml';
	 $.get(url,function(data){
			var maxx = data.documentElement.getElementsByTagName("row")[0].attributes[0].nodeValue;
			var maxy = data.documentElement.getElementsByTagName("row")[0].attributes[1].nodeValue; 
			$('#maxX').text('width: '+maxx);
			$('#maxX').attr('value',maxx);
			$('#maxY').text('height: '+maxy);
			$('#maxY').attr('value',maxy);
			});
	 var thumbimg=new Image();
	 thumbimg.src = '/tcga/images/thumbnail/wsi;imageuid='+ImageUID+';length=500;format=jpg';
	 thumbimg.onload = function (){
		 canvas.width = thumbimg.width; 
		 canvas.height = thumbimg.height; 
		 $('#canvascontainer').css("height",thumbimg.height);
		 $('#canvascontainer').css("width",thumbimg.width);
		 $('#canvascontainer').css("background-image",'url('+thumbimg.src+')');
		 $('#imageloader').fadeOut(200);
		 $('#thumbnaildiv').fadeIn(300);
		 
	 };

	 url = '/tcga/images/image/imagereferences;imagereferenceuid='+ImageUID+';format=xml';
	 var $methodInfo=$('#methodInfo');
	 $.get(url,function(data){
		 pais_uids=data.documentElement.getElementsByTagName("row");
		 var paishtml='<option>Select One Method</option>';
		 var html='<table id="box-table-a" width="100%" >'+
	     '<tr>'+
         '<th scope="col">MethodName</th>'+         
         '<th scope="col">Role</th>'+
         '<th scope="col">Sequence Numer</th>'+
         '<th scope="col">Study Date</th>'+
         '</tr>';
		 for (var i=0;i<pais_uids.length;i++) 
		 { 
			 var paisuid=pais_uids[i].attributes[0].nodeValue;
			 paisuid=paisuid.substring(ImageUID.length+1,paisuid.length);
			 paishtml+='<option>'+paisuid+'</option>';
			 html+='<tr>';
			 html+='<td>'+pais_uids[i].attributes[3].nodeValue+'</td>';;
	         html+='<td>'+pais_uids[i].attributes[2].nodeValue+'</td>';
	         html+='<td>'+pais_uids[i].attributes[4].nodeValue+'</td>';
	         html+='<td>'+pais_uids[i].attributes[5].nodeValue+'</td>';
	         html+='</tr>';
		 }
		 html+='</table>';
		 $methodInfo.html(html);
		 $('#pais_uidsel').html(paishtml); 
		 document.getElementById("pais_uidsel").options[1].selected='selected';
		 changePais();
	 });
	}
	else//no image is selected
	{
		$('#maxX').text('');
		$('#maxX').attr('value','');
		$('#maxY').text('');
		$('#maxY').attr('value','');
		$('#pixelRemain').text('');
		$('#pais_uidsel').html('<option>Select One Method</option>');
		$('#methodInfo').html('');
		$('.workbench').fadeOut(200);
		$('#instruction').fadeIn(300);

	}
	setXYWH('','','','');
};//end change ImageUID

function changePais(){
	var $methodInfo=$('#methodInfo');
	var html='<table id="box-table-a" width="100%" >'+
    '<tr>'+
    '<th scope="col">MethodName</th>'+         
    '<th scope="col">Role</th>'+
    '<th scope="col">Sequence Numer</th>'+
    '<th scope="col">Study Date</th>'+
    '</tr>';
    var i,length;
    var selected=$("#pais_uidsel").get(0).selectedIndex;
    if(selected==0)
    {
    	i=0;
    	length=pais_uids.length;
    }else
    {
    	i=selected-1;
    	length=selected;
    }
	 for (;i<length;i++) 
	 { 
		html+='<tr>';
		html+='<td>'+pais_uids[i].attributes[3].nodeValue+'</td>';;
        html+='<td>'+pais_uids[i].attributes[2].nodeValue+'</td>';
        html+='<td>'+pais_uids[i].attributes[4].nodeValue+'</td>';
        html+='<td>'+pais_uids[i].attributes[5].nodeValue+'</td>';
        html+='</tr>';
	 }
	 html+='</table>';
	 //$methodInfo.fadeOut(200);
	 $methodInfo.html(html);
	 //$methodInfo.slideDown(300);	
	 
};

function gettiledImage(type){
        var uid = $("#ImageUID").val();
        if($("#ImageUID").get(0).selectedIndex==0)
    	{
    	  alert('please select one ImageUID');
    	}
        else
        {
        var x = document.getElementById("textX").value;
        var y = document.getElementById("textY").value;
        var w = document.getElementById("textW").value;
        var h = document.getElementById("textH").value;
        var maxx = $('#maxX').attr('value');
        var maxy = $('#maxY').attr('value');
        //alert(x+" "+y+" "+w+" "+h);
        if(+w==0||+h==0)
        	return;
        if(+w*+h>16777216)
        {
        	alert('please select an area less than 16777216!');
        }
        else if(+x + +w> +maxx|| +y + +h> +maxy)
        {
           alert('x+w should smaller than '+ maxx +' and y+h should smaller than '+maxy);	
        }
        else
        {
        var f = $('#format').val();
        if(type==1)
        {
        var url = "/tcga/images/image/window;imageuid="+uid+";x="+x+";y="+y+";w="+w+";h="+h+";format="+f;
        opensrc(url);
        }else if(type==2)
        {
        	if($("#pais_uidsel").get(0).selectedIndex==0)
        	{
        	  alert('please select one PAIS_UID');
        	}
        	else
        	{
            var url = "/tcga/images/overlay/window;paisuid="+uid+'_'+$('#pais_uidsel').val()+";x="+x+";y="+y+";w="+w+";h="+h+";format="+f;
            window.open(url);
        	}
        }
        	
        }
        }
};//end gettiledImage



</script>
</head>
<body>
	<div id="container">
		<div id="content-container">
			<div id="content">  
				<div class="wrapper">
				  <div id="formdiv">
				    <form method="get" id="getRegionImage" name="getRegionImage" >
 					<div class='formpair'>
				    <label for="ImageUID" class="formlabel">Image:</label>
				    <select id="ImageUID" name="ImageUID" onchange="javascript:changeImage();">
                    </select>    
                    </div>
					<div class='formpair'>
				    <label for="Method" class="formlabel">Method:</label>
				    <select id="pais_uidsel" name="pais_uidsel" onchange="javascript:changePais();">
				    <option>Select One Method</option>
				    </select>
				    <a id='showMethodInfo' style=' text-decoration:none;'>Method Info</a>
				    </div>
					<div class='formpair'>
						<label for="CordinateX" class="formlabel">X:</label>
						<input type="text" name="textX" id="textX">
					</div>
					<div class='formpair'>
						<label for="CordinateY" class="formlabel">Y:</label>
						<input type="text" name="textY" id="textY">
					</div>
					<div class='formpair'>
						<label for="Width" class="formlabel">W:</label>
						<input type="text" name="textW" id="textW">
					</div>
					<div class='formpair'>
						<label for="Height" class="formlabel">H:</label>
						<input type="text" name="textH" id="textH">
				    </div>
					<div class='formpair'>
				    	<label for="format">format:</label>
				        <select id="format" name="format">
				          <option value="jpg">jpg</option>
				          <option value="jpeg">jpeg</option>
				          <option value="png">png</option>
				          <option value="gif">gif</option>
				        </select>
				    </div>
				    <br/>
					<div>
						<input type="button" name="submitButton" id="gettiledimage" value="Tile  Image" class='button'>
					</div>
					<br>
					<div>
						<input type="button" name="overlayButton" id="getoverlay" value="Get Markup" class='button'>
					</div>
					<br>
				</form>
				</div>
				
				<div id="thumbnaildiv" class='workbench'>
				       <div>
				       <label for="default">default operation:</label>
				       <select id="defaultop" name="defaultop">
				       <option value="no">no operation</option>
				       <option value="tileimage">tile image</option>
				       <option value="overlayimage">get markup</option>
				       </select>
				       <label id="maxX" value=""></label>
                       <label id="maxY" value="" ></label>
                       <label id="pixelRemain"></label>    
				       </div>
		              <br>
		              <div id='canvascontainer'>
                      <canvas id="thumbnail">
                      Your user agent does not support the HTML5 Canvas element.
                      </canvas>
                      </div>
                      <div>
                           <p id="mouselocx"></p>
                           <p id="mouselocy"></p>
                     </div>     
               </div> 
               
               <div id="instruction" class='workbench'>
               <h3>Instruction:
               </h3>
               <p>
               You can create a tile image or visualize markups on top of the tile image by selecting a region in the image. The region has a limit on area due to
               the memory constraint.
               </p><br>
               <p>
               To create a tile image, select an image and then  select a rectangle region by mouse (left-click for a starting point, mouse-release for an ending point), and select "Tile Image." <br />
               </p><br>
               <p>
               To visualize markups in a region, select an image, select a method (NS-MORPH is by algorithm, TR-HUMAN is by human), select a region, and select "Get Markkup." <br />
               </p><br>
               <p>
               You can also choose a default operation and default output image type to avoid repeating selections.
               </p>

               </div>
               <div id="imageloader" class='workbench'>
               </div>
               
               
			   </div>   
			</div>
			<br/>
            <!-- div id="tiledImageviewer" class="viewer"></div> -->
        <div id="iviewer">
            <div class="loader"></div>
            <div class="viewer"></div>

            <ul class="controls">
            <li class="close"></li>
            <li class="zoomin"></li>
            <li class="zoomout"></li>
           </ul>
        </div>
		
		</div>
		<div id='methodInfodiv' style="display:none;position:absolute;">
                    <div id='methodInfo'>
                    </div>
				</div>  
	</div>
</body>
</html>