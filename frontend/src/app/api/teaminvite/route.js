import {cookies} from "next/headers";
import { NextResponse } from "next/server";

export async function POST(req) {
    try{

        const {emails, role, companyId } = await req.json();
        const inviteList = emails.map(email => ({ email, role }));

        const token= (await cookies()).get("authToken")?.value;
        console.log("Token", !!token);

        if(!token){
            return NextResponse.json({error:"Unauthorized User!"}, {status: 401});
        }

        const response = await
            fetch(`http://localhost:8080/api/company/${encodeURIComponent(companyId)}/invites`,
            {method: "POST",
            headers: {"Content-Type": "application/json"
            ,Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({ inviteList }),
            },  
        );

        if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        return NextResponse.json(
            { error: errorData.message || "Failed to send invites" },
            { status: response.status }
        );
        }

        const data = await response.json();
        return NextResponse.json(data, { status: 200 });

    }catch (err) {
    return NextResponse.json(
      { error: err.message || "Unexpected error occurred" },
      { status: 500 }
    );
  }
}