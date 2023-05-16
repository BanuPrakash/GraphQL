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
```
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
```
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

```
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
```
----

ORM
1) DataSource --> Pool of database connection
2) PersistenceContext --> env where entities are managed [ @Entity ]
3) EntityManager is a interface to manage entites [ Sync with database]
4) EntityManagerFactory

```
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
```
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


===============================

Spring MVC
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
</dependency>

@Controller
@RequestMapping("api/products")
public class ProductController {

	@GetMapping()
	m1() {

	}

	@PostMapping() {

	}
}

------------------

GraphQL

GraphQL --> Query Language for your API, not tied to any specific database or storage engine
GraphQL --> server side runtime for executing queries

json / xml / csv => different representations of state of data 


RESTful Web services:
* plural-nouns for resources
* HTTP methods for actions [ GET , POST, PUT, PATCH, DELETE ]

1) 
GET
http://localhost:8080/api/products
gets all products

2)
GET
http://localhost:8080/api/products?category=mobile
gets sub-set of products ==> filter by category

3)
GET
http://localhost:8080/api/products/3
gets product by id ==> 3

4) 
POST
http://localhost:8080/api/products

Payload contains new product data which has to added to "products" resources

------

GraphQL over REST downsides:
1) Multiple requests for multiple resources
2) Waterfall network requests
	http://adobe.com/clients ==> Client data ==> all clients [ 5 clients]
	http://adobe.com/clients/<id>/projects ==> http://adobe.com/clients/1/projects [pids]
	http://adobe.com/clients/<id>/projects/<pid>/team
3) Over-fetching of data
4) No under-fetching of data
5) Schema ==> Contract between client and server ==> Schema first approach

Schemas and Types:

Type System:
GraphQL query language --> selecting fields and objects

POST http://localhost:8080/graphql

{
	product {
		name
		price
		supplier {
			name
			phone
		}
	}
}

Schema Definition Language - SDL

Define object
type Product {
	id:Int
	name:String
	price:Float
}

type Book {
	title:String
	amount:Float
}

Scalar Types:
Int, Float, String, Boolean, ID

extended-scalar

-----
Query Type ==> Special Object type that defines all the top level entry points for queries that clients can executes

productmodule.graphqls
type Query {
	products:[Product]
	books:[Book]
}

Client:
{
	products {
		name
		price
	}
}

ordermodule.graphqls

extend type Query {
	orders:[Order]
}

==========
Operation Type
schema {
	Query
	Mutation
	Subscription
}


<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-graphql</artifactId>
</dependency>

===========

Day 2

Day 1: Spring Boot Framework on top of Spring, Annotations, JPA for ORM, H2 as in memory database

src/main/resources
application.properties ==> DB configuration, server.port=9999, any other configurations,... 


@SpringBootApplication ==> class with this annotation contains main() method --> entry point to start Spring Container [ApplicationContext]

----------

GraphQL with Spring Boot 3.0.6 ==> JDK 17+

TypeSystem and Schema Definition Language
Scalar Types : Int, Float, Boolean, String, ID { serialized string}

type Query {
	products:[Product]
}

// Object type
type Product {
	id:Int!
	name:String!
	price:Float
}

Root Object type : Opertation Type

schema {
	query,
	mutation,
	subscription
}

Http as Server Transports
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
</dependency>

Tomcat as web server by default running on port 8080

POST http://localhost:8080/graphql

configure application.properties

spring.graphql.path=/graphql

spring.graphql.graphiql.enabled=true

graphiql is GraphQL client --> just like POSTMAN for RESTful WS

spring.graphql.graphiql.path=/graphiql

spring.graphql.schema.locations=classpath:graphql/**/
spring.graphql.schema.file-extensions=.graphqls,.gqls

---

http://localhost:8080/graphiql?path=/graphql
```
"query" is default operation
query {
  products {
     id
    name
  }
}

---

{
  productById(id:2) {
     id
    name
  }
}
```

Check:
1) pom.xml ==> added web dependencies ==> for HTTP Server transport
2) moved product.graphqls to "graphql" folder
3) added DataFetcher for productById

Browser:
http://localhost:8080/graphiql

==========================================

Spring / Spring Boot simplied writing DataFetcher

```
@Component
public class ProductQueryResolver implements GraphQlQueryResolver {

	public List<Product> products() {
		///
	}

	public Product productById(int id) {
		//
	}
}
```
Spring boot 2.7+ [ currently in Spring version 3.0]
@SchemaMapping
@QueryMapping
@MutationMapping
@SubscriptionMapping

on the lines of RESTful WS:
@RequestMapping
@GetMapping
@PostMapping
@PutMapping
@PathMapping
@DeleteMapping

```
@Controller
public class ProductQueryResolver {
	@QueryMapping
	public List<Product> products() {
		///
	}

}
```

===========

Eclipse --> Maven --> exisiting maven project --> VeternirayClinic

JPA Bidirectional Relationship
```
@Table("customers")
@Entity
public class Customer {
	@Id
	email;
	name;

	@OneToMany(mappedBy="customer")
	List<Order> orders;
}

customers
email  				name  
a@adobe.com			Asha
b@adobe.com			Beena


@Table("orders")
@Entity
public class Order {
	@Id
	@Column("order_id)
	orderId;

	total;

	@ManyToOne
	@JoinColumn("customer_fk")
	Customer customer;
}

orders
order_id 	total  		customer_fk
120			899344		a@adobe.com
123			2334		b@adobe.com
124			9888		a@adobe.com

```

spring.profiles.active=mysql

application-hsqldb.properties
application-mysql.properties

```
{
 pets {
  name
  type {
    id
    name
  }
}
}

```

JP-QL [ Class and fields]
@Query("from Visit where pet.id = :pid")

same as SQL [ table and columns]

select * from visits where pet.id = 3

=====

PetType and Pet ==> Visit

Tasks: VisitController [visit -> get pet, vet and owner ] and OwnerController [ pets, visits]

Java Client for GraphiQL -> HttpGraphQlClient

type Query {
	pets:[Pet]
	pet(id:Int): Pet
	petTypes:[PetType]
	#visits:[Visit]
	#owners:[Owner]
}


====

By Default in JPA: @ManyToOne is EAGER fetching and @OneToMany is Lazy Fetching

====

Unit Testing: Testing in isolation
JUnit is default Test Framework, Mockito is default Mocking library
WebGraphQlTester

MockMvc --> to perform CRUD operations programatically

https://jsonpath.com/


=====

RuntimeWiring
DataFetchers
@Controller
@QueryMapping ==> root query
@SchemaMapping ==> filed mapping
@Argument

type, query root operation

Day 3

Variables --> dynamic values for query, pass them separetly


POSTMAN:
POST http://localhost:8080/graphql
GraphQL
```
Query:
query PetWithOwner($pid:Int!){
  pet(id:$pid) {
    name,
    type {
      name
    }
    owner {
      firstName
      lastName
    }
  }
}

variable:
{
  "pid":1
}

```

Same in graphiql

===========

Fragments: lets you construct set of resusable fields, which can be useed in queries

```
query PetWithOwner($pid:Int!){
  pet(id:$pid) {
    name,
    type {
      name
    }
    ...OwnerWithPets
  }
}
@ Fragment on Pet type of Object
fragment OwnerWithPets  on Pet {
	owner {
    firstName
    lastName
    pets {
      name
      type {
        name
      }
    } 
  }
}

```

Directives --> decorates part of GraphWL schema with additional configuration

Default directives
@deprecated(reason: String)	Marks the schema definition of a field or enum value as deprecated with an optional reason.

@skip(if: Boolean!)	If true, the decorated field or fragment in an operation is not resolved by the GraphQL server.
@include(if: Boolean!)	If false, the decorated field or fragment in an operation is not resolved by the GraphQL server.

Example:
```
query PetWithOwner($pid:Int!, $withPets: Boolean!){
  pet(id:$pid) {
    name,
    type {
      name
    }
    ...OwnerWithPets
  }
}

fragment OwnerWithPets  on Pet {
	owner {
    firstName
    lastName
    pets @include(if:$withPets){
      name
      type {
        name
      }
    } 
  }
}

Variables:
{
  "pid":1,
  "withPets": false
}
```

Custom Scalars

https://github.com/graphql-java/graphql-java-extended-scalars

The Coercing interface is used by {@link graphql.schema.GraphQLScalarType}s to parse and serialise object values.

=======

Unions and interfaces

Union:
```
type Book {
	title:String!
}

type Author {
	name:String!
}

union SearchResult = Book | Author

type Query {
	search(contains:String) : [SearchResult!]
}
```
----
```
interface Book {
	title:String!
	author:Author!
}

type TextBook implments Book {
	title:String!
	author:Author!
	courses: [Course!]!
}

type OtherBook implements Book {
	title:String!
	author:Author!
	someContent: String
}

---
interface Person {
    id: Int!
    firstName: String!
    lastName: String!
}


type Owner implements Person {
    id: Int!
    firstName: String!
    lastName: String!
    address: String!
    city: String!
    telephone: String!

    # A list of Pets this Owner owns
    pets: [Pet!]!
}



# A Vetenerian
type Vet implements Person {
    id: Int!

    # The Vetenarian's first name
    firstName: String!

    # The Vetenarian's last name
    lastName: String!

    # What is this Vet specialized in? ==> EAGER fetching, no need for @SchemaMapping
    specialties: [Specialty!]!

    # All of this Vet's visits
    visits: [Visit!]!
}

```

Query:
```
{
  people {
    firstName
    lastName
    __typename
    
    ...on Vet {
      specialties {
        name
      }
    }
    
    ...on Owner {
      city
      telephone
    }
  }
}
```

Pagination
1) Offset based pagination
 pets(size:10, offset: 2)

 size --> records
 offset --> from which page
Controller
 Pageable pageable = PageRequest.of(page, size) 

 JPA
 Page<T> findAll(Pageable pageable);

2) Cursor-based pagination
	pets(first:10, after:cursor);
	pets(first:10, before:cursor);

	Advantage: REvserse pagination
	Disadvantage: No Random Access

---
Connection Type contains "edges" and "pageInfo"
EdgeType --> cursor, node
node --> scalar, object

```
{
 petPage(first:2) {
  edges {
    cursor
    node {
      name
      owner {
        firstName
      }
    }
  }
  pageInfo {
    hasPreviousPage
    hasNextPage
    startCursor
    endCursor
  }
}
  
}


{
 petPage(first:2, after : "NA==") {
  edges {
    cursor
    node {
      name
      owner {
        firstName
      }
    }
  }
  pageInfo {
    hasPreviousPage
    hasNextPage
    startCursor
    endCursor
  }
}
  
}
```

 petPage(first:Int!, after:String) :PetConnection on Pet

 ===

 Mutation:
 ```
 mutation {
  addVisit(input: {
    petId: 1
    vetId: 3
    description: "Vaccination"
    date: "2023/03/22"
  }) {
    description
  }
}
```

Pending:
1) DataLoader --> n + 1 problem
2) Exception --> GraphQLError
3) Subscription
4) Filter using specification API
5) JWT Security 


=================

Day 3 Recap:
1) Custom Scalar type
--> graphql-extended-scalars --> dependencies --> Double, Long, DataTime, ...

*.graphqls

Scalar DateTime

--> Corecing interface --> serialize, parseValue and parseLiteral
register with RuntimeWiringConfigurer just like DataFetchers

2) Union and interfaces , fragments __typename, ...on 
3) variables
4) Directives to be used with  FIELD_DEFNITION, OBJECT, ArGUMENT
register with RuntimeWiringConfigurer
5) Pagination
5.1) based on RELAY specification
Connection --> EDGE and PageINFO
EDGE --> NODE and CURSOR

get first n records from cursor [ after or before ]

5.2) Offset


Day 4)

JPA Specification with GraphQL

JP-QL and SQL for dynamic queries will be difficult to write for requirements like E-commerce Search

Criteria API --> Object way to query the data

JPASpecification 
JpaSpecificationExecutor<T> and Specification

OwnerRepository, OwnerSpecication and "filter package"

```
 owners(page: Int, size: Int, filter: OwnerSpecification): OwnerSearchResult!
@QueryMapping
	public OwnerSearchResult owners(@Argument Optional<Integer> page, @Argument Optional<Integer> size,
			@Argument("filter") Optional<OwnerSpecification> filter) {
		int pageNo = page.orElse(0);
		int sizeNo = Math.min(size.orElse(20), 25);

		final PageRequest pageRequest = PageRequest.of(pageNo, sizeNo);
		return new OwnerSearchResult(ownerRepository.findAll(filter.orElse(null), pageRequest));
	}
```

Client:
```
{
  owners(page:0, size:2,filter:{
    city: "Madison"
  }) {
    owners {
      firstName
      lastName
      city
      address
    }
  }
}

```

GraphQL DataLoaders
1) Delay loading of field resolvers
2) n + 1 problem
3) Caching

n + 1 problem

pets {
	name
	owner {
		firstName
	}
}


select * from pets ==> [ 1, 2, 3, 4, 5] --> 1 Query
select * from owner where pet_id = 1
select * from owner where pet_id = 2
select * from owner where pet_id = 3
select * from owner where pet_id = 4
select * from owner where pet_id = 5


With DataLoader == we collect only owner ids of each pet ["o1", "o2","o3","o4", "o2"]

select * from owners where id in ["o1", "o2","o3","o4"]

<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

Publisher
Flux --> emits o...n elements 
Mono --> emits o.. 1 element

```
visits:[Visit]


type Visit {
	id:Int!
	description:String
	pet:Pet
	
	date:Date!
	treatingVet:Vet
}

pet field resolver
treatingVet field resolver

@QueryMapping
	public List<Visit> visits(@Argument Optional<Integer> petId) {
		....
	}

@SchemaMapping(typeName = "Visit")
	public CompletableFuture<Vet> treatingVet(Visit visit, DataLoader<Integer, Vet> dataLoader) {
		if (visit.getVet() == null) {
			return null;
		}

		log.info("Delegating loading of Vet with id {} from REST", visit.getVet().getId());
		return dataLoader.load(visit.getVet().getId());
	}

Check console:
{
  visits {
    description
    date
    treatingVet {
       firstName
      lastName
    }
  }
}

```

Spring Boot 3.0 introduced @BatchMapping for DataLoader implementation

```
{
  books {
    name
    author
    ratings {
      rating
      comment
    }
  }
}
```
implements GraphQLResolver 
implements GraphQLQueryResolver 
=======================================
Root Operations in GraphQL --> Query, Mutation, Subscription

webflux, websocket
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-websocket</artifactId>
</dependency>


WebFlux --> Reactive stream --> Flux , Mono, Observable --> Publishers

Admin Client needs to get a notification whenever a new visit to clinic happens
Client subscribes for a Publisher

type Subscription {
    onNewVisit: Visit
}

VisitController
@SubscriptionMapping
public Flux<Visit> onNewVisit() {
		return visitPublisher.getPublisher();
}

---

Client subscribes:
```
subscription {
  onNewVisit{
    description
    id
  }
}

```

Client gets notification when New visit happens:
mutation {
  addVisit(input: {
    petId: 1
    vetId: 3
    description: "Vaccination 434"
    date: "2023/03/22"
  }) {
    description
  }
}

===========

Security --> Graphql --> Prefer Token based authrization for GraphQL --> JWT

Spring Security
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

====

```
@Autowired
DataSource ds;

@Autowired
public void configureJdbcAuth(AuthenticationManagerBuilder auth) {
	auth.jdbcAuthentication().dataSource(dataSource); // appliation.properties --> dirver, url, username,
}

create table users(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(50) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
```

GraphQL, RESTful should be stateless --> No SessionTracking

Server authentication and sends Token to client 
Token contains claims --> subject {who you are?}, "issuer", "iat", "exp", authorites/roles

every request from client send token
Tokens can be Opaque Tokens / JWT

<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-oauth2-authorization-server</artifactId>
			<version>1.0.1</version>
		</dependency>

JWT
* Symmetric key --> same key is used for both encryption and decryption
 "topsecret12357-dotask" 

* Asymmetric key --> Different keys for encryption (private key) and decryption (public key)

use openssl to generate private key and public key

