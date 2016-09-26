/**
 * http://usejsdoc.org/
 */

var http = require("http")
var url = require("url")

/*
 * Start the server 
 * 
 */
function start(route,handles)
{
	function onRequest(request,response)
	{
		var pathname = url.parse(request.url).pathname;
		console.log("Request for "+pathname+" received.")
		var parameter=""
		route(handles,pathname,request,response,parameter);
	}
	http.createServer(onRequest).listen(8888);
	console.log("Server has started")
	//console.log("handles are:-"+ handle()['http://localhost:2345/audit'])
}

exports.start=start;