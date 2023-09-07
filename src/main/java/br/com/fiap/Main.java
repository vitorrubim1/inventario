package br.com.fiap;

import br.com.fiap.domain.entity.Bem;
import br.com.fiap.domain.entity.Departamento;
import br.com.fiap.domain.entity.Inventario;
import br.com.fiap.domain.entity.TipoDeBem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("oracle");
        EntityManager manager = factory.createEntityManager();

        addDepartamento(manager);
        addTipoDeBem(manager);
        addRelatorio(manager);
        addBem(manager);
        Inventario inventario = listaInvetarioPeloId(manager);
        List<Bem> bens = listaTodosOsBens(manager);

        for (Bem b : bens) {
            inventario.addBem(b);
        }

        manager.close();
        factory.close();
    }

    private static List<Bem> listaTodosOsBens(EntityManager manager) {
        String jpql = "FROM Bem";
        return manager.createQuery(jpql).getResultList();
    }

    private static Inventario listaInvetarioPeloId(EntityManager manager) {
        String id_invetario = JOptionPane.showInputDialog("Informe o id do inventário que deseja visualizar:");
        Inventario inventario = manager.find(Inventario.class, id_invetario);
        JOptionPane.showMessageDialog(null,  inventario.toString());
        return inventario;
    }

    private static void addBem(EntityManager manager) {
        Bem bem = new Bem();
        String nome_bem = JOptionPane.showInputDialog("Informe o nome do bem:");

        List<TipoDeBem> tipoBemList = manager.createQuery("FROM TipoDeBem").getResultList();
        TipoDeBem tipoBemSelecionado = (TipoDeBem) JOptionPane.showInputDialog(
                null,
                "Listagem de todos os tipos",
                "Tipos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tipoBemList.toArray(),
                tipoBemList.get(0)
        );

        String etiqueta = JOptionPane.showInputDialog("Informe a etiqueta do bem:");

        List<Departamento> departamentoList = manager.createQuery("FROM Departamento").getResultList();
        Departamento departamentoSelecionado = (Departamento) JOptionPane.showInputDialog(
                null,
                "Listagem de todos os departamentos",
                "Departamentos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                departamentoList.toArray(),
                departamentoList.get(0)
        );

        String aquisicao = JOptionPane.showInputDialog("Informe a data de aquisição  DD/MM/AAAA:");

        if (!aquisicao.isEmpty()) {
            int dia = Integer.parseInt(aquisicao.substring(0, 2));
            int mes = Integer.parseInt(aquisicao.substring(3, 5));
            int ano = Integer.parseInt(aquisicao.substring(6, 10));
            bem.setAquisicao(LocalDate.of(ano, mes, dia));
        } else {
            bem.setAquisicao(null);
        }

        bem.setId(null);
        bem.setNome(nome_bem);
        bem.setTipo(tipoBemSelecionado);
        bem.setEtiqueta(etiqueta);
        bem.setLocalizacao(departamentoSelecionado);

        try {
            manager.getTransaction().begin();
            manager.persist(bem);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            String error = """
                        Erro!
                        Não foi possível salvar os dados desta operação.
                                            
                        """ + ex.getMessage();
            System.err.println(error);
            JOptionPane.showMessageDialog(null, error);
        }
    }

    private static void addRelatorio(EntityManager manager) {
        Inventario inventario = new Inventario();

        String data_inicio = "";
        while (data_inicio.isEmpty()) {
            data_inicio = JOptionPane.showInputDialog("Informe a data de início DD/MM/AAAA:");
        }

        String data_fim = JOptionPane.showInputDialog("Informe a data de fim  DD/MM/AAAA:");

        if (!data_fim.isEmpty()) {
            int dia_fim = Integer.parseInt(data_fim.substring(0, 2));
            int mes_fim = Integer.parseInt(data_fim.substring(3, 5));
            int ano_fim = Integer.parseInt(data_fim.substring(6, 10));
            inventario.setFim(LocalDate.of(ano_fim, mes_fim, dia_fim));
        } else {
            inventario.setFim(null);
        }

        int dia_inicio = Integer.parseInt(data_inicio.substring(0, 2));
        int mes_inicio = Integer.parseInt(data_inicio.substring(3, 5));
        int ano_inicio = Integer.parseInt(data_inicio.substring(6, 10));

        List<Departamento> departamentoList = manager.createQuery("FROM Departamento").getResultList();
        Departamento departamentoSelecionado = (Departamento) JOptionPane.showInputDialog(
                null,
                "Listagem de todos os departamentos",
                "Departamentos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                departamentoList.toArray(),
                departamentoList.get(0)
        );

        String relatorio = JOptionPane.showInputDialog("Informe o relatório:");

        inventario.setId(null);

        inventario.setInicio(LocalDate.of(ano_inicio, mes_inicio, dia_inicio));
        inventario.setDepartamento(departamentoSelecionado);
        inventario.setRelatorio(relatorio);

        try {
            manager.getTransaction().begin();
            manager.persist(inventario);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            String error = """
                        Erro!
                        Não foi possível salvar os dados desta operação.
                                            
                        """ + ex.getMessage();
            System.err.println(error);
            JOptionPane.showMessageDialog(null, error);
        }
    }

    private static void addTipoDeBem(EntityManager manager) {
        TipoDeBem tipoDeBem = new TipoDeBem();
        String nome_tipo_bem = JOptionPane.showInputDialog("Informe o nome do tipo do bem:");
        tipoDeBem.setId(null);
        tipoDeBem.setNome(nome_tipo_bem);

        try {
            manager.getTransaction().begin();
            manager.persist(tipoDeBem);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            String error = """
                        Erro!
                        Não foi possível salvar os dados desta operação.
                                            
                        """ + ex.getMessage();
            System.err.println(error);
            JOptionPane.showMessageDialog(null, error);
        }
    }

    private static void addDepartamento(EntityManager manager) {
        Departamento departamento  = new Departamento();

        String nome_departamento = JOptionPane.showInputDialog("Informe o nome do departamento:");
        departamento.setId(null);
        departamento.setNome(nome_departamento);

        try {
            manager.getTransaction().begin();
            manager.persist(departamento);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            String error = """
                        Erro!
                        Não foi possível salvar os dados desta operação.
                                            
                        """ + ex.getMessage();
            System.err.println(error);
            JOptionPane.showMessageDialog(null, error);
        }
    }
}