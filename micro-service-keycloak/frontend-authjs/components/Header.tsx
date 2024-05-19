"use client";

import { DefaultSession } from "next-auth";
import { signIn, signOut, useSession } from "next-auth/react";
import React, { useState } from "react";

export default function Header(): React.JSX.Element {
  // if you want compulsory signin
  //   const { data } = useSession({ required: true });
  const { data } = useSession();

  const [session, setSession] = useState<DefaultSession | null>();

  React.useEffect(() => {
    setSession(data);
  }, [session, data]);

  const login = () => {
    signIn("keycloak");
  };

  const logout = () => {
    signOut();
  };

  return (
    <div className="bg-black w-full h-10 text-white flex items-center justify-end">
      {session ? (
        <>
          <p className="m-5">{session?.user?.name}</p>
          <button className="m-5 hover:text-red-950" onClick={logout}>
            sign out
          </button>
        </>
      ) : (
        <button className="m-5 hover:text-red-950" onClick={login}>
          sign in
        </button>
      )}
    </div>
  );
}
