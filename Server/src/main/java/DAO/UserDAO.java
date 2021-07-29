/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Registration;
import model.User;

/**
 *
 * @author RaelH
 */
public class UserDAO {
         private Connection con = null;
    //Sempre que instacia ele vai pegar a conexao com banco, da classe que criamos BancoConnection.
    public UserDAO() {
        con = BancoConnection.getConnection();
    }
//Metodo que recebe um animal pra adicionar no banco!
    public boolean add_user(User u) {
        //Aqui eu monto a query que vai adicionar o contato, tem que saber o basico de QUERYS SQL, 
        //os nomes da tabela, e os campos tem que estar igual a do banco.

        String sql = "INSERT INTO USER ( idade, peso, altura, tamanho_nome, nome) VALUES ( ?, ?, ?, ?, ?)";

        try {
            con = BancoConnection.getConnection();
            //O preparedStatement serve pra preparar a query que criei acima, subistituindo os '?' pelos valores que eu quero,
            //serve pra nao usar querys fixas e unicas, onde o '?' pode receber qualquer valor.
            //OBS: Só pode usar a notação '?' nos dados.
            PreparedStatement stmt = con.prepareStatement(sql);
            
     
            stmt.setInt(1, u.getIdade());
            stmt.setInt(2, u.getPeso());
            stmt.setInt(3, u.getIdade());
            stmt.setInt(4, u.getTamanhoNome());
            stmt.setString(5, u.getNome());
                        
            stmt.execute();
            //o método execute() é utilizado em situações em que a query pode retornar mais de um resultado 
            //(somente em situações muito particulares ele é utilizado, como em algumas execuções de stored procedures).
            System.out.println("Usuario adicionado no Banco de Dados\n");
            return true;
            //Pronto aqui ele já inseriu no banco.
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return false;
        } finally {
            BancoConnection.closeConnection(con);
        }

    }
    
    //Metodo que lista todos os registros.
     public ArrayList<User> mostrar_User() {

        //ArrayList que irei retornar
        ArrayList<User> retorno = new ArrayList<>();

        //Query que irei lançar, retorna todos os animais (incluido os já vendidos).
        String sql = "SELECT * FROM USER";

        try {
            con = BancoConnection.getConnection();
            //Passo a Query que vai ser preparada e executada.
            PreparedStatement stmt = con.prepareStatement(sql);

            //Executo essa Query e atribuo o resultado a rs. Onde ira guardar todo resultado que for pego no banco, 
            //ele guarda o resultado de uma pesquisa numa estrutura de dados que pode ser percorrida. 
            ResultSet rs = stmt.executeQuery();
            // O método executeQuery() é usado para executar consultas apenas, e não deve ser usado 
            //para comandos como update, delete, create, etc.
            
            //Percorro o resultado enquanto tiver proximo.
            while (rs.next()) {

                //Instacio um tipo contato pra criar o contato e adicionar no ArrayList que irei retornar.
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setIdade(rs.getInt("idade"));
                u.setPeso(rs.getInt("peso"));
                u.setAltura(rs.getInt("altura"));
                u.setTamanhoNome(rs.getInt("tamanho_nome"));
                u.setNome(rs.getString("nome"));
                
                //Adiciono no ArrayList.
                retorno.add(u);
                //Repete esse processo ate acabar o ResultSet, e no final o ARRAY vai ficar cheio com todos os animais.
            }

            rs.close();
            //Retorno o ARRAY com todos os animais.
            return retorno;

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        } finally {
            BancoConnection.closeConnection(con);
        }
        return null;
    }
     
    public boolean delete_User(int id) {

        String sql1 = "DELETE FROM USER WHERE id = ?";
        
        try {
            con = BancoConnection.getConnection();
            //Removendo todas as vendas do animal
            PreparedStatement stmt1 = con.prepareStatement(sql1);
            //Preparo sendo feito, digo que no 1º '?' ele vai ser trocado pelo chassi do animal que recebemos no parametro.
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
            System.out.println("\nUser Deletado do Banco de Dados\n");
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return false;
        } finally {
            BancoConnection.closeConnection(con);
        }

    }
    
    public User achar_User(int id) {

        User u = new User();
        u.setId(-1);
        String sql = "SELECT * FROM USER WHERE id = ?";

        try {
            con = BancoConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            //Preparo sendo feito, digo que no 1º '?' ele vai ser trocado pelo chassi do animal que recebemos no parametro.
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //Chamo o Setters padrão do animal, e no parametro coloco o rs.getTipo("nome_da_coluna_igual_do_banco");              
                u.setId(rs.getInt("id"));
                u.setIdade(rs.getInt("idade"));
                u.setPeso(rs.getInt("peso"));
                u.setAltura(rs.getInt("altura"));
                u.setTamanhoNome(rs.getInt("tamanho_nome"));
                u.setNome(rs.getString("nome"));
            }
            return u;

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            BancoConnection.closeConnection(con);
        }
    }
    
        public void alterar_User(int id, int idade, int peso, int altura, int tamanhoNome, String nome) {

        String sql = "UPDATE USER SET idade = ?, peso = ?, altura =?, tamanho_nome=?, nome=?  WHERE id = ?";

        try {
            con = BancoConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idade);
            stmt.setInt(2, peso);
            stmt.setInt(3, altura);
            stmt.setInt(4, tamanhoNome);
            stmt.setString(5, nome);
            
            
            stmt.executeUpdate();
            
            System.out.println("Resgistro atualizado no Banco de Dados\n");
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        } finally {
            BancoConnection.closeConnection(con);
        }
    }
}

