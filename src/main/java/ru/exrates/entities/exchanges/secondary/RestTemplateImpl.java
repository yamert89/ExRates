package ru.exrates.entities.exchanges.secondary;

import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class RestTemplateImpl extends RestTemplate {

    @Setter private int errorCode;

    public RestTemplateImpl() {
        super();
    }

    public <T> ResponseEntity<T> getForEntityImpl(String url, Class<T> responseType, LimitType limitType) throws RestClientException, LimitExceededException, ErrorCodeException {
        if (errorCode == 0) throw new ErrorCodeException();
        ResponseEntity<T> resp = super.getForEntity(url, responseType);
        if (resp.getStatusCode().value() == errorCode) throw new LimitExceededException(limitType);
        return resp;
    }


}
