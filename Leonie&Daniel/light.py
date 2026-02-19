import asyncio
from kasa import Discover, Credentials

async def main():
    credentials = Credentials(
        username="s241226@student.dhbw-mannheim.de",
        password="Bob1234!"
    )
    
    device = await Discover.discover_single("192.168.178.44", credentials=credentials)
    await device.update()
    await device.set_hsv(240, 100, 100)# Ist Blau hier können wir die Farbe ändern.
    await device.disconnect()

asyncio.run(main())
