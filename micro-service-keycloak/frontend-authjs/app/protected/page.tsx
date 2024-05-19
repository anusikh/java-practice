import { getServerSession } from "next-auth";
import { authOptions } from "../api/auth/[...nextauth]/route";

export default async function Protected() {
  const session = await getServerSession(authOptions);

  if (session) {
    return <p>You are authenticated</p>;
  } else {
    return <p>You are not authenticated</p>;
  }
}
