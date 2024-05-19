import Link from "next/link";
import React from "react";

export default function Home() {
  return (
    <main className="flex  h-screen items-center justify-center ">
      <div className="flex-col">
        <p>hi, nextjs14 with next-auth4 and keycloak!</p>
        <Link href="/protected" className="text-blue-600">
          <p>please visit this protected route</p>
        </Link>
      </div>
    </main>
  );
}
