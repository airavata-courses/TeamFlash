module.exports = {
  projectId: 'science-gateway-project-new',
  keyFilename: './client_secret.json',
  bucketName: '[your Google Cloud Storage bucket name]',
  cookieSecret: '[cooking signing key]',
  oauth2: {
    clientId: '842652899246-1eqq9510atpnsu3aktjmup6l8kmqk3q3.apps.googleusercontent.com',
    clientSecret: 'htkmqlCVmuTf199NEk56kAnt',
    redirectUrl: process.env.REDIRECT_URL || 'http://ec2-52-9-64-108.us-west-1.compute.amazonaws.com:8888/oauth2callback'
  }
};
