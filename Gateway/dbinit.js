/**
 * http://usejsdoc.org/
 */
var mongodb = require('mongodb');
var assert = require('assert');
var MongoClient = mongodb.MongoClient;
var fs = require("fs")
var map={}
function read(err,data){
	if(err)
		{
			return err;
		}
	
	return data;
}

function parse(data){
	var map={}
	var lines=data.trim().split('\n')
	for (var i=0;i<lines.length;i++)
		{
			var line=lines[i].split('=');
			var key=(line[0]+'').trim();
			var value=(line[1]+'').trim();
			map[key]=value
		}
	return map;
}
var data=fs.readFileSync(__dirname +'/DB_Properties','utf-8',read)
var map=parse(data);

var url = 'mongodb://'+map["HOST"]+':'+map["PORT"]+'/'+map["DB"];

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  if (err) 
  {
	    console.log('Unable to connect to the mongoDB server. Error:', err);
  } 
  else 
  {
	  var collection = db.collection('users');

	    //Create some users
	    var user1 = {name: 'debasisdwivedy', password : 'debasisdwivedy', age: 27, roles: ['super-admin','admin', 'moderator', 'user']};
	    var user2 = {name: 'abhishekmehra',  password : 'abhishekmehra', age: 27, roles: ['user']};
	    var user3 = {name: 'pranavpande',  password : 'pranavpande', age: 27, roles: ['super-admin', 'admin', 'moderator', 'user']};
	    var user4 = {name: 'girishgabra',  password : 'girishgabra', age: 27, roles: ['user']};
	    var user5 = {name: 'marlon', password : 'marlon', age: 56, roles: ['user']};

	    // Insert some users
	    collection.insert([user1, user2, user3, user4 ,user5], function (err, result) {
	      if (err) {
	        console.log('Unable to insert document to the mongoDB server. Error:'+err);
	      } else {
	        console.log('Inserted %d documents into the "users" collection. The documents inserted with "_id" are:', result.length, result);
	      }
	});
  }
  
  var collection = db.collection('users');
	document=collection.find().toArray(function (err, docs) {
		console.log("print document length: "+docs.length)
		var length=docs.length;
		if(length>0)
			{
				for(var count=0;count<length;count++)
					{
					if(docs[count].password==undefined)
						{
						collection.deleteOne(docs[count])
						}
					}
			}
		db.close();
	});
  
  		//Close connection
  	   
});