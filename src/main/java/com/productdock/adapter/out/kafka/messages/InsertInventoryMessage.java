package com.productdock.adapter.out.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@ToString
@Data
public class InsertInventoryMessage implements Serializable {
    public final Long bookId;
    public final int bookCopies;
}
