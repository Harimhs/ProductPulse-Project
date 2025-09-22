import { useState } from "react";
import { useParams } from "react-router-dom";
import { getToken, logout } from "../src/authUtils";

function TeamInvite() {
  const { companyId } = useParams();
  const [emails, setEmails] = useState([""]);
  const [role, setRole] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [message, setMessage] = useState(null);

  const fetchRoles = async (query) => {
    if (!query) {
      setSuggestions([]);
      return;
    }
    try {
      const res = await fetch(`http://localhost:8080/api/roles/search?query=${query}`);
      if (res.ok) {
        const roles = await res.json();
        setSuggestions(roles);
      }
    } catch (err) {
      console.error("Error fetching roles:", err);
    }
  };

  const handleInvite = async () => {
    try {
      const token = getToken();
      if (!token) {
        logout();
        return;
      }

  const response = await fetch(
    `http://localhost:8080/api/company/${companyId}/invites`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        inviteList: emails.map(email => ({ email, role })),
      }),
    }
  );

    const data = await response.json();
    setMessage(JSON.stringify(data, null, 2));
  } catch (err) {
    setMessage("Error: " + err.message);
  }
  };

  return (
    <div>
      <h3>Invite team members</h3>
      <input
        type="text"
        value={emails[0]}
        onChange={(e) => setEmails([e.target.value])}
        placeholder="Enter email"
      />
      <div style={{ position: "relative", width: "200px" }}>
  <input
    type="text"
    value={role}
    onChange={(e) => {
      setRole(e.target.value);
      fetchRoles(e.target.value);
    }}
    placeholder="Enter role"
  />

  {suggestions.length > 0 && (
    <ul
      style={{
        position: "absolute",
        top: "100%",
        left: 0,
        right: 0,
        border: "1px solid #e11b1bff",
        background: "black",
        margin: 0,
        padding: 0,
        listStyle: "none",
        maxHeight: "150px",
        overflowY: "auto",
        zIndex: 1000,
      }}
    >
      {suggestions.map((r) => (
        <li
          key={r.id}
          onClick={() => {
            setRole(r.name);
            setSuggestions([]);
          }}
          style={{ padding: "6px", cursor: "pointer" }}
          onMouseOver={(e) => (e.currentTarget.style.background = "#dc0606ff")}
          onMouseOut={(e) => (e.currentTarget.style.background = "white")}
        >
          {r.name}
        </li>
      ))}
    </ul>
  )}
</div>


      <button onClick={handleInvite}>Send Invite</button>
      {message && <pre>{message}</pre>}
    </div>
  );
}

export default TeamInvite;
