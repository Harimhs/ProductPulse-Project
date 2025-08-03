import Login from "../pages/login"
import Register from "../pages/register"
import Home from "../pages/home"
import { Route, Routes } from "react-router-dom"
import "./App.css"

function App() {

  return (
    <>
    <Routes>
      <Route path="/" element= {<Login />} />
      <Route path="/home" element= {<Home />} />
      <Route path="/register" element= {<Register />} />
    </Routes>
    </>
  )
}

export default App