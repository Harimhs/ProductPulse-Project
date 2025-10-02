import TeamInvite from "@/app/components/Inviteteam";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export default async function InvitePage({ params: awaitedParams }) {
  const params = await awaitedParams;
  console.log(params);
  const companyId = params.companyId;
  console.log(companyId);

  const token = (await cookies()).get("authToken")?.value;
  if (!token) redirect("/");

  return <TeamInvite companyId={companyId} />;
}
