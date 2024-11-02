package com.order.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pay")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imp_uid")
    String impUid;

    @Column(name = "merchant_uid")
    String merchantUid;

    @Column(name = "amount")
    String amount;

    @Column(name = "status")
    String status;

    @Column(name = "created_at")
    String createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
