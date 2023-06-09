package com.tdd.customer;

import com.tdd.ApiTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerApiTest extends ApiTest {

    @Test
    void customer() {
        CustomerDto customerRequest = CustomerSteps.getCustomerRequest();
        ExtractableResponse<Response> response = CustomerSteps.makeCustomerApiRequest(customerRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
