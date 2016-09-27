/**
 * http://usejsdoc.org/
 */

function route(handles,pathname,request,response,parameter)
{
	console.log("About to route a request for "+pathname);
	var fn=handles()[0]
	var url=handles()[1]
	var str = ''+pathname
	if(typeof fn[pathname] === 'function')
		{
			fn[pathname](handles,url[pathname],request,response,parameter);
		}
	else
		{
		if(str.indexOf('.css') > -1)
			{
				fn['.css'](pathname,request,response)
			}
		else if(str.indexOf('.js') > -1)
			{
				fn['.js'](pathname,request,response)
			}
		else
			{
			response.writeHead(404, {"content-type" : "text/plain"});
			response.write("404 NOT FOUND");
			response.end();
			console.log("No Request handler found for "+ pathname);
			}
		}
}

exports.route=route;
