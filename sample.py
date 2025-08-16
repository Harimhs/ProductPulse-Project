import smtplib

EMAIL = "prodctpulseoffl@gmail.com"
APP_PASSWORD = "swtmdhcgqgdqkiew"

try:
    with smtplib.SMTP("smtp.gmail.com", 587) as server:
        server.starttls()
        server.login(EMAIL, APP_PASSWORD)
        print("✅ App password works!")
except Exception as e:
    print("❌ Failed:", e)
