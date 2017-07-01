package com.basicBudgetBuilder.domain;

/**
 * Created by Hanzi Jing on 7/04/2017.
 */

import lombok.Data;

import javax.persistence.*;

// attachments were not implemented due to time constraints
@Data
@Entity
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String fileName;

    @Column (nullable = false)
    private long size;

    @ManyToOne
    private Debit debit;
}
