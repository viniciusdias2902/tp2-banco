package br.uespi.viniciusdias.banco.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
