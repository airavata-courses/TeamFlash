from StormClustering import app

import os
import unittest

class ClusteringTestCase(unittest.TestCase):

   def test_root_text(self):
        tester = app.test_client(self)
        response = tester.get('/detectClusters')
        # Assert response is 200 OK.                                           
        self.assertEquals(response.status, "200 OK")

if __name__ == '__main__':
    unittest.main()
