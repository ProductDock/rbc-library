package com.productdock.adapter.in.web.dto;

import com.productdock.domain.RentalStatus;

import java.util.Date;

public record BookRentalStateDto (UserProfileDto user, RentalStatus status, Date date){}

