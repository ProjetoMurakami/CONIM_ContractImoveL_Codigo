# Diferenças de arquitetura

## Arquitetura antiga
- Framework: Spring Boot / Spring MVC (controladores @Controller, @GetMapping, Thymeleaf).

- Utilização do Spring Data JPA (ImovelRepository extends JpaRepository).

- Controle do frontend Thymeleaf / HTML 

- Necessita de spring-boot-starter-parent no pom.xml.

## Arquitetura nova
- Framework: JSF (Facelets) + PrimeFaces para UI; CDI (Weld) para injeção de dependência.
Utilização do JPA via EntityManager + DAO (classe ImovelDao com EntityManager)

- Controle do frontend Facelets / XHTML + PrimeFaces components (.xhtml em WEB-INF/template/ e pages/), com @Named beans e @ViewScoped.

Deploy: empacota o projeto em .war para rodar dentro do webapps do tomcat

