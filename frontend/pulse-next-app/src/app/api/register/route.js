import { NextResponse } from "next/server";

export async function POST(req){
    const userDetails = await req.json();
    try{

        const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(userDetails)
          });

          console.log(response);

          if(!response.ok){
            const errorData = await response.json().catch(() => ({}));
            console.log("the response", response);
            return NextResponse.json(
                {error: errorData.message || "Register Failed"},
                {status: 404}
            )
          }

          const data = await response.json();
          return NextResponse.json(data);
        
    }
    catch(err){
       return NextResponse.json(
        {error: err || "Check your details are within criteria and try again!"},
        {status: 500}
       )

    }
}
