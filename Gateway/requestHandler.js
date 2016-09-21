/**
 * http://usejsdoc.org/
 */
var fs = require("fs")
var http = require("http")

function CSS(pathname,response)
{
	console.log("CSS request handler"+pathname)
	content=fs.readFileSync(__dirname +'/'+pathname,'utf-8',read)
	response.writeHead(200, {"content-type" : "text/css"});
	response.write(content);
	response.end();
}

function JS(pathname,response)
{
	console.log("JavaScript request handler"+pathname)
	content=fs.readFileSync(__dirname +'/'+pathname,'utf-8',read)
	response.writeHead(200, {"content-type" : "text/javascript"});
	response.write(content);
	response.end();
}

function read(err,data)
{
	if(err)
		{
			return err;
		}
	
	return data;
}

function login(url,response)
{
	console.log("start of request handler"+url)
	content=fs.readFileSync(__dirname +'/login.html','utf-8',read)
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function authenticate(url,response)
{
	console.log("authenticate user :"+url)
	
}

function gateway(url,response)
{
	console.log("start of request handler"+url)
	content=fs.readFileSync(__dirname +'/index.html','utf-8',read)
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function registry(url,response)
{
	console.log("audit of request handler"+url);
	/*var options = {
			  host: 'localhost',
			  port: 2345,
			  path: '/audit'
			};*/
	response.writeHead(200, {"content-type" : "text/html"});
	http.get(url, function(resp){
		  resp.on('data', function(chunk){
			  console.log("Got response: " + chunk);
			  response.write(chunk);
			  response.end();
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function dataIngestor(url,response)
{
	console.log("data ingestor of request handler"+url);
	/*var options = {
	  host: 'localhost',
	  port: 5679,
	  path: '/data'
	};*/
	response.writeHead(200, {"content-type" : "text/html"});
	http.get(url, function(resp){
		resp.on('data', function(chunk){
	  console.log("Got response: " + chunk);
	  response.write(chunk);
	  response.end();
		});
	}).on("error", function(e){
		console.log("Got error: " + e.message);
	});
}

function stormDetector(url,response)
{
	console.log("storm detector of request handler"+url)
}

function stormCluster(url,response)
{
	console.log("storm cluster of request handler"+url)
}

function forecastTrigger(url,response)
{
	console.log("forecast trigger of request handler"+url)
}

exports.login=login;
exports.authenticate=authenticate;
exports.gateway=gateway;
exports.registry=registry;
exports.dataIngestor=dataIngestor;
exports.stormDetector=stormDetector;
exports.stormCluster=stormCluster;
exports.forecastTrigger=forecastTrigger;
exports.CSS=CSS;
exports.JS=JS;
