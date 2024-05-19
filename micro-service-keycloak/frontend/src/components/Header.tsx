"use client";

import React, { useContext } from "react";
import { getUser, login, logout } from "@/utils/auth-manager";
import { User } from "oidc-client-ts";

export default function Header(): React.JSX.Element {
  const [user, setUser] = React.useState<User | null>(null);

  React.useEffect(() => {
    getUser()
      .then((user) => {
        setUser(user);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div className="bg-black w-full h-10 text-white flex items-center justify-end">
      {user ? (
        <p className="m-5">{user.profile.name}</p>
      ) : (
        <button className="m-5 hover:text-red-950" onClick={login}>
          sign in
        </button>
      )}
      <button
        className="m-5 hover:text-red-950"
        onClick={() => {
          logout();
          document.cookie =
            "access_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }}
      >
        sign out
      </button>
    </div>
  );
}
