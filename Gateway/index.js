/**
 * http://usejsdoc.org/
 */

/* Modules */
var fs = require("fs")
var http = require('follow-redirects').http;
var qs = require("querystring")
var mongo = require("./mongo.js")
var router = require("./router") 
var uuid = require('node-uuid')
var requestHandler=require("./requestHandler")
var updateURL=require("./updateURL")


var path    = require("path");

/* Require shared configuration variables, eg. our Google Project ID */
var config = require('./config');


/* Require "auth" service for authenticating users and getting profile info */
var auth = require('./auth')(config);

/* Require Express web framework and Express middleware */
var express = require('express');
var session = require('cookie-session');

/* Configure Express web application */
var app = express();
app.use(express.static(__dirname + '/public'));
app.enable('trust proxy');

app.use(session({ signed: true, secret: config.cookieSecret }));

app.get('/login', function(req, res) {
  res.sendFile(__dirname +'/public/login.html');
});


/* Generic operations */

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

/* Redirect user to OAuth 2.0 login URL */
app.get('/googleAuthenticate', function(req, res) {
  var authenticationUrl = auth.getAuthenticationUrl();
  console.log("in google authentication")
  res.redirect(authenticationUrl);
});

/* Use OAuth 2.0 authorization code to fetch user's profile */
app.get('/oauth2callback', function(req, res, next) {
  auth.getUser(req.query.code, function(err, user) {
    if (err) return next(err);
    req.session.user = user;
    endpoint="?username="+user.name.replace(/\s+/g, '');
    router.route(updateURL.update,"/Gateway",req,res,endpoint);
  });
});

/* Clear the session */
app.get('/logout', function(request, response) {
  console.log("/logout");
  if(request.session.user==null)
    {
      username=request.query.username;
    }
    else{
      username=request.session.user;
    }
    if(request.session==null)
    {
      id=request.query.id
    }
    else{
        id=request.session.id
    }
    endpoint="?username="+username+"&id="+id+"&status="+true;
  router.route(updateURL.update,"/pollJobs",request,response,endpoint)
  request.session = null;
  request.redirect('/');
});

/* Go to Gateway*/
app.get('/Gateway', function(req, res) {
  console.log("/Gateway");
  if(req.session ==null)
  {
    res.redirect('/');
  }
  else{
    res.sendFile(__dirname +'/public/index.html');
  }
});

/* Mongo Authentication */
app.post('/authenticate', function(request, response) {
  console.log("/authenticate");
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
            mongo.authenticate(map["HOST"],map["PORT"],map["DB"],post['login'],post['password'],updateURL.update,request,response,endpoint);
        });
    }
});

/* fetch data from registry */
  app.get('/audit', function(request, response) {
    var username=null;
    var id=null;
    if(request.session.user==null)
    {
      username=request.query.username;
    }
    else{
      username=request.session.user;
    }
    if(request.session==null)
    {
      id=request.query.id
    }
    else{
        id=request.session.id
    }
    console.log("/audit username---->"+username);
    console.log("/audit id----->"+id);
    endpoint="?username="+username+"&id="+id;
    router.route(updateURL.update,"/fetch",request,response,endpoint);
  });

  /* Data ingestor module*/
app.post('/dataIngestor', function(request, response) {
    var username=null;
    var id=null;
  if(request.session.user==null)
    {
      username=request.query.username;
    }
    else{
      username=request.session.user.id;
    }
    if(request.session==null)
    {
      id=request.query.id
    }
    else{
        id=request.session.id
    }
    
    console.log("username :"+username)
    console.log("id :"+id)
    
    endpoint="?username="+username+"&id="+id;
    router.route(updateURL.update,"/dataIngestor",request,response,endpoint)
  });

   /* Resubmit Form*/
app.post('/resubmit', function(request, response) {
    var username=null;
    var id=null;
  if(request.session.user==null)
    {
      username=request.query.username;
    }
    else{
      username=request.session.user.id;
    }
    if(request.session==null)
    {
      id=request.query.id
    }
    else{
        id=request.session.id
    }

    job=request.query.job
    
    console.log("username :"+username)
    console.log("id :"+id)
    console.log("job :"+job)
    endpoint="?username="+username+"&id="+id+"&jobId="+job;
    router.route(updateURL.update,"/insertJob",request,response,endpoint)
  });

  /* image display module*/
  app.get('/getImage', function(request, response) {
  console.log("/getImage");
  console.log("task id is-->"+request.query.img_id);
  task_id=request.query.img_id.toString()
  var open = require('open');
	var request = require('request'); // include request module
	request('http://54.215.219.32:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif', function (err, resp) {
   	if (resp.statusCode === 200) {
      		open('http://54.215.219.32:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif')
   		}
						   
	else{
			open('http://52.53.179.0:1338/download/'+request.img_id+'/wrfoutput/Precip_total.gif')
		}
	});
});


/* Run web application */
app.listen(8888);
console.log('Express started on port ' + 8888); 
