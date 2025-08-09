import { useEffect, useState } from "react";
import { getToken, isTokenExpired, logout } from "../src/authUtils";

function Home() {

  const [isChecking, setIsChecking] = useState(true);

  useEffect(()=> {
    const token = getToken();
    if (!token || isTokenExpired(token)) {
      logout();
    } else {
      setIsChecking(false); 
    }
  }, []);

  if (isChecking) {
    return null; 
  }


  return (
    <div style={{ maxWidth: '890px', margin: '100px auto' }}>
      <h1>You are authenticated to succeed!</h1>
    </div>
  );
}

export default Home;