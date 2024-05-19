"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export default async function setauth(access_token: string) {
  cookies().set("access_token", access_token);
  redirect("/");
}
