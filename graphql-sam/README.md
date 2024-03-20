### GraphQL, REST, OAuth, JWT example

Oauth Working:

- an idToken is supplied by Google, that should be coming from the frontend
- that is used to access the oauth services

auth endpoints: /auth/\*\*
graphql: /graphql

Deployment:

- install kubectl and minikube(linux) or use docker desktop and enable kubernetes (windows)
- replace db url in application.properties with this: `jdbc:mysql://${DB_SERVER}:${DB_PORT:3306}/${DB_NAME}`
- and username and password should be replaced with `${DB_USERNAME}` and `${DB_PASSWORD}`
- run `kubectl apply -f <deployment-yamls>.yaml` (config-map, secrets, sql-dep, application-dep in that order)
- if using minikube, run `minikube tunnel` to get an ip to access the api endpoints
- run `kubect get services` to get the external IP of the api

