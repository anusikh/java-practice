import NextAuth, { AuthOptions, NextAuthOptions, TokenSet } from "next-auth";
import { JWT } from "next-auth/jwt";
import KeycloakProvider from "next-auth/providers/keycloak";

const KEYCLOAK_CLIENT_ID = process.env.AUTH_KEYCLOAK_ID!;
const KEYCLOAK_CLIENT_SECRET = process.env.AUTH_KEYCLOAK_SECRET!;
const AUTH_SECRET = process.env.AUTH_SECRET!;


function requestRefreshOfAccessToken(token: JWT) {
  return fetch(
    `http://localhost:8080/realms/micro-service/protocol/openid-connect/token`,
    {
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: JSON.stringify({
        clientId: KEYCLOAK_CLIENT_ID,
        clientSecret: KEYCLOAK_CLIENT_SECRET,
        grant_type: "refresh_token",
        refresh_token: token.refresh_token,
      }),
      method: "POST",
      cache: "no-store",
    }
  );
}

export const authOptions: AuthOptions = {
  secret: AUTH_SECRET,
  providers: [
    KeycloakProvider({
      clientId: KEYCLOAK_CLIENT_ID,
      clientSecret: KEYCLOAK_CLIENT_SECRET,
      issuer: "http://localhost:8080/realms/micro-service",
    }),
  ],
  callbacks: {
    async jwt({ token, account }) {
      // Persist the OAuth access_token to the token right after signin
      if (account) {
        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.expiresAt = account.expires_at;
        return token;
      }
      if ((token.expiresAt as number) * 1000 - 60 * 1000 > Date.now()) {
        return token;
      } else {
        try {
          const response = await requestRefreshOfAccessToken(token);

          const tokens: TokenSet = await response.json();

          if (!response.ok) throw tokens;

          const updatedToken: JWT = {
            ...token, // Keep the previous token properties
            idToken: tokens.id_token,
            accessToken: tokens.access_token,
            expiresAt: Math.floor(
              Date.now() / 1000 + (tokens.expires_in as number)
            ),
            refreshToken: tokens.refresh_token ?? token.refreshToken,
          };
          return updatedToken;
        } catch (error) {
          console.error("Error refreshing access token", error);
          return { ...token, error: "RefreshAccessTokenError" };
        }
      }
    },
    async session({ session, token, user }) {
      // Send properties to the client, like an access_token from a provider.
      return { ...session, token: token.accessToken };
    },
  },
};

const handler = NextAuth(authOptions);

export { handler as GET, handler as POST };
