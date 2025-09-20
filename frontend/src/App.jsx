import Login from "../pages/login"
import Register from "../pages/register"
import Home from "../pages/home"
import CreateCompany from "../pages/CreateCompany"
import { Route, Routes, Router } from "react-router-dom"
import ProtectedRoute from "./ProtectedRoute"
import "./App.css"
import TeamInvite from "../pages/InviteTeam"
import AcceptInvite from "../pages/AcceptInvite"

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element= {<Login />} />
        <Route path="/home" element= {<ProtectedRoute> <Home /> </ProtectedRoute>} />
        <Route path="/register" element= {<Register />} />
        <Route path="/register/create-company" element= {<ProtectedRoute> <CreateCompany /> </ProtectedRoute>} />
        <Route path="/company/:companyId/invites" element= {<ProtectedRoute> <TeamInvite /> </ProtectedRoute>} />
        <Route path="/company/:companyId/invites/accept" element= {<AcceptInvite />} />
      </Routes>
    </>
  )
}

export default App