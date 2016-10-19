import unittest
from StormDetection import app
 
class TestDetection(unittest.TestCase):
    def test_detection(self):
        # Use Flask's test client for our test.
        self.test_app = app.test_client()
 
        # Make a test request to the conference app, supplying a fake From phone
        # number
        response = self.test_app.get('/detectStorm')
 
        # Assert response is 200 OK.                                           
        self.assertEquals(response.status, "200 OK")
