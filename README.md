# 🎬 Movie Ticket Booking Web Application

A full-stack web application for browsing movies, exploring cinema showtimes, selecting seats, and booking movie tickets online.

The project is built with **Java Servlet/JSP**, **Hibernate/JPA**, **PostgreSQL**, and **Apache Tomcat**, following a server-rendered web application architecture.

## ✨ Features

### 👤 User

* Browse available movies
* View movie information, including:

  * Description
  * Duration
  * Age rating
  * Release date
  * Language
  * Cast
  * Poster and trailer
* Browse cinemas and auditoriums
* View available showtimes
* Select seats for a showtime
* Book movie tickets
* User authentication with encrypted passwords

### 🎥 Cinema & Showtime

* Multiple cinema locations
* Multiple auditoriums per cinema
* Different auditorium formats
* Showtime scheduling by movie and auditorium
* Seat availability management
* Multiple seat types

## 🛠️ Tech Stack

### Backend

* Java
* Java Servlet API
* JSP / JSTL
* Hibernate ORM
* JPA
* BCrypt

### Database

* PostgreSQL

### Build & Runtime

* Maven
* Apache Tomcat 9
* Java 17
* Docker

### Frontend

* JSP
* HTML
* CSS

## 🏗️ Architecture

The application follows a traditional Java web application architecture:

```text
Browser
   │
   ▼
Servlet Controllers
   │
   ▼
Business / Data Access Layer
   │
   ▼
Hibernate / JPA
   │
   ▼
PostgreSQL
```

JSP pages are used to render the user interface, while Servlets handle HTTP requests and application logic. Hibernate/JPA provides persistence between Java entities and PostgreSQL.

## 📁 Project Structure

```text
movie-ticket-booking-webapp/
├── src/
│   └── main/
│       ├── java/               # Java source code
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml
│           ├── JSP pages
│           └── static assets
│
├── insert.sql                  # Sample database data
├── pom.xml                     # Maven dependencies
├── Dockerfile
├── TicketBooking.war
└── README.md
```

## 🗄️ Main Domain Model

The application revolves around several main entities:

```text
Movie
  │
  └── Showtime
        │
        ├── Auditorium
        │     └── Cinema
        │
        └── Seat
              │
              └── Booking
```

### Movie

Stores movie metadata such as title, description, duration, age restriction, release date, language, poster, trailer, and actors.

### Cinema

Represents physical cinema locations.

### Auditorium

Represents individual screening rooms inside a cinema.

### Showtime

Associates a movie with an auditorium and a specific screening period.

### Seat

Represents seats belonging to an auditorium, including seat number and seat type.

## 🚀 Getting Started

### Prerequisites

Install:

* Java JDK 17+
* Maven
* PostgreSQL
* Apache Tomcat 9

Verify your installation:

```bash
java -version
mvn -version
```

## 1. Clone the repository

```bash
git clone https://github.com/House1904/movie-ticket-booking-webapp.git
cd movie-ticket-booking-webapp
```

## 2. Configure PostgreSQL

Create a PostgreSQL database for the project.

Example:

```sql
CREATE DATABASE ticketbooking;
```

Configure the database connection in:

```text
src/main/resources/META-INF/persistence.xml
```

For local development, configure your own database URL, username, and password.

> ⚠️ Do not commit production database credentials to source control. Environment variables or application-server secrets should be used for deployed environments.

## 3. Initialize sample data

After the database schema has been created, the sample dataset can be loaded from:

```text
insert.sql
```

The seed data contains sample:

* Movies
* Cinemas
* Auditoriums
* Showtimes
* Seats

## 4. Build the project

```bash
mvn clean package
```

Maven generates the deployable WAR file under:

```text
target/
```

## 5. Deploy with Tomcat

Copy the generated WAR file into the Tomcat `webapps` directory:

```bash
cp target/TicketBooking.war $CATALINA_HOME/webapps/
```

Start Tomcat:

```bash
$CATALINA_HOME/bin/startup.sh
```

Then open:

```text
http://localhost:8080/TicketBooking/
```

Depending on the WAR deployment name, the application may also be available from another context path.

## 🐳 Run with Docker

The repository includes a Dockerfile based on **Tomcat 9 + JDK 17**.

Build the image:

```bash
docker build -t movie-ticket-booking .
```

Run the container:

```bash
docker run -p 8080:8080 movie-ticket-booking
```

Open:

```text
http://localhost:8080
```

The Docker image deploys the application as Tomcat's root application.

## 📦 Main Dependencies

The Maven project includes:

* `javax.servlet-api`
* `javax.servlet.jsp-api`
* JSTL
* PostgreSQL JDBC Driver
* Hibernate
* JPA
* BCrypt
* JUnit

## 🔐 Security

Passwords are handled using **BCrypt** rather than storing plain-text credentials.

For deployment, sensitive information such as:

* Database URL
* Database username
* Database password

should be stored outside the repository through environment variables or secret-management mechanisms.

## 🌱 Possible Improvements

Future improvements could include:

* REST API separation
* Online payment integration
* Booking confirmation emails
* QR-code movie tickets
* Seat reservation timeout
* Improved transaction handling for concurrent bookings
* Role-based admin dashboard
* Containerized PostgreSQL with Docker Compose
* Automated testing and CI/CD
* Environment-based configuration

## 👥 Contributors

This project was developed as a collaborative web application project.

See the GitHub repository's contributor history for individual contributions.

## 📄 License

This project is intended for educational and portfolio purposes.
