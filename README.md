Rocket
======
Network Student Project
-----------------------

Rocket is a simple file sharing client/server software. Rocket is a student project focused on network mechanisms. So, some behaviors can be unexpected.


#How to use it?
Clone the git:
```git clone https://github.com/soleneMalledant/Rocket```

```cd Rocket``` then

```cd withoutssl```

Compile the server and the client
```javac server/Server.java```
```javac client/Client.java```



##Launch the server:

```java server.Server```


##To upload a file

```java client.Client upload <filename>```


##To download a file

```java client.Client download <filename>```

#To use the ssl version
```cd withssl```
Compile the code as shown below.

If you want to use the ssl version you have to generate a certificate with keytool.
```keytool -genkey -keylalg RSA -alias "selfsigned" -keystore keystore.jks -storepass "password" -validity 360```

then launch the server:
```java -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=password server.Server```

and to use the client:
```java -Djavax.net.ssl.trustStore=keystore.jks -Djavax.net.ssl.trustPassword=password client.Client <upload|download> <filename>```  
