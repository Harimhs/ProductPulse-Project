"use client";
import { useEffect, useState } from "react";
import { useParams, useSearchParams } from "next/navigation";
import AcceptInviteForm from "@/app/components/Acceptinvite";

export default function AcceptInvitePage() {
  const { companyId } = useParams();
  const searchParams = useSearchParams();
  const token = searchParams.get("token");

  const [partialUser, setPartialUser] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!token) {
      setError("Invalid invite link.");
      setLoading(false);
      return;
    }

    const verifyInvite = async () => {
      try {
        const res = await fetch("/api/acceptinvite", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ companyId, token }),
        });

        if (!res.ok) throw new Error("Invite expired or invalid");
        const data = await res.json();
        setPartialUser(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    verifyInvite();
  }, [companyId, token]);

  if (loading) return <p>Loading invite...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!partialUser) return null;

  return (
    <AcceptInviteForm
      partialUser={partialUser}
      inviteToken={token}  
    />
  );
}
