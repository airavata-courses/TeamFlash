/**
 * http://usejsdoc.org/
 */
var server=require("./server")
var router=require("./router")
var requestHandler=require("./requestHandler")
var updateURL=require("./updateURL")

server.start(router.route,updateURL.update);

