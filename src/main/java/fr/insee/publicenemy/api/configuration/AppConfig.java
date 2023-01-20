package fr.insee.publicenemy.api.configuration;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;


@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = { "fr.insee.publicenemy.api" })
@EnableTransactionManagement
@EnableCaching
@Slf4j
public class AppConfig implements WebMvcConfigurer {

    /**
     * 
     * @param proxyUrl proxy url
     * @param proxyPort proxy port
     * @param builder webclient builder
     * @return webclient configured with proxy
     */
    @Bean
    @ConditionalOnProperty(name="application.proxy.enable", havingValue="true")
    public WebClient webClientProxy(@Value("${application.proxy.url}") String proxyUrl, 
            @Value("${application.proxy.port}") Integer proxyPort, @Value("${application.debug.webclient}") boolean debug,
            WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create().tcpConfiguration(tcpClient -> tcpClient
                        .proxy(proxy -> proxy
                                .type(ProxyProvider.Proxy.HTTP)
                                .host(proxyUrl)
                                .port(proxyPort)));

        if(debug) {
            httpClient = httpClient.wiretap("reactor.netty.http.client.HttpClient",
                    LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        }

        builder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return builder.build();
    }

    /**
     * 
     * @param builder webclient builder
     * @return webclient with json default headers
     */
    @Bean
    @ConditionalOnProperty(name="application.proxy.enable", havingValue="false")
    public WebClient webClient(WebClient.Builder builder) {
        builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return builder.build();
    }
}
