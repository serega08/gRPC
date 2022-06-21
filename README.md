## Repository Overview
Intention of this repository is to use gRPC.

gRPC is a modern open source high performance Remote Procedure Call (RPC) framework that can run in any environment. It can efficiently connect services in and across data centers with pluggable support for load balancing, tracing, health checking and authentication. 

Key points:
- uses Protocol Buffers mechanism for serializing/deserializing structured data
- under the hood uses HTTP 2

How to run:
- execute `generateProto` gradle task to generate proto
- spin up Server, by executing `greeting.server.Server.main`
- spin up different Clients, by executing `greeting.client.Client.main` with one of arguments: greet, calculate, great_stream or great_stream_request
