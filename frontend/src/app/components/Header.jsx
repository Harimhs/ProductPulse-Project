"use client";

import styles from "../styles/header.module.scss";
import { useRouter } from "next/navigation";

export default function Header(){
    const router = useRouter();
    return(
        <>
          <div className={styles.container}>
              <div className={styles.leftcontainer}>
                <h1>LOGO - PRODUCT PULSE</h1>
              </div>
              <div className={styles.rightcontainer}>  
                 <button onClick={()=>{router.push("/login")}}>Login</button>
                 <button onClick={()=>{router.push("/register")}}>Register</button>
              </div>
          </div>
        </>
    )
}
