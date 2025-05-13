
# 🧪 Demoblaze E-commerce Automation Testing Framework

This project contains an automation testing framework developed for the [Demoblaze](https://www.demoblaze.com/) e-commerce demo website. It is built using **Java**, **Selenium WebDriver**, **TestNG**, **ExtentReports**, and **Apache POI**.

> ✅ Developed as part of a Software Testing Task by Sejal Dubey and Kalyani Tewari.

---

## 📌 Project Objectives

- Validate core functionalities of an e-commerce platform
- Practice data-driven and modular test automation
- Generate detailed reports for test execution
- Demonstrate use of industry-standard testing tools

---

## 🧱 Tech Stack

- **Java**
- **Selenium WebDriver**
- **TestNG** – Test framework
- **ExtentReports** – Rich HTML reports
- **Apache POI** – Excel file interaction
- **Log4j** – Logging
- **WebDriverManager** – Browser driver management

---

## 📁 Project Structure

```bash
Demoblaze-Automation-Testing/
├── src/test/java/com/demoblaze/tests/   # Test cases
├── test_data/                            # Excel data files
├── Reports/                              # ExtentReports output
├── logs/                                 # Log4j logs
├── pom.xml                               # Maven dependencies (if used)
├── log4j.properties                      # Log configuration
└── README.md
```

---

## ✅ Key Features Tested

- 🔐 User Registration and Login
- 🛍️ Product Navigation and Selection
- 🛒 Cart Management
- 💳 Checkout Process Simulation
- 📩 Contact Form Functionality

---

## 🚦 Test Types Included

- Functional Testing
- UI Automation
- End-to-End Workflows
- Data-Driven Testing (Excel-based)

---

## 🧪 How to Run

### 📌 Prerequisites

- Java 8+
- Maven (if using `pom.xml`)
- Chrome Browser

### 🔧 To Execute Tests

1. Clone the repo:
   ```bash
   git clone https://github.com/YOUR-USERNAME/Demoblaze-Automation-Testing.git
   cd Demoblaze-Automation-Testing
   ```

2. Run with Maven:
   ```bash
   mvn clean test
   ```

3. View the HTML report at:
   ```
   Reports/DemoblazeTestReport.html
   ```

---

## 📸 Sample Output

- HTML report with test steps
- Console logs via Log4j
- Excel-based test data for parameterized testing

---

## 🙋 Authors

- **Sejal Dubey** (PRN: 22070126038)  
- **Kalyani Tewari** (PRN: 22070126051)

---

## 📚 References

- [Selenium WebDriver Docs](https://www.selenium.dev/documentation/)
- [TestNG Docs](https://testng.org/doc/)
- [ExtentReports GitHub](https://github.com/extent-framework/extent-reports)
- [Apache POI Docs](https://poi.apache.org/)
- [DemoBlaze Site](https://www.demoblaze.com/)
