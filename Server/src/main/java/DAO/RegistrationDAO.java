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

/**
 *
 * @author RaelH
 */
public class RegistrationDAO {
 
     private Connection con = null;
    //Sempre que instacia ele vai pegar a conexao com banco, da classe que criamos BancoConnection.
    public RegistrationDAO() {
        con = BancoConnection.getConnection();
    }
    
    
    //Metodo que recebe um animal pra adicionar no banco!
    public boolean add_registor(Registration r) {
        //Aqui eu monto a query que vai adicionar o contato, tem que saber o basico de QUERYS SQL, 
        //os nomes da tabela, e os campos tem que estar igual a do banco.

        String sql = "INSERT INTO REGISTRATION (mensagem, data) VALUES (?, ?)";

        try {
            con = BancoConnection.getConnection();
            //O preparedStatement serve pra preparar a query que criei acima, subistituindo os '?' pelos valores que eu quero,
            //serve pra nao usar querys fixas e unicas, onde o '?' pode receber qualquer valor.
            //OBS: Só pode usar a notação '?' nos dados.
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, r.getMensagem());
            stmt.setString(2, r.getData());
            
            
            stmt.execute();
            //o método execute() é utilizado em situações em que a query pode retornar mais de um resultado 
            //(somente em situações muito particulares ele é utilizado, como em algumas execuções de stored procedures).
            System.out.println("Registro adicionado no Banco de Dados\n");
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
     public ArrayList<Registration> mostrar_Registration() {

        //ArrayList que irei retornar
        ArrayList<Registration> retorno = new ArrayList<>();

        //Query que irei lançar, retorna todos os animais (incluido os já vendidos).
        String sql = "SELECT * FROM REGISTRATION";

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
                Registration r = new Registration();
                r.setId(rs.getInt("id"));
                r.setMensagem(rs.getString("mensagem"));
                r.setData(rs.getString("data"));
                
                //Adiciono no ArrayList.
                retorno.add(r);
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
     
    //Metodo que deleta o animal pelo numero do chassi passado pelo parametro.
    public boolean delete_Registration(int id) {

        String sql1 = "DELETE FROM REGISTRATION WHERE id = ?";
        
        try {
            con = BancoConnection.getConnection();
            //Removendo todas as vendas do animal
            PreparedStatement stmt1 = con.prepareStatement(sql1);
            //Preparo sendo feito, digo que no 1º '?' ele vai ser trocado pelo chassi do animal que recebemos no parametro.
            stmt1.setInt(1, id);
            stmt1.executeUpdate();
            System.out.println("\nContato Deletado do Banco de Dados\n");
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return false;
        } finally {
            BancoConnection.closeConnection(con);
        }

    }
    
        //Metodo que retorna o contato com o id passado pelo parametro.
    public Registration achar_Registration(int id) {

        Registration r = new Registration();
        r.setId(-1);
        String sql = "SELECT * FROM REGISTRATION WHERE id = ?";

        try {
            con = BancoConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            //Preparo sendo feito, digo que no 1º '?' ele vai ser trocado pelo chassi do animal que recebemos no parametro.
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //Chamo o Setters padrão do animal, e no parametro coloco o rs.getTipo("nome_da_coluna_igual_do_banco");              
                r.setId(rs.getInt("id"));
                r.setMensagem(rs.getString("mensagem"));
                r.setData(rs.getString("data"));
            }
            return r;

        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            BancoConnection.closeConnection(con);
        }
    }
    
        public void alterar_Registration(int id,  String mensagem, String data) {

        String sql = "UPDATE REGISTRATION SET mensagem = ?, data = ? WHERE id = ?";

        try {
            con = BancoConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, mensagem);
            stmt.setString(2, data);
            stmt.setInt(3, id);
            
            
            stmt.executeUpdate();
            
            System.out.println("Resgistro atualizado no Banco de Dados\n");
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex);
        } finally {
            BancoConnection.closeConnection(con);
        }
    }
}
