package br.uespi.viniciusdias.banco.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Telefone> telefones;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
    @ManyToMany(mappedBy = "usuarios")
    @JsonIgnore // To-do: dto pra corrigir a referência bidirecional infinita
    private List<Conta> contas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
}
