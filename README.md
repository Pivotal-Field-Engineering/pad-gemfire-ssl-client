# pad-gemfire-ssl-client

1. We need to place the keystore and truststore on the classpath. They will be available in following path on PCF container
    /home/vcap/app/BOOT-INF/classes/


2. We need to set the env variable in manifest.yml to specify the path of keystore and truststore

```
env:
    JAVA_OPTS: '-Djavax.net.ssl.keyStore=/home/vcap/app/BOOT-INF/classes/security/gemfireserver.jks -Djavax.net.ssl.keyStorePassword=****** -Djavax.net.ssl.trustStore=/home/vcap/app/BOOT-INF/classes/security/cacerts.keystore -Djavax.net.ssl.trustStorePassword=*****'
```

3. system properties can then be retrieved using following code

```
String keyStoreFilePath1=System.getProperty("javax.net.ssl.keyStore");
String keyStorePassword1=System.getProperty("javax.net.ssl.keyStorePassword");
String trustStoreFilePath1=System.getProperty("javax.net.ssl.trustStore");
String trustStorePassword1=System.getProperty("javax.net.ssl.trustStorePassword");

gemfireProperties.setProperty("log-level", "config");
gemfireProperties.setProperty("ssl-enabled-components", "server,locator");
gemfireProperties.setProperty("ssl-keystore", keyStoreFilePath1);
gemfireProperties.setProperty("ssl-keystore-password", keyStorePassword1);
gemfireProperties.setProperty("ssl-truststore", trustStoreFilePath1);
gemfireProperties.setProperty("ssl-truststore-password", trustStorePassword1);


```