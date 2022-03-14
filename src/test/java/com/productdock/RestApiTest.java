package com.productdock;

import com.productdock.api.RestApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestApiTest {

    @InjectMocks
    private RestApi restApi;

    @Test
    public void get() {
        assertThat(restApi.get()).isEqualTo("test");
    }
}