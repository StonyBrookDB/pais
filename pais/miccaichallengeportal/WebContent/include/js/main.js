function getarg(param)
{
  var url = unescape(window.location.href);
  if(url==undefined)return undefined;
  var allargs = url.split("?")[1];
  if(allargs==undefined)return  undefined;
  var args = allargs.split(";");
  for(var i=0; i<args.length; i++)
  {
   var arg = args[i].split("=");
   if(arg[0]==param)
   {
	   return arg[1];
   }
  }
};