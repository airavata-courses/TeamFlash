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
	content=fs.readFileSync(__dirname +'/public/'+pathname,'utf-8',read);
	if(response!=null && !response.headersSent)
	{
	response.writeHead(200, {"content-type" : "text/css"});
	}
	if(response!=null && !response.finished)
	{
		response.write(content);
		response.end();
	}
}

function JS(pathname,request,response)
{
	console.log("JavaScript request handler"+pathname);
	content=fs.readFileSync(__dirname +'/public/'+pathname,'utf-8',read);
	if(response!=null && !response.headersSent)
	{
		response.writeHead(200, {"content-type" : "text/javascript"});
	}
	if(response!=null && !response.finished)
	{
		response.write(content);
		response.end();
	}
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
	chunk=chunk.toString();
	console.log("in print output");
	/*if(chunk.indexOf("html")>0)
	{
		chunk='No'
	var flag='<label id="output">'
    var len= flag.length
    index=content.indexOf(flag)
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
	}
	else{*/
	var flag='<label id="output">'
    var len= flag.length
    index=content.indexOf(flag)
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
    //console.log(output)
	//}
    return output
}

function addKML(content,chunk)
{
	chunk=chunk.toString();
	console.log("KML add");
	var flag='<label id="map">'
    var len= flag.length
    index=content.indexOf(flag)
    console.log("index is :"+index)
    console.log("length is :"+len)
	chunk='<iframe src="'+chunk+'" name="iframe_a" width= "100%" height="580px"></iframe>';
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
    var chunk = '<input type="hidden" id="username" name="username" value="'+username+'" />';
	chunk= chunk + '<input type="hidden" id="id" name="id" value="'+id+'" />';
    console.log("index is :"+index)
    console.log("length is :"+len)
    var output=content.substring(0,index+len) + chunk + content.substring(index+len);
    //console.log(output)
    return output
}

function login(handles,url,request,response,parameter)
{
	console.log("start of request handler"+url);
	content=fs.readFileSync(__dirname +'/public/login.html','utf-8',read);
	if(response!=null && !response.headersSent)
	{
		response.writeHead(200, {"content-type" : "text/html"});
	}
	if(response!=null && !response.finished)
	{
		response.write(content);
		response.end();
	}
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
            console.log("username :"+post['username'])
            console.log("password :"+post['password'])
            // use POST
            endpoint="?username="+post['username'];
        	var data=fs.readFileSync(__dirname +'/DB_Properties','utf-8',read);
        	var map=parse(data)
            mongo.authenticate(map["HOST"],map["PORT"],map["DB"],post['username'],post['password'],handles,request,response,endpoint);
        	
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
    var map=splitURL(parameter+"&id="+id)
	request.session.user=map["username"]
	var indexHtml=getIndexHtml()
    var output=addHiddenParameter(indexHtml,map["username"],map["id"]);
	var endpoint1 ='?username='+ map["username"] +'&id=' +map["id"]
	if(response!=null && !response.headersSent)
	{
	response.writeHead(200, {"content-type" : "text/html"});
	}
	if(response!=null && !response.finished)
	{
		response.write(output);
		response.end();
		//router.route(handles,"/pollJobs",request,response,parameter+endpoint1+"&status="+false)
	}
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
	var map=splitURL(parameter)
	var username=map["username"];
	var id=map["id"];
	if(response!=null && !response.headersSent)
	{
	response.writeHead(200, {"content-type" : "text/html"});
	}
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  indexHtml=getIndexHtml()
			  output=addHiddenParameter(indexHtml,username,id);
			  table=createTableAudit(chunk);
			  output=printOutput(output,table);
			  //console.log("Got response: " + chunk);
			  if(response!=null && !response.finished)
				  { 
		  			response.write(output);
			  		response.end();
				  }
		  });
		}).on("error", function(e){
			indexHtml=getIndexHtml()
			output=addHiddenParameter(indexHtml,username,id)
			output=printOutput(output,"Unable to connect to Registry Database");
		  console.log("Got error: " + e.message);
		  if(response!=null && !response.finished)
				  {
		  			response.write(output);
			  		response.end();
				  }
		});
}

function dataIngestor(handles,url,request,response,parameter)
{
	console.log("data ingestor of request handler"+url+parameter);
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
			try{
        	var post = qs.parse(body)
			}
			catch (e) {
				   var err = new Error(e.message);
				   console.log(e.message);
    			   throw err;
  				}
        	var date=post['date']
        	var station=post['station']
        	var time=post['time']
        	var username=post['username']
        	var id=post['id']
			
            console.log("date :"+date)
            console.log("station :"+station)
            console.log("time :"+time)
            console.log("id :"+post['id'])

			d= new Date(date); 
			date=d.getFullYear()+"-"+("0"+(d.getMonth()+1)).slice(-2) + "-" +("0" + d.getDate()).slice(-2)
			console.log("date is :"+date)

        	var endpoint1 ='?username='+ username +'&id=' +id+'&date='+ date +'&station=' +station+ '&time='+ time
        	var endpoint2 ='?username='+ username +'&id=' +id+'&url='
        	console.log("data ingestor of request handler"+url+endpoint1);
			http.get(url+endpoint1, function(resp){
        	resp.on('data', function(chunk){
				console.log("kml is :-"+chunk)
        	  endpoint2=endpoint2+chunk
			  console.log("username :"+username);
        	  createLog(handles,request,response,endpoint1,'Data Ingestor');
				chunk=chunk.toString();
			  if(chunk.indexOf("html")>0)
			  {
				  if(response!=null && !response.headersSent)
				{
		  			response.writeHead(200, {"content-type" : "text/html"});
				}
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Data Ingestor");
        		//console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
			  }
			  else{
        	  router.route(handles,"/stormDetector",request,response,endpoint2);
			  }
        		});
        	}).on("error", function(e){
				if(response!=null && !response.headersSent)
				{
					response.writeHead(200, {"content-type" : "text/html"});
				}
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Data Ingestor");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
        	});
        });
	}
}


/* Some mistake here */
function stormDetector(handles,url,request,response,parameter)
{
	var body = [];
	var count=0;
	console.log("storm detector of request handler"+url+parameter);
	var map=splitURL(parameter)
	var username=map["username"];
	var id=map["id"];
		http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  count++;
			  if(count<=1)
			  {
				  console.log("username :"+username);
			  	createLog(handles,request,response,parameter,'Storm Detector')
			  	router.route(handles,"/stormCluster",request,response,parameter)
			  }
		  });
		}).on("error", function(e){
				if(response!=null && !response.headersSent)
				{
					response.writeHead(200, {"content-type" : "text/html"});
				}
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Storm Detector");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
		});
}

function stormCluster(handles,url,request,response,parameter)
{
	var body = [];
	var count=0;
		console.log("storm cluster of request handler"+url+parameter)
		var map=splitURL(parameter)
		var username=map["username"];
		var id=map["id"];
		http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  count++;
			  indexHtml=getIndexHtml()
			  output=addHiddenParameter(indexHtml,username,id)
			  output=printOutput(output,chunk)
			  
			  if(count<=1)
			  {
				  console.log("username :"+username);
			  createLog(handles,request,response,parameter,'Storm Cluster')
			  var rand=randomIntInc(0,1)
			  /*
			  if(rand==0)
				  router.route(handles,"/stormTrigger",request,response,parameter+'&value='+true)
			  else
				  router.route(handles,"/stormTrigger",request,response,parameter+'&value='+false)
			*/
			 router.route(handles,"/stormTrigger",request,response,parameter+'&value='+true) 
			}
			  
		  });
		}).on("error", function(e){
				if(response!=null && !response.headersSent)
				{
		  			response.writeHead(200, {"content-type" : "text/html"});
				}
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Storm Cluster");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
		});
}

function forecastTrigger(handles,url,request,response,parameter)
{
	var body = [];
	var count=0;
		console.log("forecast trigger of request handler"+url+parameter)
		var map=splitURL(parameter)
		var username=map["username"];
		var id=map["id"];
		http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  count++;
			   try {
				  console.log("chunk: "+chunk)
			  output=JSON.parse(chunk)
			  indexHtml=getIndexHtml()
				  final_output=addHiddenParameter(indexHtml,username,id)
				  final_output=printOutput(final_output,output.message)
			  //output.message='Yes'
			  }
			  catch (e) {
				   output=chunk
				   indexHtml=getIndexHtml()
				  final_output=addHiddenParameter(indexHtml,username,id)
				  final_output=printOutput(final_output,output)
  				}
			  /*if(chunk==null)
			  {
				  output.message='No';
			  }
			  else
			  {
			  try {
				  console.log("chunk: "+chunk)
			  output=JSON.parse(chunk)
			  //output.message='Yes'
			  }
			  catch (e) {
				   chunk='{"message": "No"}'
				   output=JSON.parse(chunk)
  				}
			  }*/
			  console.log("Got response: " + output.message);
			  if(count<=1)
			  {
				  console.log("username :"+username);
			  createLog(handles,request,response,parameter,'Forecast Trigger')
			  if(output.message=='Yes')
				  {
					console.log("forecast trigger if statement");
				  	//router.route(handles,"/weatherForecast",request,response,parameter+'&location=bloomington')
					  router.route(handles,"/insertJob",request,response,parameter+'&location=bloomington')
				}
			  else
				  {
					  console.log("forecast trigger else statement");
					  if(response!=null && !response.headersSent)
						{
							response.writeHead(200, {"content-type" : "text/html"});
						}
				  
				  if(response!=null && !response.finished)
				  {
				  	response.write(final_output);
				  	response.end();
				  }
				  }
			  }
		  });
		}).on("error", function(e){
				if(response!=null && !response.headersSent)
				{
		  			response.writeHead(200, {"content-type" : "text/html"});
				}
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Forecast Trigger");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
		});
}

function predictWeatherforecast(handles,url,request,response,parameter)
{
	console.log('<- in predictWeatherforecast-> '+url+parameter); 
	var body = [];
	var count=0;
	var map=splitURL(parameter)
	var username=map["username"];
	var id=map["id"];
	var job_id=map["jobId"];
	if(response!=null && !response.headersSent)
	{
	response.writeHead(200, {"content-type" : "text/html"});
	}
	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			  indexHtml=getIndexHtml()
			  output=addHiddenParameter(indexHtml,username,id)
			  output=printOutput(output,"Inserted Job ID is :"+job_id)
			  if(response!=null && !response.finished)
			  {
			  	response.write(output);
			  	response.end();
			  }
		  });
		}).on("error", function(e){
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to connect to Predict Weather Forecast");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
		});
		  
}

function getImage(handles,url,request,response,parameter)
{
	var open = require('open');
	var request = require('request'); // include request module
	var popup = require('window-popup').windowPopup;
	request('http://54.215.219.32:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif', function (err, resp) {
   	if (resp.statusCode === 200) {
      		//open('http://54.215.219.32:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif')
			  popup(500, 500, 'http://54.215.219.32:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif', 'image');
   		}
						   
	else{
			//open('http://52.53.179.0:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif')
			popup(500, 500, 'http://52.53.179.0:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif', 'image');
		}
	});
}

function createJobList(job_data_json)
{
	console.log('JSON from createJobList-> '+job_data_json);
	var table = '<form method="post" id="ResubmitForm" name="ResubmitForm" action="/resubmit" >'
	table = table+"<table style='width:100%'>";
	table=table + "<tr><td></td><td>Job ID</td><td>Task Status</td></tr>"
		var job_array = [];
		//console.log(output[0])
		count=0
		for(var i=0; i< job_data_json.length; i++)
		{
			var jobs=job_data_json[i]
			for(var j=0; j< jobs.length; j++)
			{
				count++;
				job=jobs[j]
				job_array.push(job);
				server=job.taskServer.toString()
				index=server.indexOf('1')
				table=table+"<tr>";
				table=table+"<td><input type='radio' name='job' value='"+job.jobid+"' id='job'/></td>";
				//table = table + "<td><a id='auditButton' href='/getImage?img_id="+job.taskid+"'>"+job.jobid+"</a></td><td>"+job.taskStatus+"</td>";
				if(index>0)
				popup='"http://52.53.179.0:1338/download/'+job.taskid+'/wrfoutput/Precip_total.gif","image", "width=500,height=500"'
				else
				popup='"http://54.215.219.32:1338/download/'+job.taskid+'/wrfoutput/Precip_total.gif","image", "width=500,height=500"'

				table = table + "<td><a id='auditButton' href='javascript:window.open("+popup+");'"+ ">"+job.jobid+"</a></td><td>"+job.taskStatus+"</td>";
				//"javascript:window.open('some.html', 'yourWindowName', 'width=200,height=150');"  "image", "width=200,height=150");'
				//taskServer
				table=table+"</tr>";
			}
		}
		table = table + "</table>";
		table = table +"</form>";
	return table;
}

function insertJob(handles,url,request,response,parameter)
{
	console.log('<- in insert Jobs-> '+url+parameter); 

	http.get(url+parameter, function(resp){
		  resp.on('data', function(chunk){
			   console.log('<- JOB ID-> '+chunk); 
			  router.route(handles,"/weatherForecast",request,response,parameter+'&jobId='+chunk)
		}).on("error", function(e){
        		console.log("Got error in insertJob: " + e.message);
		});
		});

}

function pollJobs(handles,url,request,response,parameter)
{
	console.log('<- in poll Jobs-> ');  
	var map=splitURL(parameter)
	var username=map["username"];
	var id=map["id"];
	console.log('<- in poll Jobs-> '+url+parameter); 
		http.get(url+parameter, function(resp){
		resp.on('data', function(chunk){
		indexHtml=getIndexHtml()
		output=addHiddenParameter(indexHtml,username,id)
		//console.log('data from poll data-> '+chunk);
		try{
		json_data=JSON.parse(chunk)
		}
		catch(e)
		{
			console.log('error while parsing json poll data-> '+e.message);
		}
		table=createJobList(json_data)
		output=printOutput(output,table)
		console.log("username :"+username);
		console.log("output :"+output);
		if(response!=null && !response.finished)
		{
			console.log("print response for HTML start:");
					response.write(output);
			  		response.end();
					  console.log("print response for HTML end:");
		}
		  });
		}).on("error", function(e){
				indexHtml=getIndexHtml()
				output=addHiddenParameter(indexHtml,username,id)
				output=printOutput(output,"Unable to poll data");
        		console.log("Got error: " + e.message);
				if(response!=null && !response.finished)
				  {
					response.write(output);
			  		response.end();
				  }
		});
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
exports.pollJobs=pollJobs;
exports.insertJob=insertJob
exports.CSS=CSS;
exports.JS=JS;
