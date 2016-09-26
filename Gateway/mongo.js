/**
 * http://usejsdoc.org/
 */
var fs = require("fs")
var mongodb = require('mongodb');
var assert = require('assert');
var http = require('http')
var router = require("./router.js") 
var MongoClient = mongodb.MongoClient;

function dbConnect(host,port,databasename,username,password,func,request,response)
{
// Connection URL
//var url = 'mongodb://localhost:56001/LDAP';
var url = 'mongodb://'+host+':'+port+'/'+databasename;
console.log("DB URL :"+url)
//var url = 'mongodb://'+username+':'+passowrd+'@'+host+':'+port+'/'+databasename;
// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  if (err) 
  {
	    console.log('Unable to connect to the mongoDB server. Error:', err);
	    
  } 
  else 
  {
	  func(db,host,port,databasename,username,password,request,response);
  }
  		//Close connection
  	   db.close();
});
}

function read(err,data)
{
	if(err)
		{
			return err;
		}
	
	return data;
}

function authenticate(host,port,databasename,username,password,handles,request,response,parameter)
{
	var func=function(db,host,port,databasename,username,password,request,response)
	{
		var collection = db.collection('users');
		var flag=collection.find({name : username.toString(), password : password.toString()}).toArray(function (err, docs,func) {
			console.log("print document length: "+docs.length)
			var length=docs.length;
			for(var count=0;count<length;count++)
				{
					console.log("Doc is :"+docs[count].password)
				}
			if(length>0)
				{
					router.route(handles,"/Gateway",request,response,parameter);
						
				}
			else
				{
				router.route(handles,"/login",request,response,parameter);
				}
		});
	}
	dbConnect(host,port,databasename,username,password,func,request,response)
	console.log("Connected successfully to server");
}

function insertDocument(db,request,response)
{
	// Get the documents collection
    var collection = db.collection('users');

    //Create some users
    var user1 = {name: 'modulus admin', age: 42, roles: ['admin', 'moderator', 'user']};
    var user2 = {name: 'modulus user', age: 22, roles: ['user']};
    var user3 = {name: 'modulus super admin', age: 92, roles: ['super-admin', 'admin', 'moderator', 'user']};

    // Insert some users
    collection.insert([user1, user2, user3], function (err, result) {
      if (err) {
        console.log('Unable to insert document to the mongoDB server. Error:'+err);
      } else {
        console.log('Inserted %d documents into the "users" collection. The documents inserted with "_id" are:', result.length, result);
      }
});
}

function updateDocument(db,request,response)
{
	// Get the documents collection
	var collection = db.collection('users');

	// Insert some users
	collection.update({name: 'modulus user'}, {$set: {enabled: false}}, function (err, numUpdated) {
	  if (err) {
		  console.log('Unable to update document to the mongoDB server. Error:'+err);
	  } else if (numUpdated) {
	    console.log('Updated Successfully %d document(s).', numUpdated);
	  } else {
	    console.log('No document found with defined "find" criteria!');
	  }
	});
}

function deleteDocuments(db,request,response)
{
	// Get the documents collection
	var collection = db.collection('users');

	// Delete some users
	collection.deleteOne({name: 'modulus user'}, {$set: {enabled: false}}, function (err, numUpdated) {
	  if (err) {
		  console.log('Unable to delete document to the mongoDB server. Error:'+err);
	  } else if (numUpdated) {
	    console.log('Deleted Successfully %d document(s).', numUpdated);
	  } else {
	    console.log('No document found with defined "find" criteria!');
	  }
	});
}

exports.authenticate=authenticate;
