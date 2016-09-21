/**
 * 
 */
registryButton = function() {
	var request = createCORSRequest("get", "http://localhost:2345/audit?test=1234");
	if (request){
	    request.onload = function() {
	        alert("done")
	    };
	    request.onreadystatechange = function () {
	        if(request.readyState === XMLHttpRequest.DONE && request.status === 200) {
	            console.log(request.responseText);
	        }
	    };
	    request.send(null);
}
	return
}
function createCORSRequest(method, url){
    var xhr = new XMLHttpRequest();
    if ("withCredentials" in xhr){
        xhr.open(method, url, true);
    } else if (typeof XDomainRequest != "undefined"){
        xhr = new XDomainRequest();
        xhr.open(method, url);
    } else {
    	xhr.open(method, url, false);
    }
    xhr.setRequestHeader('Access-Control-Allow-Headers', 'http://localhost:8080');
    xhr.setRequestHeader('Access-Control-Allow-Methods', 'GET');
    return xhr;
}

dataIngestorButton = function() {
	var request = createCORSRequest("get", "http://localhost:5679/data/");
	if (request){
	    request.onload = function() {
	        alert("done")
	    };
	    request.onreadystatechange = function () {
	        if(request.readyState === XMLHttpRequest.DONE && request.status === 200) {
	            console.log(request.responseText);
	        }
	    };
	    request.send(null);
}
	return
}

$(document)
.ready(
		function() {
			$('#registryButton').click(registryButton);
			$('#dataIngestorButton').click(dataIngestorButton);
			$('#stormDetectorButton').click(stormDetectorButton);
			$('#stormCluster').click(stormCluster);
			$('#forecastTrigger').click(forecastTrigger);
			$('#weatherForecast').click(weatherForecast);
			
			var $cy = $("#cy");
			$cy.cytoscape({
				ready : function() {
				} // ready
			}); // cy initializer: cytoscape

		}) // document ready
