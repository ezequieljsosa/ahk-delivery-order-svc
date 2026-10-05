package ahk.order;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

final class Http {

    private Http() {}

    /** RestClient con timeouts cortos, para que un servicio caído no cuelgue al resto. */
    static RestClient client(String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        return RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }
}
