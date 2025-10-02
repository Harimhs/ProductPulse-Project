import { NextResponse } from "next/server";

export async function POST(req) {
    try{
        const {token, companyId}= await req.json();
        const response = 
        await fetch(`http://localhost:8080/api/company/${companyId}/invites/accept?token=${token}`,
            {method: 'POST'}
        );

        if(!response.ok){
            let errorData;
        try{
            errorData= await response.json();
        }catch{
            errorData= await response.text();
        }
        return NextResponse.json(
            {error: errorData},
            {status: response.status}
        );
        }

        const data = await response.json();
        return NextResponse.json(data);
        
    }
    catch(err){
       return NextResponse.json(
        {error: err},
        {status: 500}
       )
    }
}