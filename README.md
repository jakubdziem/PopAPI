# Higher or Lower - Server 🏆📊

![18](https://github.com/user-attachments/assets/35c74a56-54ba-46bf-8459-c2a3c6567c97)


## Overview
This repository contains the backend server for the mobile game **Higher or Lower**, available on [Google Play](https://play.google.com/store/apps/details?id=com.adamdawi.higherorlower). The game has been downloaded over **1,000 times**, and the server is responsible for managing player statistics, game data, image storage, and a private statistics webpage for the development team.

## Technologies Used  
- **Java (Spring Boot)** – Main backend framework  
- **PostgreSQL** – Relational database for storing player and game data  
- **Docker** – Server deployment and containerization  
- **AWS EC2 (Free Tier)** – Hosting environment with security rules restricting database and SSH access to the developer's IP only  
- **AWS ECR** – Used for storing and managing Docker images   
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

## Screenshots of the private statistics webpage for developers 
![Zrzut ekranu 2025-03-29 233043](https://github.com/user-attachments/assets/94905a23-3ebe-43f5-a170-bb3ff9761fc8)
*Main page and chart of the daily total game played*
![Untitled](https://github.com/user-attachments/assets/75d91b01-cc36-4fc3-b463-91eca10132fb)
*Another chart of daily new users*
![screen1](https://github.com/user-attachments/assets/f9cf56c9-9176-4478-9727-9a37dec8a7cf)
*All time user stats in all modes sorted randomly and weekly growth colorized*
![screen2](https://github.com/user-attachments/assets/86969805-59c2-4a3f-a500-8cb6d637d34d)
*Active users stats this week*

