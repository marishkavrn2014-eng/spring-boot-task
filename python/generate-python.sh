cd python
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt

python -m grpc_tools.protoc \
  -I../proto \
  --python_out=. \
  --grpc_python_out=. \
  ../proto/ping.proto