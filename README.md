# GraphQL with Spring Boot 3 

Banuprakash C

Full Stack Architect, 

Co-founder Lucida Technologies Pvt Ltd., 

Corporate Trainer,

Email: banuprakashc@yahoo.co.in

https://www.linkedin.com/in/banu-prakash-50416019/

https://github.com/BanuPrakash/GraphQL

===================================

Softwares Required:
1)  openJDK 17
https://jdk.java.net/java-se-ri/17

2) Eclipse for JEE  
	https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-enterprise-java-and-web-developers

https://mvnrepository.com/

Lombok
java -jar lombok.1.18.26.jar

===================================


Spring Framework provides a light weight container for building enterprise applications
* Life Cycle management of beans
* wiring dependencies
* Enterprise application integration 

Any object managed by spring container -=> bean
Wiring dependecies: Dependency injection

Eclipse ==> Help ==> Eclipse Market Place ==> Search [STS] ==> GO ==> Install Spring Tool suite 4

Spring Boot is a framework on top of Spring framework
* Highly opiniated framework
configures lots of api out-of-the-box depending on Context
1) If we choose to connect to RDBMS --> Gives HikariCP database connection pool [ C3p0, DMDS, ..]
2) If we choose Web application dev --> Gives Embedded Tomcat Server [ jetty, reactor, ..]
3) If we build RESTful ws --> Gives Jackson apis to convert Java <--> JSON [ GSON, Jettison, Moxy, ...]
4) JPA --> gives Hibernate as JPAVendor [ Toplink, KODO, JDO, EclipseLink, OpenJPA, ...]

====

Spring creates instances of class which has one of these annotations:
1) @Component
2) @Repository ==> additional capabilities like handle SQL related exception
3) @Service
4) @Configuration
5) @Controller
6) @RestController



@Repository
public class ProductDaoDBImpl implements ProductDao {

Name of the Object ==> productDaoDBImpl ==> Singleton by default

@SpringBootApplication
1) @ComponentScan ==> scan current package and sub-packages for above mentioned 6 annotations and create instance.
2) @Configuration
3) @EnableAutoConfiguration ==> built-in configuration objects [ many ...]




@Autowired ==> look in the container is there an instance of given interface/class ==> Wiring
Internally Spring boot uses : ByteBuddy / JavaAssist/ CGLib

=====


@Repository
public class ProductDaoDBImpl implements ProductDao {

@Repository
public class ProductDaoJpaImpl implements ProductDao {

@Service
public class AppService {
	
	@Autowired
	private ProductDao productDao; // issue

Solution 1:

Mark one of them as @Primary

@Primary
@Repository
public class ProductDaoJpaImpl implements ProductDao {
	
Solution 2:
Remove @Primary

and add @Qualifier


@Service
public class AppService {	
	@Autowired
	@Qualifier("productDaoJpaImpl")
	private ProductDao productDao;
------

Solution 3: using @Profile
Remove @Primary and @Qualifier

@Profile("prod")
@Repository
public class ProductDaoJpaImpl implements ProductDao {


@Profile("dev")
@Repository
public class ProductDaoDBImpl implements ProductDao {


@Service
public class AppService {	
	@Autowired
	private ProductDao productDao;

src/main/resources
application.properties
spring.profiles.active=prod


Command Line argument
java -jar name.jar --spring.profiles.active=prod

======

JPA and @Bean

Factory Method --> @Bean

objects of 3rd party classes

@Configuration
public class AppConfig {
	@Bean
	public DataSource getDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
		cpds.setUser("swaldman");                                  
		cpds.setPassword("test-password");                                  	
		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);                                     
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);	
		return cpds;
	}
}

@Repository
public class ProductRepo {
	@Autowired
	DataSource ds;
	...
}

===========

ORM and JPA Specification

Object Relational Mapping --> Framework for CRUD operations on RDBMS
ORM frameworks:
1) Hiberante
2) Toplink
3) KODO
4) JDO
5) OpenJPA

products table
id  name  amount    quantity
1   A     5344.22    100


@Entity
@Table("products)
public class Product {
	@Id
	int id;

	String name;

	@Column(name="amount")
	double price;

	@Column(name="quantity")
	int qty;
}

----

ORM
1) DataSource --> Pool of database connection
2) PersistenceContext --> env where entities are managed [ @Entity ]
3) EntityManager is a interface to manage entites [ Sync with database]
4) EntityManagerFactory


@Configuration
public class AppConfig {
	@Bean
	public DataSource getDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
		cpds.setUser("swaldman");                                  
		cpds.setPassword("test-password");                                  	
		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);                                     
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);	
		return cpds;
	}

	@Bean
	public EntityManagerFactory emf(DataSource ds) {
		LocalContainerEntityMAnagerFactoryBean emf  = ..
		emf.setDataSource(ds);
		emf.setJpaVendor(new HibernateJpaVendor());
		emf.setPackagesToScan("com.adobe.prj.entity");
		// other properties
	}
}

@Repository
public class ProductRepo {
	@PersistenceContext
	EntityManager em;
	
	public void saveProduct(Product p) {
		em.save(p); // SQL --> MySQL, H2, Oracle
		/*
		try {
		Connection con = DriverManager.getConnection(;..);
		PreparedStatement ps  = con.preparedStatement("insert into products values(?,?, ? , ?));
		ps.setInt(1, p.getId());
		...

		ps.executeUpdate();
		} catch(SQLException ex) {
			..
		} finally {
			con.close();
		}
		*/
	}

	public List<Product> getPRoducts() {
		return em.findAll();
	}
}

=====

Spring Data JPA provides abstraction over JPA
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

public interface ProductRepo extends JpaRepository<Product, Integer> {

}

No need for @Repository class --> Spring Data Jpa generates class with methods in JpaRepository


