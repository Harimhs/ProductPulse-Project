import { useEffect } from "react";
import { getToken, isTokenExpired, logout } from "../src/authUtils";
import { useNavigate } from "react-router-dom";

function Home() {

  const navigate = useNavigate();

  useEffect(()=> {
    const token = getToken();
    if(!token||isTokenExpired(token)){
      navigate('/');
    }
  }, [])

  return (
    <div style={{ maxWidth: '890px', margin: '100px auto' }}>
      <h1>You are authenticated to succeed!</h1>
    </div>
  );
}

export default Home;