import os
import unittest
from StormDetection import app
 
class DetectionTestCase(unittest.TestCase):
    def test_detection(self):
        tester = app.test_client(self)
        response = tester.get('/detectStorm')                                         
        self.assertEquals(response.status, "200 OK")


if __name__ == '__main__':
	unittest.main()
