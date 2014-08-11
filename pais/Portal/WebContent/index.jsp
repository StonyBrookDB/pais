<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<link href="include/css/navstyle.css" rel="stylesheet" type="text/css"/>
<style>
.navframe{
width:1100px;
}

</style>
<script src="include/js/jquery-1.7.2.min.js"></script>
<script>
$(document).ready(function() {
	$('.navframe').hide();	
	$('.jumppage').click(function(){
		window.open($(this).attr('href'));
		return false;
	});
	
	var formerframeid='#indexframe';
	$('.nav').click(function(){
		
		$('.nav').removeClass('selectednav');
		var relid=$(this).attr('relid');
		$('#subtitle').text($(this).attr('title'));
		if($(relid).attr('src')==''||relid==formerframeid)
		{
			$(relid).attr('src','');
			$(relid).attr('src',$(this).attr('href'));
		}
		$(formerframeid).fadeOut(300);
		formerframeid=relid;
		$(relid).fadeIn(200);
		$(this).addClass('selectednav');
		return false;
	});

	$('#indexnav').click();
	
});


</script>
<title>PAIS</title>
</head>
<body>
	<div id="framecontainer">
		<jsp:include page="include/header.html"></jsp:include>
		<div class="menu">
			<ul>
			    <li><a href=home.jsp relid="#indexframe" title='Home Page' class="nav" id='indexnav'>Home</a></li>
				<li><a href=imagebrowser.jsp relid="#imagebrowserframe" title='Image Browser' class="nav">Image</a></li>
				<li><a href=listpt.jsp relid="#listpframe" title='View by Patient' class="nav">Patient</a></li>
				<li><a href=tiledImage.jsp relid="#tiledImageframe" title='Tiling Image'class="nav">Tiling</a></li>
				<li><a href=https://web.cci.emory.edu/confluence/display/PAIS class='jumppage'>PAIS Wiki</a></li>
				<li><a href=http://fushengwang.net class='jumppage'>Contact Us</a></li>
			</ul>
        </div>
        <h2 id='subtitle'></h2>	
        <iframe id="indexframe" frameborder="0" src="" style='overflow-y:auto;overflow-x:hidden' class='navframe' height=600px></iframe>
        <iframe id="imagebrowserframe"  frameborder="0" src="" style='overflow-y:auto;overflow-x:hidden' class='navframe' height=1200px></iframe>
        <iframe id="listpframe" frameborder="0" src="" style='overflow-y:auto;overflow-x:hidden' class='navframe' height=1450px></iframe>
        <iframe id="tiledImageframe" frameborder="0" src="" style='overflow-y:auto;overflow-x:hidden' class='navframe' height=1000px></iframe>
	<jsp:include page="include/footer.html"></jsp:include>
	</div>
	
</body>

</html>