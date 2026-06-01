package START.Models;

import jakarta.persistence.*;
import START.Enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "payments")
public class Payment extends BaseClass {

    @OneToOne(optional = false)
    private Order order;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false, length = 4)
    private String lastFourDigits;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column
    private LocalDateTime paidAt;
}
