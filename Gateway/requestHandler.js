/**
 * http://usejsdoc.org/
 */
var fs = require("fs")
var http = require("http")
var qs = require("querystring")
var mongo = require("./mongo.js")

function CSS(pathname,request,response)
{
	console.log("CSS request handler"+pathname);
	content=fs.readFileSync(__dirname +'/'+pathname,'utf-8',read);
	response.writeHead(200, {"content-type" : "text/css"});
	response.write(content);
	response.end();
}

function JS(pathname,request,response)
{
	console.log("JavaScript request handler"+pathname);
	content=fs.readFileSync(__dirname +'/'+pathname,'utf-8',read);
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

function login(handles,url,request,response)
{
	console.log("start of request handler"+url);
	content=fs.readFileSync(__dirname +'/login.html','utf-8',read);
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function authenticate(handles,url,request,response)
{
	console.log("authenticate user :"+url)
	var body = [];
	if (request.method == 'POST') {
        request.on('data', function (data) {
        	body.push(data);
            // 1e6 === 1 * Math.pow(10, 6) === 1 * 1000000 ~~~ 1MB
            if (body.length > 1e6) { 
                // FLOOD ATTACK OR FAULTY CLIENT, NUKE REQUEST
                request.connection.destroy();
            }
        });
        request.on('end', function () {

        	body = Buffer.concat(body).toString();
        	var post = qs.parse(body)
            console.log("username :"+post['login'])
            console.log("password :"+post['password'])
            // use POST
            mongo.authenticate('localhost','27017','LDAP',post['login'],post['password'],request,response);
        });
    }
}

function gateway(handles,url,request,response)
{
	console.log("start of request handler"+url)
	content=fs.readFileSync(__dirname +'/index.html','utf-8',read)
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function registry(handles,url,request,response)
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

function dataIngestor(handles,url,request,response)
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

function stormDetector(handles,url,request,response)
{
	console.log("storm detector of request handler"+url)
}

function stormCluster(handles,url,request,response)
{
	console.log("storm cluster of request handler"+url)
}

function forecastTrigger(handles,url,request,response)
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
