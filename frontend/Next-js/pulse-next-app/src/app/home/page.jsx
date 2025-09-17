import { cookies } from "next/headers"
import { redirect } from "next/navigation"

export default  async function HomePage() {
  const token = (await cookies()).get("authToken")?.value;
  if (!token) {
    redirect("/");
  }

  return (
    <main>
      <h1>Home</h1>
      <p>Welcome! You are authenticated</p>
    </main>
  )
}
