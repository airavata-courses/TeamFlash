/**
 * http://usejsdoc.org/
 */
var mongodb = require('mongodb');
var assert = require('assert');
var MongoClient = mongodb.MongoClient;

var url = 'mongodb://localhost:27017/LDAP';

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  /*if (err) 
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

	    // Insert some users
	    collection.insert([user1, user2, user3, user4], function (err, result) {
	      if (err) {
	        console.log('Unable to insert document to the mongoDB server. Error:'+err);
	      } else {
	        console.log('Inserted %d documents into the "users" collection. The documents inserted with "_id" are:', result.length, result);
	      }
	});
  }*/
  
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