package com.productdock.rbcprojectstarterdp;

import com.productdock.rbcprojectstarterdp.api.RestApi;
import com.productdock.rbcprojectstarterdp.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestApiTest {

    @InjectMocks
    private RestApi restApi;

    @Test
    public void get() {
        assertThat(restApi.getAll().size()).isEqualTo(1);
    }

    @Test
    public void post() {
        int size = restApi.members.size();
        restApi.addMember(new Member("proba", "proba"));
        assertThat(restApi.members.size()).isEqualTo(++size);
    }
}
