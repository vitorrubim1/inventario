package br.com.fiap.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TB_INVETARIO")
public class Inventario  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ID_INVENTARIO")
    @SequenceGenerator(name = "SQ_ID_INVENTARIO", sequenceName = "SQ_ID_INVENTARIO")
    @Column(name = "ID_INVENTARIO")
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "TB_BENS_INVENTARIADOS",
            joinColumns = {
                    // Dados da classe que estamos (Inventario)
                    @JoinColumn(
                            name = "ID_INVENTARIO",
                            referencedColumnName = "ID_INVENTARIO",
                            foreignKey = @ForeignKey(name = "FK_INVENTARIO_BEM")
                    )
            },

            inverseJoinColumns = {
                    @JoinColumn(
                            name = "ID_BEM",
                            referencedColumnName = "ID_BEM",
                            foreignKey = @ForeignKey(name = "FK_BEM_INVENTARIO")
                    )
            }
    )
    private Set<Bem> bens = new LinkedHashSet<>();

    public Set<Bem> getBens() {
        return Collections.unmodifiableSet(bens); // Não permitir acesso e modificação diretamente
    }

    public Inventario addBem(Bem bem) {
        bens.add(bem);
        return this;
    }

    public Inventario removeBem(Bem bem) {
        bens.remove(bem);
        return this;
    }

    @Column(name = "DT_INICIO", nullable = false)
    private LocalDate inicio;

    @Column(name = "DT_FIM")
    private LocalDate fim;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
            name = "ID_DEPARTAMENTO",
            referencedColumnName = "ID_DEPARTAMENTO",
            foreignKey = @ForeignKey(name = "FK_INVENTARIO_DEPARTAMENTO"),
            nullable = false
    )
    private Departamento departamento;

    @Column(name = "RELATORIO")
    private String relatorio;

    public Inventario() {
    }

    public Inventario(Long id, Departamento departamento, LocalDate inicio, LocalDate fim, String relatorio) {
        this.id = id;
        this.departamento = departamento;
        this.inicio = inicio;
        this.fim = fim;
        this.relatorio = relatorio;
    }

    public Long getId() {
        return id;
    }

    public Inventario setId(Long id) {
        this.id = id;
        return this;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Inventario setDepartamento(Departamento departamento) {
        this.departamento = departamento;
        return this;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public Inventario setInicio(LocalDate inicio) {
        this.inicio = inicio;
        return this;
    }

    public LocalDate getFim() {
        return fim;
    }

    public Inventario setFim(LocalDate fim) {
        this.fim = fim;
        return this;
    }

    public String getRelatorio() {
        return relatorio;
    }

    public Inventario setRelatorio(String relatorio) {
        this.relatorio = relatorio;
        return this;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "id=" + id +
                ", departamento=" + departamento +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", relatorio='" + relatorio + '\'' +
                '}';
    }
}
