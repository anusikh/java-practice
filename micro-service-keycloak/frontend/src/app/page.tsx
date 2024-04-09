"use client";

import { getUser, login, logout } from "@/utils/auth-manager";
import React, { useEffect, useState } from "react";

export default function Home() {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    getUser()
      .then((user) => setUser(user))
      .catch((err) => console.log(err));
  }, []);

  return (
    <main>
      {user ? (
          <button onClick={logout}>logout</button>
      ) : (
        <button className="" onClick={login}>login</button>
      )}
    </main>
  );
}
