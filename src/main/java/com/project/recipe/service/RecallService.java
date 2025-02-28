package com.project.recipe.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import javax.net.ssl.SSLException;

@Service
public class RecallService {

    @Value("${api.consumer24.key}")
    private String apiKey;

    private final String API_URL = "https://www.consumer.go.kr/openapi/recall/contents/index.do";

    // WebClient 생성 (SSL 검증 무시)
    private WebClient createWebClient() {
        try {
            // SslContext를 미리 생성 (예외 발생 시 try-catch로 처리)
            io.netty.handler.ssl.SslContext sslContext = io.netty.handler.ssl.SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(spec -> spec.sslContext(sslContext));

            return WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("SSL 설정 오류", e);
        }
    }

    // 소비자24 API 호출 후 XML 응답을 파싱하여 Map으로 반환
    public Mono<Map> getRecallInfo(int pageNo, int cntPerPage) {
        WebClient webClient = createWebClient();

        String url = API_URL + "?serviceKey=" + apiKey
                + "&pageNo=" + pageNo
                + "&cntPerPage=" + cntPerPage
                + "&cntntsId=0201"
                + "&type=json"; // 실제로는 XML 응답일 수 있음

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(String.class)
                .map(xml -> {
                    try {
                        XmlMapper xmlMapper = new XmlMapper();
                        return xmlMapper.readValue(xml, Map.class);
                    } catch (Exception e) {
                        throw new RuntimeException("XML 파싱 오류", e);
                    }
                });
    }
}