package com.productdock.application.port.out.web;

import com.productdock.domain.BookRentalState;

import java.io.IOException;
import java.util.Collection;

public interface RentalsClient {

    Collection<BookRentalState> getRentals(Long bookId) throws IOException, InterruptedException;
}
