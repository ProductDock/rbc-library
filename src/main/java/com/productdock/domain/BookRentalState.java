package com.productdock.domain;

import java.util.Date;

public record BookRentalState(UserProfile user, RentalStatus status, Date date) {
}

