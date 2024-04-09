import { UserManager, UserManagerSettings } from "oidc-client-ts";

const settings: UserManagerSettings = {
  authority: "http://keycloak-auth:8080/realms/micro-service/",
  client_id: "micro-service",
  redirect_uri: "http://localhost:3000/logincb",
  client_secret: "xbGNkLVL1xWNxeuzUj2pPCOCZk5IBZin",
  response_type: "code",
};

export const userManager = new UserManager(settings);

export const getUser = () => {
  return userManager.getUser();
};

export const login = () => {
  return userManager.signinRedirect();
};

export const logout = () => {
  return userManager.signoutRedirect();
};
