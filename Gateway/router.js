/**
 * http://usejsdoc.org/
 */

function route(handles,pathname,response)
{
	console.log("About to route a request for "+pathname);
	var fn=handles()[0]
	var url=handles()[1]
	
	if(typeof fn[pathname] === 'function')
		{
			fn[pathname](url[pathname],response);
		}
	else
		{
		if((''+pathname).includes('.css'))
			{
				fn['.css'](pathname,response)
			}
		else if((''+pathname).includes('.js'))
			{
				fn['.js'](pathname,response)
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
