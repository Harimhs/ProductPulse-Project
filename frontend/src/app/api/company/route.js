import { cookies } from "next/headers";
import { NextResponse } from "next/server";

export async function POST(req) {
  try {
    const companyDetails = await req.json();

    const token = (await cookies()).get("authToken")?.value;
    console.log("Token", companyDetails)

    if (!token) {
      return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }
if(token){
const res = await fetch("http://localhost:8080/register/company", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`, 
      },
      body: JSON.stringify(companyDetails),
    });
    if (!res.ok) {
  const text = await res.text();  
  console.error("Backend error:", text); 
  return NextResponse.json(
    { error: text || "Company registration failed" },
    { status: res.status }
  );
}
    const data = await res.json();
    return NextResponse.json(data);
}
    
  } catch (err) {
    console.error("Company API error:", err);
    return NextResponse.json(
      { error: err.message || "Unexpected error" },
      { status: 500 }
    );
  }
}
