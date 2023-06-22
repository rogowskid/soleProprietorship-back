# soleProprietorship-back
Projekt jest częścią back-endową portalu do zarządzania jednosobową działalnością gospodarczą.

# Podstawowe pojęcia
Aby uruchomić projekt, należy mieć zainstalowane lokalnie relacyjne bazy danych postreSQL. Dodatkowo należy, przed uruchomieniem projektu stworzyć pustą bazę o nazwie "soleprietorship". 
Uruchomienie projektu odbywa się za pomocą środowiska deweloperskiego. Dodatkowo przed startem należy w pliku application.properties ustawić login oraz hasło dla klienta bazy danych.

# Aplikacja
Aplikacja została napisana przez Javę wraz z biblioteką Spring Boot. Wykorzystywana jest tutaj technologia REST oraz do autoryzacji użytkowników JWT Token.
Aby móc w pełni korzystać z aplikacji, należy nowostworzonego użytkownika zaautoryzować.
Każdy z obiektów biznesowych został wydzielony w osobny pakiet, w których dla każdego pakietu został zaimplementowany:
  * Serwis - klasa, w której wykonywana jest część logiczna aplikacji
  * Kontroler - klasa, w której zostały wystawione "endpointy" do komunikacji części front-endowej z back-endową
  * Encja - klasa, która odwzierciedla byt w tabeli w relacyjnej bazie danych
    
Dodatkowo metody, które wykonują operacje usuwania lub tym podobne zostały zabezpieczone dodatkową ochroną w postaci 2FA (jeśli użytkownik oczywiście wybrał taką opcję przy rejestracji).
