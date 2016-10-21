module.exports = {
  projectId: 'velvety-harbor-146921',
  keyFilename: './client_secret.json',
  bucketName: '[your Google Cloud Storage bucket name]',
  cookieSecret: '[cooking signing key]',
  oauth2: {
    clientId: '454533374641-7pi0orpb4u90odj6umitgsnpjj4vge1p.apps.googleusercontent.com',
    clientSecret: 'Ickb2LRup5yIKisnyydmz0Ay',
    redirectUrl: process.env.REDIRECT_URL || 'http://52.55.196.63:8888/oauth2callback'
  }
};
