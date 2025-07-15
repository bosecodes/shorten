# Shorten

A lightweight URL shortener built using plain Java with `com.sun.net.httpserver.HttpServer`, H2 Database, and vanilla HTML/CSS/JS frontend.

## ✅ Features

* 🔗 **Anonymous URL shortening**: Convert long URLs into short ones instantly.
* 🧑‍💻 **User login**: Create an account and log in.
* 🌐 **Custom short URLs**: Logged-in users can create their own alias URLs.
* ↪️ **Redirection**: Short URLs automatically redirect to the long ones.

## 🛠️ Tech Stack

| Layer    | Technology                                |
| -------- | ----------------------------------------- |
| Frontend | HTML, CSS, JavaScript (no framework)      |
| Backend  | Java, `com.sun.net.httpserver.HttpServer` |
| Database | H2 using JDBC                             |
| Logging  | SLF4J                                     |
| Testing  | JUnit 5, Mockito                          |
| CI/CD    | GitHub Actions                            |

```

## 🚀 Getting Started

### Prerequisites

* Java 17+
* Maven

### Run Application

```bash
mvn clean package
java -jar target/shorten.jar
```

Visit `http://localhost:8080` in your browser.

### Database

* H2 in-memory mode used.
* JDBC with prepared statements.

### GitHub Actions

* CI configured in `.github/workflows/ci.yml` to run tests on every PR and merge to `main`.

## 🔌 API Endpoints

| Endpoint        | Method | Description                    |
| --------------- | ------ | ------------------------------ |
| `/shorten`      | POST   | Shortens a given long URL      |
| `/s/{shortUrl}` | GET    | Redirects to original long URL |
| `/register`     | POST   | Register a new user            |
| `/login`        | POST   | Log in existing user           |

## 🧪 Testing

* All core components are tested with JUnit 5
* Mocked database and auth dependencies with Mockito

## 📌 Project Management

* [ ] GitHub **Issues** created for each task
* [ ] Separate **branches** for every feature
* [ ] Pull Requests with self-review
* [ ] CI runs on every PR and merge

## 📖 License

This project is licensed under the MIT License.

---

Built with ❤️ using pure Java.

Snapshots:
Anonymous User:
<img width="1916" height="991" alt="image" src="https://github.com/user-attachments/assets/ca6f4cd8-da59-4465-bbc2-c47df755c32f" />
<img width="697" height="444" alt="image" src="https://github.com/user-attachments/assets/951d2ba0-6c4e-4b93-8caa-090ade1331ec" />

Logged in user: (Custom URL workflow)
<img width="735" height="490" alt="image" src="https://github.com/user-attachments/assets/fca61cd8-499c-46ae-b49b-355e74ab77eb" />
<img width="657" height="433" alt="image" src="https://github.com/user-attachments/assets/c12a7fcf-8ea8-4385-8241-0fad76821502" />

Registration Screen:
<img width="942" height="483" alt="image" src="https://github.com/user-attachments/assets/df718295-bad6-41e1-85f2-add95d48de2f" />

Log in screen:

<img width="356" height="379" alt="image" src="https://github.com/user-attachments/assets/1cb20a14-a1c1-4254-b4b7-645fa55e11dd" />

