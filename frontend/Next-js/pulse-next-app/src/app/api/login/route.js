import { NextResponse } from "next/server";

export async function POST(req) {
  try {
    const { email, password } = await req.json();

    const backendRes = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!backendRes.ok) {
      const errorData = await backendRes.json().catch(() => ({}));
      return NextResponse.json(
        { error: errorData.message || "Login failed" },
        { status: backendRes.status }
      );
    }

    const data = await backendRes.json();

    const res = NextResponse.json(data);
    res.cookies.set("authToken", data.token, {
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "strict",
      path: "/",
      maxAge: 60 * 60 * 24,
    });

    return res;
  } 
  catch (err) {
    return NextResponse.json(
      { error: err || "This is not working because of stuipd backend" },
      { status: 500 }
    );
  }
}

