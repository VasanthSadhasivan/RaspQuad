
import serial, thread, time, socket

ser = serial.Serial('/dev/ttyUSB0',9600)	
SWITCHES=["throttle", "roll", "pitch", "yaw", "lmswitch", "aux"]

def initializeSerial():
	print(ser.readline())

def lowToHigh(switch):
	for i in range(60):
		time.sleep(.1)
		writeValue(switch, i*3)

def highToLow(switch):
	for i in range(60):
		time.sleep(.1)
		writeValue(switch, 180-i*3)

def maxOutAll():
	for i in SWITCHES:
		lowToHigh(i)
		highToLow(i)

def neutralizeAll():
	for i in SWITCHES:
		time.sleep(.2)
		neutralize(i)

def neutralize(switch):
	writeValue(switch,90)		

def writeValue(switch, value):
	ser.write(switch+","+str(value)+"\n")

def threadedRead():
	while True:
		read_serial=ser.readline()
		print(read_serial)

def arm():
	highToLow("throttle")
	lowToHigh("aux")

def disarm():
	highToLow("aux")
	writeValue("throttle", 0)

initializeSerial()
thread.start_new_thread(threadedRead,())
neutralizeAll()
#while True:
#	for i in SWITCHES:
#		datain = raw_input("Type: ")
#		lowToHigh(i)
#		highToLow(i)
#		time.sleep(.2)
#highToLow("throttle")
#disarm()
#arm()
#raw_input("u")
#while True:
#	lowToHigh("throttle")
#	highToLow("throttle")
#	raw_input("u")
while True:
	pass
