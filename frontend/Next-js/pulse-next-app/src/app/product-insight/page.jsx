"use client"
import {useState} from "react";

export default function ProductInsightPage(){

    const [productName, setProductName] = useState("");
    const [productDescription, setProductDescription] = useState("");
    const [productLaunchDate, setProductLaunchDate] = useState("");
 
    return(
     <>
       <div>
           <div>
               <form>
                 <label>product Name</label>
                 <input type="text" placeholder="enter your product name" onChange={(e) => {setProductName(e.target.value)}}/>
                 <label>product description</label>
                 <textarea placeholder="Enter product description" onChange={(e) =>{setProductDescription(e.target.value)}}></textarea>
                 <lable>product launch date</lable>
                 <input type="date" onChange={(e)=>{setProductLaunchDate(e.target.value)}}/>
                 <button> search the product insights</button>
               </form>               
           </div>
           
       </div>
     </>
    )
}