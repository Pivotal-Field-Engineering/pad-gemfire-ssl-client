# pad-gemfire-ssl-client

1. We need to place the keystore and truststore on the classpath. They will be available in following path on PCF container
    /home/vcap/app/BOOT-INF/classes/


2. We need to set the env variable in manifest.yml to specify the path of keystore and truststore

```
env:
    JAVA_OPTS: '-Djavax.net.ssl.keyStore=/home/vcap/app/BOOT-INF/classes/security/gemfireserver.jks -Djavax.net.ssl.keyStorePassword=****** -Djavax.net.ssl.trustStore=/home/vcap/app/BOOT-INF/classes/security/cacerts.keystore -Djavax.net.ssl.trustStorePassword=*****'
```