"use client";

import { userManager } from "@/utils/auth-manager";
import React, { useEffect } from "react";
import setauth from "./actions";
import { useRouter } from "next/navigation";

export default function Callback() {
  const router = useRouter();

  useEffect(() => {
    userManager
      .signinRedirectCallback()
      .then(async (u) => {
        await setauth(u.access_token);
      })
      .catch((err) => console.log(err));
    // window.location.reload();
  }, [router]);

  return <main>Authenticating.....</main>;
}
