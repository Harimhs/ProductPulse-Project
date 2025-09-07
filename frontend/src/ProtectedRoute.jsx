import { Navigate } from "react-router-dom";
import { getToken, isTokenExpired } from "../src/authUtils";

function ProtectedRoute({ children }) {
  const token = getToken();
  if (!token || isTokenExpired(token)) {
    return <Navigate to="/" replace />;
  }
  return children;
}

export default ProtectedRoute;
