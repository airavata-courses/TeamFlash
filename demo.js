/**
 * 
 */
registryButton = function() {
	alert("Hi");
	return
}

dataIngestorButton = function() {
	alert("Hello");
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
