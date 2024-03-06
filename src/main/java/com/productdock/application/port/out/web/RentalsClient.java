package com.productdock.application.port.out.web;

import com.productdock.adapter.in.web.dto.BookRentalStateDto;

import java.io.IOException;
import java.util.Collection;

public interface RentalsClient {

    Collection<BookRentalStateDto> getRentals(Long bookId) throws IOException, InterruptedException;
}
