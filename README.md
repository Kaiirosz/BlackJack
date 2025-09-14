# Blackjack (Java Console Game)

## 🎮 Overview
A fully object-oriented Blackjack game implemented 100% in **Java**.  
It simulates a real casino environment with multiple players (human + AI), betting, and advanced rules.

## ✨ Features
- **Multiple Players**  
  One human player plus configurable number of AI opponents

- **Casino Rules Implemented**  
  Hit, Stand, Blackjack, Bust, Push, Dealer hits until 17  
  (Support designed for Split, Double Down, Surrender, Insurance, Resplitting)

- **Betting System**  
  Validated bets, starting balances, and precise payout handling
 
- **Clean Architecture**  
  - `model/` → Core domain objects (eg. Cards, Deck, Player, Dealer)  
  - `logic/` → Game logic, managers, context, evaluators
  - `handlers/` → Game flow, turn handlers
  - `io/` → Input/output abstraction (currently console, GUI-ready)  
  - `utils/` → Pacing effects

## 🚀 Why This Project Matters
This project showcases **practical software engineering skills** beyond toy examples:
- Strong grasp of **OOP principles** (encapsulation, abstraction, inheritance, composition)
- Application of **patterns & clean coding practices**
- A project that’s **both complete and extensible**
- Handles **complex rules & edge cases**
- Demonstrates **scalable, maintainable Java code**
## 🏃‍♂️‍➡ To Run
- Java 17+  
- An IDE of your choice

## 🔮 Future Improvements
- Smarter AI strategies    
- Persistent leaderboard (file or database)
- **Test coverage** with JUnit & Mockito  
- Migration to GUI (**JavaFX/Swing**) or Web (**Spring Boot + React**)
- Containerization (Docker, Kubernetes) 
