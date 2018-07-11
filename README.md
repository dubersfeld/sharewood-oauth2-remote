# sharewood-oauth2-user
I present here a different implementation of the previous project https://github.com/dubersfeld/sharewood-boot-oauth2-upgrade. It uses the same Spring Boot version 2.0.3.RELEASE. The main difference is the resource server implementation. Here I use RemoteTokenServices instead of sharing a token database between resource server and authorization server.
