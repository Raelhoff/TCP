# Servidor TCP e Cliente TCP

Foi criado um servidor TCP para o recebimento de mensagens de um cliente TCP, posteriormente, as mensagens receidas pelo cliente são armazenadas em um banco de dados H2 (https://www.h2database.com/).


# Configuração do Banco de dados
Dessa forma, foi instalado o banco de dados H2 ( Link: https://h2database.com/h2-setup-2019-10-14.exe) no Sistema Operacional Windows.

Caminho de instalação do banco de dados:
->  C:\Program Files (x86)\H2

Apos realizar a instalação, verificar se o mesmo foi instalado entrando na pasta:

-> C:\Program Files (x86)\H2\bin

Posteriormente execute: java -jar h2-1.4.200.jar

Logo, deverá abrir uma interface do banco de dados.


# Download do ambiente de desenvolvimento
Foi utilizado o Apache NetBeans 12.0 com o jdk1.8.0_241

Link: https://downloads.apache.org/netbeans/netbeans/12.0/Apache-NetBeans-12.0-bin-windows-x64.exe

Depois de realizar a instalação do ambiente de desenvolvimento, realize o download do projeto pelo com a ajuda do git (execute o comando dentro de uma pasta da sua escolha):

git clone https://github.com/Raelhoff/TCP.git


# Configuração do Projeto
Abra o projeto Client e Server em um ambiente de desenvolvimento da sua escolha (NetBeans, Eclipse, etc). Posteriomente,
realize o download das dependências do projeto por meio do Maven.


## Criar tabela do banco de dados
Para criar as tabelas (REGISTRATION, USER), entre no projeto Server, e execute as classes:

-> CreateTablesREGISTRATION.java

-> CreateTablesUSER.java

As mesmas podem ser encontradas no pacote:
-> Test Packages
         |
	    util
		

## Testar consulta e inserção de dados no banco de dados H2        
Para realizar o cadastro de dados nas tabelas (REGISTRATION, USER), execute as classes:

-> H2jdbcInsertREGISTRATION.java

-> H2jdbcInsertUSER.java 

Logo, para realizar a consulta nas mesmas tabelas, execute:

-> H2jdbcGetREGISTRATION.java

-> H2jdbcGetUSER.java

Como resultado, deve ser exibido:

###Tabela USER

ID: 1

Idade: 30

Peso: 80

Altura: 30

Tamanho do nome: 6

Nome: Rafael

###Tabela REGISTRATION

ID: 1

Mensagem: Ok

Data: 27/07/2021

## Testar CRC-8
Para testar a criptografia, execute a classe:
Main.java

As mesmas podem ser encontradas no pacote:
->	Test Packages
         |
	    crc

O buffer a ser verificado foi:
public static final byte[] TestBytes = new byte[]{9, 1, 49, 50, 51, 52};

Como resultado, deve ser obtido:

-> CRC-8 - BAD ALGO!!! C6

## Testar configurações do arquivo de Log (Log4j)
Na raiz do projeto (Server e Cliente) existe um arquivo "log4j.properties". Edite cada arquivo e 
modifique o paramentro "log4j.appender.file.File" para o diretorio da sua escolha:
Exemplo:

Dentro da pasta Server (log4j.properties): 

log4j.appender.file.File=E:\\TCP\\loggingServerTCP.log  


Dentro da pasta Client (log4j.properties): 

log4j.appender.file.File=E:\\TCP\\loggingClientTCP.log  
 		  
Após realizar as modificações, execute a classe ( nos dois projetos: Server e Cliente):
-> LogTest.java
		 
Como resultado se estiver tudo certo devera aparecer:

  2021-07-29 19:36:42 INFO  LogTest:27 - This is an Info
  
  2021-07-29 19:36:42 WARN  LogTest:28 - This is a Warn
  
  2021-07-29 19:36:42 ERROR LogTest:29 - This is an Error
  
  2021-07-29 19:36:42 FATAL LogTest:30 - This is a Fatal
  
# Iniciando Projeto TCP 

## Execute Projeto Server
Para executar o projeto server, entre no pacote serverTCP, e posteriormente execute (Run File):
-> Server_X_Client.java (Run File)

Por padrão, o servidor está configurado com a porta: 4447

## Executando do Projeto Client
Para executar o projeto client, entre no pacote com.mycompany.client, e posteriormente execute (Run File):
-> SendPackage.java

A ideia desse projeto é simular outros parametros a serem cadastrados no banco de dados, dessa forma, o client envia novos paramentros e 
recebe o retorno do server e posterioment, fecha a conexão.

Logo, apos executar o mesmo, será exibida a seguinte mensagem via console:
Escolha a opção desejada (tipo de pacote a ser enviado ao servidor):
1) Buffer A1
   Entre com a mensagem:
   
2) Buffer A2
   Entre com a idade
   Entre com o peso
   Entre com a altura
   Entre com o nome
   
3) Buffer A3
   Entre com o servidor (America/Sao_Paulo)	
   
4) Sair

# Simular ServidorTCP com  várias conexões (client)

Similar a ferramenta como PacketSender, foi utilizado a aplicação Hercules SETUP utility (Link para download: https://www.hw-group.com/files/download/sw/version/hercules_3-2-8.exe).

Hercules SETUP utility possibilita realizar a comunicação serial, TCP (Server e Client) e UDP (Server e Client).

Diante do exposto, pode ser executado multiplas aplicações (Hercules) TCP Client para realizar a comunicação com Server (Java).

Na pasta docs (raiz do projeto) tem imagens que ajudam a realizar a simulação com multiplos clientes (Hercules Setup).

##  Exemplos de pacotes aceitos

### Pacote A1
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (1): 

Entrada: Bom dia

Comando enviado (client to server): 

0A00000007000000A1000000426F6D20646961E80000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (2): 

Entrada: Boa tarde

Comando enviado (client to server): 

0A00000009000000A1000000426F61207461726465200000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (3): 

Entrada: Boa noite

Comando enviado (client to server): 

0A00000009000000A1000000426F61206E6F697465120000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de comando recebido (server to client):

0A000000050000000A000000C30000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Pacote A2
Exemplo de mensagem a ser enviar (1):

Entre com a idade
-> 30

Entre com peso
-> 50

Entre com altura
-> 170

Entre com o nome
-> Rafael Hoffmann 

Comando enviado (client to server): 

0A0000001F000000A20000001E00000032000000AA0000000F00000052616661656C20486F66666D616E6E310000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (2): 

Entre com a idade
-> 15

Entre com peso
-> 64

Entre com altura
-> 120

Entre com o nome
-> Ana Maria

Comando enviado (client to server): 

0A00000019000000A20000000F000000400000007800000009000000416E61204D61726961CA0000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (3): 

Entre com a idade
-> 89

Entre com peso
-> 56

Entre com altura
-> 160

Entre com o nome
-> Lourdes Maria de Souza

Comando enviado (client to server): 

0A00000027000000A20000005900000038000000A0000000170000004C6F7572646573204D6172696120646520536F757A6120330000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Exemplo de comando recebido (server to client): 

0A000000050000000A000000C30000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
### Pacote A3

Exemplo de mensagem a ser enviar (1): 

Entre com o servidor (America/Sao_Paulo)

-> America/Sao_Paulo

Comando enviado (client to server): 

0A00000011000000A3000000416D65726963612F53616F5F5061756C6F6F0000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (2): 

Entre com o servidor (America/Sao_Paulo)

-> Europe/Amsterdam

Comando enviado (client to server):
 
0A00000010000000A30000004575726F70652F416D7374657264616D660000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de mensagem a ser enviar (3): 

Entre com o servidor (America/Sao_Paulo)

-> America/Noronha

Comando enviado (client to server): 

0A0000000F000000A3000000416D65726963612F4E6F726F6E68616A0000000D000000

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Exemplo de comando recebido (server to client):

0A00000018000000A30000001D0000000700000015000000140000003A00000004000000160000000D000000



----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
