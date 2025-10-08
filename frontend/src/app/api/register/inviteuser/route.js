import { NextResponse } from "next/server";

export async function POST(req) {
    try {
        const userDetails = await req.json();

        const response = await fetch('http://localhost:8080/register/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDetails)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            return NextResponse.json(
                { error: errorData.message || "Register Failed" },
                { status: response.status || 400 }
            );
        }

        const data = await response.json();
        return NextResponse.json(data, { status: 200 });

    } catch (err) {
        console.error("Error in /register/invite POST handler:", err);
        return NextResponse.json(
            {
                error: err.message || "Check your details are within criteria and try again!"
            },
            { status: 500 }
        );
    }
}
