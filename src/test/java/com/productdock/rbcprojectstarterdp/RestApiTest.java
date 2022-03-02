package com.productdock.rbcprojectstarterdp;

import com.productdock.rbcprojectstarterdp.api.RestApi;
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
    public void addMember() {
        Member member = new Member("Nenad", "Becanovic");
        assertThat(restApi.addMember(member)).isEqualTo(member);
    }

    @Test
    public void getAllMembers() {
        assertThat(restApi.getAll().size()).isEqualTo(0);
    }
}
