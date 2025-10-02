import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import TeamInvite from "../components/Inviteteam";

export default async function CompanyPage({ searchParams }) {
  const params = await searchParams;
  const companyId = params?.companyId;

  const token = (await cookies()).get("authToken")?.value;

  if (!token) {
    redirect("/");
  }

  return <TeamInvite companyId={companyId} />;
}
