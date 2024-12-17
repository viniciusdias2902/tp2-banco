package br.uespi.viniciusdias.banco.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToMany
    @JoinTable(
            name = "conta_transacao",
            joinColumns = @JoinColumn(name = "transacao_id"),
            inverseJoinColumns = @JoinColumn(name = "conta_id")
    )
    private List<Conta> contas;
}
