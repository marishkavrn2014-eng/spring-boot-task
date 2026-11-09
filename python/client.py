from datetime import datetime, timezone

import grpc
import ping_pb2
import ping_pb2_grpc
from google.protobuf.timestamp_pb2 import Timestamp


with grpc.insecure_channel("localhost:8080") as channel:
    stub = ping_pb2_grpc.PingServiceStub(channel)

    timestamp = Timestamp()
    timestamp.FromDatetime(datetime.now(timezone.utc))

    response = stub.Ping(
        ping_pb2.PingRequest(
            message="Ping",
            timestamp=timestamp,
        )
    )
    print(f"Server replied: {response.message}")