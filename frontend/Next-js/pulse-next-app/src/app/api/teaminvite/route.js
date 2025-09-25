import { cookies } from "next/headers";
import { NextResponse } from "next/server";

export async function GET(req) {

    try{
        const { searchParams } = new URL(req.url);
        const query = searchParams.get("query");

        const token = (await cookies()).get("authToken")?.value;
        console.log("Auth token found:", !!token);

        if(!token) {
            return NextResponse.json({error: "Unauthorized User"}, {status: 401})
        }

        if (!query) {
            return NextResponse.json({ error: "Missing query parameter" }, { status: 400 });
        }

        const response = await 
        fetch(`http://localhost:8080/api/roles/search?query=${encodeURIComponent(query)}`, {
        method: "GET",
        headers: { "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
         }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            return NextResponse.json(
            { error: errorData.message || "Failed to fetch roles from database" },
            { status: response.status }
            );
        }

        const roles = await response.json();
        return NextResponse.json(roles, { status: 200 });
        
    } catch (err) {
        return NextResponse.json(
        { error: err.message || "Unexpected error occurred" },
        { status: 500 }
    );
  }
    
}