package com.productdock;

import com.productdock.api.RestApi;
import com.productdock.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RestApiTest {

    @InjectMocks
    private RestApi restApi;

    @Test
    public void get() {
        assertThat(restApi.get()).isEqualTo("test");
    }

    @Test
    public void add() {
        Member member = new Member("Natasa", "Tutus");
        ResponseEntity<Member> response = restApi.add(member);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Member responseMember = response.getBody();
        assertThat(responseMember).isNotNull();
        assertThat(responseMember.getId()).isNotNull();
    }

    @Test
    public void addReturnsBadRequest() {
        ResponseEntity<Member> response = restApi.add(null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
