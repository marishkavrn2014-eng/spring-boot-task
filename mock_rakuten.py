from http.server import BaseHTTPRequestHandler, HTTPServer
import json

class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        response = {
            "Items": [
                {"Item": {"genreId": 1001, "genreName": "Football"}},
                {"Item": {"genreId": 1002, "genreName": "Tennis"}}
            ]
        }
        body = json.dumps(response).encode("utf-8")

        self.send_response(200)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

HTTPServer(("127.0.0.1", 8084), Handler).serve_forever()