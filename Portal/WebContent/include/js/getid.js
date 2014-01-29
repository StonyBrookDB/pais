
function getimgid( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}

function dl_Ft(imgid,type){
	window.open ("http://europa.cci.emory.edu/tcga/pais/features/aggregation/document;paisuid="+imgid+";format="+type,"JSON","status=1");
}