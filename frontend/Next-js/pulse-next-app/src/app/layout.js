import { Geist, Geist_Mono } from "next/font/google";
import "./styles/globals.scss"; 

export const metadata = {
  title: "My App",
  description: "Next.js App Router ",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
