import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import CompanyRegister from "../components/CompanyRegister";


export default async function CompanyPage() {
  const cookieStore = await cookies();
  const token = cookieStore.get("authToken")?.value;
  console.log(token);

  if (!token) {
    redirect("/");
  }

  return <CompanyRegister />;
}
