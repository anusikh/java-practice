- Added registry service
- added `127.0.0.1  registry-server` in hosts file
- run server and open `registry-server:8761` on browser

------------

- added gateway client
- added `127.0.0.1  gateway-client` in hosts file
- running on port `8083`

------------

- added docker compose file for keycloak-auth
- add `127.0.0.1  keycloak-auth` in hosts file
- running on port `8080`

------------


- after running keycloak, create client
- add a callback/redirect uri and the origin
- enable `Client Authentication`
- then set the `proof key for code Exchange code challenge method` to S256
- create 2 client roles `admin` and `user`
- go to `realm settings` and in Login section, select `User Registration` to enable user registration
- in the user registration tab, select the default role as `user`
- this makes sure that every new user registered is assigned the `user` role automatically

- since we have enabled PKCE, we need a code_verifier and a code_challenge
- code_verifier can be any random string like `anusikh`
- code_challenge needs to be the SHA256 hash of that string

- navigate to this link and register: http://localhost:8080/realms/micro-service/protocol/openid-connect/auth?response_type=code&client_id=micro-service&scope=openid&redirect_uri=http://localhost:3000/logincb&code_challenge_method=S256&code_challenge=<code_verifier>
- i registered my personal account and to assign it admin role, i went to `Users` > my user > role mapping tab > assign role button and then i assigned the admin role
- get the client secret from the client settings

------------

- created a new project, add web, oauth-resource-server, validation, lombok and spring security as dependencies
- added added `127.0.0.1  sample-service` in hosts file
- in config server, created a new config for sample service
- sample service is running on port `8082`
- then update the application.yml for service, so that it uses the config server
- then run all the servers

------------

- check the postman collection for api endpoints
- use this to export the keycloak realm and import it when using
```sh
/opt/keycloak/bin/kc.sh export --file micro-service
```




### TODO:
- nextjs frontend
    - refer PKCE flow diagram
    - store the user details in some sort of global state
    - then use it to call the api