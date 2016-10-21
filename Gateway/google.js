/* Require shared configuration variables, eg. our Google Project ID */
var config = require('./config');

/* Require "books" service for querying, creating, and deleting books */


/* Require "auth" service for authenticating users and getting profile info */
var auth = require('./auth')(config);

/* Require Express web framework and Express middleware */
var express = require('express');
var multer = require('multer')
var session = require('cookie-session');

/* Configure Express web application */
var app = express();
app.use(express.static('public'));
app.set('view engine', 'jade');
app.enable('trust proxy');
//app.use(multer({ inMemory: true }));
app.use(session({ signed: true, secret: config.cookieSecret }));





/* Redirect user to OAuth 2.0 login URL */
app.get('/login', function(req, res) {
  var authenticationUrl = auth.getAuthenticationUrl();
  res.redirect(authenticationUrl);
});

/* Use OAuth 2.0 authorization code to fetch user's profile */
app.get('/oauth2callback', function(req, res, next) {
  auth.getUser(req.query.code, function(err, user) {
    if (err) return next(err);
    req.session.user = user;
    res.redirect('/');
  });
});

/* Clear the session */
app.get('/logout', function(req, res) {
  req.session = null;
  res.redirect('/');
});

/* Add a new book */
app.post('/books', function(req, res, next) {
  if (! req.body.title || ! req.body.author)
    return next(new Error('Must provide book Title and Author'));

  var coverImageData;
  if (req.files['cover'])
    coverImageData = req.files['cover'].buffer;

  var userId;
  if (req.session.user)
    userId = req.session.user.id;
});


/* Run web application */
app.listen(8000);
