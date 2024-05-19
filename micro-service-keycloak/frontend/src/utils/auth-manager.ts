import { UserManager, UserManagerSettings } from "oidc-client-ts";

const settings: UserManagerSettings = {
  // NOTE: make sure host is localhost and not keycloak-auth that we set in our host file
  authority: "http://localhost:8080/realms/micro-service/",
  client_id: "micro-service",
  redirect_uri: "http://localhost:3000/callback",
  client_secret: "Lt2gXdGGTPnJGNmAZyxenS3VAsUQc2lA",
  response_type: "code",
  scope: "openid"
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
