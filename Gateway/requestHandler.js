/**
 * http://usejsdoc.org/
 */
/* Require "auth" service for authenticating users and getting profile info */
var config = require('./config');
var auth = require('./auth')(config);


var fs = require("fs")
var http = require('http');
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

function randomIntInc (low, high) {
    return Math.floor(Math.random() * (high - low + 1) + low);
}

function parse(data)
{
	var map={}
	var lines=data.trim().split('\n')
	for (var i=0;i<lines.length;i++)
		{
			var line=lines[i].split('=');
			var key=(line[0]+'').trim();
			var value=(line[1]+'').trim();
			map[key]=value
		}
	return map
}

function createLog(handles,request,response,endpoint,microservice)
{
    router.route(handles,"/registry",request,response,endpoint+'&msvc='+microservice)
}

function splitURL(str)
{
	var map = {};
	var arr=str.split("?")
	var queryString=arr[1];
	var parameters=queryString.split("&")
	for(var i=0;i<parameters.length;i++) {
		var param=parameters[i].split("=")
		map[param[0]] = param[1];
	}
	return map
}

function printOutput(content,chunk)
{
	console.log("in print output");
	var flag='<label id="output">'
    var len= flag.length
    index=content.indexOf(flag)
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
    //console.log(output)
    return output
}

function getIndexHtml()
{
	content=fs.readFileSync(__dirname +'/public/index.html','utf-8',read)
	return content
}

function addHiddenParameter(content,username,id)
{
	console.log(username+"-----in hidden------>"+id);
	var flag='<form method="post" id="Form1" name="Form1" action="/dataIngestor" >'
    var len= flag.length
    index=content.indexOf(flag)
    var chunk = '<input type="hidden" id="username" name="username" value='+username+' />';
	chunk= chunk + '<input type="hidden" id="id" name="id" value='+id+' />';
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
    //console.log(output)
    return output
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
        	var data=fs.readFileSync(__dirname +'/DB_Properties','utf-8',read);
        	var map=parse(data)
            mongo.authenticate(map["HOST"],map["PORT"],map["DB"],post['login'],post['password'],handles,request,response,endpoint);
        	
        });
    }
}

function gateway(handles,url,request,response,parameter)
{
	console.log("start of request handler"+url)
	var id=uuid.v4();
	request.session.id=id;
    console.log("Unique id is :"+id)
    createLog(handles,request,response,parameter+"&id="+id,'Gateway')
	//content=fs.readFileSync(__dirname +'/index.html','utf-8',read)
    var map=splitURL(parameter+"&id="+id)
	var indexHtml=getIndexHtml()
    var output=addHiddenParameter(indexHtml,map["username"],map["id"])
	response.writeHead(200, {"content-type" : "text/html"});
	response.write(output);
	response.end();
}

function registry(handles,url,request,response,parameter)
{
	console.log("registry of request handler"+url+parameter);
	
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  console.log("Got response: " + chunk);
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function createTableAudit(user_data)
{
	console.log("in table");
	var userArr = (user_data+"").split(';');
	var table = "<table style='width:100%'>";
	table=table + "<tr><th>Request ID</th><th>User Name</th><th>Date Created</th><th>Description</th><th>Microservice</th></tr>"
	for (var i=0; i< userArr.length; i++) {
		var userValues = userArr[i].split(',');
		table=table+"<tr>";
		for(var j=0;j<userValues.length;j++)
		{
    		table = table + "<td>"+ userValues[j]+"</td>"; 
		}
		table=table+"</tr>";
	}
	table = table + "</table>";
	return table;
}
function fetch(handles,url,request,response,parameter)
{
	console.log("audit of request handler"+url+parameter);
	console.log("boom boom:- "+request.session.id);
	response.writeHead(200, {"content-type" : "text/html"});
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  indexHtml=getIndexHtml()
			  output=addHiddenParameter(indexHtml,request.query.username,request.session.id)
			  table=createTableAudit(chunk);
			  output=printOutput(output,table);
			  //console.log("Got response: " + chunk);
			  response.write(output);
			  response.end();
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
        	var username=post['username']
        	var id=post['id']
            console.log("date :"+date)
            console.log("station :"+station)
            console.log("time :"+time)
            console.log("id :"+post['id'])
        	var endpoint1 ='?username='+ username +'&id=' +id+'&date='+ date +'&station=' +station+ '&time='+ time
        	var endpoint2 ='?username='+ username +'&id=' +id+'&url='
        	http.get(url+endpoint1, function(resp){
        	resp.on('data', function(chunk){
        	  endpoint2=endpoint2+chunk
        	  createLog(handles,request,response,endpoint1,'Data Ingestor')
        	  router.route(handles,"/stormDetector",request,response,endpoint2);
        		});
        	}).on("error", function(e){
        		console.log("Got error: " + e.message);
        	});
        });
	}
}

function stormDetector(handles,url,request,response,parameter)
{
	console.log("storm detector of request handler"+url)
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  
			  indexHtml=getIndexHtml()
			  kml=printOutput(indexHtml,chunk)
			  createLog(handles,request,response,parameter,'Storm Detector')
			  router.route(handles,"/stormCluster",request,response,parameter)
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function stormCluster(handles,url,request,response,parameter)
{
	console.log("storm cluster of request handler"+url)
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  indexHtml=getIndexHtml()
			  output=printOutput(indexHtml,chunk)
			  createLog(handles,request,response,parameter,'Storm Cluster')
			  var rand=randomIntInc(0,1)
			  if(rand==0)
				  router.route(handles,"/stormTrigger",request,response,parameter+'&value='+true)
			  else
				  router.route(handles,"/stormTrigger",request,response,parameter+'&value='+false)
			  
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function forecastTrigger(handles,url,request,response,parameter)
{
	console.log("forecast trigger of request handler"+url+parameter)
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  output=JSON.parse(chunk)
			  console.log("Got response: " + output.message);
			  createLog(handles,request,response,parameter,'Forecast Trigger')
			  if(output.message=='Yes')
				  {
					  console.log("forecast trigger if statement");
				  	router.route(handles,"/weatherForecast",request,response,parameter+'&location=bloomington')
				  }
			  else
				  {
					  console.log("forecast trigger else statement");
					  if(response!=null && !response.isCommitted())
					  {
				  response.writeHead(200, {"content-type" : "text/html"});
				  indexHtml=getIndexHtml()
				  var final_output=printOutput(indexHtml,output.message)
				  response.write(final_output);
				  response.end();
					  }
				  }
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message);
		});
}

function predictWeatherforecast(handles,url,request,response,parameter)
{
	console.log("predict weather forecast of request handler"+url+parameter)
	if(response!=null && !response.isCommitted())
	{
	response.writeHead(200, {"content-type" : "text/html"});
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  
			  indexHtml=getIndexHtml()
			  output=printOutput(indexHtml,chunk)
			  createLog(handles,request,response,parameter,'Weather Forecast')
			  response.write(output);
			  response.end();
		  });
		}).on("error", function(e){
		  console.log("Got error: " + e.message); 
		});
	}
}

exports.login=login;
exports.authenticate=authenticate;
exports.fetch=fetch;
exports.gateway=gateway;
exports.registry=registry;
exports.dataIngestor=dataIngestor;
exports.stormDetector=stormDetector;
exports.stormCluster=stormCluster;
exports.forecastTrigger=forecastTrigger;
exports.predictWeatherforecast=predictWeatherforecast;
exports.CSS=CSS;
exports.JS=JS;
