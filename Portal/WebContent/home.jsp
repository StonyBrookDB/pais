<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="include/css/style.css" rel="stylesheet" type="text/css" />
<style>
.count{
font-size:20px;
color:#6600CC
}
</style>
<script src="include/js/jquery-1.7.2.min.js"></script>
<script>
var url="/tcga/pais/list/summary";
$.get(url,function(data){
	var infos=data.documentElement.getElementsByTagName("row");
	for(var i=0;i<infos.length;i++)
	{
		$('#'+infos[i].attributes[0].nodeName).text(infos[i].attributes[0].nodeValue);
	}
	
});

</script>
<title>Home</title>
</head>
<body>
<p>
Welcome to the portal of pathology analytical imaging. 
<br>
<br>
The database has <a class='count' id="images"></a> whole slide images from <a class='count' id="patients"></a> patients.<br> 
There are <a class='count' id="algorithm"></a> analytical result documents, and <a class='count' id="human"></a> human annotated documents on these images.
<br>
<br>
You can browse, visualize, and download images and annotations (including markups and features) from the portal. <br>
<br>
From "Image"", you can browse images by thumbnails, download images, and visualize mean features of the images. You can also download tiled images and their markups and features. <br>
<br>
From "Patient", you can browse patient information and browse images of a patient. <br>
<br>
From "Tiling", you can dynamically generate a tile, and visualize the result on top of a tile. <br>
</p>
</body>
</html>