"use client";

import { userManager } from "@/utils/auth-manager";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";

export default function LoginCb() {
  const router = useRouter();

  useEffect(() => {
    userManager
      .signinRedirectCallback()
      .then((user) => {
        console.log(user);
        router.push("/");
      })
      .catch((err) => console.log(err));
  });

  return <main>Authenticating.....</main>;
}
