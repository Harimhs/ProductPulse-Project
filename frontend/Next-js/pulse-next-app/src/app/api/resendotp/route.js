import { NextResponse } from "next/server";

export async function POST(req) {
  console.log(req);
  const { searchParams } = new URL(req.url);
  const email = searchParams.get("email");
  console.log("email from query:", email);

  try {
    const response = await fetch(
      `http://localhost:8080/resend-otp?email=${encodeURIComponent(email)}`,
      { method: "POST" }
    );

    if (!response.ok) {
      const errorText = await response.text().catch(() => "Unknown error");
      return NextResponse.json(
        { error: errorText || "Resend OTP failed" },
        { status: response.status }
      );
    }

    console.log(response);

    const data = await response.text();
    return NextResponse.json({ message: data });
  } catch (err) {
    return NextResponse.json(
      { error: err.message || "Resend failed because of backend" },
      { status: 500 }
    );
  }
}
