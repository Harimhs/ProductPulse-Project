import Login from "../pages/login"
import Register from "../pages/register"
import Home from "../pages/home"
import { Route, Routes, Router } from "react-router-dom"
import ProtectedRoute from "./ProtectedRoute"
import "./App.css"

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element= {<Login />} />
        <Route path="/home" element= {<ProtectedRoute> <Home /> </ProtectedRoute>} />
        <Route path="/register" element= {<Register />} />
      </Routes>
    </>
  )
}

export default App