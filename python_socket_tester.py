import socket, sys, time, serial, thread, urllib2, os

dictionary = {'c1':90,'c2':90,'c3':90,'c4':90,'c5':90,'c6':90}


def internet_on():
    try:
        urllib2.urlopen('http://216.58.192.142', timeout=1)
        return True
    except urllib2.URLError as err: 
        return False

def remove_padding(padded_text):
	return padded_text.replace('*','')

def threadedRead():
    while True:
            read_serial=ser.readline()
            print(read_serial)
			
def writeData(data, power):
	if 'c1' in data:
		print('#1 P'+str((1960*power/180+520))+' T1\n\r')
		for i in range(dictionary['c1'], power):
			ser.write('#1 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
		ser.write('#1 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c1'] = power
	elif 'c2' in data:
		print('#3 P'+str((1960*power/180+520))+' T1\n\r')
		for i in range(dictionary['c2'], power):
			ser.write('#3 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
		ser.write('#3 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c2'] = power
    elif 'c3' in data:
	    print('#4 P'+str((1960*power/180+520))+' T1\n\r')
		for i in range(dictionary['c3'], power):
			ser.write('#4 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
		ser.write('#4 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c3'] = power
    elif 'c4' in data:
	    print('#7 P'+str((1960*power/180+520))+' T1\n\r')
		for i in range(dictionary['c4'], power):
			ser.write('#7 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
		ser.write('#7 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c4'] = power
    elif 'c5' in data:
		print('#8 P'+str((1960*power/180+520))+' T1\n\r')	
		for i in range(dictionary['c5'], power):
			ser.write('#8 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
        ser.write('#8 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c5'] = power
    elif 'c6' in data:
		print('#9 P'+str((1960*power/180+520))+' T1\n\r')
		for i in range(dictionary['c6'], power):
			ser.write('#9 P'+str((1960*i/180+520))+' T1\n\r')
			time.sleep(.005)
        ser.write('#9 P'+str((1960*power/180+520))+' T1\n\r')
		dictionary['c5'] = power

while not(internet_on()):
	pass
socket_server = socket.socket()

ser = serial.Serial('/dev/ttyAMA0',115200)

socket_server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
try:
	socket_server.bind(('', 90))
except socket.error as msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit()	
socket_server.listen(1)
conn, addr = socket_server.accept()
print 'Connected'
thread.start_new_thread(threadedRead,())

while True:
	data = remove_padding(str(conn.recv(20).decode('utf-8')).replace('\n',''))
	if 'poweroff' in data:
		os.system("sudo poweroff")
	try:
		power = int(data.split(',')[1].replace(' ',''))
		writeData(data, power)
		
	except:
		pass