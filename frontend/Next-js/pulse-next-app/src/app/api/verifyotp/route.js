import { NextResponse } from "next/server";

export async function POST(req) {
  console.log(req)
  try {
    const { searchParams } = new URL(req.url);
    const email = searchParams.get("email");
    const otp = searchParams.get("otp");

    console.log("Verify OTP request:", { email, otp });

    const response = await fetch(
      `http://localhost:8080/verify-otp?email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`,
      { method: "POST" }
    );

    if (!response.ok) {
      const errorData = await response.text().catch(() => "");
      return NextResponse.json(
        { error: errorData || "OTP verification failed" },
        { status: response.status }
      );
    }

    const data = await response.json(); 

     const res = NextResponse.json(data);
    res.cookies.set("authToken", data.data                     , {
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "strict",
      path: "/",
      maxAge: 60 * 60 * 24,
    });

    console.log(res);
    return res;
  } catch (err) {
    return NextResponse.json(
      { error: err.message || "OTP verification error" },
      { status: 500 }
    );
  }
}
