package io.pivotal.gemfire.demo;

import java.util.Properties;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.JSONFormatter;
import org.apache.geode.pdx.PdxInstance;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Configuration
@Controller
public class GemfireSslClientApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(GemfireSslClientApplication.class, args);
	}


	/***
	 * Properties to configure GemFire client cache
	 */


	@Bean(name ="gemfireProperties")
	Properties gemfireProperties( ) {

		Properties gemfireProperties = new Properties();

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

		return gemfireProperties;
	}

	@Bean
	ClientCache initGemfireClientCache(@Autowired Properties gemfireProperties) {

//		System.out.println("Gemfire propeties:" + gemfireProperties.toString());
		// #3.a Configure Client Cache with Serializer and locator running on localhost
				ClientCache gemfireCache = new ClientCacheFactory(gemfireProperties)
		                .addPoolLocator("52.91.212.60", 10334) //52.91.212.60
		                .setPdxSerializer(new ReflectionBasedAutoSerializer(".*"))
		                .setPdxReadSerialized(false).create();

				return gemfireCache;
	}

	@RequestMapping("/")
	@ResponseBody
    public String homeController(@Autowired ClientCache gemfireCache) {

		ClientRegionFactory<String, PdxInstance> regionFactory
		= gemfireCache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		// #3.b Create a region reference
		Region<String, PdxInstance> customerRegion = regionFactory.create("Customer");

		String value = "{\"firstName\" : \"Allice\", \"lastName\" : \"Speed\"}";

		// #3.c GemFire APIs for CRUD operations
		customerRegion.put("1", JSONFormatter.fromJSON(value));

		System.out.println("Retrieved Customer Data");
		System.out.println(customerRegion.get("1"));

    	return "Execution complete !";
    }

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Execution complete");

	}
}
