<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Image Viewer</title>
    <link href="include/css/style.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        #container {
            width: auto;
            height: 600px;
            background-color: black;
            border: 1px solid black;
            color: white;   /* for error messages */
        }
    </style>
    <script src="include/js/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="include/openseadragon/openseadragon.min.js" type="text/javascript"></script>
    <script src="include/js/main.js" type="text/javascript"></script>
    <script type="text/javascript">
        var viewer;
        var img = getarg('img');
        //var img = "TCGA-02-0001-01Z-00-DX3";
        var fullscreen = getarg('fullscreen'); 

        $(document).ready(function(){
        	var viewer = OpenSeadragon({
                //debugMode: true,
                id: "container",
                prefixUrl: "include/openseadragon/images/",
                tileSources: "/DZImage/"+img+"/image.js",
                showNavigator:true
            });
        	if(fullscreen=='true')
        	{
        		viewer.setFullPage('fullPage');
        	}
        });//end ready      
        
    </script>
</head>

<body>
<div id="content">    
    <div id="container">
    </div>  
</div>
</body>

</html>