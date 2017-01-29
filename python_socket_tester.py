import socket, sys, time, serial, thread, urllib2, os

def internet_on():
    try:
        urllib2.urlopen('http://216.58.192.142', timeout=1)
        return True
    except urllib2.URLError as err: 
        return False

def remove_padding(padded_text):
	return padded_text.replace('*','')

def initializeSerial():
        print(ser.readline())

def threadedRead():
        while True:
                read_serial=ser.readline()
                print(read_serial)
while not(internet_on()):
	pass
socket_server = socket.socket()

ser = serial.Serial('/dev/ttyUSB0',9600)

socket_server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
try:
	socket_server.bind(('', 90))
except socket.error as msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit()	
socket_server.listen(1)
conn, addr = socket_server.accept()
print 'Connected'
initializeSerial()
thread.start_new_thread(threadedRead,())

while True:
	data = remove_padding(str(conn.recv(100).decode('utf-8')).replace('\n',''))
	if 'poweroff' in data:
		os.system("sudo poweroff")
	ser.write(data+'\n')
	print data
