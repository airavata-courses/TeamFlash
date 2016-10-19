import unittest
from StormClustering import app
 
class TestClustering(unittest.TestCase):
    def test_clustering(self):
        # Use Flask's test client for our test.
        self.test_app = app.test_client()
 
        # Make a test request to the conference app, supplying a fake From phone
        # number
        response = self.test_app.get('/detectClusters')
 
        # Assert response is 200 OK.                                           
        self.assertEquals(response.status, "200 OK")
