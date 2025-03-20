# Higher or Lower - Server 🏆📊

## Overview
This repository contains the backend server for the mobile game **Higher or Lower**, available on [Google Play](https://play.google.com/store/apps/details?id=com.adamdawi.higherorlower). The game has been downloaded over **1,000 times**, and the server is responsible for managing player statistics, game data, image storage, and a private statistics webpage for the development team.

## Technologies Used  
- **Java (Spring Boot)** – Main backend framework  
- **PostgreSQL** – Relational database for storing player and game data  
- **Docker** – Server deployment and containerization  
- **AWS EC2 (Free Tier)** – Hosting environment with security rules restricting database and SSH access to the developer's IP only  
- **AWS ECR** – Used for storing and managing Docker images  
- **Firebase** – Authentication and user management  
- **Python** – Data scraping for game modes, images, and game data  
- **Selenium & Jsoup** – Web scraping and image source verification  
- **MapStruct** – Extensively used for object mapping, with modifications  
- **Lombok** – Utilized extensively to reduce boilerplate code  
- **JavaScript (Chart.js)** – Used for visualizing statistics in a chart  
- **HTML + Thymeleaf** – Web interface for statistics, handling attributes from the model  

## Features  
- Secure user authentication (Google and registered accounts are via Firebase, while guest accounts are not)  
- Dynamic game mode data retrieval via REST API  
- Leaderboards and player statistics management  
- Image storage and retrieval  
- Private statistics webpage for developers  
- Rate limiting (100 requests per minute) to ensure fair usage  
- Automated tasks for statistics updates and data backups  

## API Highlights  
The server provides multiple API endpoints for managing player data, game data, and leaderboards, ensuring smooth gameplay and real-time updates.  

## Performance & Scaling  
- Supports **~100 active weekly users**, with **20+ daily users**.  
- Performance testing confirms smooth operation up to **100 concurrent active users**.  
- Bottlenecks observed at **500 active concurrent users**, highlighting areas for future optimization.  

## Testing  
- **JUnit & Spring Boot Test** – Test coverage for controllers and data validation  
- **MockMvc** – Verification of API endpoints and HTTP responses  
- **Integration Tests** – Ensuring proper interaction with the database and data generation  
- **Image URL Validation Tests** – Automated checks to verify image accessibility and correctness  

## Development & Contributions  
This repository serves as a **presentation of the project** rather than an open-source initiative. Collaboration is not currently open.
