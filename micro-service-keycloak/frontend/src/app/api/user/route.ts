import { cookies } from "next/headers";

export async function GET(req: Request) {
  let access_token = cookies().get("access_token")?.value;
  const fet = await fetch("http://gateway-client:8083/sample/user", {
    headers: {
      Authorization: `Bearer ${access_token}`,
    },
  });
  try {
    const res = await fet.json();
    return Response.json({ res });
  } catch (e) {
    return Response.json({});
  }
}
