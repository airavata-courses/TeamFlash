/**
 * http://usejsdoc.org/
 */
var fs= require("fs")
var requestHandler= require("./requestHandler")
var filePath='Routing_Properties'

function update()
{
	var functionHandle={}
	var urlHandle={}
	var data=fs.readFileSync(filePath,'utf-8',read);
	parse(data+'',functionHandle,urlHandle)
	//console.log(handle['http://localhost:2345/audit'])
	return [functionHandle,urlHandle]
}

function read(err,data)
{
	if(err)
		{
			console.log(err);
		}
	
	return data;
}

function parse(data,functionHandle,urlHandle)
{
	var lines=data.trim().split('\n')
	for (var i=0;i<lines.length;i++)
		{
			var url=lines[i].split('=');
			var route=(url[0]+'').trim();
			var url=(url[1]+'').trim();
			//console.log("in update url: "+route);
			switch (route) {
			case '/':
		    	functionHandle[route]=requestHandler.login;
		    	urlHandle[route]=url;
		        break;
			case '/login':
		    	functionHandle[route]=requestHandler.login;
		    	urlHandle[route]=url;
		        break;
			case '/authenticate':
		    	functionHandle[route]=requestHandler.authenticate;
		    	urlHandle[route]=url;
		        break;
		    case '/Gateway':
		    	functionHandle[route]=requestHandler.gateway;
		    	urlHandle[route]=url;
		        break;
		    case '/registry':
		    	functionHandle[route]=requestHandler.registry;
		    	urlHandle[route]=url;
		        break;
		    case '/dataIngestor':
		    	functionHandle[route]=requestHandler.dataIngestor;
		    	urlHandle[route]=url;
		        break;
		    case '/stormDetector':
		    	functionHandle[route]=requestHandler.stormDetector;
		    	urlHandle[route]=url;
		        break;
		    case '/stormCluster':
		    	functionHandle[route]=requestHandler.stormCluster;
		    	urlHandle[route]=url;
		        break;
		    case '/stormTrigger':
		    	functionHandle[route]=requestHandler.forecastTrigger;
		    	urlHandle[route]=url;
		        break;
		    case '/weatherForecast':
		    	functionHandle[route]=requestHandler.predictWeatherforecast;
		    	urlHandle[route]=url;
		        break;
		    default:
		    	functionHandle[route]=requestHandler.login;
		    	urlHandle[route]=url;
		}
		}
			functionHandle['.css']=requestHandler.CSS;
			functionHandle['.js']=requestHandler.JS;
}

exports.update=update;