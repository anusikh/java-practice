import { getServerSession } from "next-auth";
import { authOptions } from "../auth/[...nextauth]/route";
import { getToken } from "next-auth/jwt";
import { NextRequest } from "next/server";

export async function GET(req: NextRequest, res: Response) {
  const session = await getServerSession(authOptions);
  if (session) {
    const getuser = await fetch(
      "http://localhost:8080/realms/micro-service/protocol/openid-connect/userinfo",
      {
        headers: {
          Authorization: `Bearer ${session?.token}`,
        },
      }
    );
    try {
      const res = await getuser.json();
      return Response.json({ res });
    } catch (e) {
      return Response.json({});
    }
  } else {
    return Response.json({ message: "not authenticated" });
  }
}
