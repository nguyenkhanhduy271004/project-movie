package nguyenduy.local.movie.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity{

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "coin")
  private double coin;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "status")
  private boolean status;
}
