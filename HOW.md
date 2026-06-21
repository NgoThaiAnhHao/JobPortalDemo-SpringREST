## REST - REpresentational State Transfer:
- JSON: JavaScript Oject Notation
- XML
- etc...

## Data Binding (Liên Kết Dữ Liệu)
- POJO is Class in Java
- Data Binding is the process of converting JSON data to a JAVA POJO
- Spring uses the Jackson Project handles data binding between JSON and JAVA POJO
- By Default, Jackson will call getter/setter methods

## Swagger
- Swagger's path is hard to remember:
```
    localhost:8080/swagger-ui/index.html
```

- We can fix this by custom in application.properties: localhost:8080/docs
```
    springdoc.swagger-ui.path=/docs
```






## GET methods


### Path Variables @PathVariable
- Use %20 to replace for empty space in path
```
    /api/books/title%20one
```
=> "title one"


### Query Parameters @RequestParam
- QP are request parameters that have been attached after a "?"
- QP have name=value pairs
- Used for filtering of data
```
    localhost:8080/api/books?category=math
```

- @RequestParam(required = false) is used to declare that a request parameter is not mandatory
```
    @GetMapping("/api/books")
    public List<Book> getBooks(@RequestParam(required = false) String category) {
        ...
    }
```






## POST methods


### @RequestBody
```
    @PostMapping("/api/books")
    public void createBook(@RequestBody Book newBook) {
        ...
    }
```





### PUT methods
```
    @PutMapping("/api/books/{title}")
    public void updateBook(@PathVariable String title,
                            @RequestBody Book updatedBook) {
        ...
    }
```




### DELETE methods
```
    @DeleteMapping("/api/books/{title}")
    public void deletedBook(@PathVariable String title) {
        ...
    }
```


## Validation
- @NotEmpty: Not null or empty
- @Size: A constraint on the size of a field
- @Min: Min value
- @Max: Max value

```
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable @Min(value=1) long id) {
        ...
    }
```


```
    @Size(min = 1, max = 30, message = "Title is between 1 and 30 characters")
    private String title;

    @Size(min = 1, max = 40, message = "Author is between 1 and 40 characters")
    private String author;

    @Size(min = 1, max = 30, message = "Category is between 1 and 30 characters")
    private String category;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot go past 5")
    private int rating;
```



```
    @PutMapping("/{id}")
    public void updateBook(@PathVariable @Min(value = 1) long id,
                           @Valid @RequestBody BookRequest bookRequest) {
        ...
    }
```



## @ResponseStatus
- Used to specify the HTTP status code that should be returned by a
  handler method or an exception class.
- Typically applied to methods in a controller or directly on an
  exception class.
- Customizing HTTP Status Codes

```
    @ResponseStatus(HttpStatus.OK) // Response 200 
    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category) {
        ...
    }
    colo

    @ResponseStatus(HttpStatus.OK) // Response 200
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable @Min(value=1) long id) {
        ...
    }

    @ResponseStatus(HttpStatus.CREATED) // Response 201
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {
        ...
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // Response 204: Used for void
    @PutMapping("/{id}")
    public void updateBook(@PathVariable @Min(value = 1) long id,
                           @Valid @RequestBody BookRequest bookRequest) {
        ...
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // Response 204: Used for void
    @DeleteMapping("/{id}")
    public void deletedBook(@PathVariable long id) {
        ...
    }
```




## More logic to our Swagger UI
### @Tag
Ability to see why the group of endpoints are used for.
```
    @Tag(name="Book REST API Endpoints", description = "Operations related to books")
    @RestController
    @RequestMapping("/api/books")
    public class BookController {
        ...
    }
```

### @Operation
Ability to see why an endpoint exists.
```
    @Operation(summary = "Delete a book", description = "Retrieve a book from the list")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Response 204: Used for void
    @DeleteMapping("/{id}")
    public void deletedBook(@Parameter(description = "Id of the book to delete")
                                @PathVariable long id) {
        ...
    }
```

### @Parameter
Ability to see why a parameter must be included
```
    @Operation(summary = "Delete a book", description = "Retrieve a book from the list")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Response 204: Used for void
    @DeleteMapping("/{id}")
    public void deletedBook(@Parameter(description = "Id of the book to delete")
                                @PathVariable long id) {
        books.removeIf(book -> book.getId() == id);
    }
```




## Exceptions
- ResponseEntity<>: Response clearly status (By default Spring response 200)
- @ControllerAdvice: Handling global exception
- paths:
```
|-- exception
|    |-- common
|    |   |-- BookNotFoundException
|    |-- error
|    |   |-- ApiErrorResponse
|    |-- handler
|    |   |-- BookExceptionHandler
```
- Development Process
    1. Create a custom error response class
    ```
    public class ApiErrorResponse {
        private int status;
        private String message;
        private long timeStamp; 

        // constructors
        
        // getters/setters
    }
    ```

    2. Create a custom exception class
    ```
    public class BookNotFoundException extends RuntimeException {

        public BookNotFoundException(String message) {
            super(message);
        }

    }
    ```

    3. Update REST service to throw exception if book not found
    ```
    .orElseThrow(() ->
        new BookNotFoundException("BOOK NOT FOUND - " + id)
    );        
    ```

    4. Add exception handler method (We can config in GlobalHandlerException class or BookHandlerException)
        - Creating BookExceptionHandler class
        - Mark @ControllerAdvice annotation
        - Define exception handler methods with @ExceptionHandler annotation
        - Exception handler will return a ResponseEntity
        - ResponseEntity is a wrapper for the HTTP response object
        - ResponseEntity provides fine-grained control to specify (Kha nang kiem soat): HTTP status code, HTTP headers, Response body

```
    @ControllerAdvice
    public class BookExceptionHandler {


        // Response for not found - 404
        @ExceptionHandler(BookNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleNotFoundException(BookNotFoundException e) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    System.currentTimeMillis()
            );

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
        }


        // Response for bad request - 400
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidBookException(Exception e) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid Request",
                    System.currentTimeMillis()
            );

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
        }


}
```



## H2 Database
- H2 most used as a memory on RAM, usually used for testing
- Development Process
    1. Add H2 database dependency
    2. Add the configurations in our application.properties file
    ```
        # H2 Database configurations
        spring.datasource.url=jdbc:h2:file:./data/employeedb
        spring.datasource.driver-class-name=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=password
        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

        # H2 console configurations
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2-console
        spring.jpa.hibernate.ddl-auto=update
    ```
    3. Run app, refresh data from disk, access localhost:8080/h2-console
    4. Create a new database table: employee (File: employee SQL table) and load table
    ```
    -- Drop table if exists.
    DROP TABLE IF EXISTS employee;

    -- Create employee table
    CREATE TABLE employee (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(45),
        last_name VARCHAR(45),
        email VARCHAR(45)
    );

    -- Inserting data
    INSERT INTO employee (first_name, last_name, email)
    VALUES
        ('Leslie', 'Andrews', 'leslie@luv2code.com'),
        ('Emma', 'Baumgarten', 'emma@luv2code.com'),
        ('Yuri', 'Petrov', 'yuri@luv2code.com'),
        ('Juan', 'Vega', 'juan@luv2code.com');

    ``` 
    5. Create Employee Entity in java class


## Config Spring Security for Swagger API
- Development Process
    1. Add exception handling for filterChain methods in SecurityConfig Class
    ```
    // For authenticationEntryPoint()
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint()));
    ```

    2. Create new method authenticationEntryPoint()
    ```
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {
            // Send 401 authorized status without triggering a basic auth
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            // Remove the WWW-Authenticate header to prevent browser popup
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("{\"error\" : \"Unauthorized access\"}");
        };
    }
    ```

    3. Create SwaggerConfig Class in security package
    4. Config security for SwaggerConfig Class
    ```
    @Configuration
    @OpenAPIDefinition(info = @Info(title = "My API", version = "v1"), security = @SecurityRequirement(name="basicAuth"))
    @SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    public class SwaggerConfig {

    }
    ```



## Config Spring Security for H2 Database
- Developmet Process
    1. Disable frames for H2 in filterChain methods in SecurityConfig Class
    ```
    // Disable frames for H2
        http.headers(headers -> headers.frameOptions(
                frameOptionsConfig -> frameOptionsConfig.disable()
        ));
    ```

    2. Add requestMatchers for authorizeHttpRequests
    ```
    // For H2
    .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
    ```



## Spring Security in Database
- Development Process
    1. Run sql-script in /h2-console
    2. Adding userDetailsManager method
    ```
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // Tell Spring Security to use JDBC authentication without data source
        return new JdbcUserDetailsManager(dataSource);
    }
    ```



## Customization of table in Spring Security
- Default Spring Security Schema:
    + users(username, password, enabled)
    + authorities(username, authority)
- Development Process for customization of table
    1. We have our schema:
        + members(user_id, pw, active)
        + roles(user_id, role)
    2. Run sql-scipt
    3. Fixing userDetailsManager
    ```
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT user_id, password, active FROM system_users WHERE user_id=?"
        );

        // Define query to retrieve the roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT user_id, role FROM roles WHERE user_id=?"
        );

        return jdbcUserDetailsManager;
    }
    ```


## DOCKER
- In terminal in Intelij
    + Check version
        ```
        docker --version
        ```
    + Run docker
        ```
        docker run -d -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=tododb --name mysqldb -p 3307:3306 mysql:8.0
        ```
    + Config in application.properties
        ```
        # CONFIG FOR DOCKER AND MYSQL DATABASE
        spring.datasource.url=jdbc:mysql://localhost:3307/tododb?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
        spring.datasource.username=root
        spring.datasource.password=secret

        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.open-in-view=false
        ```
    + Use DBeaver to manage Database



## JWT
1. Config JJWT into our project
    - Add dependencies:
    ```
    <dependency>
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt-api</artifactId>
		<version>0.11.5</version>
	</dependency>

	<dependency>
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt-impl</artifactId>
		<version>0.11.5</version>
	</dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>
    ```

    - Run "openssl rand -hex 32" in OpenSSL (Windows) to get secret
    ```
    openssl rand -hex 32
    ```

    - Config in application.properties with expiration ~ 900.000ms ~ 15m
    ```
    # CONFIG FOR JWT
    spring.jwt.secret=2b163fa30a21ef9b8dd2592cc79bad6276c366dcd9cca748b7d64564b434b318
    spring.jwt.expiration=900_000 
    ```


2. Create JWT Service
    - Token Parsing (Đọc dữ liệu từ token)
        + Creates JWTs for authenticated users
        + Signs token using HS256 algorithm
        + Example:
        ```
        @Override
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parser()

                    // Use the secret key to verify the token.
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        ```

    - Token Validation (Kiểm tra token hợp lệ)
        + Verifies if a token is valid or expired
        + Check if token belongs to correct user
        + Example:
        ```
        @Override
        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokeExpired(token) ;
        }

        private boolean isTokeExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }
        ```

    - Token Generation (Khi user login thành công, server tạo JWT trả về frontend)
        + Extracts username, claims and expiration date
        + Example:
        ```
        @Override
        public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
            return Jwts.builder()
                    // Claim: This is additional data that you want to include in the token.
                    .claims(claims)

                    // Subject: Token Owner
                    .subject(userDetails.getUsername())

                    // IssuedAt: When were the tokens created?
                    .issuedAt(new Date(System.currentTimeMillis()))

                    // Expiration: When were the tokens expiration (15m)
                    .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))


                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();
        }

        private SecretKey getSigningKey() {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        }
        ```

3. JWT Security Authentication Filter
    - Intercept every HTTP request
    - If endpoint require authentication, validate token
    - Development Process:
        + Create config package and JwtAuthenticationFilter class
        ```
        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

            filterChain.doFilter(request, response);
        }
        ```



