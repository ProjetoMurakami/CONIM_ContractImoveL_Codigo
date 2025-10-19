# CONIM_ContractImoveL_Codigo
Repositório criado para armazenar os códigos desenvolvidos para o projeto CONIM.

# Configurações para verificar
- Visual Studio usado como IDE;
- JAVA JDK - 21;
- apache-maven-3.9.11
- Servidor: Tomcat 9.0
- Mysql Ver 8.4.4
- Path organizado.

# Configurações no projeto
- Pom.xhtml na aba outputDirectory, permite definir para onde o .war do projero irá.
- src/main/resources/META-INF -> persistence.xml, na aba jdbc.user e jdbc.password, configure com as informações para acessar o MySQL local.
- Nome do bd para criar: locacao, pode ser alterado, entretanto, avise.

-log4j.properties, permite pausar as mensagens escritas no terminal caso deseje

# Comandos para o Maeven
- Execute mvn -version para verificar as configs.
Configuração no meu computador:
    Apache Maven 3.9.11 
    Maven home: C:\Program Files\Apache Software Foundation\apache-maven-3.9.11
    Java version: 21.0.8, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21

- Execute mvn clean install para compilar o projeto, verifique para onde o .war foi enviado a pasta webapps, por padrão é incluido na pasta target do projeto
- Acesse a pasta do TomCat -> Bin -> startup.bat.
