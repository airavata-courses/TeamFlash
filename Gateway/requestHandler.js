/**
 * http://usejsdoc.org/
 */
var fs = require("fs")
var http = require("http")
var qs = require("querystring")
var mongo = require("./mongo.js")
var router = require("./router.js") 
var uuid = require('node-uuid')

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

function createLog(handles,request,response,endpoint,microservice)
{
    router.route(handles,"/registry",request,response,endpoint+'&msvc='+microservice)
}

function login(handles,url,request,response,parameter)
{
	console.log("start of request handler"+url);
	content=fs.readFileSync(__dirname +'/login.html','utf-8',read);
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function authenticate(handles,url,request,response,parameter)
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
            endpoint="?username="+post['login'];
            mongo.authenticate('localhost','27017','LDAP',post['login'],post['password'],handles,request,response,endpoint);
        	//var id=uuid.v4();
        	//console.log("Unique id is :"+id)
        	//endpoint="?username="+post['login']+"&id="+id+"&msvc='Gateway'";
        	//router.route(handles,"/registry",request,response,endpoint)
        	//createLog(handles,request,response,'',post['login'],id,'Gateway')
        	//endpoint="?username="+post['login']+"&id="+id
        	//router.route(handles,"/Gateway",request,response,endpoint);
        });
    }
}

function gateway(handles,url,request,response,parameter)
{
	console.log("start of request handler"+url)
	var id=uuid.v4();
    console.log("Unique id is :"+id)
    createLog(handles,request,response,parameter+"&id="+id,'Gateway')
	content=fs.readFileSync(__dirname +'/index.html','utf-8',read)
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(content);
	response.end();
}

function registry(handles,url,request,response,parameter)
{
	console.log("audit of request handler"+url+parameter);
	/*var options = {
			  host: 'localhost',
			  port: 2345,
			  path: '/audit'
			};*/
	//response.writeHead(200, {"content-type" : "text/html"});
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  console.log("Got response: " + chunk);
			  //response.write(chunk);
			  //response.end();
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function dataIngestor(handles,url,request,response,parameter)
{
	console.log("data ingestor of request handler"+url);
	var body = [];
	if (request.method == 'POST') {
		request.on('data', function (data) {
        	body.push(data);
            if (body.length > 1e6) {
                request.connection.destroy();
            }
        });
        request.on('end', function () {

        	body = Buffer.concat(body).toString();
        	var post = qs.parse(body)
        	var date=post['date']
        	var station=post['station']
        	var time=post['time']
            console.log("date :"+date)
            console.log("station :"+station)
            console.log("time :"+time)
        	var endpoint1 ='?'+'date='+ date +'&station=' +station+ '&time='+ time
        	var endpoint2 ='?url='
            //response.writeHead(200, {"content-type" : "text/html"});
        	http.get(url+endpoint1, function(resp){
        	resp.on('data', function(chunk){
        	  //console.log("Got response: " + chunk);
        	  //nextrad_URL=printOutput(chunk)
        	  endpoint2=endpoint2+chunk
        	  router.route(handles,"/stormDetector",request,response,endpoint2)
        	  //stormDetector(handles,url+endpoint2,request,response)
        	  //response.write(output);
        	  //response.end();
        		});
        	}).on("error", function(e){
        		console.log("Got error: " + e.message);
        	});
        });
	}
}

function printOutput(chunk)
{
	content=fs.readFileSync(__dirname +'/index.html','utf-8',read)
	var flag='<label id="output">'
    var len= flag.length
    index=content.indexOf(flag)
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
    //console.log(output)
    return output
}


function stormDetector(handles,url,request,response,parameter)
{
	console.log("storm detector of request handler"+url)
	response.writeHead(200, {"content-type" : "text/html"});
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  //console.log("Got response: " + chunk);
			  kml=printOutput(chunk)
			  response.write(kml);
			  response.end();
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function stormCluster(handles,url,request,response,parameter)
{
	console.log("storm cluster of request handler"+url)
}

function forecastTrigger(handles,url,request,response,parameter)
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
